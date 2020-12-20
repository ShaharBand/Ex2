package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.GeoLocation;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.game_service;
import api.geo_location;
import api.node_data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class Ex2 implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static directed_weighted_graph currentGraph;
	
    private static long user_id;
    private static int level_number;
    
	public static void main(String[] a) {
		_win = new MyFrame("Pokemon Game: Main Menu");
        _win.InitMenu();
	}
	
	@Override
	public void run() {
		game_service game = Game_Server_Ex2.getServer(GetLevel()); // you have [0,23] games
		
		// game.login(user_id);

		// initializing the game:
		init(game);		
		_win.setTime(game.timeToEnd());
		
		int index = 0;
		long dt=100;
		
		while(game.isRunning()) {
			moveAgents(game);	// agents move according to their algorithms.
			_win.setTime(game.timeToEnd());
			
			// send update request to jframe every dt (100ms).
			try {
				if(index%1 == 0) _win.repaint();
				Thread.sleep(dt);
				index++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		// once the game ended print the results
		String gameResults = game.toString();
		System.out.println(gameResults);
		
		System.exit(0);
	}
	/** 
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgents(game_service game) {
		List<CL_Agent> agentList = Arena.getAgents(game.move(), currentGraph);
		ArrayList<CL_Pokemon> pokemonList = Arena.json2Pokemons(game.getPokemons());
		
		_ar.setAgents(agentList);
		_ar.setPokemons(pokemonList);
		
		for(int i=0; i < agentList.size(); i++) {
			CL_Agent ag = agentList.get(i);
			
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			
			if(dest==-1) { // reached is target and needs to find a new one
				// get all pokemons
				// go through all agents mark all pokemon they marked out
				// search from that list the closest one to you
				// go to it
				dest = nextNode(currentGraph, src);
				game.chooseNextEdge(ag.getID(), dest);
				System.out.println("Agent: "+ id + ", value: " + v + "\t turned to node: " + dest);
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(directed_weighted_graph g, int src) {
		
		/*List<CL_Agent> agentList = Arena.getAgents(game.move(), currentGraph);
		CL_Pokemon test;
		//test.
		//*/
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}
	
	private void init(game_service game) {
		ArrayList<CL_Pokemon> pokemonList = Arena.json2Pokemons(game.getPokemons());
		
		// Graph initialization:
		dw_graph_algorithms algoGraph = new DWGraph_Algo();
		
		String fileName = "currentGraph.json";
		createFile(fileName, game.getGraph());
		algoGraph.load(fileName);
		
		currentGraph = algoGraph.getGraph();
		
		// Arena initialization:
		_ar = new Arena();
		_ar.setGraph(currentGraph);
		_ar.setPokemons(pokemonList);
		
		// JFrame initialization:
		_win = new MyFrame("Pokemon Game: Level " + level_number);
		_win.update(_ar);
		
		JSONObject line;
		try {
			line = new JSONObject(game.toString());
			JSONObject serverObject = line.getJSONObject("GameServer");
			int amountOfAgents = serverObject.getInt("agents");

			for(int i = 0; i < pokemonList.size(); i++) Arena.updateEdge(pokemonList.get(i), currentGraph);
			
			for(int i = 0; i < amountOfAgents; i++) {
				// Calculating which starting point is the closest to start with for the first pokemons

				CL_Pokemon c = pokemonList.get(i);
				geo_location loc = new GeoLocation(c.getLocation());
				
				node_data src_node = currentGraph.getNode(c.get_edge().getSrc());
				node_data dest_node = currentGraph.getNode(c.get_edge().getDest());
				int spawnpoint = 0;
				
				if(loc.distance(src_node.getLocation()) > loc.distance(dest_node.getLocation()))
					spawnpoint = c.get_edge().getDest();
				else spawnpoint = c.get_edge().getSrc();
				
				game.addAgent(spawnpoint);
			}
		}
		catch (JSONException e) { e.printStackTrace();}
		
		game.startGame();
	}
	
	// creates a text file by given name and contains given string.
	public void createFile(String file, String txt) {
		try {
			new File(file);
			FileWriter writer = new FileWriter(file);
			writer.write(txt);
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static long GetUserID() {
		return user_id;
	}

	public static long SetUserID(long user_id) {
		Ex2.user_id = user_id;
		return user_id;
	}

	public static int GetLevel() {
		return level_number;
	}

	public static int SetLevel(int level_number) {
		Ex2.level_number = level_number;
		return level_number;
	}
	
	public static void StartGame() {
		Thread client = new Thread(new Ex2());
		client.start();
	}
   

}
