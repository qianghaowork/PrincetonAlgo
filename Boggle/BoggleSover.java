mport edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BoggleSolver {
    private final Trie trie;
    private ArrayList<String> answers = null;
    private int uid = 0;

    private static class Trie {
        private final TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public void insert(String word) {
            if (word.length() >= 3) {
                put(word, root, 0);
            }
        }

        private void put(String word, TrieNode node, int d) {
            char c = word.charAt(d);
            if (!node.contains(c)) {
                node.put(c);
            }
            TrieNode cur = node.get(c);
            if (c == 'Q') {
                d++;
                if (d >= word.length() || word.charAt(d) != 'U')
                    return;
            }
            if (d == word.length() - 1) {
                cur.setWord(word);
                return;
            }
            put(word, cur, d + 1);
            return;
        }

        public boolean search(String word) {
            if (word.length() < 3)
                return false;
            return search(word, root, 0);
        }

        private boolean search(String word, TrieNode node, int d) {
            char c = word.charAt(d);
            if (!node.contains(c)) {
                return false;
            }
            TrieNode cur = node.get(c);
            if (c == 'Q') {
                d++;
                if (d >= word.length() || word.charAt(d) != 'U')
                    return false;
            }
            if (d == word.length() - 1)
                return cur.isEnd();
            return search(word, cur, d + 1);
        }

        public TrieNode prefixNode(char c, TrieNode cache) {
            if (cache == null)
                cache = root;
            if (cache.contains(c)) {
                cache = cache.get(c);
            } else
                return null;
            return cache;
        }
    }

    private static class TrieNode {
        private final TrieNode[] next;
        private boolean hasChild;
        private String word;
        private int uid;

        public TrieNode() {
            next = new TrieNode[26];
            hasChild = false;
            word = null;
            uid = 0;
        }

        public boolean isEnd() {
            return word != null;
        }

        public void setWord(String w) {
            this.word = w;
        }

        public TrieNode get(char c) {
            return next[c - 'A'];
        }

        public void put(char c) {
            next[c - 'A'] = new TrieNode();
            hasChild = true;
        }

        public boolean contains(char c) {
            return next[c - 'A'] != null;
        }

        public boolean hasChild() {
            return hasChild;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getUid() {
            return this.uid;
        }

        public String getWord() {
            return word;
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
            throw new IllegalArgumentException();
        trie = new Trie();
        for (String word : dictionary) {
            trie.insert(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null)
            throw new IllegalArgumentException();
        int m = board.rows();
        int n = board.cols();
        answers = new ArrayList<>();
        uid++;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                clearVistied(visited);
                dfs(i, j, m, n, visited, board, null);
            }
        return new ArrayList<>(answers);
    }

    private void dfs(int x, int y, int m, int n, boolean[][] visited, BoggleBoard board, TrieNode cache) {
        char c = board.getLetter(x, y);
        visited[x][y] = true;
        TrieNode node = trie.prefixNode(c, cache);
        if (node != null) {
            if (node.isEnd() && node.getUid() != uid) {
                String word = node.getWord();
                answers.add(word);
                node.setUid(uid);
            }
            if (node.hasChild()) {
                for (int i = -1; i < 2; i++)
                    for (int j = -1; j < 2; j++) {
                        if (isValid(x + i, y + j, m, n) && !visited[x + i][y + j])
                            dfs(x + i, y + j, m, n, visited, board, node);
                    }
            }
        }
        visited[x][y] = false;
    }

    private boolean isValid(int x, int y, int m, int n) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    private void clearVistied(boolean[][] visited) {
        for (boolean[] row : visited)
            Arrays.fill(row, false);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return trie.search(word) ? scoreOfGen(word) : 0;
    }

    private int scoreOfGen(String word) {
        int len = word.length();
        int score;
        switch (len) {
            case 0:
            case 1:
            case 2:
                score = 0;
                break;
            case 3:
            case 4:
                score = 1;
                break;
            case 5:
                score = 2;
                break;
            case 6:
                score = 3;
                break;
            case 7:
                score = 5;
                break;
            default:
                score = 11;
                break;
        }
        return score;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
