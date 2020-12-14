package gameClient;

import Server.Game_Server_Ex2;
import api.game_service;

public class Ex2
{
    public static void main(String[] args)
    {
    	int level_number = 0;
    	game_service game = Game_Server_Ex2.getServer(level_number);
    	// game.login(209361310);
    	System.out.println(game.getGraph());
    	System.out.println(game.getPokemons());
    	System.out.println(game);
    	
    	game.addAgent(0);
    	System.out.println(game.getAgents());
    	
    	game.startGame();
    	
    	while(game.isRunning())
    	{
    		//if()
    		game.chooseNextEdge(0, 3);
    		game.move();
    		System.out.println("test");
    	}
    }
}