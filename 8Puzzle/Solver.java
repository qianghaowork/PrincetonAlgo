import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private class SearchNode implements Comparable<SearchNode> {
        private Board board = null;
        private int move = -1;
        private SearchNode prev = null;
        private int priorityCache = -1;

        SearchNode(Board board) {
            if (board == null)
                return;
            this.board = board;
            this.priorityCache = board.manhattan();
            this.move = 0;
        }

        SearchNode(Board board, SearchNode prev) {
            this.board = board;
            this.prev = prev;
            this.move = prev.move + 1;
            this.priorityCache = board.manhattan();
        }

        public int compareTo(SearchNode that) {
            int thisMan = this.priorityCache >= 0 ? this.priorityCache : this.board.manhattan();
            int thatMan = that.priorityCache >= 0 ? that.priorityCache : that.board.manhattan();
            if (thisMan + this.move < thatMan + that.move) return -1;
            if (thisMan + this.move > thatMan + that.move) return 1;
            return 0;
        }
    }

    private SearchNode solutionNode = new SearchNode(null);

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("null argument.");
        solutionNode = new SearchNode(initial);
        SearchNode twinSolutionNode = new SearchNode(initial.twin());
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinPq = new MinPQ<>();
        pq.insert(solutionNode);
        twinPq.insert(twinSolutionNode);
        while (true) {
            solutionNode = pq.delMin();
            twinSolutionNode = twinPq.delMin();

            if (twinSolutionNode.board.isGoal()) {
                solutionNode.move = -1;
                solutionNode.prev = null;
                return;
            }

            if (solutionNode.board.isGoal()) {
                return;
            }
            for (Board b : solutionNode.board.neighbors()) {
                if (solutionNode.prev == null || !b.equals(solutionNode.prev.board))
                    pq.insert(new SearchNode(b, solutionNode));
            }
            for (Board b : twinSolutionNode.board.neighbors()) {
                if (twinSolutionNode.prev == null || !b.equals(twinSolutionNode.prev.board))
                    twinPq.insert(new SearchNode(b, twinSolutionNode));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solutionNode.move >= 0;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solutionNode.move;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        Stack<Board> steps = new Stack<>();
        while (solutionNode != null) {
            steps.push(solutionNode.board);
            solutionNode = solutionNode.prev;
        }
        return steps;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        // In in = new In(args[0]);
        int n = 3;
        int[][] tiles = {
                {0, 1, 3},
                {4, 2, 5},
                {7, 8, 6}
        };
/*        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();*/

        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
