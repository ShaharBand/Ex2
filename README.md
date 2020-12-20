# Ex2
Ariel OOP course: Exersice 2 is about directed weighted graphs and Junit tests with Threads and Json.
This project is implements Pokémon game based on weighted directed graph with graph theory algorithms. 

***
<center>
<a href="https://ibb.co/j3pZgDC"><img src="https://i.ibb.co/j3pZgDC/image.png" alt="image" border="0"></a>
<a href="https://ibb.co/FYxyrQz"><img src="https://i.ibb.co/FYxyrQz/image.png" alt="image" border="0"></a>
</center>

***

== DWGraph_DS.java ==
-Use HashMap to represent Node in the graph and neighbors.

getNode(int key)
-Gives the Node by the @key.

getEdge(int src, int dest)
-Gives the Edge by the @src (key of source) and @dest (key of destination).

addNode(int key)
-Add Node by key to the graph.

connect(int src, int dest, double w)
-Conecting with direction between @src and @dest and value of @w.

Collection<node_info> getV(int key)
-Gives a Nodes collection with all neighbors of this @key.

Collection<node_info> getV()
-Gives a collection with all the Nodes of this graph (shallow copy).

Collection<node_info> getE(int node_id)
-Gives a Edges collection with all neighbors of this @node_id (key) (shallow copy).

removeNode(int key)
-Remove the Node by using this @key from the graph
-Removes all edges that connect to this Node.

removeEdge(int src, int dest)
-Remove the directed edge between @src and @dest (keys) from the graph.

nodeSize()
-Gives the number of Nodes in this graph.

edgeSize()
-Gives the number of edge in this graph.

getMC()
-(Mode Count) Gives the number of changes in this graph.


== WGraph_Algo.java ==

init(weighted_graph g)
-Initiate the graph.

getGraph()
-Get this graph @g.

copy()
-Makes a deep copy of the DWGraph_DS.

isConnected()
-Returns true if and only if the graph is connectivity.

FlipGraph()
-flips the graph edges by deep copy and flip the edges.
-used in isConnected algorithm to become Depth-first search from Breadth First Search.

BFS_isConnected(int src, DWGraph_DS_Algo ga)
-Dijkstra's Algorithm by given @src (key of source) and graph @ga .

shortestPathDist(int src, int dest)
-Gives the shortest distance(by weight of edges) between Node key @src to @dest.

shortestPath(int src, int dest)
-Return List of Nodes that the shortest distance(by weight of edges) between Node key @src to @dest.

save(String file)
-Save graph in Json file.

load(String file)
-Load graph from Json file.

resetGraph()
-Restart the values of the graph for re-use.

reverseList(List<T> list)
-Reverse list.

compare(node_data a, node_data b)
-Using for priority queue.
