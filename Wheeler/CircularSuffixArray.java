import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final int len;
    private final Integer[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException("null String s");
        len = s.length();
        index = new Integer[len];
        for (int i = 0; i < len; i++)
            index[i] = i;
        Arrays.sort(index, (a, b) -> {
            for (int i = 0; i < len; i++) {
                char c = s.charAt((a + i) % len);
                char c1 = s.charAt((b + i) % len);
                if (c < c1) return -1;
                if (c > c1) return 1;
            }
            return a < b ? -1 : 1;
        });
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= len)
            throw new IllegalArgumentException(i + " is not within range");
        return this.index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(s);

        StdOut.println(csa.length());

        for (int i = 0; i < s.length(); i++) {
            int a = csa.index(i);
            StdOut.println("Index of " + i + " is " + a);
            if (a == 0)
                StdOut.println(s);
            else
                StdOut.println("Sorted string is " + String.format("%s%s", s.substring(a), s.substring(0, a)));
        }
    }

}
