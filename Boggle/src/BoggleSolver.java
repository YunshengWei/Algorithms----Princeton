import java.util.ArrayList;

public class BoggleSolver
{
    private final TrieSET_26 dict;
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        dict = new TrieSET_26();
        for (String word : dictionary)
        {    
            if (word.length() >= 3)
                dict.add(word);
        }
    }
    
    static private final class Tuple
    {
        private int x;
        private int y;
        Tuple(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }
    
    private void getAllValidWordsRecursive(BoggleBoard board, TrieSET_26 result, 
            boolean[][] closed, Tuple[][][] adj, StringBuilder prefix, int r, int c)
    {
        if (closed[r][c])
            return;
        closed[r][c] = true;
        char letter = board.getLetter(r, c);
        prefix.append(letter);
        if (letter == 'Q')
            prefix.append('U');
        if (dict.hasPrefix(prefix.toString()))
        {
            if (dict.contains(prefix.toString()))
                result.add(prefix.toString());
            for (Tuple next : adj[r][c])
                getAllValidWordsRecursive(board, result, closed, adj, prefix, next.x, next.y);
        }
        closed[r][c] = false;
        prefix.deleteCharAt(prefix.length() - 1);
        if (letter == 'Q')
            prefix.deleteCharAt(prefix.length() - 1);
    }
    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        boolean[][] closed = new boolean[board.rows()][board.cols()];
        Tuple[][][] adj = new Tuple[board.rows()][board.cols()][];
        StringBuilder prefix = new StringBuilder();
        
        // Precompute adjacent cubes to each cube
        for (int r = 0; r < board.rows(); r++)
        {
            for (int c = 0; c < board.cols(); c++)
            {
                ArrayList<Tuple> tmp = new ArrayList<Tuple>();
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                    {
                        int x = r + i;
                        int y = c + j;
                        if (x >= 0 && x < board.rows() && y >= 0 && y < board.cols() && !(i == 0 && j == 0))
                            tmp.add(new Tuple(x, y));
                    }
                adj[r][c] = new Tuple[tmp.size()];
                for (int i = 0; i < adj[r][c].length; i++)
                    adj[r][c][i] = tmp.get(i);
            }
        }
        
        TrieSET_26 result = new TrieSET_26();
        
        for (int c = 0; c < board.cols(); c++)
            for (int r = 0; r < board.rows(); r++)
                getAllValidWordsRecursive(board, result, closed, adj, prefix, r, c);
        
        return result;
    }
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        if (!dict.contains(word))
            return 0;
            
        switch (word.length())
        {
        case 0 :
        case 1 :
        case 2 : return 0;
        case 3 :
        case 4 : return 1;
        case 5 : return 2;
        case 6 : return 3;
        case 7 : return 5;
        default: return 11;
        }
    }
    
    // Unit test
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
