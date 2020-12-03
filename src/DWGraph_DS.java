import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;

public class DWGraph_DS implements directed_weighted_graph {

	public static class NodeData implements node_data {

		public static int keyCounter = 0;
		// each node will contain the edges related to it directing an edge as a source.
		private HashMap<Integer, edge_data> edges = new HashMap<Integer, edge_data>(); // node: 'key' (dest), node: data.
		private String info;
		private int tag;
		private int key;
		private double weight;
		private double counter; //counter for weight in algorithm
		private GeoLocation location = new GeoLocation();
		
		public NodeData(){
			this.key = keyCounter;
			keyCounter++;
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

		// return the keys of the neighboors from the node has a source of the edges.
		public Collection<Integer> getKeys(){
			return (Collection<Integer>) edges.keySet();
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

	public static class EdgeData implements edge_data {

		private int src;
		private int dest;
		private double weight;
		private String info;
		private int tag;
		
		public EdgeData(int src, int dest, double w) {
			this.src = src;
			this.dest = dest;
			this.weight = w;
		}
		@Override
		public int getSrc() {
			return this.src;
		}

		@Override
		public int getDest() {
			return this.dest;
		}

		@Override
		public double getWeight() {
			return this.weight;
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
		public int getTag() {
			return this.tag;
		}

		@Override
		public void setTag(int t) {
			this.tag = t;
		}
	}
	
	public static class GeoLocation implements geo_location {

		private double x = 0;
		private double y = 0;
		private double z = 0;
		
		@Override
		public double x() {
			return this.x;
		}

		@Override
		public double y() {
			return this.y;
		}

		@Override
		public double z() {
			return this.z;
		}
		@Override
		public double distance(geo_location g) {
	        double dx = this.x() - g.x();
	        double dy = this.y() - g.y();
	        double dz = this.z() - g.z();
	        double t = (dx*dx+dy*dy+dz*dz);
	        return Math.sqrt(t);
		}
	}
	
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
		modeCount++;
	}

	@Override
	public void connect(int src, int dest, double w) {
		if(!nodes.containsKey(src) || !nodes.containsKey(dest) || src == dest) return;
		((NodeData)getNode(src)).addEdge(dest, w);
		modeCount++;
		amountOfEdges++;
	}

	// return the neigboors collection of this specific key (node).
	public Collection<node_data> getV(int key) {
		Collection<Integer> keys = ((NodeData)getNode(key)).getKeys();
		Iterator<Integer> iterator = keys.iterator();
		 
		Collection<node_data> neighboors = null;
		while (iterator.hasNext()) {
			int nodeKey = iterator.next();
		    neighboors.add(getNode(nodeKey));
		}
		
		return neighboors;
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
			    amountOfEdges--;
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
		
		edge_data data = ((NodeData)getNode(src)).getEdge(dest);
		((NodeData)getNode(src)).removeEdge(getNode(dest).getKey());
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
	
	public double getCounter(int key) {
		return ((NodeData)getNode(key)).getCounter();
	}
	public void setCounter(int key, double c) {
		((NodeData)getNode(key)).setCounter(c);
	}

}
