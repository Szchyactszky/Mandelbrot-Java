package mandelbrot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client Socket part
 * Creates connection to server and sends the data
 */
public class cli {


	public static void main(String[] args) throws UnknownHostException, IOException {

	}

	/**
	 * Creates connection, sends data to server, receives and returns.
	 */
	public static String[] sendDataToServer(String server, Position data, int max_N) {

		// split server in to ip and port.
		String[] server_array = server.split(":");
		String server_ip = server_array[0];
		int server_port = Integer.parseInt(server_array[1]);

		String[] image_data = new String[data.getHeight_y()];

		try {
			Socket cliSoc = new Socket(server_ip, server_port);

			PrintWriter out = new PrintWriter(cliSoc.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(cliSoc.getInputStream()));

			String fromServer;

			String get_value_string = "/mandelbrot/" + data.getMin_c_re() + "/" + data.getMin_c_im() + "/"
					+ data.getMax_c_re() + "/" + data.getMax_c_im() + "/" + data.getLength_x() + "/"
					+ data.getHeight_y() + "/" + max_N;
			System.out.print("->Sending to server "+server+"");
			System.out.println(get_value_string);

			out.println(get_value_string);

			fromServer = in.readLine();

			image_data = fromServer.split(",");
			
			cliSoc.close();
			

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return image_data;

	}

}
