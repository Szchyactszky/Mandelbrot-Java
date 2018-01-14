package mandelbrot;

import java.util.*;

/**
* Imgmgm class implenets function to handle the PGM image actions and its helper functions.
* 
*
* @author  Ugis Varslavans
* @version 1.0
* @since   2018-01-13
*/
public class Imgmgm {
	private static Formatter forma;
	static String newline = System.getProperty("line.separator");

	/**
	 * Creates file with hard coded name.
	 * @return Nothing
	 */
	public void createFile() {
		try {
			forma = new Formatter("fractal.pgm");
			System.out.println("File created");

		} catch (Exception e) {
			System.out.println("Error making pgm file");
		}
	}

	/**
	 * Tries to open a file with specified name.
	 * @param name This is first parameter to the method.
	 * @return Nothing
	 */
	public static void openFile(String name) {
		try {
			forma = new Formatter(name + ".pgm");
		} catch (Exception r) {
			System.out.println("Error opening pgm file");
		}
	}

	/**
	 * Closes the file.
	 * @return Nothing
	 */
	public static void closeFile() {
		forma.close();
	}
	/**
	 * Converts 2D array in to 1D array of Strings no line breaks.
	 * @arr This is array that contains gray scale values for image.
	 * @return This returns 1D array of strings by rows.
	 */
	public static String[] array_to_string(int[][] arr) {
		// define block array
		String[] block = new String[arr.length];
		int counter = 0;
		for (int i[] : arr) {
			// define s
			String s = "";
			// loop trough second array
			for (int j : i) {
				// append the values
				s = s + " " + j;
			}
			block[counter] = s;
			counter++;
		}
		return block;
	}
	/**
	 * Takes array of objects and makes a complete picture out of it and saves it
	 * package folder
	 * @width defines width of image in pixels
	 * @height defines height of image in pixels
	 * @arr Position type object contains all data about the image chunks.
	 * @return Nothing.
	 */
	public static void objects_to_final_image(int width, int height, Position[][] arr) {
		System.out.println("Creating final_large_img.pgm file...");
		openFile("final_large_img");
		forma.format("%s", "P2" + newline);
		forma.format("%s", "#A test file for mandral fractral" + newline);
		forma.format("%s", width + " " + height + newline);
		forma.format("%s", "255" + newline);

		// loop trough array divisions row
		for (int i = 0; i < arr.length; ++i) {
			int selected_row = 0;
			// this gives us rows for current pixel block row
			String[] pgm_rows = arr[i][0].getPgmdata();
			// run this as many times as there is lines in the pixel block.
			for (int k = 0; k < pgm_rows.length; ++k) {
				String row = "";
				// loop trough array division column
				for (int j = 0; j < arr[i].length; ++j) {
					// in each column there is pgm data that contains multiple records.
					String[] pgm_data = arr[i][j].getPgmdata();
					// walk trough array and get one row of data from all set of data.
					row = row + " " + pgm_data[selected_row];
				}
				selected_row++;
				// end of the cycle you got 1 full row, add new line and record it to file.
				forma.format("%s", row + newline);
			}
		}
		System.out.println("Final image done.");
		closeFile();
	}
}
