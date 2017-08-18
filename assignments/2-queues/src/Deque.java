/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       8/15/2017
 *  Last updated:  8/15/2017
 *
 *  Compilation:   javac Deque.java
 *  Execution:     java Deque
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int size;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;

        Node(Item value) {
            item = value;
        }
    }


    public Deque() {

    }

    /**
     * @return true if queue is empty
     */
    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            init(item);
        } else {
            Node old = first;
            first = new Node(item);
            first.next = old;
            old.prev = first;
        }

        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            init(item);
        } else {
            Node old = last;
            last = new Node(item);
            last.next = null;
            old.next = last;
            last.prev = old;
        }
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node removed = first;
        first = removed.next;
        if (first != null) {
            first.prev = null;
            first.next = removed.next;
        } else { // single item in queue
            last = null;
        }

        size--;

        return removed.item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node removed = last;
        last = removed.prev;
        if (last != null) {
            last.prev = removed.prev;
            last.next = null;
        } else { // single item in queue
            first = null;
        }

        size--;

        return removed.item;
    }

    private void init(Item item) {
        first = new Node(item);
        first.next = null;
        first.prev = null;
        last = first;
    }

    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<Integer> d;
        int v;

        d = fixture("Construction");
        test("is empty", d.isEmpty());
        test("size is zero", d.size() == 0);

        d = fixture("addFirst 1st call");
        d.addFirst(458);
        test("is not empty", !d.isEmpty());
        test("size is 1", d.size() == 1);

        d = fixture("addFirst 2nd call");
        d.addFirst(412);
        d.addFirst(459);
        test("is not empty", !d.isEmpty());
        test("size is 2", d.size() == 2);

        d = fixture("addLast 1st call");
        d.addLast(458);
        test("is not empty", !d.isEmpty());
        test("size is 1", d.size() == 1);

        d = fixture("addLast 2nd call");
        d.addLast(412);
        d.addLast(459);
        test("is not empty", !d.isEmpty());
        test("size is 2", d.size() == 2);

        d = fixture("addFirst removeFirst");
        d.addFirst(458);
        v = d.removeFirst();
        test("returns added value", v == 458);
        test("is empty", d.isEmpty());
        test("size is zero", d.size() == 0);

        d = fixture("addFirst removeLast");
        d.addFirst(458);
        v = d.removeLast();
        test("returns added value", v == 458);
        test("is empty", d.isEmpty());
        test("size is zero", d.size() == 0);

        d = fixture("addLast removeLast");
        d.addLast(458);
        v = d.removeLast();
        test("returns added value", v == 458);
        test("is empty", d.isEmpty());
        test("size is zero", d.size() == 0);

        d = fixture("addLast removeFirst");
        d.addLast(451);
        v = d.removeFirst();
        test("returns added value", v == 451);
        test("is empty", d.isEmpty());
        test("size is zero", d.size() == 0);

        d = fixture("iterator (3 items)");
        d.addLast(12);
        d.addLast(13);
        d.addFirst(11);

        Iterator<Integer> iterator = d.iterator();
        test("1st hasNext() returns true", iterator.hasNext());
        test("1st next() returns 1st value", iterator.next() == 11);
        test("2nd hasNext() returns true", iterator.hasNext());
        test("2nd next() returns 2nd value", iterator.next() == 12);
        test("3rd hasNext() returns true", iterator.hasNext());
        test("3rd next() returns 3rd value", iterator.next() == 13);
        test("4th hasNext() returns false", !iterator.hasNext());
        try {
            iterator.next();
            test("4th next() throws NoSuchElementException", false);
        } catch (NoSuchElementException exception) {
            test("4th next() throws NoSuchElementException", true);
        }

    }

    private static Deque<Integer> fixture(String description) {
        StdOut.println();
        StdOut.println(description);
        return new Deque<>();
    }

    private static void test(String description, boolean assertion) {
        StdOut.printf(" * %-32s %s\n", description, assertion ? "PASSED" : "FAILED");
    }
}
