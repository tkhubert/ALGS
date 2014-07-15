
import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> 
{
    private int    last;
    private Item[] rq;    
    private int    N;
    
    public RandomizedQueue()                 // construct an empty randomized queue
    {
        last  = 0;
        N     = 0;
        rq = (Item[]) new Object[2];
    }
    //
    public boolean isEmpty()                 // is the queue empty?
    {
        return N==0;
    }
    //
    public int size()                        // return the number of items on the queue
    {
        return N;
    }
    //
    private void resize(int size)
    {
        Item[] temp = rq;
        rq = (Item[]) new Object[size];
        
        for (int i=0; i<N; ++i)
            rq[i] = temp[i];
        
        last  = N;
    }
    //
    public void enqueue(Item item)           // add the item
    {
        if (item==null)
           throw new NullPointerException("item is null");
               
        if (last==rq.length)
            resize(rq.length*2);

        rq[last] = item;
        last++;
        N++;
    }
    //
    public Item dequeue()                    // delete and return a random item
    {
        if (isEmpty()) 
           throw new NoSuchElementException("RandomizedQueue underflow");
        
        if (N<=rq.length/4)
            resize(rq.length/2);
        
        int i = StdRandom.uniform(N);
        Item item = rq[i];
        
        rq[i] = rq[last-1];
        rq[last-1] = null;
        
        last--;
        N--;
        
        return item;
    }
    //
    public Item sample()                     // return (but do not delete) a random item
    {
        if (isEmpty()) 
            throw new NoSuchElementException("RandomizedQueue underflow");
                
        int i = StdRandom.uniform(N);
        return rq[i];        
    }
    //
    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
   {
       return new RandomQueueIterator();  
   }
   //
   private class RandomQueueIterator implements Iterator<Item> 
   {
       private int       idx;
       private Integer[] idxArray;
       public RandomQueueIterator()
       {
           idx=0;
           idxArray= new Integer[N];
           for (int i=0; i<N; ++i)
               idxArray[i]=i;
           
           Knuth.shuffle(idxArray);
       }
       //
       public boolean hasNext()  
       { 
           return (idx != N);                     
       }
       //
       public void remove()      
       { 
           throw new UnsupportedOperationException();  
       }
       //
       public Item next() 
       {
           if (!hasNext()) 
               throw new NoSuchElementException();

           Item item = rq[idxArray[idx]];
           idx++;

           return item;
       }
   }
   //
   //
   public static void main(String[] args)   // unit testing
   {
       RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();
       int N=10;
       
       for (int i=0; i<N; ++i)
           d.enqueue(i);
       
       Iterator<Integer> itr1 = d.iterator();
       Iterator<Integer> itr2 = d.iterator();
//       while(itr1.hasNext())
//           StdOut.print(itr1.next());
//       StdOut.print("\n");
//       while(itr2.hasNext())
//           StdOut.print(itr2.next());
       
       for (int val1 : d) 
       {  
           StdOut.print(", " + val1);  
           
           StdOut.print("\n");
           StdOut.print("    ");
           for (int val2 : d)  
               StdOut.print(", " + val2);  
           StdOut.print("\n");
           
       }  

   }
    
}