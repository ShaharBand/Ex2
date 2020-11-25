import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;

public class DWGraph_DS implements directed_weighted_graph {

	public static class NodeData implements node_data {

		// each node will contain the edges related to it directing an edge as a source.
		private HashMap<Integer, edge_data> edges = new HashMap<Integer, edge_data>(); // node: key, node: data.
		private String info;
		private int tag;
		private int key;
		private double weight;
		private geo_location location;

		// return the edge from current node to 'key' as dest.
		public edge_data getEdge(int key) {
			return edges.get(key);
		}
		
		// return the edge collection from the node has a source of the edges.
		public Collection<edge_data> getEdges() {
			return (Collection<edge_data>) edges.values();
		}

		// adds an edge to the HashMap has the dest from the current node.  
		public void addEdge(int dest, double w) {
			edges.put(dest, new EdgeData(this.key, dest, w));
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
			this.location = p;
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
	private HashMap<Integer, node_data> nodes = new HashMap<Integer, node_data>();
	
	private int amountOfEdges = 0;
	private int modeCount = 0;
	
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
		if(!nodes.containsKey(src) || !nodes.containsKey(dest)) return;
		((NodeData)getNode(src)).addEdge(dest, w);
		modeCount++;
		amountOfEdges++;
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

}
