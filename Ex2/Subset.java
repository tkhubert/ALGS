public class Subset 
{
    
    public static void main(String[] args)
    {
        if (args.length!=1)
            throw new java.lang.IllegalArgumentException("should have exactly two integers as inputs");
        
        int k;
        try
        {
            k = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException nfe) 
        {
            throw new java.lang.IllegalArgumentException("should have exactly one integer as inputs");
        }
        
        String[] sArray = StdIn.readAllStrings();
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        
        for (String s : sArray)
            rq.enqueue(s);
        for (int i=0; i<k; ++i)
            System.out.println(rq.dequeue());
    }
}