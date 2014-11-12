public class Board {
    private int[][] tiles;
    private int N;
    private int blankX;
    private int blankY;

    // Construct a board from an N-by-N array of blocks
    // where blocks[i][j] = block in row i, column j
    public Board(int[][] blocks) {
        N = blocks.length;
        tiles = new int[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                tiles[i][j] = blocks[i][j];
                if (tiles[i][j] == 0) {
                    blankX = i;
                    blankY = j;
                }
            }
        }
    }

    // Board dimension N
    public int dimension() {
        return N;
    }

    // Number of blocks out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if ((i != blankX || j != blankY) && tiles[i][j] != i * N + j + 1) {
                    ++hamming;
                }
            }
        }
        return hamming;
    }

    // Sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (i != blankX || j != blankY) {
                    int goalI = (tiles[i][j] - 1) / N;
                    int goalJ = (tiles[i][j] - 1) % N;
                    manhattan += Math.abs(goalI - i) + Math.abs(goalJ - j);
                }
            }
        }
        return manhattan;
    }

    // Is this board the goal board
    public boolean isGoal() {
        return hamming() == 0;
    }

    // A board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        Board twin = new Board(tiles);
        if (blankX != 0) {
            int swap = twin.tiles[0][0];
            twin.tiles[0][0] = twin.tiles[0][1];
            twin.tiles[0][1] = swap;
        }
        else {
            int swap = twin.tiles[1][0];
            twin.tiles[1][0] = twin.tiles[1][1];
            twin.tiles[1][1] = swap;
        }
        return twin;
    }

    // Does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (N != that.N) {
            return false;
        }
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // All neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<Board>();

        if (blankX != 0) {
            Board neighbor = new Board(tiles);
            int swap = neighbor.tiles[blankX][blankY];
            neighbor.tiles[blankX][blankY] = neighbor.tiles[blankX - 1][blankY];
            neighbor.tiles[blankX - 1][blankY] = swap;
            --neighbor.blankX;
            neighbors.enqueue(neighbor);
        }

        if (blankX != N - 1) {
            Board neighbor = new Board(tiles);
            int swap = neighbor.tiles[blankX][blankY];
            neighbor.tiles[blankX][blankY] =  neighbor.tiles[blankX + 1][blankY];
            neighbor.tiles[blankX + 1][blankY] = swap;
            ++neighbor.blankX;
            neighbors.enqueue(neighbor);
        }

        if (blankY != 0) {
            Board neighbor = new Board(tiles);
            int swap = neighbor.tiles[blankX][blankY];
            neighbor.tiles[blankX][blankY] = neighbor.tiles[blankX][blankY - 1];
            neighbor.tiles[blankX][blankY - 1] = swap;
            --neighbor.blankY;
            neighbors.enqueue(neighbor);
        }

        if (blankY != N - 1) {
            Board neighbor = new Board(tiles);
            int swap = neighbor.tiles[blankX][blankY];
            neighbor.tiles[blankX][blankY] = neighbor.tiles[blankX][blankY + 1];
            neighbor.tiles[blankX][blankY + 1] = swap;
            ++neighbor.blankY;
            neighbors.enqueue(neighbor);
        }

        return neighbors;
    }

    // String representation of the board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}