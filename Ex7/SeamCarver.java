
import java.awt.Color;

public class SeamCarver 
{
    private static final double  INFINITY = Double.MAX_VALUE;

    private int [][] nrgM;
    private int [][] rgbM;
    
    private int      W;
    private int      H;
    private boolean  isTransposed;
    
    //
    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        W = picture.width();
        H = picture.height();
        isTransposed = false;
        
        nrgM = new int [H][W];
        rgbM = new int [H][W];
        
        for (int y=0; y<H; ++y)
            for (int x=0; x<W; ++x)
                rgbM[y][x] = picture.get(x,y).getRGB();

        for (int y=0; y<H; ++y)
            for (int x=0; x<W; ++x)
                nrgM[y][x] = energyV(x,y);
    }
    //
    private void Transpose()
    {
        isTransposed = !isTransposed;
        
        int tmp = W;
        W = H;
        H = tmp;
        
        int[][] nrgM_t = new int[H][W];
        int[][] rgbM_t = new int[H][W];        
        
        for (int x=0; x<W; ++x)
        {
            for (int y=0; y<H; ++y)
            {
                rgbM_t[y][x] = rgbM[x][y];
                nrgM_t[y][x] = nrgM[x][y];
            }
        }
        nrgM = nrgM_t;
        rgbM = rgbM_t;
    }
    //
    public Picture picture()                          // current picture
    {
        if (isTransposed)
            Transpose();
        
        Picture curPic = new Picture(W, H);
        for (int y=0; y<H; ++y)
        {
            for (int x=0; x<W; ++x)
            {
                Color col = new Color(rgbM[y][x]);
                curPic.set(x, y, col);
            }
        }
        return curPic;
    }
    //
    public int width()                            // width of current picture
    {
        return !isTransposed ? W : H;
    }
    //
    public int height()                           // height of current picture
    {
        return !isTransposed ? H : W;
    }
    //
    public  double energy(int x, int y)               // energy of pixel at column x and row y
    {
        if (isTransposed)
            return energyV(y,x);
        else
            return energyV(x,y);
    }
    //
    private int energyV(int x, int y)
    {
        if (x<0 || x>W-1 || y<0 || y>H-1)
            throw new java.lang.IndexOutOfBoundsException("x, y are out of bounds in energy");
            
        if (x==0 || x==W-1 || y==0 || y==H-1)
            return 195075;
        
        int gradX = Gradient(x+1, y, x-1, y);
        int gradY = Gradient(x, y+1, x, y-1);
        return gradX + gradY;
    }
    //
    private int Gradient(int x1, int y1, int x2, int y2)
    {
        Color col1 = new Color(rgbM[y1][x1]);
        Color col2 = new Color(rgbM[y2][x2]);
        
        int r = col2.getRed()   - col1.getRed();
        int b = col2.getBlue()  - col1.getBlue();
        int g = col2.getGreen() - col1.getGreen();
        return r*r+b*b+g*g;
    }
    //
    public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        if (!isTransposed)
            Transpose();   
        return findSeam();   
    }
    //
    public int[] findVerticalSeam()                // sequence of indices for vertical seam
    {
        if (isTransposed)
            Transpose();
        return findSeam();
    }
    //
    private int[] findSeam()                 
    {
        int[]      seam   = new int[H];
        int[][]    edgeTo = new int[H][W];
        double[][] distTo = new double[H][W];
        
        for (int y=0; y<H; ++y)
            for (int x=0; x<W; ++x)
                distTo[y][x] = 0.;
        
        for (int y=0; y<H-1; ++y)
        {
            for (int x=0; x<W; ++x)
            {
                double dd = x==0   ? INFINITY : distTo[y][x-1];
                double dm = distTo[y][x];
                double du = x==W-1 ? INFINITY : distTo[y][x+1];

                int idx = 0;                
                if (dd<dm && dd<du)
                    idx = x-1;
                else if (du<dd && du<dm)
                    idx = x+1;
                else
                    idx = x;
                
                edgeTo[y+1][x] = idx;
                distTo[y+1][x] = distTo[y][idx] + nrgM[y+1][x];
            }
        }
        
        double distMin = INFINITY;
        int    idxMin  = -1;
        for (int x=0; x<W; ++x)
        {
            if (distTo[H-1][x]<distMin)
            {
                idxMin = x;
                distMin = distTo[H-1][x];
            }
        }
        
        seam[H-1] = idxMin;
        for (int y=H-1; y>=1; --y)
            seam[y-1] = edgeTo[y][seam[y]];
        
        return seam;
    }
    //
    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        if (!isTransposed)
            Transpose();   
        removeSeam(seam); 
    }
    //
    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        if (isTransposed)
            Transpose();   
        removeSeam(seam); 
    }
    //
    private void removeSeam(int[] seam)
    {
        if (seam==null)
            throw new java.lang.NullPointerException("removeSeam: null input");
        if (seam.length!=H)
            throw new java.lang.IllegalArgumentException("removeSeam: input is of the wrong size");
        
        int prevVal=seam[0];
        for (int y=0; y<H; ++y)
        {
            int curVal = seam[y];
            if (curVal<0 || curVal>W-1 || Math.abs(curVal-prevVal)>1)
                throw new java.lang.IllegalArgumentException("removeSeam: input is of the wrong format");
            prevVal = curVal;
        }
        
        if (H<1)
            throw new java.lang.IllegalArgumentException("removeSeam: width or height already smaller than 1");
        
        
        int[][] nrgM_t = new int[H][W-1];
        int[][] rgbM_t = new int[H][W-1];
        
        for (int y=0; y<H; ++y)
        {
            System.arraycopy(rgbM[y], 0        , rgbM_t[y], 0      , seam[y]);
            System.arraycopy(rgbM[y], seam[y]+1, rgbM_t[y], seam[y], W-1-seam[y]);
            System.arraycopy(nrgM[y], 0        , nrgM_t[y], 0      , seam[y]);
            System.arraycopy(nrgM[y], seam[y]+1, nrgM_t[y], seam[y], W-1-seam[y]);
        }
        nrgM = nrgM_t;
        rgbM = rgbM_t;
        W--;
        
        for (int y=0; y<H; ++y)
        {
            int x0 = seam[y];
            if (x0>0)
                nrgM[y][x0-1] = energyV(x0-1,y);
            if (x0<W)
                nrgM[y][x0] = energyV(x0,y);
        }
        
    }
    //
}