public class SAP 
{
    private static final int INFINITY = Integer.MAX_VALUE;
    
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph _G)
    {
        G = new Digraph(_G);
    }
    
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        Stack<Integer> vIterable = new Stack<Integer>();
        Stack<Integer> wIterable = new Stack<Integer>();
        vIterable.push(v);
        wIterable.push(w);
        return length(vIterable, wIterable);
    }
    
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {
        Stack<Integer> vIterable = new Stack<Integer>();
        Stack<Integer> wIterable = new Stack<Integer>();
        vIterable.push(v);
        wIterable.push(w);
        return ancestor(vIterable, wIterable);
    }
    
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        BreadthFirstDirectedPaths bf_v = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bf_w = new BreadthFirstDirectedPaths(G, w);
        
        int a_tmp = -1;
        int l_tmp = INFINITY;
        for (int a=0; a<G.V(); ++a)
        {
            int  l_v    = bf_v.distTo(a);
            int  l_w    = bf_w.distTo(a);           
            
            if (bf_v.hasPathTo(a) && bf_w.hasPathTo(a) && l_v+l_w < l_tmp)
            {
                a_tmp = a;
                l_tmp = l_v+l_w; 
            }
        }
        return a_tmp==-1 ? -1 : l_tmp;
    }
    
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        BreadthFirstDirectedPaths bf_v = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bf_w = new BreadthFirstDirectedPaths(G, w);
        
        int a_tmp = -1;
        int l_tmp = INFINITY;
        for (int a=0; a<G.V(); ++a)
        {
            int  l_v    = bf_v.distTo(a);
            int  l_w    = bf_w.distTo(a);           
            
            if (bf_v.hasPathTo(a) && bf_w.hasPathTo(a) && l_v+l_w < l_tmp)
            {
                a_tmp = a;
                l_tmp = l_v+l_w; 
            }
        }
        return a_tmp==-1 ? -1 : a_tmp;
    }
    
    // do unit testing of this class
    public static void main(String[] args) 
    {
        In      in  = new In(args[0]);
        Digraph G   = new Digraph(in);
        SAP     sap = new SAP(G);
        
        while (!StdIn.isEmpty()) 
        {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

//There are a few things you can do to speed up a sequence of SAP computations on the same digraph. Do not attempt to do this or any of your own invention without thoroughly testing your code. You can gain bonus points for correctly implementing some of these optimizations but you risk losing more points than you can gain if you introduce bugs that render your solution no longer correct.
//The bottleneck operation is re-initializing arrays of length V to perform the BFS computations. This must be done once for the first BFS computation, but if you keep track of which array entries change, you can reuse the same array from computation to computation (re-initializing only those entries that changed in the previous computation). This leads to a dramatic savings when only a small number of entries change (and this is the typical case for the wordnet digraph). Note that if you have any other loops that iterates through all of the vertices, then you must eliminate those to achieve a sublinear running time. (An alternative is to replace the arrays with symbol tables, where, in constant time, the constructor initializes the value assoicated with every key to be null.)
//If you run the breadth-first searches from v and w simultaneously, then you can terminate the BFS from v (or w) as soon as the distance exceeds the length of the best ancestral path found so far.
//Implement a software cache of recently computed length() and ancestor() queries.
