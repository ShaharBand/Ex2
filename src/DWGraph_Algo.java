import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.node_data;
import src.node_info;
import src.WGraph_Algo.TagComparator;

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
		if(this.algoGraph.nodeSize() < 2)return 0;
		resetGraph();
		
		PriorityQueue<node_info> pq = new PriorityQueue<>(new TagComparator()); 	
		node_info currentNode = algoGraph.getNode(src), neighboor;
		currentNode.setTag(0);
		pq.add(currentNode);
		
		while(!pq.isEmpty()) {
			currentNode = pq.poll();
			
			Iterator<node_info> iterator = this.algoGraph.getV(currentNode.getKey()).iterator();
			while (iterator.hasNext()) {
			    neighboor = iterator.next();  
				
				if(neighboor.getTag() == -1 || pq.contains(neighboor))
			    {
					if(pq.contains(neighboor))
					{
						if(neighboor.getTag() < currentNode.getTag() + this.algoGraph.getEdge(currentNode.getKey(), neighboor.getKey()))
							continue;
					}
			    	neighboor.setTag(currentNode.getTag()+this.algoGraph.getEdge(currentNode.getKey(), neighboor.getKey()));
			    	if(neighboor.getKey() == dest)return neighboor.getTag();
			    	pq.add(neighboor);
			    }
			}
		}
		return -1; // path doesn't exists.
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		// TODO Auto-generated method stub
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
		}
	}
}
