/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       8/17/2017
 *  Last updated:  8/17/2017
 *
 *  Compilation:   javac Permutation.java
 *  Execution:     java Permutation
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }

        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            randomizedQueue.enqueue(StdIn.readString());
        }

        Iterator<String> iterator = randomizedQueue.iterator();
        for (int i = 0; i < k; i++) {
            StdOut.println(iterator.next());
        }
    }
}
