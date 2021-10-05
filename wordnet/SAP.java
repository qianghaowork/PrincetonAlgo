import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class SAP {
    private final Digraph G;
    private final HashMap<HashSet<Integer>, int[]> cache;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Argument is null");
        this.G = new Digraph(G);
        this.cache = new HashMap<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        sap(v, w);
        HashSet<Integer> keys = new HashSet<>();
        keys.add(v);
        keys.add(w);
        return cache.get(keys)[0];
    }

    private void validNode(int v) {
        if (v < 0 || v >= G.V())
            throw new IllegalArgumentException("v within 0 and V-1");
    }

    private void validNode(Iterable<Integer> v) {
        if (v == null)
            throw new IllegalArgumentException("argument is null");
        int n = G.V();
        for (Integer cur : v) {
            if (cur == null)
                throw new IllegalArgumentException("argument is null");
            if (cur < 0 || cur >= n) {
                throw new IllegalArgumentException("vertex " + cur + " is not between 0 and " + (n - 1));
            }
        }
    }

    private int[] sap(int v, int w) {
        validNode(v);
        validNode(w);

        HashSet<Integer> keys = new HashSet<>();
        keys.add(v);
        keys.add(w);

        if (cache.containsKey(keys))
            return cache.get(keys);

        int distance = Integer.MAX_VALUE;
        int ancestor = -1;
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        for (int cur = 0; cur < G.V(); cur++) {
            if (bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur)) {
                int vPath = bfsV.distTo(cur);
                int wPath = bfsW.distTo(cur);
                if (vPath + wPath < distance) {
                    distance = vPath + wPath;
                    ancestor = cur;
                }
            }
        }
        if (distance == Integer.MAX_VALUE) {
            distance = -1;
            ancestor = -1;
        }
        int[] results = {distance, ancestor};
        cache.put(keys, results);
        return results;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        sap(v, w);
        HashSet<Integer> keys = new HashSet<>();
        keys.add(v);
        keys.add(w);
        return cache.get(keys)[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return shortSubSets(v, w)[0];
    }

    private int sizeOfIterable(Iterable<Integer> v) {
        int count = 0;
        for (int cur : v)
            count++;
        return count;
    }

    private int[] shortSubSets(Iterable<Integer> v, Iterable<Integer> w) {
        validNode(v);
        validNode(w);
        if (sizeOfIterable(v) == 0 || sizeOfIterable(w) == 0)
            return new int[]{-1, -1};

        int distance = Integer.MAX_VALUE;
        int ancestor = -1;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        for (int cur = 0; cur < G.V(); cur++) {
            if (bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur)) {
                int vPath = bfsV.distTo(cur);
                int wPath = bfsW.distTo(cur);
                if (vPath + wPath < distance) {
                    distance = vPath + wPath;
                    ancestor = cur;
                }
            }
        }
        if (distance == Integer.MAX_VALUE) {
            distance = -1;
            ancestor = -1;
        }
        return new int[]{distance, ancestor};
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return shortSubSets(v, w)[1];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
