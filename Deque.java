import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

/**
 * [Coursera] Princeton-AlgorithmI Randomized Queues And Deques<P>
 * Problem Description: http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 * <p/>
 * Finished on Oct.15 2016
 * <p/>
 * Dequeue: A double-ended queue or deque is a generalization of a stack and a queue that
 * supports inserting and removing items from either the front or the back of the data structure.
 * <p/>
 * Requirement: Your deque implementation must support each deque operation in constant
 * worst-case time and use space proportional to the number of items currently
 * in the deque. Additionally, your iterator implementation must support the
 * operations next() and hasNext() (plus construction) in constant worst-case time
 * and use a constant amount of extra space per iterator.
 * @param <Item> generics
 */
public class Deque<Item> implements Iterable<Item> {
 
    private int N;
    private Node front;
    private Node end;
 
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
 
    // construct an empty deque
    public Deque() {                          
        N = 0;
        front = null;
        end = null;
    }
 
    // is the deque empty?
    public boolean isEmpty() {                
        return N == 0;
    }
 
    // return the number of items on the deque
    public int size() {                       
        return N;
    }
 
    // insert the item at the front
    public void addFirst(Item item) {         
        if (item == null)
            throw new NullPointerException();
        Node newFront = front;
        front = new Node();
        front.prev = null;
        front.item = item;
        if (isEmpty()) {
            front.next = null;
            end = front;
        } else {
            front.next = newFront;
            newFront.prev = front;
        }
        N++;
    }
 
    // insert the item at the end
    public void addLast(Item item) {           
        if (item == null)
            throw new NullPointerException();
        Node newEnd = end;
        end = new Node();
        end.next = null;
        end.item = item;
        if (isEmpty()) {
            end.prev = null;
            front = end;
        } else {
            newEnd.next = end;
            end.prev = newEnd;
        }
        N++;
    }
 
    // delete and return the item at the front
    public Item removeFirst() {               
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = front.item;
        // Should consider if there are just one node in the Linkedlist
        if (N == 1) {
            front = null;
            end = null;
        }
        else {
            front = front.next;
            front.prev = null;
        }
        N--;
        return item;
    }
 
    // delete and return the item at the end
    public Item removeLast() {                
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = end.item;
        if (N == 1) {
            front = null;
            end = null;
        }
        else {
            end = end.prev;
            end.next = null;
        }
        N--;
        return item;
    }
 
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {        
        return new ListIterator();
    }
 
    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node current = front;
 
        public boolean hasNext() {
            return current != null;
        }
 
        public Item next() {
            // check whether the current is null, not current->next is null!
            if (current == null)
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
 
        public void remove() {
            throw new UnsupportedOperationException();
        }
 
    }
    
    // unit testing
    public static void main(String[] args) {  
        Deque<String> deque = new Deque<String>();
    
        deque.addFirst("study");
        deque.addFirst("algorithm");
        deque.addLast("in");
        deque.addLast("princeton");
        deque.addLast("University");
        deque.removeLast();
        deque.removeFirst();

        StdOut.println("Queue test:");
        for (String x : deque) {
            StdOut.print(x + ' ');
        }
    }
}
