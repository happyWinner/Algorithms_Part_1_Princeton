public class Solver {
    private MinPQ<SearchNode> gameTree;
    private Stack<Board> solution;
    private int movesNumber;

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int movesNumber;
        private SearchNode previous;

        private SearchNode(Board board, int movesNumber, SearchNode previous) {
            this.board = board;
            this.movesNumber = movesNumber;
            this.previous = previous;
        }

        public int compareTo(SearchNode that) {
            int value1 = board.manhattan() + movesNumber;
            int value2 = that.board.manhattan() + that.movesNumber;
            if (value1 > value2) {
                return 1;
            }
            else if (value1 < value2) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }

    // Find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        movesNumber = -1;
        solution = null;
        gameTree = new MinPQ<SearchNode>();
        gameTree.insert(new SearchNode(initial, 0, null));
        MinPQ<SearchNode> gameTreeTwin = new MinPQ<SearchNode>();
        gameTreeTwin.insert(new SearchNode(initial.twin(), 0, null));

        boolean bGoal = false;
        while (!bGoal) {
            SearchNode node = gameTree.delMin();
            if (node.board.isGoal()) {
                bGoal = true;
                movesNumber = node.movesNumber;
                solution = new Stack<Board>();
                while (node != null) {
                    solution.push(node.board);
                    node = node.previous;
                }
            }
            else {
                for (Board board : node.board.neighbors()) {
                    if (node.previous == null || !board.equals(node.previous.board)) {
                        gameTree.insert(new SearchNode(board, node.movesNumber + 1, node));
                    }
                }
            }

            SearchNode nodeTwin = gameTreeTwin.delMin();
            if (nodeTwin.board.isGoal()) {
                bGoal = true;
            }
            else {
                for (Board board : nodeTwin.board.neighbors()) {
                    if (nodeTwin.previous == null || !board.equals(nodeTwin.previous.board)) {
                        gameTreeTwin.insert(new SearchNode(board, nodeTwin.movesNumber + 1, nodeTwin));
                    }
                }
            }
        }
    }

    // Is the initial board solvable?
    public boolean isSolvable() {
        return !(movesNumber == -1);
    }

    // Minimum number of moves to solve initial board; -1 if no solution
    public int moves() {
        return movesNumber;
    }

    // Sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {
        return solution;
    }

    // Solve a slider puzzle
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        }
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}