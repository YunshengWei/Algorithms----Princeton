public class BurrowsWheeler
{
    private static final char R = 256;
    
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode()
    {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int i = 0;
        while (csa.index(i) != 0) i++;
        BinaryStdOut.write(i);
        for (i = 0; i < csa.length(); i++) {
            BinaryStdOut.write(s.charAt((csa.index(i)
                    + s.length() - 1) % s.length()));
        }
        BinaryStdOut.flush();
    }
    
    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode()
    {
        int i = BinaryStdIn.readInt();
        char[] a = BinaryStdIn.readString().toCharArray();
        int N = a.length;
        int[] count = new int[R + 1];
        int[] next = new int[N];
        for (int j = 0; j < N; j++)
            count[a[j] + 1]++;
        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];
        for (int j = 0; j < N; j++)
            next[count[a[j]]++] = j;
        
        for (int j = 0; j < N; j++)
        {
            i = next[i];
            BinaryStdOut.write(a[i]);
        }
        BinaryStdOut.flush();
    }
    
    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args)
    {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}
