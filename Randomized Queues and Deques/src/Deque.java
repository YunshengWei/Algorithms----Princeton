import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>
{
	private Node front;
	private Node end;
	private int N;
	
	// construct an empty deque
	public Deque()
	{
		N = 0;
		// set the sentinels
		front = new Node(null, null, null);
		end = new Node(null, null, front);
		front.next = end;
	}
	
	// is the deque empty?
	public boolean isEmpty() { return N == 0; }
	
	// return the number of items on the deque
	public int size() { return N; }
	
	// insert the item at the front
	public void addFirst(Item item)
	{
		if (item == null)
			throw new NullPointerException("Item cannot be null");
		N++;
		front.next = new Node(item, front.next, front);
		front.next.next.prev = front.next;	
	}
	
	// insert the item at the end
	public void addLast(Item item)
	{
		if (item == null)
			throw new NullPointerException("Item cannot be null");
		N++;
		end.prev = new Node(item, end, end.prev);
		end.prev.prev.next = end.prev;
	}
	
	// delete and return the item at the front
	public Item removeFirst()
	{
		if (isEmpty()) 
			throw new NoSuchElementException(
					"Cannot remove first item from an empty dequeue.");
		N--;
		Item item = front.next.item;
		front.next = front.next.next;
		front.next.prev = front;
		return item;
	}
	
	// delete and return the item at the end
	public Item removeLast()
	{
		if (isEmpty()) 
			throw new NoSuchElementException(
					"Cannot remove last item from an empty dequeue.");
		N--;
		Item item = end.prev.item;
		end.prev = end.prev.prev;
		end.prev.next = end;
		return item;
	}
	
	// return an iterator over items in order from front to end
	@Override
	public Iterator<Item> iterator() { return new DequeListIterator(); }
	
	// inner class
	private class Node
	{
		Item item;
		Node next;
		Node prev;
		Node(Item item, Node next, Node prev)
		{
			this.item = item;
			this.next = next;
			this.prev = prev;
		}
	}
	
	// inner class
	private class DequeListIterator implements Iterator<Item>
	{
		private Node current = front.next;
		
		@Override
		public boolean hasNext() { return current != end; }
		@Override
		public void remove() 
		{
			throw new UnsupportedOperationException(
					"Remove operation is not supported."); 
		}
		@Override
		public Item next()
		{
			if (!hasNext())
				throw new NoSuchElementException(
						"No more items in iteration.");
			Item item = current.item;
			current = current.next;
			return item;
		}
	}
	
	// unit testing
	public static void main(String[] args)
	{
	}	
}
