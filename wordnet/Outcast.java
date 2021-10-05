import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        if (nouns.length < 2)
            throw new IllegalArgumentException("at least two words");
        int distance = Integer.MIN_VALUE;
        String word = null;
        for (int i = 0; i < nouns.length; i++) {
            int curlength = 0;
            if (!wordNet.isNoun(nouns[i]))
                throw new IllegalArgumentException(nouns[i] + " is not a valid word");
            for (int j = 0; j < nouns.length; j++) {
                if (i != j)
                    curlength += wordNet.distance(nouns[i], nouns[j]);
            }
            if (curlength > distance) {
                distance = curlength;
                word = nouns[i];
            }
        }
        return word;
    }

    public static void main(String[] args)  // see test client below
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
