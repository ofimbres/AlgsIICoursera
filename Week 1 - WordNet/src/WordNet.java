import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Topological;

public class WordNet {
    private ST<String, SET<Integer>> nounsST;  // noun and ids contain the noun
    private ST<Integer, String> synsetsST;
    private Digraph hypernymDigraph;
    private int synsetsCount;
    private int outEdgeCount;                   // the number of vertices having an out edge
    private SAP sap;
    
    // constructor takes the name of the two input files
    public WordNet(String synsetsFile, String hypernymsFile) {
        if (synsetsFile == null || hypernymsFile == null) {
            throw new IllegalArgumentException("synsets and hypernyms cannot be null.");
        }
        
        readSynsets(synsetsFile);
        readHypernyms(hypernymsFile);
    }
    
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsST.keys();
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounsST.contains(word);
    }
    
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("nounA and nounB should be WordNet nouns.");
        }
        
        SET<Integer> synsetIdASet = nounsST.get(nounA);
        SET<Integer> synsetIdBSet = nounsST.get(nounB);
        
        return sap.length(synsetIdASet, synsetIdBSet);
    }
    
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("nounA and nounB should be WordNet nouns.");
        }
        
        SET<Integer> synsetIdASet = nounsST.get(nounA);
        SET<Integer> synsetIdBSet = nounsST.get(nounB);
        
        int synsetIdAncestor = sap.ancestor(synsetIdASet, synsetIdBSet);
        return synsetsST.get(synsetIdAncestor);
    }
    
    private void readSynsets(String synsetsFile) {
        In in = new In(synsetsFile);
        nounsST = new ST<String, SET<Integer>>();
        synsetsST = new ST<Integer, String>();
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            int synsetId = Integer.parseInt(fields[0]);
            String synonymSet = fields[1];
            for (String synset : synonymSet.split(" ")) {
                SET<Integer> synsetIdSet = nounsST.get(synset);
                if (synsetIdSet == null) {
                    synsetIdSet = new SET<Integer>();
                    nounsST.put(synset, synsetIdSet);
                }
                synsetIdSet.add(synsetId);
            }
            synsetsST.put(synsetId, synonymSet);
            synsetsCount++;
        }
    }
    
    private void readHypernyms(String hypernymsFile) {
        In in = new In(hypernymsFile);
        hypernymDigraph = new Digraph(synsetsCount);
        boolean[] outEdge = new boolean[synsetsCount];
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            int synsetId = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int hypernym = Integer.parseInt(fields[i]); // synset's hypernyms
                hypernymDigraph.addEdge(synsetId, hypernym);
            }
            
            // vertex has outdegree?
            if (!outEdge[synsetId] && fields.length > 1) {
                outEdge[synsetId] = true;
                outEdgeCount++;
            }
        }
        isRootedDAG();
        sap = new SAP(hypernymDigraph);
    }
    
    private void isRootedDAG() {
        if (synsetsCount - outEdgeCount != 1) {
            throw new IllegalArgumentException("digraph must have only one root.");
        }
                
        Topological topological = new Topological(hypernymDigraph);
        
        if (!topological.hasOrder()) {
            throw new IllegalArgumentException("hypernyms is not a rooted DAG.");
        }
    }
}