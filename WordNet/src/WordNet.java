import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class WordNet
{
    private HashMap<String, Bag<Integer>> wnHm;
    private ArrayList<String> wnAl;
    private SAP wnSap;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) throws IOException
    {
        BufferedReader synIn = new BufferedReader(new FileReader(synsets));
        BufferedReader hypIn = new BufferedReader(new FileReader(hypernyms));
        
        wnHm = new HashMap<String, Bag<Integer>>();
        wnAl = new ArrayList<String>();
        String line;
        int count = 0;
        while ((line = synIn.readLine()) != null)
        {
            String[] parts = line.split(",");
            int sID = Integer.parseInt(parts[0]);
            for (String word : parts[1].split(" "))
            {
                if (!wnHm.containsKey(word))
                    wnHm.put(word, new Bag<Integer>());
                wnHm.get(word).add(sID);
            }
            wnAl.add(parts[1]);
            count++;
        }
        synIn.close();
            
        Digraph wnDag = new Digraph(count);
        while ((line = hypIn.readLine()) != null)
        {
            String[] vertices = line.split(",");
            int start = Integer.parseInt(vertices[0]);
            for (int i = 1; i != vertices.length; i++)
            {
                wnDag.addEdge(start, Integer.parseInt(vertices[i]));
            }
        }
        hypIn.close();
        
        // Detect whether it is a rooted DAG
        Digraph wnDagRe = wnDag.reverse();
        Iterable<Integer> order = new Topological(wnDagRe).order();
        if (order == null)
            throw new java.lang.IllegalArgumentException("Have cycles!");
        int candiRoot = order.iterator().next();
        BreadthFirstDirectedPaths bfdp = new BreadthFirstDirectedPaths(wnDagRe, candiRoot);
        for (int i = 0; i < count; i++)
        {
            if (!bfdp.hasPathTo(i))
                throw new java.lang.IllegalArgumentException("More than one ancestor!");
        }
        wnSap = new SAP(wnDag);
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns()
    {
        return wnHm.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        return wnHm.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        if (!wnHm.containsKey(nounA) || !wnHm.containsKey(nounB))
            throw new java.lang.IllegalArgumentException();
        return wnSap.length(wnHm.get(nounA), wnHm.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        if (!wnHm.containsKey(nounA) || !wnHm.containsKey(nounB))
            throw new java.lang.IllegalArgumentException();
        return wnAl.get(wnSap.ancestor(wnHm.get(nounA), wnHm.get(nounB)));
    }

    // for unit testing of this class
    public static void main(String[] args)
    {
        
    }
}
