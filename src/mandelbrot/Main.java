package mandelbrot;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Client version of software. Main function That starts the distribution of
 * image chunks to be calculated around given servers and saves it in a file
 * once it is done.
 *
 * @author Ugis Varslavans
 * @version 1.0
 * @since 2018-01-13
 */

public class Main {
	/**
	 * This is the main method which makes use of all the other methods to achieve
	 * the end goal.
	 * 
	 * @param args
	 *            Unused.
	 * @return Nothing.
	 */
	public static void main(String[] args) {
		System.out.println("Client version of Mandelbrot started.");
		/// read user input
		UserInput fractal_input = new UserInput();

		// all user input in userInput object
		fractal_input = readUserInput();

		// calculate the divisons
		Position[][] chunks = calculate_divisions_areas(fractal_input.getLength_x(), fractal_input.getHeight_y(),
				fractal_input.getDivisions(), fractal_input.getMinR(), fractal_input.getMinI(), fractal_input.getMaxR(),
				fractal_input.getMaxI(), fractal_input.getMaxN());

		// distribute them across servers and acquire all the parts of image
		chunks = chunk_distribution_to_servers(chunks, fractal_input);

		// finish constructing the image and save it
		Imgmgm.objects_to_final_image(fractal_input.getLength_x(), fractal_input.getHeight_y(), chunks);
		System.out.println("Finished");

	}

	/**
	 * Reads user input and processes it in object, separates server address.
	 * 
	 * @return fractal_input object that contains all input.
	 */
	public static UserInput readUserInput() {

		System.out.println("minR minIM maxR maxIM maxN width height divisions server1 ... serverN: ");
		Scanner userInput = new Scanner(System.in);
		UserInput fractal_input = new UserInput();
		try {

			if (userInput.hasNextLine()) {
				boolean looping = true;

				// loop until all input is ok
				while (looping) {

					String input = userInput.nextLine();
					if (!input.isEmpty()) {
						// split by spaces
						String[] inputArray = input.split(" ");

						// first check input must be at least 8 units including 1 server
						if (inputArray.length >= 9) {
							try {
								fractal_input.setMinR(Double.parseDouble(inputArray[0]));
								fractal_input.setMinI(Double.parseDouble(inputArray[1]));
								fractal_input.setMaxR(Double.parseDouble(inputArray[2]));
								fractal_input.setMaxI(Double.parseDouble(inputArray[3]));
								fractal_input.setMaxN(Integer.parseInt(inputArray[4]));
								fractal_input.setLength_x(Integer.parseInt(inputArray[5]));
								fractal_input.setHeight_y(Integer.parseInt(inputArray[6]));
								fractal_input.setDivisions(Integer.parseInt(inputArray[7]));
							} catch (NumberFormatException nFE) {
								System.out.println("Error: ne of the expected values was not number.");
								System.out.println(
										"minR minIM maxR maxIM maxN width height divisions server1 ... serverN: ");
								continue;
							}

							// check for image size and divisions
							if ((fractal_input.getHeight_y() * fractal_input.getLength_x()) < Math
									.pow(fractal_input.getDivisions(), 2)) {
								// not enough possible pixels to divide. cant divide 2x2 pixels in 4^2 parts.
								System.out.println("Error: Divisions ->" + fractal_input.getDivisions()
										+ "^2 are to high or image resolution to low [" + fractal_input.getLength_x()
										+ "x" + fractal_input.getHeight_y() + "]. Choose different parameters.");

								System.out
										.println("minR minIM maxR maxIM maxN width height divisions server1 ... serverN: ");
								continue;
							}

							// loop trough remaining data and clasify them as servers.
							String[] server_array = new String[inputArray.length - 8];
							
							// check if servers adress are correct. ( localhost OR x.x.x.x:xx)
							IPAddressValidator ipAddressValidator;
							ipAddressValidator = new IPAddressValidator();
							
							boolean ok_for_import = true;

							for (int i = 8; i < inputArray.length; ++i) {
								server_array[i - 8] = inputArray[i];
								String[] srv_ip_arr = server_array[i - 8].split(":");
								boolean ip_valid = ipAddressValidator.validate(srv_ip_arr[0]);
								boolean port_valid = true;

								// try to parse the port
								try {
									Integer.parseInt(srv_ip_arr[1]);
								} catch (NumberFormatException nFE) {
									port_valid = false;
								}

								if ((ip_valid || srv_ip_arr[0].equals("localhost")) && port_valid) {

								} else {
									System.out.println("Error: Server Address is not correct-> " + inputArray[i]);
									ok_for_import = false;
									continue;
								}

							}

							// only when everything is ok.
							if (ok_for_import) {
								fractal_input.setServers(server_array);
								looping = false;
								break;
							}

							System.out
									.println("minR minIM maxR maxIM maxN width height divisions server1 ... serverN: ");
						} else {
							System.out.println("Error:  Missing Arguments. must have at least 9 arguments space seperated.");
						}
					} else {
						System.out.println("minR minIM maxR maxIM maxN width height divisions server1 ... serverN: ");
					}

				}
			}
		} finally {
			userInput.close();
		}

		return fractal_input;
	}

	/**
	 * calculates fractals locally. prototype
	 * 
	 * @return Nothing.
	 */
	public static void Calculate_fractals_localy() {
		System.out.println(" minR minIM maxR maxIM maxN x y divisions servern servern+1: ");
		Scanner userInput = new Scanner(System.in);
		try {
			if (userInput.hasNextLine()) {
				String input = userInput.nextLine();

				// split the input line by whitespace
				String[] inputArray = input.split(" ");

				double min_c_re = Double.parseDouble(inputArray[0]);
				double min_c_im = Double.parseDouble(inputArray[1]);
				double max_c_re = Double.parseDouble(inputArray[2]);
				double max_c_im = Double.parseDouble(inputArray[3]);

				int max_n = Integer.parseInt(inputArray[4]);

				int x = Integer.parseInt(inputArray[5]);
				int y = Integer.parseInt(inputArray[6]);

				int divisions = Integer.parseInt(inputArray[7]);

				/*
				 * 8 9 nad 10 are server locations. filter them out and load balance the parts
				 * to the servers.
				 */

				Position[][] chunks = calculate_divisions_areas(x, y, divisions, min_c_re, min_c_im, max_c_re, max_c_im,
						max_n);

				// calculate the images seperately.
				for (int i = 0; i < chunks.length; ++i) {
					for (int j = 0; j < chunks[i].length; ++j) {
						double min_c_rec = chunks[i][j].getMin_c_re();
						double min_c_imc = chunks[i][j].getMin_c_im();
						double max_c_rec = chunks[i][j].getMax_c_re();
						double max_c_imc = chunks[i][j].getMax_c_im();
						int width = chunks[i][j].getLength_x();
						int height = chunks[i][j].getHeight_y();
						// invoke calculation
						int[][] fractalData = srv.calculateFractal(min_c_rec, min_c_imc, max_c_rec, max_c_imc, max_n,
								width, height);
						// turn the data array in to one long string with new line breaks and add it to
						// the object.
						chunks[i][j].setPgmdata(Imgmgm.array_to_string(fractalData));
						//
						// maake multiple images localy
						// String name = i + "-" + j;
					}
				}

				Imgmgm.objects_to_final_image(x, y, chunks);
				System.out.println("Finished");

			}
		} finally {
			userInput.close();
		}

	}

	/**
	 * Calculate the division area and its location relative to the whole image
	 * calculate the real and imaginary axis boundaries relative to the pixels
	 * areas.
	 * 
	 * @param width
	 *            width of the final image
	 * @param height
	 *            height of the final image
	 * @param divisions
	 *            This defines in how many pieces the image will be divided
	 * @param min_c_re
	 *            minimal boundary of complex real number
	 * @param min_c_im
	 *            minimal boundary of imaginary C
	 * @param max_c_re
	 *            max boundary of real C
	 * @param max_c_im
	 *            max boundary of imaginary C
	 * @param maxN
	 *            The amount of iterations to perform.
	 * @return Position object array in a grid system indicating where the image
	 *         blocks should be in the final image each object contains said image
	 *         data
	 * 
	 */
	public static Position[][] calculate_divisions_areas(int width, int height, int divisions, double min_c_re,
			double min_c_im, double max_c_re, double max_c_im, int maxN) {

		// ffind the x value of all blocks except last one
		int xvalue = (int) Math.round((double) width / (double) divisions);
		// area of the last one is substraction of whats left
		int xvalue_last = (int) width - (divisions - 1) * xvalue;

		int yvalue = (int) Math.round((double) height / (double) divisions);
		int yvalue_last = (int) height - (divisions - 1) * yvalue;

		// pixel ratio value for Real axis. 1 unit of real axis = this much pixels.
		double x_axis_real_ratio = (double) width / (Math.abs(min_c_re) + Math.abs(max_c_re));
		// pixel ratio value for im axis
		double y_axis_img_ratio = (double) height / (Math.abs(min_c_im) + Math.abs(max_c_im));

		// how much value 1 pixel has in X axis aka REAL
		double one_pixel_x = 1 / (double) x_axis_real_ratio;
		// value of 1 pixel in y axis
		double one_pixel_y = 1 / (double) y_axis_img_ratio;

		// define array of objects to store location xy and pgm data in
		Position[][] chunks = new Position[divisions][divisions];

		double max_I = max_c_im;

		System.out.println("division by pixels will look like this:");

		// walk trough and prepare the values of x and y
		for (int i = 0; i < divisions; ++i) {// verticaly
			int y_cor = yvalue;
			if (i == divisions - 1) {
				// last block horizontaly will have different values if
				y_cor = yvalue_last;
			}

			double block_height_in_axis_y = y_cor * one_pixel_y;
			// calculate the next minimum R value
			double temp_min_I = max_I - block_height_in_axis_y;

			// reset value with the suplied
			double min_R = min_c_re;

			for (int j = 0; j < divisions; ++j) {// Horizontally
				int x_cor = xvalue;
				if (j == divisions - 1) {
					// last block vertically
					x_cor = xvalue_last;
				}

				double block_lenght_in_axis_x = x_cor * one_pixel_x;
				// calculate the next minimum R value
				double temp_max_R = min_R + block_lenght_in_axis_x;

				// put them in array
				System.out.print(x_cor + "x" + y_cor + " ");

				chunks[i][j] = new Position();
				chunks[i][j].setLength_x(x_cor);
				chunks[i][j].setHeight_y(y_cor);
				chunks[i][j].setMin_c_re(min_R);
				chunks[i][j].setMin_c_im(temp_min_I);
				chunks[i][j].setMax_c_re(temp_max_R);
				chunks[i][j].setMax_c_im(max_I);
				chunks[i][j].setMax_n(maxN);
				// last max is now new min.
				min_R = temp_max_R;
			}
			System.out.println("");
			// old minimum is new max
			max_I = temp_min_I;
		}
		return chunks;
	}

	/**
	 * The method distributes the image chunks accross the given set of servers and
	 * receives the gray scale data.
	 * 
	 * @param chunks
	 *            object array that holds all data values for the image sets.
	 * @param fractal_input
	 *            object that holds all input values.
	 * @return Position object array in a grid system indicating where the image
	 *         blocks should be in the final image each object contains said image
	 *         data
	 */
	public static Position[][] chunk_distribution_to_servers(Position[][] chunks, UserInput fractal_input) {

		// designate chunks to servers aka set what chunk goes to what server.
		chunks = chuk_designation_by_servers(chunks, fractal_input);

		CountDownLatch latch = new CountDownLatch(fractal_input.getDivisions() * fractal_input.getDivisions());

		// do the treading/waiting part on each chunk here

		for (int i = 0; i < chunks.length; ++i) {
			for (int j = 0; j < chunks[i].length; ++j) {

				System.out.println("calling Send to server");
				// throw them in new thread and get response. also pass in latch reference

				chunks[i][j].send_to_server(latch);

			}
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return chunks;

	}

	/*
	 * This method loops trough chunk object and assigns servers to it.
	 * 
	 * @param chunks object array that holds all data values for the image sets.
	 * 
	 * @param fractal_input object that holds all input values.
	 * 
	 * @return Position object array in a grid system indicating where the image
	 * blocks should be in the final image each object contains said image data
	 */
	public static Position[][] chuk_designation_by_servers(Position[][] chunks, UserInput fractal_input) {
		// get the server
		String[] servers = fractal_input.getServers();
		// find out how many servers there are. and distribute the image across the
		int server_count = servers.length;

		// total amount of blocks the image will be divided to
		int blocks = fractal_input.getDivisions() * fractal_input.getDivisions();
		int blocks_per_server = blocks / server_count;
		// System.out.println("blocks per server: "+blocks_per_server);

		int[] server_blocks_div = new int[server_count];
		for (int y = 0; y < server_count; ++y) {
			server_blocks_div[y] = blocks_per_server;
		}

		// there is reminder and the block should be added somewhere.
		int left_blocks = blocks - (blocks_per_server * server_count);

		if (left_blocks >= 1) {
			/// check how much is left and distribute the blocks equally across the other
			/// block parties.
			for (int y = 0; y < server_count; ++y) {
				server_blocks_div[y] = server_blocks_div[y] + 1;
				left_blocks--;
				if (left_blocks <= 0) {
					break;
				}
			}
		}

		// set server designation counter
		int sbc = 0;

		// loop trough the chunks and assign the servers to it.
		for (int i = 0; i < chunks.length; ++i) {
			for (int j = 0; j < chunks[i].length; ++j) {
				String[] server_data = fractal_input.getServers();
				String[] curent_server = server_data[sbc].split(":");

				// add them to objec
				chunks[i][j].setServer_id(sbc);
				chunks[i][j].setServer_ip(curent_server[0]);
				chunks[i][j].setServer_port(Integer.parseInt(curent_server[1]));

				server_blocks_div[sbc] = server_blocks_div[sbc] - 1;
				if (server_blocks_div[sbc] == 0) {
					sbc++;
				}

			}
		}

		return chunks;
	}

}
