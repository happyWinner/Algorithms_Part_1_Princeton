import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int size;

    // Construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        size = 0;
    }

    // Is the queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // Return the number of items on the queue
    public int size() {
        return size;
    }

    // Add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        if (size == items.length) {
            resize(2 * items.length);
        }
        items[size++] = item;
    }

    // Delete and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int idx = StdRandom.uniform(size);
        Item item = items[idx];
        items[idx] = items[--size];
        items[size] = null;
        if (size > 0 && size == items.length / 4) {
            resize(items.length / 2);
        }
        return item;
    }

    // Return (but do not delete) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return items[StdRandom.uniform(size)];
    }

    // Resize the array
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; ++i) {
            copy[i] = items[i];
        }
        items = copy;
    }

    // Return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] iterItems;
        private int current;

        private RandomizedQueueIterator() {
            current = size;

            // copy the array
            iterItems = (Item[]) new Object[size];
            for (int i = 0; i < size; ++i) {
                iterItems[i] = items[i]; 
            }

            // shuffle the array
            for (int i = 0; i < size; ++i) {
                int r = StdRandom.uniform(i + 1);
                Item temp = iterItems[i];
                iterItems[i] = iterItems[r];
                iterItems[r] = temp;
            }
        }

        public boolean hasNext() {
            return current > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return iterItems[--current];
        }
    }
}