package api;
public class EdgeData implements edge_data {
	private int src;
	private int dest;
	private double weight;
	private String info;
	private int tag;
	
	public EdgeData(int src, int dest, double w) {
		this.src = src;
		this.dest = dest;
		this.weight = w;
	}
	@Override
	public int getSrc() {
		return this.src;
	}

	@Override
	public int getDest() {
		return this.dest;
	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	@Override
	public String getInfo() {
		return this.info;
	}

	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	@Override
	public int getTag() {
		return this.tag;
	}

	@Override
	public void setTag(int t) {
		this.tag = t;
	}
}
