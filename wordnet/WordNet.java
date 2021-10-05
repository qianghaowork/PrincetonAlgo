import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

public class WordNet {
    private final Digraph directedGraph;
    private final ST<Integer, String> idWordMap = new ST<>();
    private final ST<String, Bag<Integer>> wordMap = new ST<>();
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        int count = readSynsets(synsets);
        directedGraph = new Digraph(count);
        readHypernyms(hypernyms);
        DirectedCycle directedCycle = new DirectedCycle(directedGraph);
        if (directedCycle.hasCycle())
            throw new IllegalArgumentException("It's not DAG root");

        // check that the input is single rooted
        // which means there is one and only one vertex has zero out degree
        int rootNum = 0;
        for (int v = 0; v < count; v++)
            if (directedGraph.outdegree(v) == 0)
                rootNum++;
        if (rootNum != 1)
            throw new IllegalArgumentException("Input has " + rootNum + " roots.");
        sap = new SAP(directedGraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordMap.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Argument is null");
        return wordMap.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("Argument is null");
        if (!wordMap.contains(nounA) || !wordMap.contains(nounB))
            throw new IllegalArgumentException("Not a wordNet noun");
        return sap.length(wordMap.get(nounA), wordMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("Argument is null");
        int ancestor = sap.ancestor(wordMap.get(nounA), wordMap.get(nounB));
        return ancestor == -1 ? null : idWordMap.get(ancestor);
    }

    // do unit testing of this class
    //   public static void main(String[] args) {
    //  }

    // helper methods
    private int readSynsets(String synsets) {
        if (synsets == null)
            throw new IllegalArgumentException("Argument is null");
        In in = new In(synsets);
        int count = 0;
        while (in.hasNextLine()) {
            count++;
            String line = in.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            idWordMap.put(id, parts[1]);
            String[] nouns = parts[1].split(" ");
            for (String n : nouns) {
                if (wordMap.get(n) != null) {
                    Bag<Integer> bag = wordMap.get(n);
                    bag.add(id);
                } else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(id);
                    wordMap.put(n, bag);
                }
            }
        }
        return count;
    }

    private void readHypernyms(String hypernyms) {
        if (hypernyms == null)
            throw new IllegalArgumentException("Argument is null");
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++)
                directedGraph.addEdge(id, Integer.parseInt(parts[i]));
        }
    }
}
