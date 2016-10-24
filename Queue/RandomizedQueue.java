import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

/**
 * A randomized queue is similar to a stack or queue, except that the item removed is chosen
 * uniformly at random from items in the data structure.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
 
    private int length;
    private Item[] items;
 
    public RandomizedQueue() {                
        // java cannot create a generic array, so we should use cast to implements this
        items = (Item[]) new Object[2];
        length = 0;
    }
 
    // is the queue empty?
    public boolean isEmpty() {                
        return length == 0;
    }
 
    // return the number of items on the queue
    public int size() {                       
        return length;
    }
 
    private void resize(int n) {
        Item[] newItems = (Item[]) new Object[n];
        for (int i = 0; i < length; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }
 
    // add the item
    public void enqueue(Item item) {          
        if (item == null)
            throw new NullPointerException();
        if (length == items.length)
            resize(items.length * 2);
        items[length] = item;
        length++;
    }
 
    // delete and return a random item
    public Item dequeue() {                  
        if (isEmpty())
            throw new NoSuchElementException();
        int random = StdRandom.uniform(length);
        Item item = items[random];
        if (random != length - 1)
            items[random] = items[length - 1];
 
        // don't forget to put the newArray[newLength] to be null to avoid Loitering
        // Loitering: Holding a reference to an object when it is no longer needed.
        items[length - 1] = null;
        length--;
        if (length > 0 && length == items.length / 4)
            resize(items.length / 2);
        return item;
    }
 
    // return (but do not delete) a random item
    public Item sample() {                    
        if (isEmpty())
            throw new NoSuchElementException();
        int random = StdRandom.uniform(length);
        Item item = items[random];
        return item;
    }
 
     // return an independent iterator over items in random order
    public Iterator<Item> iterator() {       
        return new QueueIterator();
    }
 
    private class QueueIterator implements Iterator<Item> {
 
        // Must copy the item in items[] into a new Array
        // Because when create two independent iterators to same randomized queue, the original one
        // has been changed and the second one will lead to false result.
        private int index = 0;
        private int newLength = length;
        private Item[] newArray = (Item[]) new Object[length];
 
        private QueueIterator() {
            for (int i = 0; i < newLength; i++) {
                newArray[i] = items[i];
            }
        }
 
        public boolean hasNext() {
            return index <= newLength - 1;
        }
 
        public Item next() {
            if (newArray[index] == null)
                throw new NoSuchElementException();

            int random = StdRandom.uniform(newLength);
            Item item = newArray[random];
            if (random != newLength - 1)
                newArray[random] = newArray[newLength - 1];
            newLength--;
            newArray[newLength] = null;

            return item;
        }
 
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
 
    // unit testing
    public static void main(String[] args) {  
        RandomizedQueue<Integer> randomQueue = new RandomizedQueue<Integer>();
 
        for (int i = 0; i < 9; i++)
           randomQueue.enqueue(i);
        
        randomQueue.dequeue();
        randomQueue.dequeue();
 
        StdOut.println("Output: ");
        for (Integer x : randomQueue) {
            StdOut.print(x + " ");
        }
    }
 
}
