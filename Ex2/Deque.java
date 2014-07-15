
import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> 
{
   private Node first;
   private Node last;
   private int  N;
   
   private class Node
   {
      private Item item;
      private Node next;
      private Node prev;
   }
   //
   public Deque()                           // construct an empty deque
   {
       N = 0;
       first = null;
       last  = null;
   }
   //
   public boolean isEmpty()                 // is the deque empty?
   {
       return (N==0);
   }
   //
   public int size()                        // return the number of items on the deque
   {
       return N;
   }
   //
   public void addFirst(Item item)          // insert the item at the front
   {
       if (item==null)
           throw new NullPointerException("item is null");
       
       Node oldFirst = first;
       
       first       = new Node();
       first.item  = item;
       first.prev  = null;
       first.next  = oldFirst;
       
       if (isEmpty())
           last = first;
       else
           oldFirst.prev = first;

       N++;      
   }
   //
   public void addLast(Item item)           // insert the item at the end
   {
       if (item==null)
           throw new NullPointerException("item is null");
              
       Node oldLast = last;
       
       last      = new Node();
       last.item = item;
       last.prev = oldLast;
       last.next = null;
       
       if (isEmpty())
           first = last;
       else
           oldLast.next = last;
       
       N++;
   }
   //
   public Item removeFirst()                // delete and return the item at the front
   {
       if (isEmpty()) 
           throw new NoSuchElementException("Dequeue underflow");
       
       Item item  = first.item;
       first      = first.next;
       
       N--;
       
       if (isEmpty())
           last = null;
       else
           first.prev = null;
       
       return item;
   }
   //
   public Item removeLast()                 // delete and return the item at the end
   {
       if (isEmpty()) 
           throw new NoSuchElementException("Dequeue underflow");
              
       Item item = last.item;
       last      = last.prev;
       
       N--;
       
       if (isEmpty())
           first = null;
       else
           last.next = null;
       
       return item;
   }
   //
   public Iterator<Item> iterator()         // return an iterator over items in order from front to end
   {
       return new DequeIterator();  
   }
   //
   private class DequeIterator implements Iterator<Item> 
   {
       private Node current = first;
       
       public boolean hasNext()  
       { 
           return (current != null);                     
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
           
           Item item = current.item;
           current   = current.next; 
           return item;
       }
   }
   //
   public static void main(String[] args)   // unit testing
   {
       Deque<Integer> d = new Deque<Integer>();
       int N=10;
       
       for (int i=0; i<N; ++i)
           d.addLast(i);
//       for (int i=0; i<N; ++i)
//           System.out.println(d.removeLast());
//       for (int i=0; i<N; ++i)
//           d.addLast(i);
//       for (Integer i : d)
//           System.out.println(i);     
//       for (int i=0; i<N; ++i)
//           d.addFirst(i);
//       for (Integer i : d)
//           System.out.println(i);
       
       Iterator<Integer> iter = d.iterator();
       for (int i=0; i<N-1; ++i)
           System.out.println(iter.next());
       System.out.println(iter.hasNext());
       
       System.out.println(d.removeLast());
       System.out.println(iter.hasNext());
       System.out.println(iter.next());
//           
       
   }
}