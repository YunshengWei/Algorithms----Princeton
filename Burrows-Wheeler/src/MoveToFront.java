
public class MoveToFront
{
    private static final char R = 256;
    
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode()
    {
        char[] a = new char[R];
        for (char i = 0; i < R; i++)
            a[i] = i;
        
        while (!BinaryStdIn.isEmpty())
        {
            char ch = BinaryStdIn.readChar();
            int i = 0;
            while (a[i] != ch) i++;
            BinaryStdOut.write((char) i);
            for (int j = i; j > 0; j--)
                a[j] = a[j - 1];
            a[0] = ch;
        }
        BinaryStdOut.flush();
    }
    
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
        char[] a = new char[R];
        for (char i = 0; i < R; i++)
            a[i] = i;
        
        while (!BinaryStdIn.isEmpty())
        {
            int i = BinaryStdIn.readChar();
            char ch = a[i];
            BinaryStdOut.write(ch);
            for (int j = i; j > 0; j--)
                a[j] = a[j - 1];
            a[0] = ch;
        }
        BinaryStdOut.flush();
    }
    
    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args)
    {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}
