import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.node_data;

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
		if(!(this.graph instanceof DWGraph_DS))return null; // we have no other classes..

		 // adding all the nodes in proper keys to the graph
		DWGraph_DS deep = new DWGraph_DS();
		Iterator<node_data> iterator = this.graph.getV().iterator();
		
		while (iterator.hasNext()) {
			node_data node = iterator.next();
			deep.addNode(node);
		}
		deep.amountOfEdges = graph.edgeSize();
		deep.modeCount = graph.getMC();
		return deep;
	}

	// BFS from each node to test the connectivity of each direction.
	@Override
	public boolean isConnected() {
		if(this.graph.nodeSize() < 2)return true;
		
		int counter = 0;
		Queue<node_data> queue = new ArrayDeque<>();
		
		for(int i = 0; i < graph.nodeSize(); i++)
		{
			queue.clear();
			resetGraph();
			counter = 0;
			
			node_data currentNode = this.graph.getNode(i), neighboor;
			queue.add(currentNode);
			
			while(!queue.isEmpty()) {
				currentNode = queue.remove();
				
				Iterator<node_data> iterator = ((DWGraph_DS)this.graph).getV(currentNode.getKey()).iterator();
				while (iterator.hasNext()) {
				    neighboor = iterator.next();
				    
					if(neighboor.getTag() == -1) {
				    	queue.add(neighboor);
				    	neighboor.setTag(1);
				    	counter++;
				    }
				}
			}
			if(counter != graph.nodeSize())return false;
		}
		return true;
	}

   /**
    * returns the shortest distance between 2 nodes in the graph by using BFS search with Priority Queue.
    * by using the tag we can calculate the distance value of each node and by going through the search using Priority Queue
    * this logic becomes Dijkstra's Algorithm!
    * @param src - starting node
	* @param dest - finish node
	* @return the distance (Double).
	*/
	@Override
	public double shortestPathDist(int src, int dest) {
		if(this.graph.nodeSize() < 2)return 0;
		resetGraph();
		
		PriorityQueue<node_data> pq = new PriorityQueue<>(new CounterComparator()); 	
		node_data currentNode = graph.getNode(src), neighboor;
		((DWGraph_DS)graph).setCounter(src, 0);
		pq.add(currentNode);
		
		while(!pq.isEmpty()) {
			currentNode = pq.poll();
			
			Iterator<node_data> iterator = ((DWGraph_DS)this.graph).getV(currentNode.getKey()).iterator();
			while (iterator.hasNext()) {
			    neighboor = iterator.next();  
				
				if(((DWGraph_DS)graph).getCounter(neighboor.getKey()) == -1 || pq.contains(neighboor)) {
					
					// value of the path weight
					double weightValue=neighboor.getWeight() + 
							((DWGraph_DS)graph).getCounter(currentNode.getKey()) + 
							((DWGraph_DS)graph).getEdge(currentNode.getKey(), neighboor.getKey()).getWeight();
					
					if(pq.contains(neighboor)) {
						if(((DWGraph_DS)graph).getCounter(neighboor.getKey()) < weightValue) continue;
					}
									
					((DWGraph_DS)graph).setCounter(neighboor.getKey(), weightValue);
			    	if(neighboor.getKey() == dest)return ((DWGraph_DS)graph).getCounter(neighboor.getKey());
			    	pq.add(neighboor);
			    }
			}
		}
		return -1; // path doesn't exists.
	}

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
		node_data currentNode = graph.getNode(src), neighboor;
		((DWGraph_DS)graph).setCounter(src, 0);
		pq.add(currentNode);
		
		while(!pq.isEmpty()) {
			currentNode = pq.poll();
			
			Iterator<node_data> iterator = ((DWGraph_DS)this.graph).getV(currentNode.getKey()).iterator();
			while (iterator.hasNext()) {
			    neighboor = iterator.next();  
				
				if(((DWGraph_DS)graph).getCounter(neighboor.getKey()) == -1 || pq.contains(neighboor)) {
					
					// value of the path weight
					double weightValue=neighboor.getWeight() + 
							((DWGraph_DS)graph).getCounter(currentNode.getKey()) + 
							((DWGraph_DS)graph).getEdge(currentNode.getKey(), neighboor.getKey()).getWeight();
					
					if(pq.contains(neighboor)) {
						if(((DWGraph_DS)graph).getCounter(neighboor.getKey()) < weightValue) continue;
					}
									
					((DWGraph_DS)graph).setCounter(neighboor.getKey(), weightValue);
					parents.put(neighboor.getKey(), currentNode); // Making a HashMap where the parent is contained in the key of its child's
					
					if(neighboor.getKey() == dest) { // found --> go by the helping HashMap and find parents from bottom to top and put them in the list.
						List<node_data> path = new ArrayList<node_data>();
						currentNode = neighboor;
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
			    	pq.add(neighboor);
			    }
			}
		}
		return null;
	}

	@Override
	public boolean save(String file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean load(String file) {
		// TODO Auto-generated method stub
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
	