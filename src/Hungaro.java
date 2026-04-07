import java.util.ArrayList;
import java.util.List;

public class Hungaro {
    int[][] matrizPeso, matrizInicial;
    List<Integer> linhasCobertas;
    List<Integer> colunasCobertas;

    public Hungaro(int[][] matrizPeso) {
        this.matrizPeso = matrizPeso;
        this.matrizInicial = new int[matrizPeso.length][matrizPeso[0].length];
        clonaMatriz(matrizPeso);
        this.linhasCobertas = new ArrayList<>();
        this.colunasCobertas = new ArrayList<>();
    }

    private void clonaMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++)
            for (int j = 0; j < matriz[0].length; j++)
                matrizInicial[i][j] = matriz[i][j];
    }

    public int procuraMenorLinha(int[] linha) {
        int menor = linha[0];
        for (int i = 1; i < linha.length; i++) {
            if (linha[i] < menor)
                menor = linha[i];
        }
        return menor;
    }

    public int procuraMenorColuna(int coluna) {
        int menor = matrizPeso[0][coluna];
        for (int i = 1; i < matrizPeso.length; i++) {
            if (matrizPeso[i][coluna] < menor)
                menor = matrizPeso[i][coluna];
        }
        return menor;
    }

    public void subtraiMenorLinhas() { //subtrai menor elemento de cada linha
        for (int i = 0; i < matrizPeso.length; i++) {
            int menor = procuraMenorLinha(matrizPeso[i]);
            for (int j = 0; j < matrizPeso[i].length; j++)
                matrizPeso[i][j] -= menor;
        }
    }

    public void subtraiMenorColunas() { //subtrai menor de cada coluna
        for (int i = 0; i < matrizPeso[0].length; i++) {
            int menor = procuraMenorColuna(i);
            for (int j = 0; j < matrizPeso.length; j++)
                matrizPeso[j][i] -= menor;
        }
    }

    // Usa algoritmo de busca para emparelhar zeros
    private boolean busca(int linha, boolean[] visitadoLinhas, boolean[] visitadoColunas, int[] correspondenciaColuna) {
        visitadoLinhas[linha] = true;
        for (int col = 0; col < matrizPeso[0].length; col++) {
            if (matrizPeso[linha][col] == 0 && !visitadoColunas[col]) {
                visitadoColunas[col] = true;
                if (correspondenciaColuna[col] == -1 || busca(correspondenciaColuna[col], visitadoLinhas, visitadoColunas, correspondenciaColuna)) {
                    correspondenciaColuna[col] = linha;
                    return true;
                }
            }
        }
        return false;
    }

    public List<int[]> marcarZeros() { //marcar zeros: retorna pares linha-coluna da atribuição
        int[] correspondenciaColuna = new int[matrizPeso[0].length];
        for (int i = 0; i < correspondenciaColuna.length; i++)
            correspondenciaColuna[i] = -1;

        for (int linha = 0; linha < matrizPeso.length; linha++) {
            boolean[] visitadoLinhas = new boolean[matrizPeso.length];
            boolean[] visitadoColunas = new boolean[matrizPeso[0].length];
            busca(linha, visitadoLinhas, visitadoColunas, correspondenciaColuna);
        }

        List<int[]> pares = new ArrayList<>();
        for (int col = 0; col < correspondenciaColuna.length; col++) {
            if (correspondenciaColuna[col] != -1)
                pares.add(new int[] { correspondenciaColuna[col], col });
        }
        return pares;
    }

    public boolean encontrouSolucaoOtima() { //verifica se precisa continuar ou não
        return marcarZeros().size() == matrizPeso.length;
    }

    public void encontrarCoberturas() { //encontra linhas e colunas mínimas que cobrem todos
        linhasCobertas.clear();
        colunasCobertas.clear();

        boolean[] linhaMarcada = new boolean[matrizPeso.length];
        boolean[] colunaMarcada = new boolean[matrizPeso[0].length];

        List<int[]> pares = marcarZeros();

        for (int i = 0; i < matrizPeso.length; i++) { //marque as linhas sem atribuição de zero
            boolean atribuida = false;
            for (int[] par : pares) {
                if (par[0] == i) {
                    atribuida = true;
                }
            }
            if (!atribuida)
                linhaMarcada[i] = true;
        }
        boolean mudou = true;
        while (mudou) {
            mudou = false;
            for (int i = 0; i < matrizPeso.length; i++) {  //para cada linha marcada, marque todas as colunas com zero nessa linha
                if (linhaMarcada[i]) {
                    for (int j = 0; j < matrizPeso[i].length; j++) {
                        if (matrizPeso[i][j] == 0 && !colunaMarcada[j]) {
                            colunaMarcada[j] = true;
                            mudou = true;
                        }
                    }
                }
            }
            for (int[] par : pares) { //para cada coluna marcada, marquei as linhas com atribuição de zero nessa coluna
                if (colunaMarcada[par[1]] && !linhaMarcada[par[0]]) {
                    linhaMarcada[par[0]] = true;
                    mudou = true;
                }
            }
        }
        for (int i = 0; i < linhaMarcada.length; i++) { //linhas não marcadas são as coberturas
            if (!linhaMarcada[i])
                linhasCobertas.add(i);
        }
        for (int j = 0; j < colunaMarcada.length; j++) { //colunas marcadas são as coberturas
            if (colunaMarcada[j])
                colunasCobertas.add(j);
        }
    }

    public void ajustarMatriz() {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < matrizPeso.length; i++) { //procura menor dos elementos não cobertos
            for (int j = 0; j < matrizPeso[i].length; j++) {
                if (!linhasCobertas.contains(i) && !colunasCobertas.contains(j)) {
                    if (matrizPeso[i][j] < min)
                        min = matrizPeso[i][j];
                }
            }
        }
        for (int i = 0; i < matrizPeso.length; i++) { //subtrai dos não cobertos e soma aos cobertos
            for (int j = 0; j < matrizPeso[i].length; j++) {
                if (!linhasCobertas.contains(i) && !colunasCobertas.contains(j))
                    matrizPeso[i][j] -= min;
                else if (linhasCobertas.contains(i) && colunasCobertas.contains(j))
                    matrizPeso[i][j] += min;
            }
        }
    }

    public void imprimeResultado() {
        List<int[]> pares = marcarZeros();
        int custo = 0;
        for (int[] par : pares) {
            custo += matrizInicial[par[0]][par[1]];
            System.out.println("Linha: " + par[0] + " com Coluna: " + par[1]);
        }
        System.out.println("Custo total: " + custo);

        for (int i = 0; i < matrizPeso.length; i++) {
            for (int j = 0; j < matrizPeso[i].length; j++) {
                System.out.print(matrizPeso[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void aplicaHungaro() {
        subtraiMenorLinhas();
        subtraiMenorColunas();
        while (!encontrouSolucaoOtima()) {
            encontrarCoberturas();
            ajustarMatriz();
        }
        imprimeResultado();
    }
}
