package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame{
	private int _ind;
	private Arena _ar;
	private gameClient.util.Range2Range _w2f;
	
	private Image backgroundImage = new ImageIcon("images/Background.jpg").getImage();
	private Image nodeImage = new ImageIcon("images/Node.png").getImage();
	private Image trainerImage = new ImageIcon("images/Trainer.png").getImage();
	private Image pokemonsImage[] = new Image[] {
			new ImageIcon("images/pokemon0.png").getImage(),new ImageIcon("images/pokemon1.png").getImage()
	};
	
	MyFrame(String a) {
		super(a);
		int _ind = 0;
	}
	
	public void update(Arena ar) {
		this._ar = ar;
		updateFrame();
	}

	private void updateFrame() {
		Range rx = new Range(20, this.getWidth() - 20);
		Range ry = new Range(this.getHeight() - 10, 150);
		Range2D frame = new Range2D(rx, ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,frame);
	}
	
	public void paint(Graphics g)
	{  
		Image buffer_image = createImage(this.getWidth(), this.getHeight());
		Graphics2D g2d = (Graphics2D) buffer_image.getGraphics();

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
	
	@Override
	public void paintComponents(Graphics g)
	{
		super.paintComponents(g);
		g.clearRect(0, 0, this.getWidth(), this.getWidth());
		
		//	updateFrame();
		//drawBackground(g);
		drawGraph(g);
		drawPokemons(g);
		drawAgents(g);
		drawInfo(g);
		
		g.dispose();
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
		List<String> str = _ar.get_info();
		String dt = "none";
	    
		//System.out.println();
		for(int i=0; i < str.size(); i++) {
			g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i*20);
		}
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
}
