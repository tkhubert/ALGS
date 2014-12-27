public class BaseballElimination
{
    private static final double INFINITY = Double.MAX_VALUE;
    
    private int N;
    private int V;
    
    private int[]   w;
    private int[]   l;
    private int[]   r;
    private int[][] g;

    private String[]            idToTeam;
    private ST<String, Integer> teamToId;
    
    private String        teamCached;
    private boolean       isEliminatedCached;
    private Stack<String> inCutCached;
    
    
    public BaseballElimination(String filename) // create a baseball division from given filename in format specified below
    {
        In BEIn = new In(filename);
        N = BEIn.readInt();
        V = 2+N+N*(N-1)/2;

        w = new int[N];
        l = new int[N];
        r = new int[N];
        g = new int[N][N];
        
        idToTeam = new String[N];
        teamToId = new ST<String, Integer>();

        for (int i=0; i<N; ++i)
        {
            String team = BEIn.readString();
            teamToId.put(team,i);
            idToTeam[i] = team;
            
            w[i] = BEIn.readInt();
            l[i] = BEIn.readInt();
            r[i] = BEIn.readInt();
            
            for (int j=0; j<N; ++j)
                g[i][j] = BEIn.readInt();
        } 
        
        teamCached = "000";
    }
    //
    public int numberOfTeams() // number of teams
    {
        return N;
    }
    //
    public Iterable<String> teams() // all teams
    {
        return teamToId.keys();
    }
    //
    private void checkTeam(String team)
    {
        if (!teamToId.contains(team))
            throw new java.lang.IllegalArgumentException("this team is not part of the set of teams");
    }
    //
    public int wins(String team) // number of wins for given team
    {
        checkTeam(team);
        return w[teamToId.get(team)];
    }
    //
    public int losses(String team) // number of losses for given team
    {
        checkTeam(team);
        return l[teamToId.get(team)];
    }
    //
    public int remaining(String team) // number of remaining games for given team
    {
        checkTeam(team);
        return r[teamToId.get(team)];
    }
    //
    public int against(String team1, String team2) // number of remaining games between team1 and team2
    {
        checkTeam(team1);
        checkTeam(team2);
        return g[teamToId.get(team1)][teamToId.get(team2)];
    }
    //
    public boolean isEliminated(String team) // is given team eliminated?
    {
        checkTeam(team);
        
        if (team.equals(teamCached))
            return isEliminatedCached;
        
        int id = teamToId.get(team);
        int maxWins = w[id]+r[id];
        
        // trivial check
        for (int i=0; i<N; ++i)
        {
            if (w[i]>maxWins)
            {
                isEliminatedCached = true;
                inCutCached = new Stack<String>();
                inCutCached.push(idToTeam[i]);
                return isEliminatedCached;
            }
        }
                
        // maxFlow formulation
        FlowNetwork G = new FlowNetwork(V);

        int k=1;
        int maxFlow = 0;
        for (int i=0; i<N; ++i)
        {
            for (int j=i+1; j<N; ++j)
            {
                G.addEdge(new FlowEdge(0, k            , g[i][j] ));
                G.addEdge(new FlowEdge(k, i+1+N*(N-1)/2, INFINITY));
                G.addEdge(new FlowEdge(k, j+1+N*(N-1)/2, INFINITY));
                
                maxFlow += g[i][j];
                k++;
            }
            G.addEdge(new FlowEdge(i+1+N*(N-1)/2, V-1, maxWins-w[i]));
        }
        
        FordFulkerson FF = new FordFulkerson(G, 0, V-1);
        Double realMaxFlow  = FF.value();
        
        isEliminatedCached = maxFlow != realMaxFlow.intValue();
        
        // set inCut
        if (isEliminatedCached)
        {
            inCutCached = new Stack<String>();
            for (int i=0; i<N; ++i)
            {
                if (FF.inCut(i+1+N*(N-1)/2))
                    inCutCached.push(idToTeam[i]);
            }
        }
        else
            inCutCached = null;
        
        return isEliminatedCached;
    }
    //
    public Iterable<String> certificateOfElimination(String team) // subset R of teams that eliminates given team; null if not eliminated
    {
        checkTeam(team);
        
        if (!team.equals(teamCached))
            isEliminated(team);
        
        return inCutCached;
    }
    //
    public static void main(String[] args) 
    {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
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
}
//
