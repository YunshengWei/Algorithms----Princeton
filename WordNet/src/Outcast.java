import java.io.IOException;


public class Outcast
{
    private WordNet wordnet;
    
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet)
    {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns)
    {
        String outNoun = nouns[0];
        int maxDist = Integer.MIN_VALUE;
        for (String nounA : nouns)
        {
            int dist = 0;
            for (String nounB : nouns)
                dist += wordnet.distance(nounA, nounB);
            if (dist > maxDist)
            {
                maxDist = dist;
                outNoun = nounA;
            }
        }
        return outNoun;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) throws IOException
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) 
        {
            String[] nouns = new In(args[t]).readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
