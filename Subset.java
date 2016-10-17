import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

/*
 Write a client program Subset.java that takes a command-line integer k; reads in a sequence of
 N strings from standard input using StdIn.readString(); and prints out exactly k of them,
 uniformly at random. Each item from the sequence can be printed out at most once. You may assume
 that 0<=k<=N, where N is the number of string on standard input.
*/

public class Subset {
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();
 
        int k = Integer.parseInt(args[0]);
        int count = 0;
 
        while (!StdIn.isEmpty()) {
            randomizedQueue.enqueue(StdIn.readString());
            count++;
        }
        while (count - k > 0) {
            randomizedQueue.dequeue();
            count--;
        }
 
        for (int i = 0; i < k; i++)
            StdOut.println(randomizedQueue.dequeue());
    }
}
