import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null || nouns.length == 0)
            throw new IllegalArgumentException();

        final int N = nouns.length;
        // int[] cache = new int[(N*N - N) / 2];
        // brute force for a while
        int[][] cache = new int[N][N];
        int c = 0; // cache position
        int maxD = -1;
        String maxNoun = null;
        for (int i = 0; i < N; i++) {
            int d = 0;
            // get from cache
            for (int j = 0; j < i; j++) {
                d += cache[i][j];
            }
            for (int j = i + 1; j < N; j++) {
                int dist = wordNet.distance(nouns[i], nouns[j]);
                cache[j][i] = dist;
                d += dist;
            }

            if (d > maxD) {
                maxD = d;
                maxNoun = nouns[i];
            }
        }

        return maxNoun;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}