public class Percolation 
{
    private int                  size;
    private int                  size2;
    private boolean[]            isOpenGrid;
    private boolean[]            isConnectedGrid;
    private WeightedQuickUnionUF tree;
    
    private void checkIndex(int i, int j)
    {
        if (i<1 || i>size || j<1 || j>size)
            throw new java.lang.IndexOutOfBoundsException("cell is not in the grid");
    }
    //
    private int index(int i, int j)
    {
        checkIndex(i, j);
        return (i-1)*size+j;
    }
    //
    private void union(int idx, int idx_n)
    {
        int root   = tree.find(idx);
        int root_n = tree.find(idx_n);
        
        if (isConnectedGrid[root] || isConnectedGrid[root_n])
        {
            isConnectedGrid[root]   = true;
            isConnectedGrid[root_n] = true;
        }
        
        tree.union(root, root_n);
    }
    //
    //
    public Percolation(int N)              // create N-by-N grid, with all sites blocked
    {
        if (N<1)
            throw new java.lang.IllegalArgumentException("The size of the grid has to be strictly positive");
        
        size  = N;
        size2 = N*N;
        
        isOpenGrid      = new boolean[size2+1];
        isConnectedGrid = new boolean[size2+1];
        
        isOpenGrid[0] = true;
        
        tree = new WeightedQuickUnionUF(size2+1);
    }
    //
    public void open(int i, int j)         // open site (row i, column j) if it is not already
    {
        int idx = index(i, j);
        isOpenGrid[idx] = true;
        
        if (i==size)
            isConnectedGrid[tree.find(idx)]=true;
        
        int root = tree.find(idx);
        
        int idx0 = i>1    ? index(i-1, j) : 0;
        int idx1 = i<size ? index(i+1, j) : root;
        int idx2 = j>1    ? index(i, j-1) : root;
        int idx3 = j<size ? index(i, j+1) : root;
        
        if (isOpenGrid[idx0]) union(root, idx0);
        if (isOpenGrid[idx1]) union(root, idx1);
        if (isOpenGrid[idx2]) union(root, idx2);
        if (isOpenGrid[idx3]) union(root, idx3);        
    }
    //
    public boolean isOpen(int i, int j)    // is site (row i, column j) open?
    {
        int idx = index(i, j);
        return isOpenGrid[idx];
    }
    //
    public boolean isFull(int i, int j)    // is site (row i, column j) full?
    {
        int idx = index(i, j);
        return tree.connected(0, idx);
    }
    //
    public boolean percolates()            // does the system percolate?
    {
        return isConnectedGrid[tree.find(0)];
    }
}