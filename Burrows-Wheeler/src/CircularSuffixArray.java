public class CircularSuffixArray
{
    private static final int CUTOFF =  15;   // cutoff to insertion sort
    private final char[] text;
    private final int N;
    private int[] index;
    
    // circular suffix array of s
    public CircularSuffixArray(String s)
    {
        text = s.toCharArray();
        N = text.length;
        index = new int[N];
        for (int i = 0; i < N; i++)
            index[i] = i;
        sort(index);
    }
    
    // sort the array a[] of strings
    private void sort(int[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length-1, 0);
    }
    
    // 3-way string quicksort a[lo..hi] starting at dth character
    private void sort(int[] a, int lo, int hi, int d) { 

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = text[(a[lo] + d) % N];
        int i = lo + 1;
        while (i <= gt) {
            int t = text[(a[i] + d) % N];
            if      (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else              i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
        sort(a, lo, lt-1, d);
        if (v >= 0) sort(a, lt, gt, d+1);
        sort(a, gt+1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private void insertion(int[] a, int lo, int hi, int d)
    {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
                exch(a, j, j-1);
    }

    // exchange a[i] and a[j]
    private static void exch(int[] a, int i, int j)
    {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    // is v less than w, starting at character d
    private boolean less(int v, int w, int d)
    {
        for (int i = d; i < N; i++) {
            if (text[(i + v) % N] < text[(i + w) % N]) return true;
            if (text[(i + v) % N] > text[(i + w) % N]) return false;
        }
        return false;
    }
    
    // length of s
    public int length()
    {
        return N;
    }
    
    // returns index of ith sorted suffix
    public int index(int i)
    {
        return index[i];
    }
    
    // Unit tests
    public static void main(String[] args)
    {
        String s = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(s);
        System.out.println(csa.length());
        for (int i = 0; i < csa.length(); i++)
        {
            System.out.println(csa.index(i));
        }
    }
}
