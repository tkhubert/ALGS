public class BoggleSolver
{
    private TrieSET     dict;
    
    private BoggleBoard board;
    private Node[]      nodeBoard;

    private int         r;
    private int         c;
    
    private SET<String> words;
    
    private class Node
    {
        public int i;
        public int j;
        public String letter;
        public boolean marked;
        public Stack<Node> adj;
        
        public Node(int i, int j)
        {
            this.i = i;
            this.j = j;
            marked = false;
            adj = new Stack<Node>();
        }
        
        public void setLetter(char l)
        {
            letter = "" + (l == 'Q' ? "QU" : l);
        }
    }
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        dict = new TrieSET();
        for (String word : dictionary)
            dict.add(word);        
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        words = new SET<String>();
        
        this.board = board;
        r = board.rows();
        c = board.cols();
        nodeBoard = new Node[r*c];
        
        for (int i=0; i<r; ++i)
        {
            for (int j=0; j<c; ++j)
            {
                nodeBoard[i*c+j] = new Node(i,j);
                nodeBoard[i*c+j].setLetter(board.getLetter(i,j));
            }
        }
                
        for (Node node : nodeBoard)
        {
            int i = node.i;
            int j = node.j;
            
            for (int i_n =i-1; i_n<=i+1; ++i_n)
                for (int j_n=j-1; j_n<=j+1; ++j_n)
                    if (i_n>=0 && i_n<r && j_n>=0 && j_n<c)
                        node.adj.push(nodeBoard[i_n*c+j_n]);
        }
        
        for (Node node : nodeBoard)
            dfs(node, "");
        
        return words;
    }
    
    private void dfs(Node node, String currWord)
    {
        node.marked = true;
        String newWord = currWord + node.letter;
        
        if (!dict.containsPrefix(newWord))
        {
            node.marked = false;
            return;
        }
                
        if (dict.contains(newWord) && newWord.length()>2)
            words.add(newWord);
        
        for (Node next_node : node.adj)
        {
            if (!next_node.marked)
                dfs(next_node, newWord);
        }

        node.marked = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        if (!dict.contains(word))
            return 0;
        
        int len = word.length();

        if (len<=2)
            return 0;
        else if (len<=4)
            return 1;
        else if (len<=5)
            return 2;
        else if (len<=6)
            return 3;
        else if (len<=7)
            return 5;
        else
            return 11;
    }
    
    //
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}