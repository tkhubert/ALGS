import java.util.Arrays;

public class Fast
{
    private static int binarySearch(Point[] array, Point origin, double slope)
    {   
        int lo = 0;
        int hi = array.length - 1;
        while (lo <= hi) 
        {
            int mid = lo + (hi - lo) / 2;
            if (slope < origin.slopeTo(array[mid]))
                hi = mid - 1;
            else if (slope > origin.slopeTo(array[mid]))
                lo = mid + 1;
            else 
                return mid;
        }
        return -1;     
    }
    //
    public static void main(String[] args)
    {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        StdDraw.setPenRadius(0.01);  // make the points a bit larger

        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        
        Point[] points = new Point[N];
        
        for (int i=0; i<N; ++i) 
        {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            points[i] = p;
            p.draw();
        }
        
        Arrays.sort(points);   
       
          
        for (int i=0; i<N; ++i)
        {
            Point p_ori = points[i];
            
            Point[] sameSlopePoints = new Point[N-i];
            sameSlopePoints[0] = p_ori;
            
            Point[] fwdPoints = new Point[N-i-1];
            Point[] bckPoints = new Point[i];
            
            for (int j=0; j<i; ++j)
                bckPoints[j] = points[j];
            for (int j=i+1; j<N; ++j)
                fwdPoints[j-i-1] = points[j];
            
            Arrays.sort(bckPoints, p_ori.SLOPE_ORDER);
            Arrays.sort(fwdPoints, p_ori.SLOPE_ORDER);
            
            int    count = 1;
            double currentSlope =  N-1-i>0 ? p_ori.slopeTo(fwdPoints[0]) : 0;
            
            for (int j=0; j<N-1-i; ++j)
            {
                Point pj = fwdPoints[j];
                double slope = p_ori.slopeTo(pj);
                
                if (slope==currentSlope)
                {
                    sameSlopePoints[count] = pj;
                    count++;
                }
                
                if (slope!=currentSlope || j==N-2-i)
                {
                    if (count>=4)
                    {
                        int hasAlreadySeen = binarySearch(bckPoints, p_ori, currentSlope);
                                                
                        if (hasAlreadySeen==-1)
                        {
                            Arrays.sort(sameSlopePoints, 0, count);
                            sameSlopePoints[0].drawTo(sameSlopePoints[count-1]);
                            
                            String s = sameSlopePoints[0].toString();
                            for (int k=1; k<count; ++k)
                                s   +=  " -> " + sameSlopePoints[k].toString();
                            System.out.println(s);
                        }
                    }
                    sameSlopePoints[0] = p_ori;
                    sameSlopePoints[1] = pj;
                    
                    count=2;
                    currentSlope = slope;
                }
            }
        }
                
        // display to screen all at once
        StdDraw.show(0);

        // reset the pen radius
        StdDraw.setPenRadius();
    }
}