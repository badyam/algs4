/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       11/26/2017
 *  Last updated:  11/26/2017
 *
 *  Compilation:   javac WordNet.java
 *  Execution:     java WordNet
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;

import java.util.Hashtable;

/**
 * Implements graph test for n-by-n grid structure
 */
public class WordNet {

    private final String[] synsets;
    private final Hashtable<String, Bag<Integer>> nouns = new Hashtable<>();
    private final SAP sap;

    /**
     * Constructor, takes the name of the two input files.
     *
     * @param synsetsFilename   synsets input file
     * @param hypernymsFilename hypernyms input file
     */
    public WordNet(String synsetsFilename, String hypernymsFilename) {
        if (synsetsFilename == null) {
            throw new IllegalArgumentException();
        }
        if (hypernymsFilename == null) {
            throw new IllegalArgumentException();
        }


        In synsetsFile = new In(synsetsFilename);
        String[] synsetsLines = synsetsFile.readAllLines();
        synsetsFile.close();

        synsets = new String[synsetsLines.length];

        for (int i = 0; i < synsetsLines.length; i++) {
            String[] fields = synsetsLines[i].split(",");
            if (fields.length < 2) continue;
            int id = Integer.parseInt(fields[0]);
            synsets[id] = fields[1];
            String[] words = fields[1].split(" ");
            for (String word : words) {
                if (nouns.containsKey(word)) {
                    nouns.get(word).add(id);
                } else {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(id);
                    nouns.put(word, bag);
                }
            }
        }

        Digraph dg = new Digraph(synsets.length);
        In hypernymsFile = new In(hypernymsFilename);
        String[] hypernymsLines = hypernymsFile.readAllLines();
        hypernymsFile.close();

        for (int i = 0; i < hypernymsLines.length; i++) {
            String[] fields = hypernymsLines[i].split(",");
            int id = Integer.parseInt(fields[0]);
            for (int k = 1; k < fields.length; k++) {
                int hypernymId = Integer.parseInt(fields[k]);
                dg.addEdge(id, hypernymId);
            }
        }

        sap = new SAP(dg);
    }

    /**
     * Returns all WordNet nouns.
     */
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    /**
     * Examines if the word a WordNet noun.
     */
    public boolean isNoun(String word) {
        return nouns.containsKey(word);
    }

    /**
     * Distance between nounA and nounB (defined below)
     */
    public int distance(String nounA, String nounB) {
        if (!nouns.containsKey(nounA))
            throw new IllegalArgumentException();
        if (!nouns.containsKey(nounB))
            throw new IllegalArgumentException();

        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    /**
     * Gets a synset that is the common ancestor of nounA and nounB
     */
    public String sap(String nounA, String nounB) {
        if (!nouns.containsKey(nounA))
            throw new IllegalArgumentException();
        if (!nouns.containsKey(nounB))
            throw new IllegalArgumentException();

        int anc = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
        return anc == -1 ? null : synsets[anc];
    }


    // unit tests
    public static void main(final String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        String word = "World_Trade_Center";
        StdOut.print(word);
        StdOut.print(" is noun? - ");
        StdOut.println(wn.isNoun(word));

        // for (String noun : wn.nouns()) StdOut.println(noun);
    }

}
