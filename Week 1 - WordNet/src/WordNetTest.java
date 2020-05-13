import junit.framework.TestCase;

public class WordNetTest extends TestCase {
    
    public void testConstructor_ThrowsExceptionWhenFilenameIsNull() {
        try {
            WordNet wordnet = new WordNet(null, "../test-input/hypernyms.txt");
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testConstructor_ThrowsExceptionWhenIsNotDAG() {
        try {
            WordNet wordnet = new WordNet("../test-input/synsets.txt", "../test-input/hypernyms3InvalidCycle.txt");
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testConstructor_ThrowsExceptionWhenHasMoreThanOneRoot() {
        try {
            WordNet wordnet = new WordNet("../test-input/synsets3.txt", "../test-input/hypernyms3InvalidTwoRoots.txt");
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testNouns() {
        WordNet wordnet = new WordNet("../test-input/synsets.txt", "../test-input/hypernyms.txt");
        assertNotNull(wordnet.nouns());
        
        int nounCount = 0;
        for (String noun : wordnet.nouns()) {
            nounCount++;
        }
        assertEquals(119188, nounCount);
    }
    
    public void testIsNoun() {
        WordNet wordnet = new WordNet("../test-input/synsets.txt", "../test-input/hypernyms.txt");
        assertTrue(wordnet.isNoun("word"));
        assertTrue(wordnet.isNoun("bird"));
        assertFalse(wordnet.isNoun("2word2"));
    }
    
    public void testDistance() {
        WordNet wordnet = new WordNet("../test-input/synsets.txt", "../test-input/hypernyms.txt");
        assertEquals(5, wordnet.distance("worm", "bird"));
        assertEquals(23, wordnet.distance("white_marlin", "mileage"));
        assertEquals(29, wordnet.distance("Brown_Swiss", "barrel_roll"));
    }
    
    public void testDistance_ThrowsExceptionWhenNounIsNotAWordnetNoun() {
        try {
            WordNet wordnet = new WordNet("../test-input/synsets.txt", "../test-input/hypernyms.txt");
            wordnet.distance("2word2", "2bird2");
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
    
    public void testSap() {
        WordNet wordnet = new WordNet("../test-input/synsets.txt", "../test-input/hypernyms.txt");
        assertEquals("animal animate_being beast brute creature fauna", wordnet.sap("worm", "bird"));
        assertEquals("physical_entity", wordnet.sap("individual", "edible_fruit"));
    }
    
    public void testSap_ThrowsExceptionWhenNounIsNotAWordnetNoun() {
        try {
            WordNet wordnet = new WordNet("../test-input/synsets.txt", "../test-input/hypernyms.txt");
            wordnet.sap("2word2", "2bird2");
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }
}