import java.util.Scanner;

/**
 * Created by Dipti on 1/19/2016.
 */
public class ChocolateGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine());
        String line = sc.nextLine();
        String[] split = line.split(" ");
        int[] piles = new int[n];
        for (int i = 0; i < n; i++) {
            piles[i] = Integer.parseInt(split[i]);
        }
        int count = 0;
        boolean b[] = null;
        for (int i = 0; i < n; i++) {
            b = new boolean[n];
            for (int j = i; j < n; j++) {
                if (j == i) {
                    if (piles[j] % 2 == 0) {
                        b[j] = true;
                    }
                } else {
                    b[j] = (b[j - 1] && piles[j] % 2 == 0) || (!b[j - 1] && piles[j] % 2 != 0);
                    if (b[j])
                        count++;
                }
            }
        }
        System.out.println(count);
    }

    private static int sum(int[] piles, int i, int j) {
        int sum = 0;
        for (; i <= j; i++) {
            sum += piles[i];
        }
        return sum;
    }
}
