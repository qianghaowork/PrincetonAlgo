import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> ascii = initialize();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = ascii.indexOf(c);
            if (index != 0) {
                ascii.remove(index);
                ascii.addFirst(c);
            }
            BinaryStdOut.write(index, 8);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> ascii = initialize();
        while (!BinaryStdIn.isEmpty()) {
            int position = BinaryStdIn.readInt(8);
            char c = ascii.get(position);
            if (position != 0) {
                ascii.remove(position);
                ascii.addFirst(c);
            }
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }

    private static LinkedList<Character> initialize() {
        LinkedList<Character> ascii = new LinkedList<Character>();
        for (char c = 0; c < 256; c++)
            ascii.add(c);
        return ascii;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            MoveToFront.encode();
        } else if ("+".equals(args[0])) {
            MoveToFront.decode();
        }
    }
}
