import java.util.Arrays;

public class Brute 
{
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
            Point pi = points[i];
            
            for (int j=i+1; j<N; ++j)
            {
                Point  pj = points[j];
                double sj = pi.slopeTo(pj);
                
                for (int k=j+1; k<N; ++k)
                {
                    Point  pk = points[k];
                    double sk = pi.slopeTo(pk);
                    
                    if (sk!=sj)
                        continue;
                    
                    for (int l=k+1; l<N; ++l)
                    {
                        Point  pl = points[l];
                        double sl = pi.slopeTo(pl);
                        
                        if (sl!=sj)
                            continue;
                        else
                        {
                            pi.drawTo(pl);
                            String s   = pi.toString();
                            s+= " -> " + pj.toString();
                            s+= " -> " + pk.toString();
                            s+= " -> " + pl.toString();
                            System.out.println(s);
                        }
                        
                    }
                }
            }
        }
        
        // display to screen all at once
        StdDraw.show(0);

        // reset the pen radius
        StdDraw.setPenRadius();
    }
}
    