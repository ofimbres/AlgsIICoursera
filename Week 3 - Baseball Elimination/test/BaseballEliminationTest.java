import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class BaseballEliminationTest extends TestCase {

    public void testNumberOfTeams() {
        BaseballElimination baseballEliminationWith5Teams = new BaseballElimination("test-input/teams5.txt");
        BaseballElimination baseballEliminationWith8Teams = new BaseballElimination("test-input/teams8.txt");

        assertEquals(5, baseballEliminationWith5Teams.numberOfTeams());
        assertEquals(8, baseballEliminationWith8Teams.numberOfTeams());
    }

    public void testWins() {
        BaseballElimination baseballElimination = new BaseballElimination("test-input/teams5.txt");

        assertEquals(75, baseballElimination.wins("New_York"));
        assertEquals(71, baseballElimination.wins("Baltimore"));
        assertEquals(69, baseballElimination.wins("Boston"));
        assertEquals(63, baseballElimination.wins("Toronto"));
        assertEquals(49, baseballElimination.wins("Detroit"));
    }

    public void testWins_ThrowsExceptionWhenCityDoesNotExist() {
        BaseballElimination baseballElimination = new BaseballElimination("test-input/teams5.txt");

        try {
            baseballElimination.wins("CityThatDoesNotExist");
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }

    public void testLosses() {
        BaseballElimination baseballElimination = new BaseballElimination("test-input/teams5.txt");

        assertEquals(59, baseballElimination.losses("New_York"));
        assertEquals(63, baseballElimination.losses("Baltimore"));
        assertEquals(66, baseballElimination.losses("Boston"));
        assertEquals(72, baseballElimination.losses("Toronto"));
        assertEquals(86, baseballElimination.losses("Detroit"));
    }

    public void testLosses_ThrowsExceptionWhenCityDoesNotExist() {
        BaseballElimination baseballElimination = new BaseballElimination("test-input/teams5.txt");

        try {
            baseballElimination.losses("CityThatDoesNotExist");
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }

    public void testRemaining() {
        BaseballElimination baseballElimination = new BaseballElimination("test-input/teams5.txt");

        assertEquals(28, baseballElimination.remaining("New_York"));
        assertEquals(28, baseballElimination.remaining("Baltimore"));
        assertEquals(27, baseballElimination.remaining("Boston"));
        assertEquals(27, baseballElimination.remaining("Toronto"));
        assertEquals(27, baseballElimination.remaining("Detroit"));
    }

    public void testRemaining_ThrowsExceptionWhenCityDoesNotExist() {
        BaseballElimination baseballElimination = new BaseballElimination("test-input/teams5.txt");

        try {
            baseballElimination.remaining("CityThatDoesNotExist");
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }

    public void testAgainst() {
        BaseballElimination baseballElimination = new BaseballElimination("test-input/teams5.txt");

        assertEquals(3, baseballElimination.against("New_York", "Baltimore"));
        assertEquals(8, baseballElimination.against("New_York", "Boston"));
        assertEquals(7, baseballElimination.against("New_York", "Toronto"));
        assertEquals(3, baseballElimination.against("New_York", "Detroit"));
        assertEquals(8, baseballElimination.against("Boston", "New_York"));
        assertEquals(3, baseballElimination.against("Detroit", "Toronto"));
    }

    public void testAgainst_ThrowsExceptionWhenCityDoesNotExist() {
        BaseballElimination baseballElimination = new BaseballElimination("test-input/teams5.txt");

        try {
            baseballElimination.against("Toronto", "CityThatDoesNotExist");
            fail("Exception should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }

    public void testTeams() {
        BaseballElimination baseballElimination = new BaseballElimination("test-input/teams5.txt");

        List<String> actualTeams = new ArrayList<>();
        for (String team : baseballElimination.teams()) {
            actualTeams.add(team);
        }

        assertEquals(5, actualTeams.size());
        assertTrue(actualTeams.containsAll(List.of("New_York", "Baltimore", "Boston", "Toronto", "Detroit")));
    }

    public void testIsEliminated() {
        BaseballElimination baseballElimination = new BaseballElimination("test-input/teams5.txt");

        assertEquals(false, baseballElimination.isEliminated("New_York"));
        assertEquals(true, baseballElimination.isEliminated("Detroit"));
        assertEquals(false, baseballElimination.isEliminated("Baltimore"));
        assertEquals(false, baseballElimination.isEliminated("Toronto"));
        assertEquals(false, baseballElimination.isEliminated("Boston"));
    }

    public void testCertificateOfElimination() {
        BaseballElimination baseballElimination = new BaseballElimination("test-input/teams5.txt");
        List<String> subset = new ArrayList<>();
        for (String team: baseballElimination.certificateOfElimination("Detroit")) {
            subset.add(team);
        }
        assertEquals(4, subset.size());
        assertEquals(true, subset.containsAll(List.of("New_York", "Baltimore", "Boston", "Toronto")));
        assertNull(baseballElimination.certificateOfElimination("New_York"));
    }
}