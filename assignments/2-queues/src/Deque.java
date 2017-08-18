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

    // virtual master of queue
    // master.prev holds last element
    // master.next holds first element
    private final Node master;
    private int size;

    private class Node {
        private final Item item;
        private Node next;
        private Node prev;

        Node(Item value) {
            item = value;
        }
    }


    public Deque() {
        master = new Node(null);
        master.next = master;
        master.prev = master;
    }

    /**
     * @return true if queue is empty
     */
    public boolean isEmpty() {
        return master.next == master;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        insert(item, master, master.next);
    }

    public void addLast(Item item) {
        insert(item, master.prev, master);
    }

    public Item removeFirst() {
        return remove(master.next);
    }

    public Item removeLast() {
        return remove(master.prev);
    }

    private void insert(Item item, Node after, Node before) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node newNode = new Node(item);
        newNode.prev = after;
        newNode.next = before;

        after.next = newNode;
        before.prev = newNode;

        size++;
    }

    private Item remove(Node node) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        node.prev.next = node.next;
        node.next.prev = node.prev;

        size--;

        return node.item;
    }

    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private Node next = master.next;

        public boolean hasNext() {
            return next != master;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = next.item;
            next = next.next;
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
