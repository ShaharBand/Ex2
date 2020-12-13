import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.geo_location;
import api.node_data;
import java.io.FileReader;
import java.io.IOException;

public class DWGraph_Algo implements dw_graph_algorithms {

	private directed_weighted_graph graph;
	
	@Override
	public void init(directed_weighted_graph g) {
		this.graph = g;
	}

	@Override
	public directed_weighted_graph getGraph() {
		return this.graph;
	}

	@Override
	public directed_weighted_graph copy() {
		if(!(this.graph instanceof DWGraph_DS))return null;

		DWGraph_DS deep = new DWGraph_DS();
		Iterator<node_data> iterator = this.graph.getV().iterator();
		
		while (iterator.hasNext()) {
			node_data node = iterator.next();
			NodeData newNode = new NodeData(node);
			deep.addNode(newNode);
		}
		
		iterator = this.graph.getV().iterator();

		edge_data e = new EdgeData(0, 0, 0);
		while(iterator.hasNext())
		{
			node_data node = iterator.next();
			Iterator<edge_data> it2 = ((NodeData) node).getEdges().iterator();
			while(it2.hasNext())
			{
				e = it2.next();
				deep.connect(e.getSrc(), e.getDest(), e.getWeight());
			}
		}
		return deep;
	}

	// DFS, doing a BFS on a node, then fliping the edges and doing it again.
	@Override
	public boolean isConnected() {
		if(this.graph.nodeSize() < 2)return true;
		
		int src = this.graph.getV().iterator().next().getKey();
		if(!BFS_isConnected(src,this))
			return false;
		
		DWGraph_DS g = (DWGraph_DS) copy();
		
		FlipGraph();
		if(!BFS_isConnected(src,this))
		{
			init(g);
			return false;
		}
		
		init(g);
		return true;
	}
	
	private void FlipGraph()
	{
		node_data currentNode;
		
		Iterator<node_data> iterator = this.graph.getV().iterator();
		while(iterator.hasNext())
		{
			currentNode = iterator.next();
			Iterator<edge_data> iterator2 = this.graph.getE(currentNode.getKey()).iterator();
			while(iterator2.hasNext()) {
				edge_data currentEdge = iterator2.next();
				this.graph.removeEdge(currentEdge.getSrc(), currentEdge.getDest());
				this.graph.connect(currentEdge.getSrc(), currentEdge.getDest(), currentEdge.getWeight());
			}
		}
	}
	private boolean BFS_isConnected(int src, DWGraph_Algo ga)
	{
		int counter = 0;
		Queue<node_data> queue = new ArrayDeque<>();
		ga.resetGraph();
		counter = 0;
				
		node_data currentNode = ga.getGraph().getV().iterator().next(), neighbor;
		queue.add(currentNode);
	
		while(!queue.isEmpty()) {
			currentNode = queue.remove();
			Iterator<node_data> iterator = ((DWGraph_DS)ga.getGraph()).getV(currentNode.getKey()).iterator();

			while (iterator.hasNext()) {
				neighbor = iterator.next();
				if(neighbor.getTag() == -1) {
				 	queue.add(neighbor);
				 	neighbor.setTag(1);
					counter++;
				}
			}
		}
		if(counter != ga.getGraph().nodeSize())return false;
		return true;
	}
    // returns the shortest distance between 2 nodes in the graph by using BFS search with Priority Queue.
    // by using the tag we can calculate the distance value of each node and by going through the search using Priority Queue
    // this logic becomes Dijkstra's Algorithm!

	@Override
	public double shortestPathDist(int src, int dest) {
		if(this.graph.nodeSize() < 2)return 0;
		resetGraph();
		
		PriorityQueue<node_data> pq = new PriorityQueue<>(new CounterComparator()); 	
		node_data currentNode = graph.getNode(src), neighbor;
		
		((DWGraph_DS)graph).setCounter(src, 0);
		pq.add(currentNode);
		
		while(!pq.isEmpty()) {
			currentNode = pq.poll();
			
			Iterator<node_data> iterator = ((DWGraph_DS)this.graph).getV(currentNode.getKey()).iterator();
			while (iterator.hasNext()) {
			    neighbor = iterator.next();  
				
				if(((DWGraph_DS)graph).getCounter(neighbor.getKey()) == -1 || pq.contains(neighbor)) {
					
					// value of the path weight
					double weightValue=neighbor.getWeight() + 
							((DWGraph_DS)graph).getCounter(currentNode.getKey()) + 
							((DWGraph_DS)graph).getEdge(currentNode.getKey(), neighbor.getKey()).getWeight();
					
					if(pq.contains(neighbor)) {
						if(((DWGraph_DS)graph).getCounter(neighbor.getKey()) < weightValue) continue;
					}
									
					((DWGraph_DS)graph).setCounter(neighbor.getKey(), weightValue);
			    	if(neighbor.getKey() == dest)return ((DWGraph_DS)graph).getCounter(neighbor.getKey());
			    	pq.add(neighbor);
			    }
			}
		}
		return -1; // path doesn't exists.
	}

	// we allocate nodes using an helper HashMap to indicate its parent in the BFS search once we found the dest we reverse the order and create a list from it.
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if(this.graph==null || this.graph.getNode(dest)==null || this.graph.getNode(src)==null) return null;
		if(src==dest) {
			List<node_data> list=new ArrayList<>(); //make a new list
			list.add(this.graph.getNode(src));
			return list;
		}
		resetGraph();
		
		HashMap<Integer, node_data> parents = new HashMap<Integer, node_data>();
		PriorityQueue<node_data> pq = new PriorityQueue<>(new CounterComparator()); 	
		node_data currentNode = graph.getNode(src), neighbor;
		
		((DWGraph_DS)graph).setCounter(src, 0);
		pq.add(currentNode);
		
		while(!pq.isEmpty()) {
			currentNode = pq.poll();
			
			Iterator<node_data> iterator = ((DWGraph_DS)this.graph).getV(currentNode.getKey()).iterator();
			while (iterator.hasNext()) {
			    neighbor = iterator.next();  
				
				if(((DWGraph_DS)graph).getCounter(neighbor.getKey()) == -1 || pq.contains(neighbor)) {
					
					// value of the path weight
					double weightValue=neighbor.getWeight() + 
							((DWGraph_DS)graph).getCounter(currentNode.getKey()) + 
							((DWGraph_DS)graph).getEdge(currentNode.getKey(), neighbor.getKey()).getWeight();
					
					if(pq.contains(neighbor)) {
						if(((DWGraph_DS)graph).getCounter(neighbor.getKey()) < weightValue) continue;
					}
									
					((DWGraph_DS)graph).setCounter(neighbor.getKey(), weightValue);
					parents.put(neighbor.getKey(), currentNode); // Making a HashMap where the parent is contained in the key of its child's
					
					if(neighbor.getKey() == dest) { // found --> go by the helping HashMap and find parents from bottom to top and put them in the list.
						List<node_data> path = new ArrayList<node_data>();
						currentNode = neighbor;
				        while(currentNode != graph.getNode(src)) { //while we have parent
				        	path.add(currentNode);
				        	currentNode = parents.get(currentNode.getKey());
				        }
				        path.add(graph.getNode(src));
				        // reverse order to top to bottom:
				        List<node_data> path2 = new ArrayList<node_data>(); 
				        for (int i = path.size()-1; i >= 0; i--) 
				        	path2.add(path.get(i)); 
				        
				        return path2;
					}		
			    	pq.add(neighbor);
			    }
			}
		}
		return null;
	}

	
	@Override
	public boolean save(String file) {
		
		try {
			// creating JSONObject 
	        JSONObject jo = new JSONObject(); 
	        
	        // Firstly, we will create an array for the nodes:
	        JSONArray nodeArray = new JSONArray(); 
	        JSONArray edgeArray = new JSONArray(); 
	        
	        Map<String, Object> nodeMap = new HashMap<String, Object>(); 
	        Map<String, Object> edgeMap = new HashMap<String, Object>();
	        
	        for(node_data node : this.graph.getV())
	        {
	        	String geoLocation = node.getLocation().x() + "," + node.getLocation().y() + "," + node.getLocation().z();
		        nodeMap.put("pos", geoLocation);
	        	nodeMap.put("id", node.getKey());
		        
		        for(edge_data edge : ((DWGraph_DS)graph).getE(node.getKey()))
		        {
		        	edgeMap.put("src", edge.getSrc());
		        	edgeMap.put("w", edge.getWeight());
		        	edgeMap.put("dest", edge.getDest());	        	
		        	edgeArray.put(edgeMap);
		        }
	
		        // adding map to list 
		        nodeArray.put(nodeMap); 
			}
	        jo.put("Edges", edgeArray);
	        jo.put("Nodes", nodeArray); 

	        // writing JSON to file.
	        PrintWriter pw = new PrintWriter(file); 
	        pw.write(jo.toString()); 

	        pw.flush(); 
	        pw.close(); 
		}
		catch(FileNotFoundException | JSONException e)
		{
			System.out.println("failed! to save graph data into json.");
			 return false;
		}
		System.out.println("finished! to save graph data into json.");
		return true;
	}

	@Override
	public boolean load(String file) {
		try {
			FileReader reader = new FileReader(file);
			JSONTokener buffer = new JSONTokener(reader); // json tokener converting the data.
			JSONObject tmp = new JSONObject();

			tmp.put("graph", buffer.nextValue());
			JSONObject dataObj = new JSONObject();
			dataObj = (JSONObject)tmp.get("graph");

			JSONArray edges = (JSONArray)dataObj.get("Edges");
			JSONArray nodes = (JSONArray)dataObj.get("Nodes");

			directed_weighted_graph g = new DWGraph_DS();

			for(int i = 0; i < nodes.length(); i++)
			{
				node_data node = new NodeData(nodes.getJSONObject(i).getInt("id"));
				geo_location location = new GeoLocation(nodes.getJSONObject(i).getString("pos"));
				node.setLocation(location);
				g.addNode(node);
			}
			for(int i = 0; i < edges.length(); i++)
				g.connect(edges.getJSONObject(i).getInt("src"), 
						edges.getJSONObject(i).getInt("dest"), 
						edges.getJSONObject(i).getDouble("w"));
			
			init(g);
			reader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (JSONException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// restart the values of the graph for re-use.
	public void resetGraph() {
		Iterator<node_data> iterator = this.graph.getV().iterator();
		
		while (iterator.hasNext()) {
			node_data node = iterator.next();
			node.setTag(-1);
			((DWGraph_DS)graph).setCounter(node.getKey(), -1);			
		}
	}

	// for priority queue
	public class CounterComparator implements Comparator<node_data>{

		@Override
		public int compare(node_data a, node_data b) {    
	        if(((DWGraph_DS)graph).getCounter(a.getKey()) > ((DWGraph_DS)graph).getCounter(b.getKey()))
	            return 1;
	        else if(((DWGraph_DS)graph).getCounter(a.getKey()) == ((DWGraph_DS)graph).getCounter(b.getKey()))
	             return 0;
	         return -1;
	    }
	}
}	
	