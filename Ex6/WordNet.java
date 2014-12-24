public class WordNet 
{
    private SAP sapG;
    private ST<String,  SET<Integer> >  NounToIdST;
    private ST<Integer, String >        IdToSynsST;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        readSynsets(synsets);
        readHypernyms(hypernyms);
    }
    //
    private void readHypernyms(String hypernyms)
    { 
        if (hypernyms == null)
            throw new java.lang.NullPointerException("hypernyms files is empty");
        
        Digraph G = new Digraph(IdToSynsST.size());
        
        In hypernymsIn = new In(hypernyms); 
        while (hypernymsIn.hasNextLine() )
        {
            String line = hypernymsIn.readLine();
            String[] fields = line.split(",");
            
            int u = Integer.parseInt(fields[0]);
            for (int i=1; i<fields.length; ++i)
            {
                int v = Integer.parseInt(fields[i]);
                G.addEdge(u,v);
            }
        }
        
        DirectedCycle DC = new DirectedCycle(G);
        if (DC.hasCycle())
            throw new java.lang.IllegalArgumentException("input graph has a cycle");
        
        int count = 0;
        for (int v=0; v<G.V(); ++v)
        {
            Iterable<Integer> nextNodes = G.adj(v);
            if (!nextNodes.iterator().hasNext())
                count++;
        }
        if (count!=1)
            throw new java.lang.IllegalArgumentException("input graph has not exactly one root");
        
        sapG = new SAP(G);
    }
    //
    private void readSynsets(String synsets)
    {
        if (synsets == null)
            throw new java.lang.NullPointerException("synsets files is empty");
                
        NounToIdST = new ST<String, SET<Integer> >();
        IdToSynsST = new ST<Integer, String>();
        
        In synsetsIn = new In(synsets);
        while (synsetsIn.hasNextLine() )
        {
            String line = synsetsIn.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            
            String[] nouns = fields[1].split(" ");
            IdToSynsST.put(id, fields[1]);
            
            for (String n : nouns)
            {
                if (NounToIdST.contains(n))
                {
                    SET<Integer> ids = NounToIdST.get(n);
                    ids.add(id);
                    NounToIdST.put(n, ids);
                }
                else
                {
                    SET<Integer> ids = new SET<Integer>();
                    ids.add(id);
                    NounToIdST.put(n, ids);
                }
            }
        }
    }
    
    
    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        return NounToIdST.keys();
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        if (word==null)
            throw new java.lang.NullPointerException("input in isNoun is null");
        
        return NounToIdST.contains(word);
    }
    
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException("inputs in distance are not WordNet nouns");
                
        return sapG.length(NounToIdST.get(nounA), NounToIdST.get(nounB));
    }
    
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException("inputs in sap are not WordNet nouns");
        
        int a = sapG.ancestor(NounToIdST.get(nounA), NounToIdST.get(nounB));
        return IdToSynsST.get(a);
    }
    
    // do unit testing of this class
    public static void main(String[] args)
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        int d = wordnet.distance("Isis", "Gloomy_Dean");
        String s = wordnet.sap("Isis", "Gloomy_Dean");
        StdOut.printf("distance1 := %d, ancestor:= %s", d,s);
    }
}