import java.util.ArrayList;

public class KdTree 
{
    // dummy head
    private Node root;
    private int size;
    
    private static class Node
    {
        // the point
        private Point2D p;
        // the axis-aligned rectangle corresponding to this node
        private RectHV rect;
        // the left/bottom subtree
        private Node lb;
        // the right/top subtree
        private Node rt;
        
        public Node(Point2D p, Node lb, Node rt, RectHV rect)
        {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }
    
    // construct an empty set of points
    public KdTree()
    {
        root = new Node(null, null, null, new RectHV(0, 0, 1, 1));
        size = 0;
    }
    
    // is the set empty?
    public boolean isEmpty()
    {
        return size == 0;
    }
    
    // number of points in the set
    public int size()
    {
        return size;
    }
    
    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p)
    { 
        root = insert(root, p, true);
    }
    
    private Node insert(Node x, Point2D p, boolean flag)
    {
        if (x.p == null)
        {
            size++;
            x.p = p;
            if (flag)
            {
                x.lb = new Node(null, null, null, 
                new RectHV(x.rect.xmin(), x.rect.ymin(),
                           p.x(), x.rect.ymax()));
                x.rt = new Node(null, null, null,
                new RectHV(p.x(), x.rect.ymin(),
                           x.rect.xmax(), x.rect.ymax()));
            }
            else
            {
                x.lb = new Node(null, null, null, 
                        new RectHV(x.rect.xmin(), x.rect.ymin(),
                                   x.rect.xmax(), p.y()));
                x.rt = new Node(null, null, null,
                new RectHV(x.rect.xmin(), p.y(),
                           x.rect.xmax(), x.rect.ymax()));
            }
        }
        if (p.equals(x.p)) return x;
        boolean cmp = flag ? p.x() < x.p.x() : p.y() < x.p.y();
        if (cmp)
            x.lb = insert(x.lb, p, !flag);
        else
            x.rt = insert(x.rt, p, !flag);
        return x;
    }
    
    // does the set contain the point p?
    public boolean contains(Point2D p)
    {
        Node x = root;
        boolean flag = true;
        while (x.p != null)
        {
            if (x.p.equals(p)) return true;
            boolean cmp = flag ? p.x() < x.p.x() : p.y() < x.p.y();
            flag = !flag;
            if (cmp)
                x = x.lb;
            else 
                x = x.rt;
        }
        return false;
    }
    
    // draw all of the points to standard draw
    public void draw()
    {
        draw(root, true);
    }
    
    private void draw(Node x, boolean flag)
    {
        if (x.p == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        x.p.draw();
        StdDraw.setPenRadius();
        if (flag)
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        
        draw(x.lb, !flag);
        draw(x.rt, !flag);
    }
    
    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect)
    {
        ArrayList<Point2D> pal = new ArrayList<Point2D>();
        range(root, rect, pal);
        return pal;
    }
    
    private void range(Node x, RectHV rect, ArrayList<Point2D> pal)
    {
        if (x.p == null) return;
        if (!rect.intersects(x.rect)) return;
        if (rect.contains(x.p)) pal.add(x.p);
        range(x.lb, rect, pal);
        range(x.rt, rect, pal);
    }
    
    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p)
    {
        return nearest(root, p, root.p, true);
    }
    
    private Point2D nearest(Node x, Point2D p, Point2D np, boolean flag)
    {
        Point2D tnp = np;
        if (x.p == null || p.distanceSquaredTo(np) < x.rect.distanceSquaredTo(p)) return tnp;
        if (p.distanceSquaredTo(x.p) < p.distanceSquaredTo(np))
            tnp = x.p;
        boolean cmp = flag ? p.x() < x.p.x() : p.y() < x.p.y();
        if (cmp)
        {  
            tnp = nearest(x.lb, p, tnp, !flag);
            tnp = nearest(x.rt, p, tnp, !flag);
        }
        else
        {
            tnp = nearest(x.rt, p, tnp, !flag);
            tnp = nearest(x.lb, p, tnp, !flag);
        }
        return tnp;
    }
}
