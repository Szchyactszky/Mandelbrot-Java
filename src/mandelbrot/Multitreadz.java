package mandelbrot;

/*
 * https://www.youtube.com/watch?v=KUdro0G1BV4&index=5&list=PLBB24CFB073F1048E
 */
class MyThread extends Thread {

	private int to;

	public MyThread(int to) {
		this.to = to;
	}

	@Override
	public void run() {
		System.out.println("hello " + to);
	}
}

/*
 * -------------------------------------------
 */

/*
 * https://docs.oracle.com/javase/6/docs/api/java/lang/Thread.html?is-external=
 * true
 * 
 */
public class Multitreadz {
	public static void main(String[] args) {

		// Thread[] threads = new Thread[2];
		// for (int i = 0; i < threads.length; i++) {
		// // threads[i]=new MyThread("world!").start();
		// threads[i] = new Thread(new Runner());
		// threads[i].start();
		// }

		for (int i = 0; i < 3; i++) {
			new MyThread(i).start();

		}

	}

}
