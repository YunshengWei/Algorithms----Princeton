import java.util.Comparator;

public class Point implements Comparable<Point> 
{
    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();       // YOUR DEFINITION HERE

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    //inner Comparator
    private class SlopeOrder implements Comparator<Point>
    {
    	@Override
    	public int compare(Point p1, Point p2)
    	{
    		double t1 = slopeTo(p1);
    		double t2 = slopeTo(p2);
    		if (t1 < t2)
    			return -1;
    		if (t1 == t2)
    			return 0;
    		return 1;
    	}
    }
    
    // create the point (x, y)
    public Point(int x, int y) 
    {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() 
    {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) 
    {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) 
    {
        /* YOUR CODE HERE */
    	if (that.x == x)
    		if (that.y == y) return Double.NEGATIVE_INFINITY;
    		else return Double.POSITIVE_INFINITY;
    	if (that.y == y) return +0;
    	return (that.y - y) / (double) (that.x - x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    @Override
    public int compareTo(Point that) 
    {
        /* YOUR CODE HERE */
    	if (y < that.y) return -1;
    	if (y == that.y)
    		if (x < that.x) return -1;
    		else if (x == that.x) return 0;
    	return 1;
    }

    // return string representation of this point
    public String toString() 
    {
        /* DO NOT MODIFY */
        return String.format("(%d, %d)", x, y);
    }

    // unit test
    public static void main(String[] args) 
    {
        /* YOUR CODE HERE */
    }
}
