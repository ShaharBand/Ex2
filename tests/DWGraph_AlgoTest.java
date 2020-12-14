import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.NodeData;
import api.dw_graph_algorithms;
import api.edge_data;
import api.node_data;

class DWGraph_AlgoTest {

	@Test
	public void copy_Test() {
		DWGraph_DS G=Graph1();
		dw_graph_algorithms algo = new DWGraph_Algo();
		algo.init(G);
		
		DWGraph_DS g = (DWGraph_DS) algo.copy();
    	assertEquals(G.nodeSize(),g.nodeSize()); //Nodes
    	assertEquals(G.amountOfEdges,g.amountOfEdges); //Edges

    	for(node_data N:G.getV()) {	
    		assertEquals(N.getTag(), g.getNode(N.getKey()).getTag());
    		assertEquals(N.getInfo(), g.getNode(N.getKey()).getInfo());
    	}
	}
	
	@Test
	public void isConnected_Test() {
		DWGraph_DS G=Graph2();
		dw_graph_algorithms algo = new DWGraph_Algo();
		algo.init(G);
		boolean flag=algo.isConnected();
		assertTrue(flag);	
	}
	
	@Test
	public void shortestPathDist_Test() {
		DWGraph_DS G=Graph1();
		dw_graph_algorithms algo = new DWGraph_Algo();
		algo.init(G);
		double dist=algo.shortestPathDist(15,20);
		boolean flag=false;
		if(dist==19.2) flag=true;
		assertTrue(flag);
	}
	
	@Test
	public void shortestPath_Test() {
		DWGraph_DS G=Graph1();
		dw_graph_algorithms algo = new DWGraph_Algo();
		algo.init(G);
		List<node_data> list=new ArrayList<node_data>();
		list=algo.shortestPath(21,26);
		assertEquals(list.size(),4);
	}
	
	@Test
	public void Save_Test() {
		DWGraph_DS G=Graph3();
		dw_graph_algorithms algo = new DWGraph_Algo();
		algo.init(G);
		algo.save("Test.json");
	}
	
	@Test
	public void load_Test() {
		DWGraph_DS G1=Graph3();
		dw_graph_algorithms algo1 = new DWGraph_Algo();
		algo1.init(G1);
		algo1.save("Test.json");
		Iterator<node_data> iter1 = algo1.getGraph().getV().iterator();
		
		dw_graph_algorithms algo0 = new DWGraph_Algo();
		algo0.load("Test.json");
		Iterator<node_data> iter0 = algo0.getGraph().getV().iterator();
		
		boolean flag=true;
		
		if(algo0.getGraph().nodeSize() == algo1.getGraph().nodeSize() && algo0.getGraph().edgeSize() == algo1.getGraph().edgeSize()) {
			while(iter0.hasNext() && iter1.hasNext()) {
				node_data n0 = iter0.next();
				node_data n1 = iter1.next();
				if(n0.getKey() != n1.getKey()) {
					flag=false;
				break;
				}
				if(n0.getLocation().distance(n1.getLocation()) != 0) {
					flag=false;
					break;
				}
				Iterator<edge_data> iter0e = algo0.getGraph().getE(n0.getKey()).iterator();
				Iterator<edge_data> iter1e = algo1.getGraph().getE(n1.getKey()).iterator();
				
				while(iter0e.hasNext()) {
					if(iter1e.next().getWeight()!=iter0e.next().getWeight()) {
						flag=false;
						break;
					}
				}
			}
		}
		else flag=false;
		
		assertTrue(flag);
	}
	
	public static DWGraph_DS Graph1() {
		NodeData n0=new NodeData();
		NodeData n1=new NodeData();
		NodeData n2=new NodeData();
		NodeData n3=new NodeData();
		NodeData n4=new NodeData();
		NodeData n5=new NodeData();
		
		DWGraph_DS G=new DWGraph_DS();
		
		G.addNode(n0); //add node n0 to G
		G.addNode(n1); //add node n1 to G
		G.addNode(n2); //add node n3 to G
		G.addNode(n3); //add node n3 to G
		G.addNode(n4); //add node n4 to G
		G.addNode(n5); //add node n5 to G

		G.connect(n0.getKey(),n1.getKey(),2.2); //connect n0 to n1 with edge of 2.2
		G.connect(n0.getKey(),n2.getKey(),5.3); //connect n0 to n2 with edge of 5.3
		G.connect(n1.getKey(),n4.getKey(),7); //connect n1 to n4 with edge of 7
		G.connect(n4.getKey(),n5.getKey(),10); //connect n4 to n5 with edge of 10
		G.connect(n5.getKey(),n1.getKey(),1.1); //connect n5 to n1 with edge of 1.1
		G.connect(n5.getKey(),n2.getKey(),1.2); //connect n5 to n2 with edge of 1.2
		G.connect(n2.getKey(),n1.getKey(),1.2); //connect n2 to n1 with edge of 1.2		
		G.connect(n5.getKey(),n3.getKey(),6.5); //connect n5 to n3 with edge of 6.5
		G.connect(n3.getKey(),n0.getKey(),4.7); //connect n3 to n0 with edge of 4.7

		return G;
	}
	
	public static DWGraph_DS Graph2() {
		NodeData n0=new NodeData();
		NodeData n1=new NodeData();
		NodeData n2=new NodeData();
		
		DWGraph_DS G=new DWGraph_DS();
		
		G.addNode(n0); //add node n0 to G
		G.addNode(n1); //add node n1 to G
		G.addNode(n2); //add node n3 to G
		
		G.connect(n0.getKey(),n1.getKey(),2.2); //connect n0 to n1 with edge of 2.2
		G.connect(n1.getKey(),n2.getKey(),5.3); //connect n1 to n2 with edge of 5.3
		G.connect(n2.getKey(),n0.getKey(),7); //connect n2 to n3 with edge of 7

		return G;
	}
	
	public static DWGraph_DS Graph3() {
		
		DWGraph_DS G = new DWGraph_DS();
		NodeData n0 = new NodeData();
		n0.setWeight(1.0);
		n0.setInfo("node 0");
		n0.setTag(20);
		G.addNode(n0);
		
		NodeData n1 = new NodeData();
		n1.setWeight(1.1);
		n1.setInfo("node 1");
		n1.setTag(21);
		G.addNode(n1);
		
		NodeData n2 = new NodeData();
		n1.setWeight(1.2);
		n1.setInfo("node 2");
		n1.setTag(22);
		G.addNode(n2);
	
		G.connect(n0.getKey(), n1.getKey(), 3.21);	
		G.connect(n1.getKey(), n2.getKey(), 3.81);
		G.connect(n2.getKey(), n0.getKey(), 3);
		
		return G;
	}

}
