import java.util.Arrays;

public class Brute 
{
	public static void main(String[] args)
	{
		// rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);

        // read in the input
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] pArray = new Point[N];
        for (int i = 0; i < N; i++) 
        {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            pArray[i] = p;
            p.draw();
        }

        // display to screen all at once
        StdDraw.show(0);
        
        Arrays.sort(pArray);
        
        for (int i = 0; i < N; i++)
        	for (int j = i + 1; j < N; j++)
        		for (int k = j + 1; k < N; k++)
        			for (int l = k + 1; l < N; l++)
        			{
        				if (pArray[i].slopeTo(pArray[j]) == pArray[i].slopeTo(pArray[k])
        					&& pArray[i].slopeTo(pArray[k]) == pArray[i].slopeTo(pArray[l]))
        				{
        					pArray[i].drawTo(pArray[l]);
        					StdOut.printf("%s -> %s -> %s -> %s\n", pArray[i], pArray[j], pArray[k], pArray[l]);
        				}
        			}
        
        StdDraw.show(0);
        			
		in.close();
	}
}
