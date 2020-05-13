import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import java.util.Collections; 

public class SAP {
    private final Digraph g;   // copy of associated digraph
    
    private final boolean[] marked1; // marked1[v] = is there an V->v path?
    private final boolean[] marked2; // marked2[v] = is there an W->v path?
    private final int[] distTo1;     // distTo1[v] = length of shortest V->v path
    private final int[] distTo2;     // distTo2[v] = length of shortest W->v path
    private int ancestor;     // length of the shortest path between V and W
    private int distance;     // the nearest ancestor of V and W
    
    // constructor takes a diagraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("G cannot be null.");
        }   
        // defensive copy
        g = new Digraph(G);
        
        marked1 = new boolean[g.V()];
        marked2 = new boolean[g.V()];
        distTo1 = new int[g.V()];
        distTo2 = new int[g.V()];
    }
    
    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return length(Collections.singletonList(v), Collections.singletonList(w));
    }
    
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return ancestor(Collections.singletonList(v), Collections.singletonList(w));
    }
    
    // length of shortest ancetral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        
        bfs(v, w);
        
        return distance;
    }
    
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        
        bfs(v, w);
        
        return ancestor;
    }
    
     // run two bfs lockstep from vSources and wSources to compute sap
    private void bfs(Iterable<Integer> vSources, Iterable<Integer> wSources) {
        ancestor = -1;
        distance = -1;
        
        Stack<Integer> s1 = new Stack<Integer>();
        Stack<Integer> s2 = new Stack<Integer>();
        
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        
        for (int s : vSources) {
            marked1[s] = true;
            distTo1[s] = 0;
            q1.enqueue(s);
            s1.push(s);
        }
        for (int s : wSources) {
            marked2[s] = true;
            distTo2[s] = 0;
            q2.enqueue(s);
            s2.push(s);
        }
        
        while (!q1.isEmpty() || !q2.isEmpty()) {
            
            if (!q1.isEmpty()) {
                int v = q1.dequeue();
                
                if (marked2[v]) {
                    if (distTo1[v] + distTo2[v] < distance || distance == -1) {
                        ancestor = v;
                        distance = distTo1[v] +  distTo2[v];
                    }
                }
                
                // stop adding new vertex to queue if the distance exceeds the length
                if (distTo1[v] < distance || distance == -1) {
                    for (int w : g.adj(v)) {
                        if (!marked1[w]) {
                            marked1[w] = true;
                            distTo1[w] = distTo1[v] + 1;
                            q1.enqueue(w);
                            s1.push(w);
                        }
                    }
                }
            }

            if (!q2.isEmpty()) {
                int v = q2.dequeue();
                
                if (marked1[v]) {
                    if (distTo1[v] +  distTo2[v] < distance || distance == -1) {
                        ancestor = v;
                        distance = distTo1[v] +  distTo2[v];
                    }
                }
                
                // stop adding new vertex to queue if the distance exceeds the length
                if (distTo2[v] < distance || distance == -1) {
                    for (int w : g.adj(v)) {
                        if (!marked2[w]) {
                            marked2[w] = true;
                            distTo2[w] = distTo2[v] + 1;
                            q2.enqueue(w);
                            s2.push(w);
                        }
                    }
                }
            }
        }
        
        // reinitialize auxiliary array for next bfs
        while (!s1.isEmpty()) {
            int v = s1.pop();
            marked1[v] = false;
            distTo1[v] = 0;
        }
        while (!s2.isEmpty()) {
            int v = s2.pop();
            marked2[v] = false;
            distTo2[v] = 0;
        }
    }
    
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("vertices cannot be null.");
        }
        
        for (Integer v : vertices) {
            if (v == null) {
                throw new IllegalArgumentException("vertex cannot be null.");
            }
            
            if (v < 0 || v >= g.V()) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (g.V() - 1) + ".");
            }
        }
    }
    
    // do unit testing in this class
    public static void main(String[] args) {
        // run SAP ../test-input/digraph1.txt
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}