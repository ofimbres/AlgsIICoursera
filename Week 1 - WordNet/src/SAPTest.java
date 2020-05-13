import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import junit.framework.TestCase;
import java.util.Iterator;
import java.util.Arrays; 

public class SAPTest extends TestCase {
    
    public void testConstructor_ThrowsExceptionWhenDigraphIsNull() {
        try {
            SAP sap = new SAP(null);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testLength_SingleSource() {
        In in = new In("../test-input/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        assertEquals(4, sap.length(3, 11));
        assertEquals(3, sap.length(9, 12));
        assertEquals(4, sap.length(7, 2));
        assertEquals(-1, sap.length(1, 6));
    }
    
    public void testLength_SingleSource2() {        
        In in = new In("../test-input/digraph3.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        assertEquals(4, sap.length(12, 13));
    }
    
    public void testLength_SingleSource_ThrowsExceptionWhenVertexOutOfRange() {
        In in = new In("../test-input/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        try {
            sap.length(3, 13);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testAncestor_SingleSource() {
        In in = new In("../test-input/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        assertEquals(1, sap.ancestor(3, 11));
        assertEquals(5, sap.ancestor(9, 12));
        assertEquals(0, sap.ancestor(7, 2));
        assertEquals(-1, sap.ancestor(1, 6));
    }
    
    public void testAncestor_SingleSource_ThrowsExceptionWhenVertexOutOfRange() {
        In in = new In("../test-input/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        try {
            sap.ancestor(-1, 12);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testLength_MultipleSources() {
        In in = new In("../test-input/digraph25.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        assertEquals(4, sap.length(Arrays.asList(13, 23, 24), Arrays.asList(6, 16, 17)));
    }
    
    public void testLength_MultipleSources_ThrowsExceptionWhenVertexOutOfRange() {
        In in = new In("../test-input/digraph25.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        try {
            sap.length(Arrays.asList(13, 23, 29), null);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testLength_MultipleSources_ThrowsExceptionWhenIterableIsNull() {
        In in = new In("../test-input/digraph25.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        try {
            sap.length(Arrays.asList(13, 23, 24), null);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testAncestor_MultipleSources() {
        In in = new In("../test-input/digraph25.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        assertEquals(3, sap.ancestor(Arrays.asList(13, 23, 24), Arrays.asList(6, 16, 17)));
    }
    
    public void testAncestor_MultipleSources_ThrowsExceptionWhenVertexOutOfRange() {
        In in = new In("../test-input/digraph25.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        try {
            sap.ancestor(null,  Arrays.asList(6, 42, 17));
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testAncestor_MultipleSources_ThrowsExceptionWhenIterableIsNull() {
        In in = new In("../test-input/digraph25.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        try {
            sap.ancestor(null,  Arrays.asList(6, 16, 17));
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
}