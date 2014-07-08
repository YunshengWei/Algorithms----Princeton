import java.util.TreeSet;
import java.util.ArrayList;

public class PointSET 
{
    private TreeSet<Point2D> ps;
    
    // construct an empty set of points
    public PointSET()
    {
        ps = new TreeSet<Point2D>();
    }
    
    // is the set empty?
    public boolean isEmpty()
    {
        return ps.isEmpty();
    }
    
    // number of points in the set
    public int size()
    {
        return ps.size();
    }
    
    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        ps.add(p);
    }
    
    // does the set contain the point p?
    public boolean contains(Point2D p)
    {
        return ps.contains(p);
    }
    
    // draw all of the points to standard draw
    public void draw()
    {
        for (Point2D p : ps)
            p.draw();
    }
    
    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect)
    {
        ArrayList<Point2D> pal = new ArrayList<Point2D>();
        for (Point2D p : ps)
            if (rect.contains(p))
                pal.add(p);
        return pal;
    }
    
    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p)
    {
        double min = Double.MAX_VALUE;
        Point2D np = null;
        for (Point2D pt : ps)
            if (pt.distanceTo(p) < min)
            {
                min = pt.distanceTo(p);
                np = pt;
            }
        return np;
    }
}
