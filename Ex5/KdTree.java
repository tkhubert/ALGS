public class KdTree 
{
    private static final boolean SEARCH_X  = true;
    private static final boolean SEARCH_Y  = false;
    private static final RectHV  UNIT_RECT = new RectHV(0., 0., 1., 1.);
    
    private int  N;
    private Node root;
    
    private static class Node 
    {
        private Point2D p;        // the point
        private RectHV  rect;     // the axis-aligned rectangle corresponding to this node
        private Node    left;     // the left/bottom subtree
        private Node    right;    // the right/top subtree
        
        Node(Point2D p, RectHV rect, Node left, Node right)
        {
            this.p     = p;
            this.rect  = rect;
            this.left  = left;
            this.right = right;
        }
    }
    
    public KdTree()                                 // construct an empty set of points
    {
        N    = 0;
        root = null;
    }
    //
    public boolean isEmpty()                        // is the set empty?
    {
        return N==0;
    }
    //
    public int size()                               // number of points in the set
    {
        return N;
    }
    //
    public void insert(Point2D p)                   // add the point p to the set (if it is not already in the set)
    {
        root = insert(p, root, UNIT_RECT, SEARCH_X);      
    }
    //
    private Node insert(Point2D p, Node n, RectHV rect, boolean SEARCH_AXIS)
    {
        if (n==null)
        {
            n = new Node(p, rect, null, null);
            N++;
            return n;
        }
        
        if (!p.equals(n.p))
        {
            boolean searchLeft = SEARCH_AXIS==SEARCH_X ? p.x()<n.p.x() : p.y()<n.p.y();
            
            if (searchLeft)
            {
                RectHV childRect = n.left==null ? buildRectFromParentNode(n, searchLeft, SEARCH_AXIS) : n.left.rect;
                n.left = insert(p, n.left, childRect, !SEARCH_AXIS);
            }
            else
            {
                RectHV childRect = n.right==null ? buildRectFromParentNode(n, searchLeft, SEARCH_AXIS) : n.right.rect;
                n.right = insert(p, n.right, childRect, !SEARCH_AXIS);
            }
        }
        return n;
    }
    //
    private RectHV buildRectFromParentNode(Node n, boolean searchLeft, boolean SEARCH_AXIS)
    {
        double  xmin = (SEARCH_AXIS==SEARCH_X && !searchLeft) ? n.p.x() : n.rect.xmin();
        double  xmax = (SEARCH_AXIS==SEARCH_X &&  searchLeft) ? n.p.x() : n.rect.xmax();
        double  ymin = (SEARCH_AXIS==SEARCH_Y && !searchLeft) ? n.p.y() : n.rect.ymin();
        double  ymax = (SEARCH_AXIS==SEARCH_Y &&  searchLeft) ? n.p.y() : n.rect.ymax();
        return new RectHV(xmin, ymin, xmax, ymax); 
    }
    //
    public boolean contains(Point2D p)              // does the set contain the point p?
    {
        return contains(p, root, SEARCH_X);
    }
    //
    private boolean contains(Point2D p, Node n, boolean SEARCH_AXIS)
    {
        if (n==null)
            return false;
        if (p.equals(n.p))
            return true;
        
        boolean searchLeft = SEARCH_AXIS==SEARCH_X ? p.x()<n.p.x() : p.y()<n.p.y();
        
        if (searchLeft)
            return contains(p, n.left, !SEARCH_AXIS);
        else
            return contains(p, n.right, !SEARCH_AXIS);
    }
    //
    public void draw()                              // draw all of the points to standard draw
    {
        draw(root, SEARCH_X);
    }
    //
    private void draw(Node n, boolean SEARCH_AXIS)
    {
        if (n==null)
            return;
        
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.p.draw();
        
        StdDraw.setPenRadius();
        if (SEARCH_AXIS==SEARCH_X)
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
        
        draw(n.left,  !SEARCH_AXIS);
        draw(n.right, !SEARCH_AXIS);
    }
    //
    public Iterable<Point2D> range(RectHV rect)     // all points in the set that are inside the rectangle
    {
        Stack<Point2D> stack = new Stack<Point2D>();
        stack = searchForRange(root, rect, stack);
        return stack;
    }
    //
    private Stack<Point2D> searchForRange(Node n, RectHV rect, Stack<Point2D> stack)
    {
        if (n==null || !rect.intersects(n.rect))
            return stack;
        
        if (rect.contains(n.p))
            stack.push(n.p);
        
        stack = searchForRange(n.left , rect, stack);
        stack = searchForRange(n.right, rect, stack);
        return stack;
    }
    //
    public Point2D nearest(Point2D p)               // a nearest neighbor in the set to p; null if set is empty
    {
        if (root==null)
            return null;
        
        double  nearestDist  = root.p.distanceSquaredTo(p);
        Point2D nearestPoint = searchForNearest(p, root, root.p, nearestDist);        
        return  nearestPoint;
    }
    //
    private Point2D searchForNearest(Point2D p, Node n, Point2D currentBestPoint, double currentBestDist)
    {
        if (n==null)
            return currentBestPoint;
        
        double distanceToPoint = n.p.distanceSquaredTo(p);
        if (distanceToPoint<currentBestDist)
        {
            currentBestDist = distanceToPoint;
            currentBestPoint = n.p;
        }
        
        double distanceToLeft  = n.left != null ? n.left.rect.distanceSquaredTo(p) : currentBestDist+1;
        double distanceToRight = n.right!= null ? n.right.rect.distanceSquaredTo(p): currentBestDist+1;
        
        if (distanceToLeft>currentBestDist && distanceToRight>currentBestDist)
            return currentBestPoint;
        
        if (distanceToLeft<distanceToRight)
        {
            currentBestPoint = searchForNearest(p, n.left, currentBestPoint, currentBestDist);
            currentBestDist  = currentBestPoint.distanceSquaredTo(p);
            currentBestPoint = searchForNearest(p, n.right, currentBestPoint, currentBestDist);          
        }
        else
        {
            currentBestPoint = searchForNearest(p, n.right, currentBestPoint, currentBestDist);
            currentBestDist  = currentBestPoint.distanceSquaredTo(p);
            currentBestPoint = searchForNearest(p, n.left, currentBestPoint, currentBestDist);
        }
        
        return currentBestPoint;
    }
}