public class Outcast 
{
    private WordNet wordNet;
    
    public Outcast(WordNet _wordNet)         // constructor takes a WordNet object
    {
        wordNet = _wordNet;
    }
    //
    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        int distMax = -1;
        int idxMax  = -1;
        
        for (int i=0; i<nouns.length; ++i)
        {
            int dist=0;
            for (int j=0; j<nouns.length; ++j)
            {
                dist+=wordNet.distance(nouns[i], nouns[j]);
            }
            
            if (dist>distMax)
            {
                distMax = dist;
                idxMax = i;
            }
        }
        return nouns[idxMax];
    }
    
    public static void main(String[] args) 
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        
        for (int t = 2; t < args.length; t++) 
        {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
   }
}