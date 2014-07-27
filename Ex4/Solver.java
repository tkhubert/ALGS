public class Solver 
{
    private MinPQ<SearchNode> tree;
    private MinPQ<SearchNode> treeTwin;
    
    private boolean isTreeSolved;
    private boolean isTreeTwinSolved;
    
    private class SearchNode implements Comparable<SearchNode>
    {
        private SearchNode prevNode ;
        private Board      currBoard;
        private int        nbMoves;
        private int        priority;
        
        public SearchNode(SearchNode prevNode, Board currBoard, int nbMoves)
        {
            this.prevNode  = prevNode;
            this.currBoard = currBoard;
            this.nbMoves   = nbMoves;
            
            priority  = nbMoves + currBoard.manhattan();
        }
        //
        public int getPriority()
        {
            return priority;
        }
        //
        public int compareTo(SearchNode that) // is this point lexicographically smaller than that one? comparing y-coordinates and breaking ties by x-coordinates
        {
            if (this.priority < that.priority)
                return -1;
            else if (this.priority == that.priority)
                return 0;
            else
                return 1;
        }
    }
    //
    public Solver(Board initial)            // find a solution to the initial board (using the A* algorithm)
    {
        isTreeSolved     = false;
        isTreeTwinSolved = false;
        tree     = new MinPQ<SearchNode>();
        treeTwin = new MinPQ<SearchNode>();
        
        Board      twinBoard = initial.twin();
        SearchNode node      = new SearchNode(null, initial, 0);
        SearchNode nodeTwin  = new SearchNode(null, twinBoard, 0);
        
        tree.insert(node);
        treeTwin.insert(nodeTwin);
        
        while (!isTreeSolved && !isTreeTwinSolved)
        {
            isTreeSolved     = step(tree);
            isTreeTwinSolved = step(treeTwin);
        }
    }
    //
    private boolean step(MinPQ<SearchNode> t)
    {
        SearchNode parentNode  = t.min();
        Board      parentBoard = parentNode.currBoard;
        
        if (parentBoard.isGoal())
            return true;
        
        parentNode      = t.delMin();
        
        Iterable<Board> childrenBoard = parentBoard.neighbors();
        for (Board childBoard : childrenBoard)
        {
            if (parentNode.prevNode==null || !childBoard.equals(parentNode.prevNode.currBoard))
            {
                SearchNode childNode = new SearchNode(parentNode, childBoard, parentNode.nbMoves+1);
                t.insert(childNode);
            }
        }
        
        return false;
    }
    //
    public boolean isSolvable()             // is the initial board solvable?
    {
        return isTreeSolved;
    }
    //
    public int moves()                      // min number of moves to solve initial board; -1 if no solution
    {
        if (isSolvable())
            return tree.min().nbMoves;
        else
            return -1;
    }
    //
    public Iterable<Board> solution()       // sequence of boards in a shortest solution; null if no solution
    {
        if (isSolvable())
        {
            Stack<Board> s  = new Stack<Board>();
            SearchNode node = tree.min();
            
            while (node!=null)
            {
                s.push(node.currBoard);
                node = node.prevNode;    
            }
            return s;
        }
        else
            return null;
    }
    //
    public static void main(String[] args)  // solve a slider puzzle (given below)
    {     
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else 
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        
    }
}