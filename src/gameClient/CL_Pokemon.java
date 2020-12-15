package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

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
		targeted = false;
	}

	//public String toString() {return "Pokemon: value = " + _value + ", Type =" + _type + "";}
	
	public edge_data get_edge() {
		return _edge;
	}

	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	public Point3D getLocation() {
		return location;
	}
	public int getType() {return _type;}

	public double getValue() { return _value;}
}
