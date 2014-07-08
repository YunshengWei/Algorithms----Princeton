import java.util.HashSet;

public class Solver 
{
    private boolean solvable = false;
    private Stack<Board> solSeq;
    
    private class Key implements Comparable<Key>
    {
        Board board;
        int cost;
        Key prev;
        
        public Key(Board board, int cost, Key prev)
        {
            this.board = board;
            this.cost = cost;
            this.prev = prev;
        }
        
        @Override
        public int compareTo(Key that) 
        {
            int prior1 = board.manhattan() + cost;
            int prior2 = that.board.manhattan() + that.cost;
            if (prior1 == prior2) return 0;
            if (prior1 < prior2) return -1;
            return 1;
        }
    }
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        Board twinInitial = initial.twin();
        
        MinPQ<Key> minPQ1 = new MinPQ<Key>();
        MinPQ<Key> minPQ2 = new MinPQ<Key>();
            
        minPQ1.insert(new Key(initial, 0, null));
        minPQ2.insert(new Key(twinInitial, 0, null));
        HashSet<Board> hs1 = new HashSet<Board>();
        HashSet<Board> hs2 = new HashSet<Board>();
        
        while (true)
        {
            Key key1 = minPQ1.delMin();
            Key key2 = minPQ2.delMin();
            if (key2.board.isGoal()) 
            {
                solvable = false;
                return;
            }
            if (key1.board.isGoal())
            {
                solvable = true;
                solSeq = new Stack<Board>();
                do
                {
                    solSeq.push(key1.board);
                    key1 = key1.prev;
                } while (key1 != null);
                return;
            }
            
            if (!hs1.contains(key1.board))
            {   
                hs1.add(key1.board);
                for (Board neighbor : key1.board.neighbors())
                {
                    if (!hs1.contains(neighbor))
                        minPQ1.insert(new Key(neighbor, key1.cost + 1, key1));
                }
            }
            if (!hs2.contains(key2.board))
            {   
                hs2.add(key2.board);
                for (Board neighbor : key2.board.neighbors())
                {
                    if (!hs2.contains(neighbor))
                        minPQ2.insert(new Key(neighbor, key2.cost + 1, key2));
                }
            }
        }   
    }
    
	// is the initial board solvable?
	public boolean isSolvable()
	{
	    return solvable;	   
	}
	
	// min number of moves to solve initial board; -1 if no solution
	public int moves()
	{
	    if (solSeq == null)
	        return -1;
	    else
	        return solSeq.size() - 1;
	}
	
	// sequence of boards in a shortest solution; null of no solution
	public Iterable<Board> solution()
	{
	    return solSeq;
	}
	
	// solve a slider puzzle (given below)
	public static void main(String[] args)
	{
	    // create initial board from file
	    In in = new In(args[0]);
	    int N = in.readInt();
	    int[][] blocks = new int[N][N];
	    for (int i = 0; i < N; i++)
	        for (int j = 0; j < N; j++)
	            blocks[i][j] = in.readInt();
	    in.close();
	    Board initial = new Board(blocks);
	    
	    //****************************************
	    Stopwatch sw = new Stopwatch();
	    //****************************************
	    
	    // solve the puzzle
	    Solver solver = new Solver(initial);
	    
	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else
	    {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	    
	    //****************************************
        StdOut.println(sw.elapsedTime());
        //****************************************
	}
}
