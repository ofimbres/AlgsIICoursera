import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;
    
    // constructor takes a WordNet object
    public Outcast(WordNet wn) {
        if (wn == null) {
            throw new IllegalArgumentException("wordnet cannot be null.");
        }   
        wordnet = wn;
    }
    
    // given an array of WordNet nouns, return an outcase
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException("nouns cannot be null.");
        }   
        
        int distanceMax = -1;
        int nounId = -1;
        
        // compute the sum of the distances between each noun
        // and return a noun xt for which dt is maximum
        for (int i = 0; i < nouns.length; i++) {
            int distanceSum = 0;
            for (int j = 0; j < nouns.length; j++) {
                distanceSum += wordnet.distance(nouns[i], nouns[j]);
            }
            
            if (distanceSum > distanceMax) {
                distanceMax = distanceSum;
                nounId = i;
            }
        }
        return nouns[nounId];
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