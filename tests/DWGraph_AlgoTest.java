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

}
