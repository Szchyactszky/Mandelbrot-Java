package mandelbrot;
/*
 * Class for Position object
 */
public class Position {
	private int length_x;
	private int height_y;
	private String[] pgmdata;
	private double min_c_re;
	private double min_c_im;
	private double max_c_re;
	private double max_c_im;
	private int server_id;
	private String server_ip;
	
	
	
	public String getServer_ip() {
		return server_ip;
	}

	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}

	public int getServer_port() {
		return server_port;
	}

	public void setServer_port(int server_port) {
		this.server_port = server_port;
	}

	private int server_port;

	public int getLength_x() {
		return length_x;
	}

	public void setLength_x(int length_x) {
		this.length_x = length_x;
	}



	public int getServer_id() {
		return server_id;
	}

	public void setServer_id(int server_id) {
		this.server_id = server_id;
	}

	public int getHeight_y() {
		return height_y;
	}

	public void setHeight_y(int height_y) {
		this.height_y = height_y;
	}

	public double getMin_c_re() {
		return min_c_re;
	}

	public void setMin_c_re(double min_c_re) {
		this.min_c_re = min_c_re;
	}

	public double getMin_c_im() {
		return min_c_im;
	}

	public void setMin_c_im(double min_c_im) {
		this.min_c_im = min_c_im;
	}

	public double getMax_c_re() {
		return max_c_re;
	}

	public void setMax_c_re(double max_c_re) {
		this.max_c_re = max_c_re;
	}

	public double getMax_c_im() {
		return max_c_im;
	}

	public void setMax_c_im(double max_c_im) {
		this.max_c_im = max_c_im;
	}

	public String[] getPgmdata() {
		return pgmdata;
	}

	public void setPgmdata(String[] pgmdata) {
		this.pgmdata = pgmdata;
	}




}
