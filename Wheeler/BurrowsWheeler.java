import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String inString = BinaryStdIn.readString();
        // Construct a CircularSuffixArray from the input string
        CircularSuffixArray csa = new CircularSuffixArray(inString);
        int len = csa.length();
        int first = 0;
        char[] last = new char[len];
        for (int i = 0; i < len; i++) {
            int orig = csa.index(i);
            if (orig == 0) first = i;
            last[i] = inString.charAt((orig + (len - 1)) % len);
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < len; i++)
            BinaryStdOut.write(last[i], 8);
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        int R = 256;
        int[] count = new int[R + 1];

        // 1. Traverse over the t[] array, and count the character frequencies
        String inString = BinaryStdIn.readString();
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            count[c + 1]++;
        }

        for (int i = 1; i <= R; i++) {
            count[i] = count[i] + count[i - 1];
        }

        int[] next = new int[inString.length()];
        char[] orig = new char[inString.length()];
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            int index = count[c];
            next[index] = i;
            orig[index] = c;
            count[c]++;
        }

        for (int i = 0; i < inString.length(); i++) {
            BinaryStdOut.write(orig[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            BurrowsWheeler.transform();
        } else if ("+".equals(args[0])) {
            BurrowsWheeler.inverseTransform();
        }
    }

}
