package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.GeoLocation;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.game_service;
import api.geo_location;
import api.node_data;
import api.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Ex2 class is responsible for initiating the game client, starting the game and calculating agents movement.
 */
public class Ex2 implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static directed_weighted_graph currentGraph;
	
    private static long user_id;
    private static int level_number;
    
    
	public static void main(String[] a) {
        if (a.length == 2) {
        	user_id = Integer.valueOf(a[0]);
        	level_number = Integer.valueOf(a[1]);
            StartGame();
        } else {
			_win = new MyFrame("Pokemon Game: Main Menu");
	        _win.InitMenu();
        }
	}
	
	@Override
	public void run() {
		game_service game = Game_Server_Ex2.getServer(GetLevel()); // you have [0,23] games
		game.login(GetUserID());

		// initializing the game:
		init(game);		
		_win.setTime(game.timeToEnd());
		
		int index = 0;
		long dt=100;
		
		while(game.isRunning()) {
			moveAgents(game);	// agents move according to their algorithms.
			_win.setTime(game.timeToEnd());
			
			// send update request to JFrame every dt (100ms).
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
	 * in case the agent is on a node the next destination (next edge) is chosen (by order: value * speed / distance).
	 * @param game
	 */
	private static void moveAgents(game_service game) {
		List<CL_Agent> agentList = Arena.getAgents(game.move(), currentGraph);
		ArrayList<CL_Pokemon> pokemonList = Arena.json2Pokemons(game.getPokemons());
		
		_ar.setAgents(agentList);
		_ar.setPokemons(pokemonList);
		
		for(int i=0; i < agentList.size(); i++) {
			CL_Agent ag = agentList.get(i);
			
			int dest = ag.getNextNode();
			
			if(dest == -1 && pokemonList.size() > 0) {
				dest = nextNode(ag);
				game.chooseNextEdge(ag.getID(), dest);
			}
		}
	}
	/**
	 *  this function is responsible to the agents walk implementation by returning the next node
	 *  by calculating the entire path and returning the next node in the path we can make better
	 *  paths for the agent
	 * @param agent
	 * @return next node key value in the path algorithm using dijkstra.
	 */
	private static int nextNode(CL_Agent agent) {

        if(agent.getPath()== null || agent.getPath().isEmpty()) {
            synchronized (_ar.getPokemons()) {
            	if(_ar.getPokemons().size() > 0) 
	            	CalculateAgentsPath();
            	}
        }
        
        if(!agent.getPath().isEmpty())
        {
        	int index = agent.getPath().size()-1;
        	if(index < 0)index = 0;
        	
		    node_data nextNode = agent.getPath().get(index);
		    agent.getPath().remove(index);
		    return nextNode.getKey();
        }
        return -1;
	}
	
	/**
	 *  this function is responsible initialization of the game by loading all the data to its 
	 *  proper classes and constructing all relevant data
	 *  paths for the agent
	 * @param agent
	 */
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

				if(c.getType() < 0)
					game.addAgent(c.get_edge().getSrc());
				else if(c.getType() >= 0)
					game.addAgent(c.get_edge().getDest());
			}
		}
		catch (JSONException e) { e.printStackTrace();}
		
		game.startGame();
	}
	
	/**
	 *  this function is responsible to convert the game string into a text file by a given name.
	 * @param file - the file name.
	 * @param txt - the text to put in the file.
	 */
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

	/**
	 *  this function is responsible to return the playing user id.
	 */
	public static long GetUserID() {
		if(user_id < 99999999) user_id = 0;
		return user_id;
	}

	/**
	 *  this function is responsible to set the playing user id
	 *  @param user_id - the user id
	 */
	public static long SetUserID(long user_id) {
		Ex2.user_id = user_id;
		return user_id;
	}

	/**
	 *  this function is responsible to return the game level.
	 */
	public static int GetLevel() {
		if(level_number < 0 || level_number > 23) level_number = 0;
		return level_number;
	}

	/**
	 *  this function is responsible to set the game level.
	 *  @param level_number - the game level.
	 */
	public static int SetLevel(int level_number) {
		Ex2.level_number = level_number;
		return level_number;
	}
	
	/**
	 *  this function is responsible to start a new window which is the game itself
	 *  and to initialization the thread in which the game runs on.
	 */
	public static void StartGame() {
		Thread client = new Thread(new Ex2());
		client.start();
	}
    
	/**
	 *  this function is responsible to calculate the pokemon value in the priority queue in order
	 *  to find the most valuable ones.
	 *  @param agent - the agent that searches to the pokemon.
	 *  @param pokemon - the pokemon that we want to calculate its value related to the agent.
	 */
	private static double PokemonValueForAgent(CL_Agent agent, CL_Pokemon pokemon)
	{
		double pokemonValue = 0;
		
		dw_graph_algorithms algoGraph = new DWGraph_Algo();
		algoGraph.init(currentGraph);
		
		if(pokemon.get_edge() != null)
		{
			double distance = algoGraph.shortestPathDist(agent.getSrcNode(), pokemon.get_edge().getDest());
			pokemonValue = (agent.getSpeed() * pokemon.getValue()) / distance;
		}

		return pokemonValue;
	}
	
	/**
	 *  this function is responsible to calculate the priority queue in which all the agents
	 *  are measuring the other pokemons values related to their status like distance etc..
	 *  to indicate which pokemon is the most valuable once all measurements has been calculated.
	 *  and saved in a custom entry class containing the agent, pokemon and value in the priority queue.
	 *  we mark the targeted pokemon and pull only the best valued ones for the agents.
	 */
	private static void CalculateAgentsPath()
	{
		List<CL_Agent> agentList = _ar.getAgents();
		List<CL_Pokemon> pokemonList = _ar.getPokemons();
		if(agentList.isEmpty() || pokemonList.isEmpty())return;
		
		// updating the pokemon edges
		for(int i = 0; i < pokemonList.size(); i++) Arena.updateEdge(pokemonList.get(i), currentGraph);
		
		PriorityQueue<Entry<CL_Agent, CL_Pokemon>> pq = new PriorityQueue<>(); 	
		
		// create priority queue with values and agents entries
		// by the equation value = pokemon_value * agent_speed / distance.
		for(int i = 0; i < agentList.size(); i++) {
			CL_Agent agent = agentList.get(i);
			
			for(int j = 0; j < pokemonList.size(); j++) {
				CL_Pokemon pokemon = pokemonList.get(j);

				if(pokemon.get_edge() != null)
					pq.add(new Entry<CL_Agent, CL_Pokemon>(agent, pokemon, PokemonValueForAgent(agent, pokemon)));
			}
		}

		// reset all agent paths
		for(int i = 0; i < agentList.size(); i++) {
			CL_Agent agent = agentList.get(i);
			agent.setPath(null);
		}
		// reset all pokemons
		for(int j = 0; j < pokemonList.size(); j++)
			pokemonList.get(j).setTargeted(false);
		
		dw_graph_algorithms algoGraph = new DWGraph_Algo();
		algoGraph.init(currentGraph);
		
		// set agents paths to the best valued pokemon
		for(int i = 0; i < agentList.size();)
		{
			Entry<CL_Agent, CL_Pokemon> entry = pq.poll();
			
			CL_Agent agent = entry.GetKey();
			if (agent.getPath() == null || agent.getPath().isEmpty())
			{
				CL_Pokemon pokemon = entry.GetValue2();
				
				if(!pokemon.getTargeted() && pokemon.get_edge() != null)
				{
					AgentPath(pokemon, agent);

					i++;
					if(i > pokemonList.size())break;
				}
			}
		}
	}

	/**
	 *  this function is responsible to convert the agent desired target into its path
	 *  @param p - desired pokemon to go to.
	 *  @param ag - agent to set the path for.
	 */
	public static void AgentPath(CL_Pokemon p, CL_Agent ag)
	{
		p.setTargeted(true);
	    
		dw_graph_algorithms algoGraph = new DWGraph_Algo();
		algoGraph.init(currentGraph);
		
		List<node_data> path = new ArrayList<node_data>();
	
		// because pokemon can be on same node but different edges we add them both.
		path.add(currentGraph.getNode(p.get_edge().getSrc()));
		path.add(currentGraph.getNode(p.get_edge().getDest()));
	    
	    path.addAll(algoGraph.shortestPath(ag.getSrcNode(), p.get_edge().getSrc()));
	    
	    if(!path.isEmpty()) path.remove(path.size()-1);
	    path.remove(0);
	
	    ag.setPath(path); 
	}
}
