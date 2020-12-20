package api;

public class Entry<T,K> implements Comparable<Entry> {
    private T key;
    private double value;
    private K value2;

    /**
     * This class is responsible to indicate a key which contains value to be implemented in priority queue.
     */
    public Entry(T key, K value2, double value) {
        this.key = key;
        this.value = value;
        this.value2 = value2;
    }

    public T GetKey()
    {
    	return this.key;
    }
    
    public double GetValue()
    {
    	return this.value;
    }
    
    public K GetValue2()
    {
    	return this.value2;
    }

	@Override
	public int compareTo(Entry other) {    
        if(other.GetValue() > this.GetValue())
            return 1;
        else if(other.GetValue() == this.GetValue())
             return 0;
         return -1;
    }
}