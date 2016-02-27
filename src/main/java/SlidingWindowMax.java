import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Dipti on 1/26/2016.
 */
public class SlidingWindowMax {
    public static void main(String[] args) {
        int a[] = {1, 13, -1, -3, 5, 3, 6, 7, 2};
        int ws = 3;
        int b[] = new int[a.length - ws + 1];

        Deque<Integer> deque = new ArrayDeque<>();
        /* Add first ws numbers into priority queue */
        for (int i = 0; i < ws; i++) {
            // remove all numbers smaller than current
            while (!deque.isEmpty() && deque.getLast() <= a[i])
                deque.removeLast();
            deque.addLast(a[i]);
        }

        for (int i = ws; i < a.length; i++) {
            b[i - ws] = deque.getFirst();
            // remove all numbers smaller than window's end number
            while (!deque.isEmpty() && deque.getLast() <= a[i])
                deque.removeLast();
            // remove all numbers smaller than window's start number
            while (!deque.isEmpty() && deque.getFirst() <= a[i - ws])
                deque.removeFirst();
            // add the current number into the queue
            deque.addLast(a[i]);
        }
        b[a.length - ws] = deque.getFirst();

        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i] + " ");
        }
    }
}
