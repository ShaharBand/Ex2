import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import api.geo_location;
import api.node_data;

class DWGraph_AlgoTest {

	@Test
	void test() {
		DWGraph_DS g = new DWGraph_DS();
		node_data node1 = new DWGraph_DS.NodeData();
		node1.setWeight(2);
		node1.setInfo("shalom");
		node1.setTag(51);
		
		g.addNode(node1);
		
		DWGraph_Algo ga = new DWGraph_Algo();
		ga.init(g);
		ga.save("Test.json");
	}

}
