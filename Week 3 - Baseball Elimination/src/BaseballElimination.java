import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
    private final int numOfTeams;
    private final String[] teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] remainingGames;
    private final Map<String, Integer> teamNameToIndexMap;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numOfTeams = in.readInt();

        teams = new String[numOfTeams];
        wins = new int[numOfTeams];
        losses = new int[numOfTeams];
        remaining = new int[numOfTeams];
        remainingGames = new int[numOfTeams][numOfTeams];
        teamNameToIndexMap = new HashMap<>(numOfTeams);

        for (int i = 0; i < numOfTeams; i++) {
            teams[i] = in.readString();
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            for (int j = 0; j < numOfTeams; j++) {
                remainingGames[i][j] = in.readInt();
            }

            teamNameToIndexMap.put(teams[i], i);
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teamNameToIndexMap.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        validateTeam(team);
        int i = teamNameToIndexMap.get(team);
        return wins[i];
    }

    // number of losses for given team
    public int losses(String team) {
        validateTeam(team);
        int i = teamNameToIndexMap.get(team);
        return losses[i];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validateTeam(team);
        int i = teamNameToIndexMap.get(team);
        return remaining[i];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        int i = teamNameToIndexMap.get(team1);
        int j = teamNameToIndexMap.get(team2);
        return remainingGames[i][j];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validateTeam(team);
        CheckFlowResult result = checkFlow(team);
        return result.isEliminated;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        CheckFlowResult result = checkFlow(team);
        return result.certicateOfElimination;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("test-input/teams5.txt");
        for (String team : division.teams()) {
            if (!team.equals("Detroit")) continue;
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    private void validateTeam(String team) {
        int i = teamNameToIndexMap.getOrDefault(team, -1);
        if (i == -1) throw new IllegalArgumentException("Team not valid");
    }

    private int getNumberOfMatches() {
        // triangular matrix
        int n = numOfTeams - 1;
        return n * (n - 1) / 2;
    }

    private CheckFlowResult checkFlow(String team) {
        int teamIndex = teamNameToIndexMap.get(team);
        boolean isEliminated = false;
        List<String> teamSubset = new ArrayList<>();

        // trivial elimination
        for (int i = 0; i < numOfTeams; i++) {
            if (i == teamIndex) continue;

            if (wins[teamIndex] + remaining[teamIndex] < wins[i]) {
                teamSubset.add(teams[i]);
                isEliminated = true;
            }
        }

        if (isEliminated) {
            return new CheckFlowResult(true, teamSubset);
        }

        // non-trivial elimination. Initialize flow network with source [size-2] and sink [size-1]
        int numOfMatches = getNumberOfMatches();
        int numOfVertices = 2 + numOfMatches + numOfTeams; // including a vertex not connected (the team itself)

        int sourceVerticeIndex = numOfVertices - 2;
        int sinkVerticeIndex = numOfVertices - 1;
        FlowNetwork flowNetwork = new FlowNetwork(numOfVertices);

        // add edges for source to game combination vertices to teams
        int gameMatchVerticeIndex = 0;

        for (int i = 0; i < numOfTeams; i++) {
            if (i == teamIndex) continue;

            for (int j = i + 1; j < numOfTeams; j++) {
                if (j == teamIndex) continue;

                // add edges between the sourceVertex and the matches.
                flowNetwork.addEdge(new FlowEdge(sourceVerticeIndex, gameMatchVerticeIndex, remainingGames[i][j]));
                // add edge between the match vertex and the two teams playing in that match
                flowNetwork.addEdge(new FlowEdge(gameMatchVerticeIndex, numOfMatches + i, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(gameMatchVerticeIndex, numOfMatches + j, Double.POSITIVE_INFINITY));

                gameMatchVerticeIndex++;
            }

            // add edge between the teams and the target vertex
            flowNetwork.addEdge(new FlowEdge(numOfMatches + i, sinkVerticeIndex, wins[teamIndex] + remaining[teamIndex] - wins[i]));
        }

        FordFulkerson ff = new FordFulkerson(flowNetwork, sourceVerticeIndex, sinkVerticeIndex);

        // check each edge from the source to each match vertex. If any edge
        // flow value is not at full capacity, the team is eliminated
        // because they cannot win even if they manage to win all of there
        // remaining games and the team in the lead loses all of their remaining games
        for (FlowEdge edge : flowNetwork.adj(sourceVerticeIndex)) {
            if (edge.flow() != edge.capacity()) {
                isEliminated = true;
                break;
            }
        }
        // loop through each team vertex and check if it is in the mincut
        if (isEliminated) {
            for (int v = gameMatchVerticeIndex; v < sourceVerticeIndex; v++) {
                if (ff.inCut(v)) {
                    int i = v - gameMatchVerticeIndex;
                    teamSubset.add(teams[i]);
                }
            }
        }

        return new CheckFlowResult(isEliminated, teamSubset.isEmpty() ? null : teamSubset);
    }

    private class CheckFlowResult {
        boolean isEliminated;
        Iterable<String> certicateOfElimination;

        public CheckFlowResult(boolean isEliminated, Iterable<String> certicateOfElimination) {
            this.isEliminated = isEliminated;
            this.certicateOfElimination = certicateOfElimination;
        }
    }
}