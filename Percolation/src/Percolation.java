public class Percolation 
{
	private boolean[][] sites;
	private int N;
	private WeightedQuickUnionUF wquUF;
	private WeightedQuickUnionUF wquUF2;	//**************
	
	// Translate index (i, j) into index in wquUF
	private int translateIndex(int i, int j)
	{
		return (i - 1) * N + j;
	}
	
	// create a N-by-N grid, with all sites blocked
	public Percolation(int N)
	{
		sites = new boolean[N + 2][N + 2];
		this.N = N;
		wquUF = new WeightedQuickUnionUF(N * N + 2);
		wquUF2 = new WeightedQuickUnionUF(N * N + 2);
		for (int i = 1, last = N * N + 1; i <= N; i++)
		{
			wquUF.union(0, i);
			wquUF.union(last, last - i);
			wquUF2.union(0, i);
		}
	}
	
	// open site (row i, column j) if it is not already
	public void open(int i, int j)
	{
		int index = translateIndex(i, j);
		if (index < 1 || index > N * N)
			throw new IndexOutOfBoundsException("index out of bounds");
		if (!sites[i][j])
		{
			sites[i][j] = true;
			if (sites[i - 1][j])
			{
				wquUF.union(index - N, index);
				wquUF2.union(index - N, index);
			}
			if (sites[i + 1][j])
			{
				wquUF.union(index + N, index);
				wquUF2.union(index + N, index);
			}
			if (sites[i][j - 1])
			{
				wquUF.union(index, index - 1);
				wquUF2.union(index, index - 1);
			}
			if (sites[i][j + 1])
			{
				wquUF.union(index, index + 1);
				wquUF2.union(index, index + 1);
			}
		}
	}
	
	// is site (row i, column j) open?
	public boolean isOpen(int i, int j)
	{
		if (i <= 0 || i > N || j <= 0 || j > N)
			throw new IndexOutOfBoundsException("index out of bounds");
		return sites[i][j];
	}
	
	// is site (row i, column j) full?
	public boolean isFull(int i, int j)
	{
		int index = translateIndex(i, j);
		if (index < 1 || index > N * N)
			throw new IndexOutOfBoundsException("index out of bounds");
		return isOpen(i, j) && wquUF2.connected(0, index);
	}
	
	// does the system percolate?
	public boolean percolates()
	{
		return N == 1 ? isOpen(1, 1) : wquUF.connected(0, N * N + 1);
	}
}
