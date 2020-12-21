package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class is responsible for the graphics of the graph and game information.
 * The game window is resizeable, includes a game timer, total score and agent score.
 * This JFrame contains JPanel.
 * The Frame is made into 2 sections game-play and main menu where u implement the id and level.
 */
public class MyFrame extends JFrame{
	private Arena _ar;
	private gameClient.util.Range2Range _w2f;
	
	private long time = 0;
	private static MainMenu panel;
	
	private Image backgroundImage = new ImageIcon("/images/Game_Background.jpg").getImage();
	private Image nodeImage = new ImageIcon("/images/Node.png").getImage();
	private Image trainerImage = new ImageIcon("/images/Trainer.png").getImage();
	private Image pokemonsImage[] = new Image[] {
			new ImageIcon("/images/pokemon0.png").getImage(),new ImageIcon("/images/pokemon1.png").getImage()
	};
	
	MyFrame(String a) {
		super(a);
		this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
		ImageIcon image_icon = new ImageIcon("images/Icon.jpg");
		this.setIconImage(image_icon.getImage());
		this.setSize(800, 800);
		this.setLocationRelativeTo(null);
	}
	
    /**
     * this function is responsible for the initial game window
     */
    public void InitMenu() {
        panel = new MainMenu(this.getWidth(), this.getHeight(), this);
        this.add(panel);
        this.setVisible(true);
        this.setResizable(false);
        panel.setVisible(true);
        
    }
    
	public void update(Arena ar) {
		this._ar = ar;
		updateFrame();
		this.revalidate();
	}

	private void updateFrame() {
		Range rx = new Range(20, this.getWidth() - 50);
		Range ry = new Range(this.getHeight() - 50, 200);
		Range2D frame = new Range2D(rx, ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,frame);
		this.revalidate();
	}
	
	public void paint(Graphics g)
	{  
	 	this.revalidate();
	        
		if (_ar != null && this._w2f != null) 
		{
			Image buffer_image = createImage(this.getWidth(), this.getHeight());
			Graphics2D g2d = (Graphics2D) buffer_image.getGraphics();
	
			updateFrame();
			
			// Rendering the Graphics to be smoother by using ANTI ALIASING.
	        g2d.setRenderingHint(
	                RenderingHints.KEY_RENDERING,
	                RenderingHints.VALUE_RENDER_QUALITY);
	        g2d.setRenderingHint(
	        		RenderingHints.KEY_ANTIALIASING, 
	        		RenderingHints.VALUE_ANTIALIAS_ON);
	        
			paintComponents(g2d);
			
			// updating the graphics once calculated throughly.
			g.drawImage(buffer_image, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}
	
	@Override
	public void paintComponents(Graphics g)
	{
		super.paintComponents(g);
		g.clearRect(0, 0, this.getWidth(), this.getWidth());
		
		drawBackground(g);
		drawGraph(g);
		drawPokemons(g);
		drawAgents(g);
		drawInfo(g);
		
		g.dispose();
	}
	
	public long setTime(long t) {
		return this.time = t;
	}
	private String TimeFormat()
	{
		// 44.432
		int minutes = 0, seconds, hundredth_of_sec;
		seconds = (int) (this.time / 1000);
		if(seconds > 60)
		{
			minutes = seconds / 60;
			seconds -= minutes * 60;
		}
		hundredth_of_sec = (int) ((this.time % 1000)/10);
		
		return String.format("%02d:%02d:%02d", minutes, seconds, hundredth_of_sec);
	}
	private void drawBackground(Graphics g) {
		
		// tile option in case of a tile image
		boolean tile = false;

        if (tile) {
            int iw = backgroundImage.getWidth(this);
            int ih = backgroundImage.getHeight(this);
            if (iw > 0 && ih > 0) {
                for (int x = 0; x < this.getWidth(); x += iw) {
                    for (int y = 0; y < this.getHeight(); y += ih) {
                        g.drawImage(backgroundImage, x, y, iw, ih, this);
                    }
                }
            }
        } else {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }	
	}
	private void drawInfo(Graphics g) {
		
		List<CL_Agent> agentList = _ar.getAgents();
		int total_Score = 0;
		
		if(agentList != null) {
			int i = 0;
			Iterator<CL_Agent> itr = agentList.iterator();
			
			g.setFont(new Font("Calibri", Font.BOLD, 12));
			g.drawString("Agents:", this.getWidth()/2 - 80, 80);
			
			g.setFont(new Font("Ariel", Font.PLAIN, 12));
			while(itr.hasNext()) {
				CL_Agent agent = itr.next();
				int agent_id = agent.getID();

				String agentInfo = "ID: " + agent.getID() + ", Score:" + agent.getValue() + 
						", Speed: " + agent.getSpeed();
				if(agent_id >= 0) 
					g.drawString(agentInfo, this.getWidth()/2 - 80,100 + 17 * i);
				
				total_Score += agent.getValue();
				i++;
			}
		}
		g.setFont(new Font("Calibri", Font.BOLD, 12));
		String timeFormat = TimeFormat();
		g.drawString("Total Score: " + total_Score + ", Time Left: " + timeFormat, this.getWidth()/2 - 80,50);
	}
	
	private void drawGraph(Graphics g) {
		directed_weighted_graph gg = _ar.getGraph();
		Iterator<node_data> iter = gg.getV().iterator();
		
		while(iter.hasNext()) {
			node_data n = iter.next();
			Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
			
			while(itr.hasNext()) {
				edge_data e = itr.next();
				g.setColor(Color.black);
				drawEdge(e, g);
			}
		}
		
		iter = gg.getV().iterator();
		while(iter.hasNext()) {
			node_data n = iter.next();
			drawNode(n,g);
		}
	}
	private void drawPokemons(Graphics g) {
		List<CL_Pokemon> pokemonList = _ar.getPokemons();
		
		if(pokemonList != null) {
			Iterator<CL_Pokemon> itr = pokemonList.iterator();
			
			while(itr.hasNext()) {
				
				CL_Pokemon pokemon = itr.next();
				Point3D pos = pokemon.getLocation();

				if(pos != null) 
				{
					geo_location frame_position = this._w2f.world2frame(pos);
						
					int pokemon_id = pokemon.getType();
					if(pokemon_id < 0 || pokemon_id > pokemonsImage.length-1)pokemon_id = 0;
					
					Image pokemonImage = pokemonsImage[pokemon_id];
					
					int image_width = pokemonImage.getWidth(this);
					int image_height = pokemonImage.getHeight(this);
					int scale = 20;
					
					int centerX = (int)(frame_position.x() - (image_width / scale) / 2);
					int centerY = (int)(frame_position.y() - (image_height / scale) / 2);
					
					g.drawImage(pokemonImage, centerX, centerY, image_width/scale, image_height/scale, this);
				}
			}
		}
	}
	private void drawAgents(Graphics g) {
		List<CL_Agent> agentList = _ar.getAgents();

		int i=0;
		while(agentList != null && i < agentList.size()) {
			geo_location pos = agentList.get(i).getLocation();
			i++;
			
			if(pos != null) {
				geo_location frame_position = this._w2f.world2frame(pos);
				
				int image_width = trainerImage.getWidth(this);
				int image_height = trainerImage.getHeight(this);
				int scale = 8;
				
				int centerX = (int)(frame_position.x() - (image_width / scale) / 2);
				int centerY = (int)(frame_position.y() - (image_height / scale) / 2);
				
				g.drawImage(trainerImage, centerX, centerY, image_width/scale, image_height/scale, this);
				
				g.drawString("ID: " + agentList.get(i-1).getID(), (int)frame_position.x()+10, (int)frame_position.y()-10);
			}
		}
	}
	private void drawNode(node_data n, Graphics g) {
		geo_location pos = n.getLocation();
		geo_location frame_position = this._w2f.world2frame(pos);
		
		g.drawString(""+n.getKey(), (int)frame_position.x()-1, (int)frame_position.y()+20);
		
		int image_width = nodeImage.getWidth(this);
		int image_height = nodeImage.getHeight(this);
		int scale = 30;
		
		int centerX = (int)(frame_position.x() - (image_width / scale) / 2);
		int centerY = (int)(frame_position.y() - (image_height / scale) / 2);
		
		g.drawImage(nodeImage, centerX, centerY, image_width/scale, image_height/scale, this);
		
	}
	private void drawEdge(edge_data e, Graphics g) {
		directed_weighted_graph currentGraph = _ar.getGraph();
		
		geo_location src = currentGraph.getNode(e.getSrc()).getLocation();
		geo_location dest = currentGraph.getNode(e.getDest()).getLocation();
		
		geo_location s0 = this._w2f.world2frame(src);
		geo_location d0 = this._w2f.world2frame(dest);
		
		g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
	}
	
	/**
     * this class is responsible for the main menu section of the frame.
     * in which, you decide the user id and scenario level.
     */
    public static class MainMenu extends JPanel {
    	
    	private MyFrame instance;
    	
        public MainMenu(int x, int y, MyFrame instance) {
            super();
            this.setSize(x, y);
            this.setLayout(null);
            Inputs();
            GameTitle();
            Border();
            MenuBackground();
            
            this.instance = instance;
        }

        private void GameTitle() {          
            Image logo = new ImageIcon("images/Logo.png").getImage();
            Image resizedLogo = logo.getScaledInstance(400, 300,  java.awt.Image.SCALE_SMOOTH);  
            ImageIcon logoIcon = new ImageIcon(resizedLogo);
            
            JLabel title = new JLabel();
            title.setIcon(logoIcon);
            title.setBounds(0, 0, logoIcon.getIconWidth(), logoIcon.getIconHeight());
            title.setLocation(this.getWidth() / 2 - logoIcon.getIconWidth() / 2,
            		this.getHeight() / 2 - logoIcon.getIconHeight() / 2 - 40);
            
            this.add(title);
            
            title = new JLabel("   � Shahar Band, Lior Cohen");
            title.setFont(new Font("Tahoma", Font.BOLD, 14));
            title.setBounds(0, 0, 210, 16);
            title.setLocation(this.getWidth() / 2 - 105, this.getHeight() / 2 + 15);
            add(title);
        }

        private void Inputs() {
            JLabel user_id = new JLabel("ID:");
            user_id.setFont(new Font("Tahoma", Font.PLAIN, 12));
            user_id.setBounds(0, 0, 100, 22);
            user_id.setLocation(this.getWidth() / 2 - 70, this.getHeight() / 2 + 50);
            
            add(user_id);

            JTextField id_input = new JTextField();
            id_input.setFont(new Font("Tahoma", Font.PLAIN, 12));
            id_input.setBounds(this.getWidth() / 2 - 40, this.getHeight() / 2 + 50, 140, 22);
            this.add(id_input);

            JLabel game_level = new JLabel("Level [0-23]:");
            game_level.setFont(new Font("Tahoma", Font.PLAIN, 12));
            game_level.setBounds(0, 0, 100, 22);
            game_level.setLocation(this.getWidth() / 2 - 70 - 50, this.getHeight() / 2 + 80 - 2);
            add(game_level);

            JTextField game_input = new JTextField();
            game_input.setFont(new Font("Tahoma", Font.PLAIN, 12));
            game_input.setBounds(this.getWidth()/2 - 40, this.getHeight() / 2 + 80, 140, 22);
            this.add(game_input);

            JButton submit_button = new JButton("Start");
            submit_button.setFont(new Font("Tahoma", Font.PLAIN, 12));
            submit_button.setBounds(this.getWidth()/2 - 100, this.getHeight() / 2 + 115, 200, 15);
            this.add(submit_button);
            
            
            submit_button.addActionListener(e ->
                    Ex2.SetLevel(Integer.parseInt(game_input.getText())));
            submit_button.addActionListener(e ->
                    Ex2.SetUserID(Integer.parseInt(id_input.getText())));
            
            submit_button.addActionListener(e -> Ex2.StartGame());
            submit_button.addActionListener(e -> instance.setVisible(false));
        }
        private void Border() {
            Image border = new ImageIcon("images/Border.png").getImage();
            Image resizedborder = border.getScaledInstance(400, 330,  java.awt.Image.SCALE_SMOOTH); 
            ImageIcon borderIcon = new ImageIcon(resizedborder);
            
            JLabel borderLabel = new JLabel();
            borderLabel.setIcon(borderIcon);
            borderLabel.setBounds(0, 0, borderIcon.getIconWidth(), borderIcon.getIconHeight());
            borderLabel.setLocation(this.getWidth() / 2 - borderIcon.getIconWidth() / 2,
            		this.getHeight() / 2 - borderIcon.getIconHeight() / 2 + 10);
            
            this.add(borderLabel);
        }
        private void MenuBackground() {
            JLabel backgroundLabel = new JLabel();
            backgroundLabel.setIcon(new ImageIcon("images/Background.jpg"));
            backgroundLabel.setBounds(0, 0, this.getWidth(), this.getHeight());
            this.add(backgroundLabel);
        }
    }
}
