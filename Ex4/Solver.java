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
        Board prevBoard = parentNode.prevNode.currBoard;
        
        Iterable<Board> childrenBoard = parentBoard.neighbors();
        for (Board childBoard : childrenBoard)
        {
            if (!childBoard.equals(prevBoard))
            {
                SearchNode childNode = new SearchNode(parentNode, childBoard, parentNode.nbMoves+1);
                tree.insert(childNode);
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
            
            while (node.prevNode!=null)
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
         for (String filename : args) 
         {
            In in = new In(filename);
            int N = in.readInt();
            int[][] tiles = new int[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            Solver solver = new Solver(initial);
            System.out.println(filename + ": " + solver.moves());
        }
        
    }
}