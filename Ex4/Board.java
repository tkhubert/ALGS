public class Board 
{
    private int       N;
    private int       zeroI;
    private int       zeroJ;
    private short[][] grid;
    private int       hammingScore;
    private int       manhattanScore;
    
    public Board(int[][] blocks)           // construct a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column j)                                           
    {
        if (blocks[0].length>181)
            throw new java.lang.IndexOutOfBoundsException("grid is too big, should be smaller than 181");
        
        N              = blocks[0].length;
        hammingScore   = 0;
        manhattanScore = 0;
        
        grid = new short[N][N];
        for (int i=0; i<N; ++i)
        {
            for (int j=0; j<N; ++j)
            {
                short val  = (short) blocks[i][j];
                grid[i][j] = val;
                
                updateData(i, j, val, 1);
            }
        }   
    }
    //
    private Board(short[][] blocks)           // construct a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column j)                                           
    {
        N              = blocks[0].length;
        hammingScore   = 0;
        manhattanScore = 0;
        
        grid = new short[N][N];
        for (int i=0; i<N; ++i)
        {
            for (int j=0; j<N; ++j)
            {
                short val  = (short) blocks[i][j];
                grid[i][j] = val;
                
                updateData(i, j, val, 1);
            }
        }          
    }
    //
    public int dimension()                 // board dimension N
    {
        return N;
    }
    //
    public int hamming()                   // number of blocks out of place
    {
        return hammingScore;
    }
    //
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        return manhattanScore;
    }
    //
    public boolean isGoal()                // is this board the goal board?
    {
        return (manhattanScore==0);
    }
    //
    public Board twin()                    // a board obtained by exchanging two adjacent blocks in the same row
    {
        int row = (zeroI==0) ? 1 : 0;
        
        Board twinBoard = new Board(grid);
        twinBoard.swap(row, 0, row, 1);
        return twinBoard;
    }
    //
    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == this) 
            return true;
        if (y == null) 
            return false;
        if (y.getClass() != this.getClass()) 
            return false;
        
        Board that = (Board) y;
        
        if (that.N!=this.N)
            return false;
        
        for (int i=0; i<N; ++i)
        {
            for (int j=0; j<N; ++j)
            {
                if (that.grid[i][j] != this.grid[i][j])
                    return false;
            }
        }
        return true;
    }
    //
    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Stack<Board> s = new Stack<Board>();
        
        if (zeroI>0)
        {
            Board b = new Board(grid);
            b.swap(zeroI, zeroJ, zeroI-1, zeroJ);
            s.push(b);
        }
        if (zeroI<N-1)
        {
            Board b = new Board(grid);
            b.swap(zeroI, zeroJ, zeroI+1, zeroJ);
            s.push(b);
        }
        if (zeroJ>0)
        {
            Board b = new Board(grid);
            b.swap(zeroI, zeroJ, zeroI, zeroJ-1);
            s.push(b);
        }
        if (zeroJ<N-1)
        {
            Board b = new Board(grid);
            b.swap(zeroI, zeroJ, zeroI, zeroJ+1);
            s.push(b);
        }
        
        return s;
    }
    //
    public String toString()               // string representation of the board (in the output format specified below)
    {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", grid[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    //
    private void updateData(int i, int j, int val, int sign)
    {
        if (val!=0)
        {
            int correctRow = (val-1)/N;
            int correctCol = (val-1)%N;
            
            if (i!=correctRow || j!=correctCol)
                hammingScore+=sign;
            
            manhattanScore+=sign*(Math.abs(correctRow-i) + Math.abs(correctCol-j));
        } 
        else
        {
            zeroI = i;
            zeroJ = j;
        }
    }
    //
    private void swap(int i1, int j1, int i2, int j2)
    {
        short val1 = grid[i1][j1];
        short val2 = grid[i2][j2];
        
        updateData(i1, j1, val1, -1);
        updateData(i2, j2, val2, -1);
        
        grid[i1][j1] = val2;
        grid[i2][j2] = val1;
        
        updateData(i1, j1, val2, 1);
        updateData(i2, j2, val1, 1);
    }
}