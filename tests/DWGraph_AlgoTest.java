import java.util.List;

import org.junit.jupiter.api.Test;

import api.node_data;

class DWGraph_AlgoTest {

	@Test
	void SaveTest() {
		DWGraph_DS g = new DWGraph_DS();
		node_data node1 = new NodeData();
		node1.setWeight(2);
		node1.setInfo("shalom");
		node1.setTag(51);
		g.addNode(node1);
		
		node_data node2 = new NodeData();
		node2.setWeight(2);
		node2.setInfo("shalom2");
		node2.setTag(51);
		g.addNode(node2);
		
		g.connect(node1.getKey(), node2.getKey(), 3.21);	
		g.connect(node2.getKey(), node2.getKey(), 3.81);
		g.connect(node2.getKey(), node1.getKey(), 3);
		
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(g);
		ga.save("data/Test.json");
	}
	@Test
	void ShortestPath() {
		DWGraph_DS g = new DWGraph_DS();
		node_data node1 = new NodeData();
		node1.setWeight(2);
		node1.setInfo("shalom");
		node1.setTag(51);
		g.addNode(node1);
		
		node_data node2 = new NodeData();
		node2.setWeight(2);
		node2.setInfo("shalom2");
		node2.setTag(51);
		g.addNode(node2);
		
		node_data node3 = new NodeData();
		node3.setWeight(2);
		node3.setInfo("shalom2");
		node3.setTag(51);
		g.addNode(node3);
		
		g.connect(node1.getKey(), node2.getKey(), 3.21);	
		g.connect(node1.getKey(), node3.getKey(), 13.81);
		g.connect(node2.getKey(), node2.getKey(), 3.81);
		g.connect(node2.getKey(), node3.getKey(), 3);
		g.connect(node2.getKey(), node1.getKey(), 3);
		
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(g);
		
		double val = ga.shortestPathDist(node1.getKey(),node3.getKey());
		System.out.println(val);
	}
}
