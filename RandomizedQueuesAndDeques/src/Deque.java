import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int size;

    private class Node {
        private Item item;
        private Node prev;
        private Node next;

        private Node(Item item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    // Construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // Is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // Return the number of items on the deque
    public int size() {
        return size;
    }

    // Insert the item at the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        first = new Node(item, null, first);
        if (size++ == 0) {
            last = first;
        }
        else {
            first.next.prev = first;
        }
    }

    // Insert the item at the end
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        last = new Node(item, last, null);
        if (size++ == 0) {
            first = last;
        }
        else {
            last.prev.next = last;
        }
    }

    // Delete and return the item at the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node oldFirst = first;
        first = oldFirst.next;
        if (first != null) {
            first.prev = null;
        }
        if (--size == 0) {
            last = null;
        }
        return oldFirst.item;
    }

    // Delete and return the item at the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node oldLast = last;
        last = oldLast.prev;
        if (last != null) {
            last.next = null;
        }
        if (--size == 0) {
            first = null;
        }
        return oldLast.item;
    }

    // Return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}