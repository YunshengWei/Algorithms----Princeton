public class PercolationStats 
{
	private double[] threshold;
	private static final double CI95 = 1.96;
	
	// perform T independent computational experiments on an N-by-N grid
	public PercolationStats(int N, int T)
	{
		if (N <= 0 || T <= 0)
			throw new IllegalArgumentException("N or T can't <= 0");
		threshold = new double[T];
		for (int k = 0; k < T; k++)
		{
			double count = 0;
			Percolation perc = new Percolation(N);
			do 
			{
				int i = StdRandom.uniform(1, N + 1);
				int j = StdRandom.uniform(1, N + 1);
				if (!perc.isOpen(i, j))
				{
					perc.open(i, j);
					count++;
				}
			} while (!perc.percolates()); 
			threshold[k] = count / (N * N);
		}
	}
	
	// sample mean of percolation threshold
	public double mean()
	{
		return StdStats.mean(threshold);
	}
	
	// sample standard deviation of percolation threshold
	public double stddev()
	{
		return StdStats.stddev(threshold);
	}
	
	// returns lower bound of the 95% confidence interval
	public double confidenceLo()
	{
		return mean() - CI95 * stddev() / Math.sqrt(threshold.length); 
	}
	
	// returns upper bound of the 95% confidence interval
	public double confidenceHi()
	{
		return mean() + CI95 * stddev() / Math.sqrt(threshold.length);
	}
	
	// test client
	public static void main(String[] args)
	{
		PercolationStats perc = new PercolationStats(
				Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		System.out.printf("mean                    = %f\n", perc.mean());
		System.out.printf("stddev                  = %f\n", perc.stddev());
		System.out.printf("95%% confidence interval = %f, %f\n", 
				perc.confidenceLo(), perc.confidenceHi());
		
	}
}
