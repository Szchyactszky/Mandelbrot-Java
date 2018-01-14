package mandelbrot;
/**
 * UserInput object class for storing user input values.
 *
 * @author Ugis Varslavans
 * @version 1.0
 * @since 2018-01-13
 */
public class UserInput {
	private double minR;
	private double minI;
	private double maxR;
	private double maxI;
	private int maxN;
	private int length_x;
	private int height_y;
	private int divisions;
	private String [] servers;
	
	public double getMinR() {
		return minR;
	}
	public void setMinR(double minR) {
		this.minR = minR;
	}
	public double getMinI() {
		return minI;
	}
	public void setMinI(double minI) {
		this.minI = minI;
	}
	public double getMaxR() {
		return maxR;
	}
	public void setMaxR(double maxR) {
		this.maxR = maxR;
	}
	public double getMaxI() {
		return maxI;
	}
	public void setMaxI(double maxI) {
		this.maxI = maxI;
	}
	public int getMaxN() {
		return maxN;
	}
	public void setMaxN(int maxN) {
		this.maxN = maxN;
	}
	public int getLength_x() {
		return length_x;
	}
	public void setLength_x(int length_x) {
		this.length_x = length_x;
	}
	public int getHeight_y() {
		return height_y;
	}
	public void setHeight_y(int height_y) {
		this.height_y = height_y;
	}
	public int getDivisions() {
		return divisions;
	}
	public void setDivisions(int divisions) {
		this.divisions = divisions;
	}
	public String[] getServers() {
		return servers;
	}
	public void setServers(String[] servers) {
		this.servers = servers;
	}

	
	

}
