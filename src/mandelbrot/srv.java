package mandelbrot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * This is server, it should receive requests on the specified port, calculate
 * the Mandelbrot function and return it as String of pixel values separated by
 * comma
 * https://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
 */

public class srv {
	static String newline = System.getProperty("line.separator");

	public static void main(String[] args) {

		fractal_server();
	}

	/**
	 * Starts the fractal server. does infinite loop and passes each connection to
	 * new thread.
	 */
	public static void fractal_server() {
		System.out.print("->Choose the server port: ");
		Scanner sci = new Scanner(System.in);
		// get the server port and use it.
		int s_port = sci.nextInt();
		System.out.println("->Fractal server started.");
		sci.close();

		try {
			// bind server to port once
			ServerSocket serversocket = new ServerSocket(s_port);
			// loop infinitely
			while (true) {
				// accept connections
				Socket clientSocket = serversocket.accept();

				// accept connection and pass it to the thread
				new MyThread1(clientSocket).start();
			}
			// serversocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	/**
	 * Parse the input received by client and return calculated fractal image
	 */
	public static String process_input_and_calc_fractal(String input) {
		// split string by slash
		String[] inputArray = input.split("/");

		double min_c_re = Double.parseDouble(inputArray[2]);
		double min_c_im = Double.parseDouble(inputArray[3]);
		double max_c_re = Double.parseDouble(inputArray[4]);
		double max_c_im = Double.parseDouble(inputArray[5]);
		int width = Integer.parseInt(inputArray[6]);
		int height = Integer.parseInt(inputArray[7]);
		int max_n = Integer.parseInt(inputArray[8]);

		// calculate the fractal by given values.
		int[][] fractal_piece_array = calculateFractal(min_c_re, min_c_im, max_c_re, max_c_im, max_n, width, height);

		// convert the 2d array to a comma separated string for returning.
		String image_lines = array_to_string(fractal_piece_array);

		return image_lines;

	}

	/**
	 * Calculate the Mandelbrot fractal by given variables. Returns 2D array
	 * containing int values for each pixel.
	 */
	public static int[][] calculateFractal(double min_c_re, double min_c_im, double max_c_re, double max_c_im,
			int max_n, int width, int height) {
		// define the return array
		int[][] dataValues = new int[width][height];
		// define the factors
		double Re_factor = (max_c_re - min_c_re) / (width - 1);
		double Im_factor = (max_c_im - min_c_im) / (height - 1);

		for (int y = 0; y < height; ++y) {
			// apply value of the scale for y axis
			double c_im = max_c_im - y * Im_factor;
			for (int x = 0; x < width; ++x) {
				// apply value of the scale for x axis
				double c_re = min_c_re + x * Re_factor;
				double Z_re = c_re, Z_im = c_im;
				boolean isInside = true;
				int iterations = 0;
				// iterate the value.
				for (int n = 0; n < max_n; ++n) {
					double Z_re2 = Z_re * Z_re, Z_im2 = Z_im * Z_im;
					if (Z_re2 + Z_im2 > 4) {
						isInside = false;
						break;
					}
					Z_im = 2 * Z_re * Z_im + c_im;
					Z_re = Z_re2 - Z_im2 + c_re;
					iterations++;
				}
				if (isInside) {
					// color white max value 255
					dataValues[y][x] = 255;
				} else {
					// color darker black=0
					dataValues[y][x] = 0;
					dataValues[y][x] = (int) (iterations / (max_n / 255.0));
				}

			}
		}
		return dataValues;
	}

	/**
	 * Converts 2D array produced by calculateFractal function and tutn in to A
	 * String where lines are comma separated.
	 * 
	 */
	public static String array_to_string(int[][] arr) {
		String block = "";
		for (int i = 0; i < arr.length; ++i) {
			String line = "";
			for (int j = 0; j < arr[i].length; ++j) {
				if (line == "") {
					line = "" + arr[i][j];
				} else {
					line = line + " " + arr[i][j];
				}
			}
			if (block == "") {
				block = "" + line;
			} else {
				block = block + "," + line;
			}
		}
		return block;
	}

}

/**
 * Threading class
 */
class MyThread1 extends Thread {
	private Socket clientSocket;

	// constructor
	public MyThread1(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	// default function to run.
	@Override
	public void run() {
		try {
			// accept connections
			Socket clientSocket = this.clientSocket;

			// create object to communicate with client
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			// create object to read what client is sending.
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String inputLine;

			// read the line
			inputLine = in.readLine();

			System.out.println("input:" + inputLine);

			// do calculations
			String img_part = srv.process_input_and_calc_fractal(inputLine);

			// print out to the client the result
			out.println(img_part);

			System.out.println("->returning Response.");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}