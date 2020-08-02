import edu.princeton.cs.algs4.Picture;
import junit.framework.TestCase;
import java.util.Iterator;
import java.util.Arrays;
    
import static org.junit.Assert.*;

public class SeamCarverTest extends TestCase {
    public void testConstructor_ThrowsExceptionWhenPictureIsNull() {
        try {
            SeamCarver sc = new SeamCarver(null);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testWidth() {
        SeamCarver sc = new SeamCarver(new Picture("../test-input/3x7.png"));
        assertEquals(3, sc.width());
    }
    
    public void testHeight() {
        SeamCarver sc = new SeamCarver(new Picture("../test-input/3x7.png"));
        assertEquals(7, sc.height());
    }
    
    public void testEnergy_ThrowsExceptionWhenXIsOutOfRange() {
        SeamCarver sc = new SeamCarver(new Picture("../test-input/3x4.png"));
        
        // x is less than zero.
        try {
            sc.energy(-1, 2);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
        
        // x is higher than width-1.
        try {
            sc.energy(3, 2);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testEnergy_ThrowsExceptionWhenYIsOutOfRange() {
        SeamCarver sc = new SeamCarver(new Picture("../test-input/3x4.png"));
        
        // y is less than zero.
        try {
            sc.energy(0, -1);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
        
        // y is higher than height-1.
        try {
            sc.energy(0, 4);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testEnergy() {
        SeamCarver sc = new SeamCarver(new Picture("../test-input/3x4.png"));
        assertEquals(1000, sc.energy(1, 0), 0.01);
        assertEquals(Math.sqrt(52225), sc.energy(1, 1), 0.01);
        assertEquals(Math.sqrt(52024), sc.energy(1, 2), 0.01);
    }
    
    public void testFindVerticalSeam() {        
        SeamCarver sc = new SeamCarver(new Picture("../test-input/6x5.png"));
        int[] seam = sc.findVerticalSeam();
        
        assertEquals(5, seam.length);
        assertTrue(3 <= seam[0] && seam[0] <= 5); // edge could be 3, 4 or 5
        assertEquals(4, seam[1]);
        assertEquals(3, seam[2]);
        assertEquals(2, seam[3]);
        assertTrue(1 <= seam[4] && seam[4] <= 3); // edge could be 1, 2 or 3
    }
    
   public void testFindHorizontalSeam() {
       
        SeamCarver sc = new SeamCarver(new Picture("../test-input/6x5.png"));
        int[] seam = sc.findHorizontalSeam();
        
        assertEquals(6, seam.length);
        assertTrue(1 <= seam[0] && seam[0] <= 3); // edge could be 1, 2 or 3
        assertEquals(2, seam[1]);
        assertEquals(1, seam[2]);
        assertEquals(2, seam[3]);
        assertEquals(1, seam[4]);
        assertTrue(0 <= seam[5] && seam[5] <= 2); // edge could be 0, 1, or 2
    }
   
    public void testFindVerticalSeam_OnePixelWidth() {
       
        SeamCarver sc = new SeamCarver(new Picture("../test-input/1x8.png"));
        int[] seam = sc.findVerticalSeam();
        
        assertEquals(8, seam.length);
        
        for (int i = 0; i < 8; i++) {
            assertEquals(0, seam[i]);
        }
    }
   
    public void testRemoveVerticalSeam() {
        Picture original = new Picture("../test-input/6x5.png");
        SeamCarver sc = new SeamCarver(original);
        int[] seam = new int[] { 4, 4, 3, 2, 2 };
        
        sc.removeVerticalSeam(seam);
        
        assertEquals(5, sc.width());
        assertEquals(5, sc.height());
        
        // shallow test
        Picture modified = sc.picture();
        for (int y = 0; y < 5; y++) {
            int x = seam[y];
            assertTrue(modified.getRGB(x, y) != original.getRGB(x, y));
        }
    }
    
    public void testRemoveHorizontalSeam() {
        Picture original = new Picture("../test-input/6x5.png");
        SeamCarver sc = new SeamCarver(original);
        int[] seam = new int[] { 2, 2, 1, 2, 1, 1 };
        
        sc.removeHorizontalSeam(seam);
        
        assertEquals(6, sc.width());
        assertEquals(4, sc.height());
        
        // shallow test
        Picture modified = sc.picture();
        for (int x = 0; x < 6; x++) {
            int y = seam[x];
            assertTrue(modified.getRGB(x, y) != original.getRGB(x, y));
        }
    }
    
    public void testRemoveVerticalSeam_ThrowsExceptionWhenSeamIsNull() {
        SeamCarver sc = new SeamCarver(new Picture("../test-input/6x5.png"));
        
        try {
            sc.removeVerticalSeam(null);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testRemoveVerticalSeam_ThrowsExceptionWhenWidthIsLessThanOne() {
        SeamCarver sc = new SeamCarver(new Picture("../test-input/1x8.png"));
        int[] seam = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        
        try {
            sc.removeVerticalSeam(seam);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testRemoveVerticalSeam_ThrowsExceptionWhenSeamLengthDoesNotMatch() {
        SeamCarver sc = new SeamCarver(new Picture("../test-input/6x5.png"));
        int[] seam = new int[] { 4, 4 }; // 2 when length should be 5
        
        try {
            sc.removeVerticalSeam(seam);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testRemoveVerticalSeam_ThrowsExceptionWhenSeamIndexIsOutOfRange() {
        SeamCarver sc = new SeamCarver(new Picture("../test-input/6x5.png"));
        int[] seam = new int[] { 4, 9, 3, 2, 2 }; // 2nd index is out of range
        
        try {
            sc.removeVerticalSeam(seam);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testRemoveVerticalSeam_ThrowsExceptionWhenTwoAdjacentEntriesDifferByMoreThanOne() {
        SeamCarver sc = new SeamCarver(new Picture("../test-input/6x5.png"));
        int[] seam = new int[] { 4, 2, 3, 2, 2 }; // 2nd index is not adjacent to 1st index
        
        try {
            sc.removeVerticalSeam(seam);
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testEnergy_recalculate() {
        SeamCarver sc = new SeamCarver(new Picture("../test-input/6x5.png"));
        
        assertEquals(Math.sqrt(22808), sc.energy(2, 1), 0.01);
        assertEquals(Math.sqrt(54796), sc.energy(3, 1), 0.01);
        
        int[] seam = new int[] { 4, 4, 3, 2, 2 };
        sc.removeVerticalSeam(seam);
        
        assertEquals(Math.sqrt(22808), sc.energy(2, 1), 0.01); // energy not affected
        assertEquals(Math.sqrt(31791), sc.energy(3, 1), 0.01); // energy changes
    }
}