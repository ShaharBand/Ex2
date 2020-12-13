import api.geo_location;

public class GeoLocation implements geo_location {
	private double x;
	private double y;
	private double z;
	
	public GeoLocation(String string) {
		String[] arrOfStr = string.split(",", 3); 
		
		this.x = Double.parseDouble(arrOfStr[0]);
		this.y = Double.parseDouble(arrOfStr[1]);
		this.z = Double.parseDouble(arrOfStr[2]);
	}

	public GeoLocation() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	@Override
	public double x() {
		return this.x;
	}

	@Override
	public double y() {
		return this.y;
	}

	@Override
	public double z() {
		return this.z;
	}
	@Override
	public double distance(geo_location g) {
        double dx = this.x() - g.x();
        double dy = this.y() - g.y();
        double dz = this.z() - g.z();
        double t = (dx*dx+dy*dy+dz*dz);
        return Math.sqrt(t);
	}
}
