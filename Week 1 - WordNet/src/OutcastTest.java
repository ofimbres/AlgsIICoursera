import junit.framework.TestCase;

public class OutcastTest extends TestCase {
    
    public void testOutcast() {
        WordNet wordnet = new WordNet("../test-input/synsets.txt", "../test-input/hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        
        
        assertEquals("table", outcast.outcast(new String[] { "horse", "zebra", "cat", "bear", "table" }));
        assertEquals("bed", outcast.outcast(new String[] { "water", "soda", "bed", "orange_juice", "milk", "apple_juice", "tea", "coffee" }));
        assertEquals("potato", outcast.outcast(new String[] { "apple", "pear", "peach", "banana", "lime", "lemon", "blueberry", "strawberry", "mango", "watermelon", "potato" }));
    }
}