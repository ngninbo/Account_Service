import java.util.Scanner;
import java.util.stream.IntStream;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int prod = 0;
        if (a < b) {
            prod = IntStream.range(a, b).reduce(1, (i, j) -> i * j);
        }
        System.out.println(prod);
    }
}