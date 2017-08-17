/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       8/16/2017
 *  Last updated:  8/16/2017
 *
 *  Compilation:   javac RandomizedQueue.java
 *  Execution:     java RandomizedQueue
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> {

    // length of queue
    private int n = 0;
    private Item[] s;

    // construct an empty randomized queue
    public RandomizedQueue(){
        s = (Item[])new Object[1];
    }

    // is the queue empty?
    public boolean isEmpty(){
        return n == 0;
    }

    // return the number of items on the queue
    public int size(){
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (n == s.length) {
            resize(s.length * 2);
        }

        s[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        int i = StdRandom.uniform(n);
        Item value = s[i];
        // move last into i
        s[i] = s[--n];
        s[n] = null;
        if (n > 0 && n == s.length/4) {
            resize(s.length/2);
        }
        return value;
    }

    // return (but do not remove) a random item
    public Item sample(){
        return s[StdRandom.uniform(n)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void resize(int capacity)
    {
        // shift head to zero
        Item[] copy =  (Item[])new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = s[i];
        }
        s = copy;
    }

    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private int i = 0;
        private final int[] indices;

        public RandomizedQueueIterator() {
            indices = new int[n];
            for(i = 0; i < n; i++) {
                indices[i] = i;
            }

            // prepare random sequence - shuffle indices
            shuffle(indices);

            i = 0;
        }

        public boolean hasNext() {
            return i < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next()
        {
            if (i == n) {
                throw new NoSuchElementException();
            }
            return s[indices[i++]];
        }
    }

    private static void shuffle(int[] indices){
        for(int j = indices.length - 1; j > 0; j--) {
            int k = StdRandom.uniform(j + 1);
            int old = indices[k];
            indices[k] = indices[j];
            indices[j] = old;
        }
    }

    public static void main(String[] args)  {
        RandomizedQueue<Integer> queue;
        int v;

        queue = fixture("Construction");
        test("is empty", queue.isEmpty());
        test("size is zero", queue.size() == 0);

        queue = fixture("enqueue 1st call");
        queue.enqueue(458);
        test("is not empty", !queue.isEmpty());
        test("size is 1", queue.size() == 1);

        queue = fixture("enqueue 2nd call");
        queue.enqueue(412);
        queue.enqueue(459);
        test("is not empty", !queue.isEmpty());
        test("size is 2", queue.size() == 2);


        queue = fixture("enqueue dequeue");
        queue.enqueue(458);
        v = queue.dequeue();
        test("returns added value", v == 458);
        test("is empty", queue.isEmpty());
        test("size is zero", queue.size() == 0);

        queue = fixture("iterator (5 items)");

        int[] testValues = new int[] {11 ,12, 13, 15, 18};

        for(int i=0; i < testValues.length; i++) {
            queue.enqueue(testValues[i]);
        }
        StdRandom.setSeed(22);
        shuffle(testValues);


        StdRandom.setSeed(22);
        Iterator<Integer> iterator = queue.iterator();
        int k;
        for(int i=0; i < testValues.length; i++) {
            k = i + 1;
            test(k + ": hasNext() returns true", iterator.hasNext());
            test(k +": next() returns value", iterator.next() == testValues[i]);
        }

        k = testValues.length + 1;
        test(k + ": hasNext() returns false", !iterator.hasNext());
        try {
            iterator.next();
            test(k + ": next() throws NoSuchElementException", false);
        } catch (Exception exception){
            test(k + ": next() throws NoSuchElementException", exception instanceof NoSuchElementException);
        }
    }

    private static RandomizedQueue<Integer> fixture(String description) {
        StdOut.println();
        StdOut.println(description);
        return new RandomizedQueue<Integer>();
    }

    private static void test(String description, boolean assertion) {
        StdOut.printf(" * %-32s %s\n", description, assertion ? "PASSED" : "FAILED");
    }

}
