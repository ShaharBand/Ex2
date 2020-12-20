package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

/**
 * This class is responsible for the pokemon information.
 */
public class CL_Pokemon {
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D location;
	private boolean targeted;
	
	public CL_Pokemon(Point3D location, int t, double v, double s, edge_data e) {
		_type = t;
		_value = v;
		set_edge(e);
		this.location = location;
		this.targeted = false;
	}

	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	public edge_data get_edge() {
		return this._edge;
	}
	
	public Point3D getLocation() {
		return this.location;
	}
	
	public int getType() {
		return this._type;
	}

	public double getValue() { 
		return this._value;
	}
	
	public void setTargeted(boolean targeted) {
		this.targeted = targeted;
	}
	
	public boolean getTargeted() {
		return this.targeted;
	}
}
