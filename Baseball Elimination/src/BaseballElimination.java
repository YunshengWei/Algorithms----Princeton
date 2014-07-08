import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class BaseballElimination
{
    private final int N;
    private final HashMap<String, Integer> teamIndex;
    private final String[] teamNames;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] against;
    private String lastQuery = null;
    private Bag<String> subsetR = null;
    
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename)
    {
        In in = new In(filename);
        N = in.readInt();
        wins = new int[N];
        losses = new int[N];
        remaining = new int[N];
        against = new int[N][N];
        teamIndex = new HashMap<String, Integer>();
        teamNames = new String[N];
        for (int i = 0; i < N; i++)
        {
            String teamName = in.readString();
            teamIndex.put(teamName, i);
            teamNames[i] = teamName;
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < N; j++)
                against[i][j] = in.readInt();
        }
        in.close();
    }
    
    // number of teams
    public int numberOfTeams()
    {
        return N;
    }
    
    // all teams
    public Iterable<String> teams()
    {
        return teamIndex.keySet();
    }
    
    // number of wins for given team
    public int wins(String team)
    {
        if (!teamIndex.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        return wins[teamIndex.get(team)];
    }
    
    // number of losses for given team
    public int losses(String team)
    {
        if (!teamIndex.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        return losses[teamIndex.get(team)];
    }
    
    // number of remaining games for given team
    public int remaining(String team)
    {
        if (!teamIndex.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        return remaining[teamIndex.get(team)];
    }
    
    // number of remaining games between team1 and team2
    public int against(String team1, String team2)
    {
        if (!teamIndex.containsKey(team1) || !teamIndex.containsKey(team2))
            throw new java.lang.IllegalArgumentException();
        return against[teamIndex.get(team1)][teamIndex.get(team2)];
    }
    
    private void calcElimination(String team)
    {
        lastQuery = team;
        subsetR = new Bag<String>();
        int index = teamIndex.get(team);
        
        // Trivial elimination
        for (int j = 0; j < N; j++)
            if (wins[j] > remaining[index] + wins[index])
                subsetR.add(teamNames[j]);
        if (!subsetR.isEmpty())
            return;
        
        // Nontrivial elimination
        HashMap<HashSet<Integer>, Integer> vm = new HashMap<HashSet<Integer>, Integer>();
        int count = 0;
        for (int i = 0; i < N; i++)
        {
            if (remaining[i] > 0 && i != index)
            {
                HashSet<Integer> key = new HashSet<Integer>();
                key.add(i);
                vm.put(key, ++count);
                for (int j = i + 1; j < N; j++)
                {
                    if (j != index && against[i][j] > 0)
                    {
                        HashSet<Integer> key2 = new HashSet<Integer>();
                        key2.add(i);
                        key2.add(j);
                        vm.put(key2, ++count);
                    }
                }
            }
        }
        
        FlowNetwork fn = new FlowNetwork(vm.size() + 2);
        for (Entry<HashSet<Integer>, Integer> e : vm.entrySet())
        {
            HashSet<Integer> key = e.getKey();
            int value = e.getValue();
            Iterator<Integer> it = key.iterator();
            if (key.size() == 2)
            {
                int i = it.next();
                int j = it.next();
                fn.addEdge(new FlowEdge(0, value, against[i][j]));
                HashSet<Integer> ikey = new HashSet<Integer>();
                ikey.add(i);
                HashSet<Integer> jkey = new HashSet<Integer>();
                jkey.add(j);
                fn.addEdge(new FlowEdge(value, vm.get(ikey), Integer.MAX_VALUE));
                fn.addEdge(new FlowEdge(value, vm.get(jkey), Integer.MAX_VALUE));
            }
            else
            {
                fn.addEdge(new FlowEdge(value, vm.size() + 1, remaining[index] + wins[index] - wins[it.next()]));
            }
        }
        
        FordFulkerson ff = new FordFulkerson(fn, 0, vm.size() + 1);
        for (int i = 0; i < N; i++)
        {
            HashSet<Integer> ikey = new HashSet<Integer>();
            ikey.add(i);
            if (vm.get(ikey) != null && ff.inCut(vm.get(ikey)))
                subsetR.add(teamNames[i]);
        }
    }
    
    // is given team eliminated?
    public boolean isEliminated(String team)
    {
        if (!teamIndex.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        if (!team.equals(lastQuery))
            calcElimination(team);
        return !subsetR.isEmpty();
    }
    
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team)
    {
        if (!teamIndex.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        if (!team.equals(lastQuery))
            calcElimination(team);
        // In common case, it should not return an Iterable shared by class and client
        return subsetR.isEmpty() ? null : subsetR;
    }
    
    public static void main(String[] args) 
    {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) 
        {
            if (division.isEliminated(team)) 
            {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else
            {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
