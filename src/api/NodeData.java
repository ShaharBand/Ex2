package api;
import java.util.Collection;
import java.util.HashMap;

public class NodeData implements node_data {
	public static int keyCounter = 0;
	// each node will contain the edges related to it directing an edge as a source.
	private HashMap<Integer, edge_data> edges = new HashMap<Integer, edge_data>(); // node: 'key' (dest), node: data.
	private String info;
	private int tag;
	private int key;
	private double weight;
	private double counter; //counter for weight in algorithm
	private geo_location location = new GeoLocation();
	
	public NodeData(){
		this.key = keyCounter;
		keyCounter++;
	}
	
    public NodeData(int key)
    {
        this.key = key;
		this.edges = new HashMap<Integer, edge_data>();
    }
    
	// copying constructor (excluding edges HashMap)
	public NodeData(node_data n){
		this.info = n.getInfo();
		this.tag = n.getTag();
		this.key = n.getKey();
		this.weight = n.getWeight();
		this.location = n.getLocation();
		this.edges = new HashMap<Integer, edge_data>();
	}

	public double getCounter() {
		return this.counter;
	}
	
	public void setCounter(double c) {
		this.counter = c;
	}

	// return the edge from current node to 'key' as dest.
	public edge_data getEdge(int key) {
		if(!edges.containsKey(key))return null;
		return edges.get(key);
	}
	
	// return the edge collection from the node has a source of the edges.
	public Collection<edge_data> getEdges() {
		return (Collection<edge_data>) edges.values();
	}

	// adds an edge to the HashMap has the dest from the current node.  
	public void addEdge(int dest, double w) {
		edges.put(dest, new EdgeData(key, dest, w));
	}
	
	// removes an edge from the HashMap of edges.
	public boolean removeEdge(int key) {
		if(edges.containsKey(key)) {
			edges.remove(key);
			return true;
		}
		return false;
	}
	@Override
	public int getTag() {
		return this.tag;
	}

	@Override
	public void setTag(int t) {
		this.tag = t;
	}

	@Override
	public int getKey() {
		return this.key;
	}
	
	@Override
	public String getInfo() {
		return this.info;
	}

	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	@Override
	public geo_location getLocation() {
		return this.location;
	}

	@Override
	public void setLocation(geo_location p) {
		this.location = (GeoLocation)p;
	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	@Override
	public void setWeight(double w) {
		this.weight = w;
	}
}
