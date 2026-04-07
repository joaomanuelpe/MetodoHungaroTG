import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int[][] matriz = {
                {9, 14, 19, 12, 8},
                {6, 9, 7, 10, 5},
                {11, 5, 3, 8, 7},
                {7, 8, 6, 9, 4},
                {5, 6, 9, 7, 3}
        };
        Hungaro hungaro = new Hungaro(matriz);
        hungaro.aplicaHungaro();
    }
}