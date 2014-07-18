
import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new BySlope();       // YOUR DEFINITION HERE

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate
    
    private class BySlope implements Comparator<Point>
    {
        public int compare(Point one, Point two)
        {
            double slope1 = slopeTo(one);
            double slope2 = slopeTo(two);
            
            if (slope1<slope2)
                return -1;
            else if (slope1==slope2)
                return 0;
            else
                return 1;           
        }
    }
    //
    public Point(int x, int y) // create the point (x, y)
    {
        this.x = x;
        this.y = y;
    }
    //
    public void draw() // plot this point to standard drawing
    {
        StdDraw.point(x, y);
    }
    //
    public void drawTo(Point that) // draw line between this point and that point to standard drawing
    {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }
    //
    public double slopeTo(Point that) // slope between this point and that point
    {
        double tmp = 1.;
        double posZero =  (tmp-tmp)/tmp;
        double posInf  =  (tmp)/(0);
        double negInf  = -(tmp)/(0);
        
        if (that.y==this.y)
        {
            if (that.x == this.x)
                return negInf;
            else                     
                return posZero;
        }
        else if (that.x==this.x)
            return posInf;
        else
            return (that.y-this.y)/(that.x-this.x);
    }
    // 
    public int compareTo(Point that) // is this point lexicographically smaller than that one? comparing y-coordinates and breaking ties by x-coordinates
    {
        if (this.y < that.y)
            return -1;
        else if (this.y == that.y)
        {
            if (this.x < that.x)
                return -1;
            else if (this.x == that.x)
                return 0;
            else
                return 1;
        }
        else
            return 1;
    }
    //
    public String toString() // return string representation of this point
    {
        return "(" + x + ", " + y + ")";
    }
    //
    public static void main(String[] args) // unit test
    {
        Point p0 = new Point(0, 0);
        Point p1 = new Point(0, 1);
        Point p2 = new Point(1, 0);
        Point p3 = new Point(0, 0);
        
        System.out.println(p0.slopeTo(p1));
        System.out.println(p0.slopeTo(p2));
        System.out.println(p0.slopeTo(p3));
    }
}