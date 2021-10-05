import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    private Node first = null;
    private Node last = null;
    private int totalNum = 0;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return totalNum == 0;
    }

    // return the number of items on the deque
    public int size() {
        return totalNum;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null argument");
        }
        Node node = new Node();
        Node old = first;
        node.item = item;
        node.prev = null;
        node.next = old;
        first = node;
        if (totalNum++ == 0) last = node;
        else old.prev = node;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null argument");
        }
        Node old = last;
        Node node = new Node();
        node.item = item;
        node.prev = old;
        node.next = null;
        last = node;
        if (totalNum++ == 0) first = node;
        else old.next = node;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Deque is empty");
        }
        Item item = first.item;
        Node newFirst = first.next;
        first = newFirst;
        if (--totalNum == 0)
            last = null;
        else
            first.prev = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Deque is empty");
        }
        Item item = last.item;
        Node newLast = last.prev;
        last = newLast;
        if (--totalNum == 0)
            first = null;
        else
            newLast.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            // check whether the current is null, not current->next is null!
            if (current == null)
                throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
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
        StdOut.println();
    }
}
