import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;

import org.junit.Test;

import api.DWGraph_DS;
import api.NodeData;
import api.edge_data;
import api.node_data;

public class DWGraph_DS_Test {
	
	@Test
	public void add_remove_Test() {
		DWGraph_DS G=new DWGraph_DS();
		NodeData n0=new NodeData();
		int key=n0.getKey();
    	assertEquals(G.nodeSize(),0); //Nodes test
		G.addNode(n0); //add node n0 to G
    	assertEquals(G.nodeSize(),1); //Nodes test
    	G.removeNode(key); //remove node n0
    	assertEquals(G.nodeSize(),0); //Nodes test
	}

	@Test
	public void connect_removeEdge_Test() {
		DWGraph_DS G=new DWGraph_DS();
		NodeData n0=new NodeData();
		NodeData n1=new NodeData();
		int key0=n0.getKey();
		int key1=n1.getKey();
		
		G.addNode(n0); //add node n0 to G
		G.addNode(n1); //add node n1 to G
    	assertEquals(G.edgeSize(),0); //Edges size test
		G.connect(key0,key1,2.2);
    	assertEquals(G.edgeSize(),1); //Edges size test
    	double e=G.removeEdge(key0,key1).getWeight();
    	if(G.removeEdge(key0,key1)==null) System.out.println(e);
    	assertEquals(e,2.2); //Edges size test
    	System.out.println(G.edgeSize());
    	assertEquals(G.edgeSize(),0); //Edges test
	}
	
	@Test
	public void getV_Test() {
		DWGraph_DS G=Graph1();
			
		Iterator<node_data> iter = G.getV(8).iterator();
		
    	assertEquals(iter.next().getKey(),9); //First neighbors test
    	assertEquals(iter.next().getKey(),10); //Second neighbors test
    	assertTrue(!iter.hasNext()); //No more neighbors
	}
	
	@Test
	public void getE_Test() {
		DWGraph_DS G=Graph1();
			
		Iterator<edge_data> iter = G.getE(2).iterator();
		
    	assertEquals(G.edgeSize(),9); //Edge size
    	assertEquals(iter.next().getWeight(),2.2); //First edge
	}
	
	public void removeEdge_Test() {
		DWGraph_DS G=new DWGraph_DS();
		NodeData n0=new NodeData();
		NodeData n1=new NodeData();
		int key0=n0.getKey();
		int key1=n1.getKey();
		
		G.addNode(n0); //add node n0 to G
		G.addNode(n1); //add node n1 to G
    	assertEquals(G.edgeSize(),0); //Edges test
		G.connect(key0,key1,2.2);
    	assertEquals(G.edgeSize(),1); //Edges test
    	G.removeEdge(key0,key1);
    	assertEquals(G.edgeSize(),0); //Edges test

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
		//System.out.println(n0.getKey());
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
}
