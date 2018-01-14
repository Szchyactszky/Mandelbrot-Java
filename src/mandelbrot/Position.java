package mandelbrot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;

/**
 * Class created to hold the Position object. This object holds all values
 * required for each image chunk It also contains method to send data to server
 * by chunk.
 *
 * @author Ugis Varslavans
 * @version 1.0
 * @since 2018-01-13
 */

public class Position implements Runnable {
	private int length_x;
	private int height_y;
	private String[] pgmdata;
	private double min_c_re;
	private double min_c_im;
	private double max_c_re;
	private double max_c_im;
	private int server_id;
	private String server_ip;
	private int max_n;
	private CountDownLatch latch;

	/**
	 * This method sends data to server and waits for response and updates the
	 * object.
	 * 
	 * @return Nothing.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			Socket cliSoc = new Socket(server_ip, server_port);

			PrintWriter out = new PrintWriter(cliSoc.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(cliSoc.getInputStream()));

			String fromServer;

			String get_value_string = "/mandelbrot/" + min_c_re + "/" + min_c_im + "/" + max_c_re + "/" + max_c_im + "/"
					+ length_x + "/" + height_y + "/" + max_n;
			System.out.println("");
			System.out.print("->Sending to server " + server_ip + "");
			System.out.println(get_value_string);

			out.println(get_value_string);

			fromServer = in.readLine();
			String[] image_data = new String[height_y];

			image_data = fromServer.split(",");

			pgmdata = image_data;

			// substract latch
			latch.countDown();

			cliSoc.close();
			

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method creates a thread that will send data to server.
	 * 
	 * @param latch
	 *            This is a counter for threads to know when all of them have
	 *            finished.
	 * @return Nothing.
	 */
	public void send_to_server(CountDownLatch latch) {
		this.latch = latch;
		(new Thread(this)).start();

	}

	public int getMax_n() {
		return max_n;
	}

	public void setMax_n(int max_n) {
		this.max_n = max_n;
	}

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
