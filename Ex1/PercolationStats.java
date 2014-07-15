public class PercolationStats 
{    
    private double[]    res;

    public PercolationStats(int N, int T)    // perform T independent computational experiments on an N-by-N grid
    {      
        if (N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException("N and T should be both strictly positive");
        
        res = new double[T];
        
        double fraction = 1./(N*N);
        for (int n=0 ; n < T; ++n)
        {
            Percolation p = new Percolation(N);
            
            int count = 0;
            while(!p.percolates())
            {
                int i = StdRandom.uniform(1, N+1);
                int j = StdRandom.uniform(1, N+1);
                
                if (!p.isOpen(i,j))
                {
                    p.open(i,j);
                    count++;
                }
            }
            res[n] = count*fraction;
        }
    }
    //
    public double mean()                     // sample mean of percolation threshold
    {
        return StdStats.mean(res);
    }
    //
    public double stddev()                   // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(res);
    }
    //
    public double confidenceLo()             // returns lower bound of the 95% confidence interval
    {
        double m = mean  ();
        double s = stddev();
        
        return m-1.96*s/Math.sqrt(res.length);
    }
    //
    public double confidenceHi()             // returns upper bound of the 95% confidence interval
    {
        double m = mean  ();
        double s = stddev();
        
        return m+1.96*s/Math.sqrt(res.length);        
    }
    //
    public static void main(String[] args)   // test client, described below
    {
        if (args.length!=2)
            throw new java.lang.IllegalArgumentException("should have exactly two integers as inputs");
        
        int N, T;
        try
        {
            N = Integer.parseInt(args[0]);
            T = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException nfe) 
        {
            throw new java.lang.IllegalArgumentException("should have exactly two integers as inputs");
        }
        
        
        PercolationStats pStats = new PercolationStats(N, T);
        
        System.out.println("mean:                    "+ pStats.mean());
        System.out.println("stddev:                  "+ pStats.stddev());
        System.out.println("95% confidence interval: "+ pStats.confidenceLo()+","+pStats.confidenceHi());
    }
}