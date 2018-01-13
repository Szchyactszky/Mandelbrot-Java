package mandelbrot;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallFuture {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newCachedThreadPool();

		//future -callaale in not anonymus function
		Future<Integer> future = executor.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {

				Random random = new Random();
				int duration = random.nextInt(2000);
				System.out.println("starting...");

				Thread.sleep(duration);

				System.out.println("Finished!");

				return duration;
			}

		});
		executor.shutdown();
		
		
		

		try {

			System.out.println("slept: " + future.get());

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
