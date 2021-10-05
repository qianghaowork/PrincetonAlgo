import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {
    private final HashMap<String, Integer> teams; // team and associated ID
    private final HashMap<Integer, String> ids; // map id to team name
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] against;
    private final int currentLead; // id of the current lead team
    private final int num;
    private final HashMap<String, HashSet<String>> certOfElimination;

    public BaseballElimination(String filename) // create a baseball division from given filename in format specified below
    {
        if (filename == null) throw new NullPointerException();
        String line;
        In in = new In(filename);
        if (in.hasNextLine()) {
            line = in.readLine();
            num = Integer.parseInt(line);
        } else throw new IllegalArgumentException("team number not found");
        wins = new int[num];
        losses = new int[num];
        remaining = new int[num];
        against = new int[num][num];
        teams = new HashMap<>();
        ids = new HashMap<>();
        certOfElimination = new HashMap<>();
        int index = 0;
        while (in.hasNextLine()) {
            line = in.readLine();
            line = line.trim(); // remove any leading and trailing spaces
            String[] items = line.split("\\s+");
            teams.put(items[0], index);
            ids.put(index, items[0]);
            wins[index] = Integer.parseInt(items[1]);
            losses[index] = Integer.parseInt(items[2]);
            remaining[index] = Integer.parseInt(items[3]);
            for (int j = 0; j < num; j++)
                against[index][j] = Integer.parseInt(items[4 + j]);
            index++;
        }
        currentLead = max(wins);
    }

    public int numberOfTeams()   // number of teams
    {
        return num;
    }

    public Iterable<String> teams()  // all teams
    {
        return teams.keySet();
    }

    public int wins(String team) // number of wins for given team
    {
        if (team == null) throw new IllegalArgumentException("Argument is null");
        if (!teams.containsKey(team)) throw new IllegalArgumentException("no team " + team);
        int id = teams.get(team);
        return wins[id];
    }

    public int losses(String team) // number of losses for given team
    {
        if (team == null) throw new IllegalArgumentException("Argument is null");
        if (!teams.containsKey(team)) throw new IllegalArgumentException("no team " + team);
        int id = teams.get(team);
        return losses[id];
    }

    public int remaining(String team) // number of remaining games for given team
    {
        if (team == null) throw new IllegalArgumentException("Argument is null");
        if (!teams.containsKey(team)) throw new IllegalArgumentException("no team " + team);
        int id = teams.get(team);
        return remaining[id];
    }

    public int against(String team1, String team2)    // number of remaining games between team1 and team2
    {
        if (team1 == null || team2 == null) throw new IllegalArgumentException("Argument is null");
        if (!teams.containsKey(team1)) throw new IllegalArgumentException("no team " + team1);
        if (!teams.containsKey(team2)) throw new IllegalArgumentException("no team " + team2);
        int id1 = teams.get(team1);
        int id2 = teams.get(team2);
        return against[id1][id2];
    }

    public boolean isEliminated(String team) {
        // input sanity check
        if (team == null) throw new IllegalArgumentException("Argument is null");
        if (!teams.containsKey(team)) throw new IllegalArgumentException("no team " + team);
        if (certOfElimination.containsKey(team)) {
            return certOfElimination.get(team) != null;
        }
        elimination(team);
        return certOfElimination.get(team) != null;
    }

    public Iterable<String> certificateOfElimination(String team) {
        // input sanity check
        if (team == null) throw new IllegalArgumentException("Argument is null");
        if (!teams.containsKey(team)) throw new IllegalArgumentException("no team " + team);
        if (certOfElimination.containsKey(team))
            return certOfElimination.get(team);
        else {
            elimination(team);
            return certOfElimination.get(team);
        }
    }

    private void elimination(String team) {
        if (team == null) throw new IllegalArgumentException("Argument is null");
        if (!teams.containsKey(team)) throw new IllegalArgumentException("no team " + team);
        int target = teams.get(team);
        int targetCapcity = wins[target] + remaining[target];
        if (targetCapcity < wins[currentLead]) {
            HashSet<String> subset = new HashSet<>();
            subset.add(ids.get(currentLead));
            certOfElimination.put(team, subset);
            return;
        }
        int gameNum = 0;
        int[] leftTeams = new int[num];
        Arrays.fill(leftTeams, -1);
        int leftIdx = 1;
        int totalLeft = 0;
        for (int i = 0; i < num; i++) {
            if (i == target) continue;
            leftTeams[i] = leftIdx++;
            for (int j = i + 1; j < num; j++) {
                if (j != target && against[i][j] > 0) {
                    gameNum++;
                    totalLeft += against[i][j];
                }
            }
        }
        FlowNetwork flowNetwork = new FlowNetwork(num + 1 + gameNum);
        int index = 1;
        for (int i = 0; i < num; i++)
            for (int j = i + 1; j < num; j++) {
                if (i != target && j != target && against[i][j] > 0) {
                    flowNetwork.addEdge(new FlowEdge(0, index, against[i][j]));
                    flowNetwork.addEdge(new FlowEdge(index, gameNum + leftTeams[i], Double.POSITIVE_INFINITY));
                    flowNetwork.addEdge(new FlowEdge(index, gameNum + leftTeams[j], Double.POSITIVE_INFINITY));
                    index++;
                }
            }
        for (int i = 0; i < num; i++) {
            if (i == target) continue;
            flowNetwork.addEdge(new FlowEdge(gameNum + leftTeams[i], gameNum + num, targetCapcity - wins[i]));
        }
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, gameNum + num);
        if (fordFulkerson.value() == totalLeft) {
            certOfElimination.put(team, null);
        } else {
            HashSet<String> hs = new HashSet<>();
            for (int i = 0; i < num; i++) {
                if (i != target && fordFulkerson.inCut(gameNum + leftTeams[i]))
                    hs.add(ids.get(i));
            }
            certOfElimination.put(team, hs);
        }
    }

    /**
     * Return the index of max value of the array entries
     */
    private int max(int[] arry) {
        int maxVal = Integer.MIN_VALUE;
        int idx = 0;
        for (int i = 0; i < arry.length; i++) {
            if (arry[i] > maxVal) {
                maxVal = arry[i];
                idx = i;
            }
        }
        return idx;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
