import java.util.Arrays;

public class Fast 
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
		in.close();

		// display to screen all at once
        StdDraw.show(0);
        
        Arrays.sort(pArray);
        Point[] aux = new Point[N];
        
        for (int i = 0; i < N; i++)
        {	
        	Point p = pArray[i];
        	for (int j = 0; j < i; j++)
        		aux[j] = pArray[j];
        	for (int j = i; j < N - 1; j++)
        		aux[j] = pArray[j + 1];
        	// Set the sentinel
        	aux[N - 1] = p;
        	Arrays.sort(aux, 0, N - 1, p.SLOPE_ORDER);
        	int count = 1;
        	double lastSlope = p.slopeTo(p);
        	for (int j = 0; j < N; j++)
        	{
        		double tmpSlope = p.slopeTo(aux[j]);
        		if (tmpSlope != lastSlope)
        		{
        			int k;
        			if (count >= 4 && p.compareTo(aux[k = j - count + 1]) < 0)
        			{
        				StdOut.print(p);
        				for (; k < j; k++)
        					StdOut.printf(" -> %s", aux[k]);
        				StdOut.println();
        				p.drawTo(aux[j - 1]);
        			}
        			count = 1;
        			lastSlope = tmpSlope;
        		}
        		count++;
        	}
        }
        
        StdDraw.show(0);
	}
}
