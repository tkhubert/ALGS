

public class PointSET 
{
    SET<Point2D> data;
    
    public PointSET()                               // construct an empty set of points
    {
        data = new SET<Point2D>();
    }
    //
    public boolean isEmpty()                        // is the set empty?
    {
        return data.isEmpty();
    }
    //
    public int size()                               // number of points in the set
    {
        return data.size();
    }
    //
    public void insert(Point2D p)                   // add the point p to the set (if it is not already in the set)
    {
        data.add(p);
    }
    //
    public boolean contains(Point2D p)              // does the set contain the point p?
    {
        return data.contains(p);
    }
    //
    public void draw()                              // draw all of the points to standard draw
    {
        for (Point2D point : data)
            point.draw();
    }
    //
    public Iterable<Point2D> range(RectHV rect)     // all points in the set that are inside the rectangle
    {
        Stack<Point2D> stack = new Stack<Point2D>();
        for (Point2D point : data)
        {
            if (rect.contains(point))
                stack.push(point);
        }
        return stack;        
    }
    //
    public Point2D nearest(Point2D p)               // a nearest neighbor in the set to p; null if set is empty
    {
        Point2D nearestPoint = data.max();
        double  distance     = p.distanceSquaredTo(nearestPoint);
        
        for (Point2D point : data)
        {
            double d = p.distanceSquaredTo(point);
            if (d<distance)
            {
                distance = d;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }
    //
}