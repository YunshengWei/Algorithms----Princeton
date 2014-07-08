import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> 
{
	private Item[] arr;
	private int N;
	
	// construct an empty randomized queue
	@SuppressWarnings("unchecked")
	public RandomizedQueue()
	{
		N = 0;
		arr = (Item[]) new Object[1];
	}
	
	// is the queue empty?
	public boolean isEmpty() { return N == 0; }
	
	// return the number of items on the queue
	public int size() { return N; }
	
	// inner method
	@SuppressWarnings("unchecked")
	private void resize(int capacity)
	{
		Item[] copy = (Item[]) new Object[capacity];
		for (int i = 0; i < N; i++)
			copy[i] = arr[i];
		arr = copy;
	}
	
	// add the item
	public void enqueue(Item item)
	{
		if (item == null)
			throw new NullPointerException("Item cannot be null.");
		if (N == arr.length)
			resize(2 * arr.length);
		arr[N++] = item;
	}
	
	// delete and return a random item
	public Item dequeue()
	{
		if (isEmpty())
			throw new NoSuchElementException("Cannot dequeue from an empty queue.");
		int r = StdRandom.uniform(0, N);
		Item item = arr[r];
		arr[r] = arr[--N];
		arr[N] = null;
		if (N > 0 && N == arr.length / 4)
			resize(arr.length / 2);
		return item;
	}
	
	// return (but do not delete) a random item
	public Item sample() 
	{
		if (isEmpty())
			throw new NoSuchElementException("Cannot sample from an empty queue.");
		return arr[StdRandom.uniform(0, N)]; 
	}
	
	// return an independent iterator over items in random order
	@Override
	public Iterator<Item> iterator() { return new RandomizedQueueArrayIterator(); }
	
	// inner class
	private class RandomizedQueueArrayIterator implements Iterator<Item>
	{
		private int current;
		private Item[] permArr;
		@SuppressWarnings("unchecked")
		public RandomizedQueueArrayIterator()
		{
			current = 0;
			permArr = (Item[]) new Object[N];
			for (int i = 0; i < N; i++)
				permArr[i] = arr[i];
			StdRandom.shuffle(permArr);
		}
		@Override
		public boolean hasNext() { return current != permArr.length; }

		@Override
		public Item next()
		{
			if (!hasNext())
				throw new NoSuchElementException(
						"No more items in iteration.");
			return permArr[current++];
		}

		@Override
		public void remove() 
		{
			throw new UnsupportedOperationException(
					"Remove operation is not supported."); 
		}
		
	}
	
	//unit test
	public static void main(String[] args)
	{
	}
}
