package api;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class DWGraph_DS implements directed_weighted_graph {

	private HashMap<Integer, node_data> nodes = new HashMap<Integer, node_data>();
	
	public int amountOfEdges = 0;
	public int modeCount = 0;
	
	@Override
	public node_data getNode(int key) {
		if(!nodes.containsKey(key)) return null;
		return nodes.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if(!nodes.containsKey(src) || !nodes.containsKey(dest)) return null;
		return ((NodeData)getNode(src)).getEdge(dest);
	}

	@Override
	public void addNode(node_data n) {
		if(nodes.containsKey(n.getKey()))return;
		nodes.put(n.getKey(), n);
		this.modeCount++;
	}

	@Override
	public void connect(int src, int dest, double w) {
		if(!nodes.containsKey(src) || !nodes.containsKey(dest) || src == dest) return;
		((NodeData)getNode(src)).addEdge(dest, w);
		this.modeCount++;
		this.amountOfEdges++;
	}

	// return the neighbors collection of this specific key (node).
	public Collection<node_data> getV(int key) {
		Iterator<edge_data> iterator = ((NodeData)getNode(key)).getEdges().iterator();
		Collection<node_data> neighbors =  new LinkedList<node_data>(); 
		
		while(iterator.hasNext()) {
			edge_data currentEdge = iterator.next();
			neighbors.add(getNode(currentEdge.getDest()));
		}

		return neighbors;
	}
	
	@Override
	public Collection<node_data> getV() {
		return (Collection<node_data>) nodes.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		return ((NodeData)getNode(node_id)).getEdges();
	}

	@Override
	public node_data removeNode(int key) {
		if(!nodes.containsKey(key)) return null;
		
		Iterator<node_data> iterator = getV().iterator();
		 
		while (iterator.hasNext()) {
			node_data node = iterator.next();
		    if(((NodeData)node).removeEdge(getNode(key).getKey())) 
		    {
		    	this.amountOfEdges--;
			    modeCount++;
		   }
		}
		NodeData data = (NodeData)getNode(key);
		
		nodes.remove(key);
		modeCount++;
		return data;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		if(!nodes.containsKey(src) || !nodes.containsKey(dest)) return null;
		if(getEdge(src, dest) == null)return null;
		
		edge_data data = ((NodeData)getNode(src)).getEdge(dest);
		((NodeData)getNode(src)).removeEdge(getNode(dest).getKey());
		modeCount++;
		this.amountOfEdges--;
		return data;
	}

	@Override
	public int nodeSize() {
		return nodes.size();
	}

	@Override
	public int edgeSize() {
		return this.amountOfEdges;
	}

	@Override
	public int getMC() {
		return this.modeCount;
	}	
}
