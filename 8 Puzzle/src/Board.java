import java.util.Arrays;
import java.util.ArrayList;

public class Board implements Comparable<Board>
{
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private int manhaDist = -1;
    private final int N;
    private final int[][] blocks;
    
    @Override
    public int compareTo(Board that) 
    {
        // ignore consistency check
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (blocks[i][j] < that.blocks[i][j])
                    return -1;
                else if (blocks[i][j] > that.blocks[i][j])
                    return 1;
        return 0;
    }

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j
    public Board(int[][] blocks)
    {
        N = blocks.length;
        this.blocks = new int[N][];
        for (int i = 0; i < N; i++)
            this.blocks[i] = blocks[i].clone();
    }
    
    public int hashCode()
    {
        return Arrays.deepHashCode(blocks);
    }
    
    // board dimension N
    public int dimension()
    {
        return N;
    }
    
    // number of blocks out of place
    public int hamming()
    {
        int count = 0;
        int t = 1;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
                if (blocks[i][j] != t++)
                    count++;
        }
        return --count;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan()
    {
        if (manhaDist < 0)
        {
            int sum = 0;
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                {
                    int t = blocks[i][j];
                    if (t != 0) sum += dist(i, j);
                }
            manhaDist = sum;
        }
        return manhaDist;
    }
    
    // is this board the goal board?
    public boolean isGoal()
    {
        return manhattan() == 0;
    }
    
    // manhattan distance between (i, j) and the place blocks[i][j] should be
    private int dist(int i, int j)
    {
        int t = blocks[i][j];
        return Math.abs((t - 1) / N - i) + Math.abs((t - 1) % N - j); 
    }
    
    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin()
    {
        Board twinBoard = new Board(blocks);
        int i = 0;
        if (blocks[0][0] == 0 || blocks[0][1] == 0)
            i = 1;
        int t = twinBoard.blocks[i][0];
        twinBoard.blocks[i][0] = twinBoard.blocks[i][1];
        twinBoard.blocks[i][1] = t;
        twinBoard.manhaDist = manhattan() - dist(i, 0) - dist(i, 1) + twinBoard.dist(i, 0) + twinBoard.dist(i, 1);
        return twinBoard;
    }
    
    // does this board equal y?
    @Override
    public boolean equals(Object y)
    {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        return Arrays.deepEquals(blocks, ((Board) y).blocks);
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        int i = 0, j = 0;
     L: for (i = 0; i < N; i++)
            for (j = 0; j < N; j++)
                if (blocks[i][j] == 0)
                    break L;
        for (int k = 0; k < DIRECTIONS.length; k++)
        {
            int x = i + DIRECTIONS[k][0];
            int y = j + DIRECTIONS[k][1];
            if (x >= 0 && x < N && y >= 0 && y < N)
            {
                Board tb = new Board(blocks);
                tb.blocks[i][j] = tb.blocks[x][y];
                tb.blocks[x][y] = 0;
                tb.manhaDist = manhattan() - dist(x, y) + tb.dist(i, j);
                neighbors.add(tb);
            }
        }
        return neighbors;
    }
    
    // string representation of the board (in the output format specified below)
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(N + "\n");
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
                sb.append(String.format("%3d", blocks[i][j]));
            sb.append("\n");
        }
        return sb.toString();
    }
    
}
