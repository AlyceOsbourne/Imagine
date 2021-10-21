/*
 * Do what the F**k you want
 */

package lib.utilities;

import javafx.application.Platform;

import java.util.PriorityQueue;
import java.util.Queue;

/*
 * the idea of this class is to have a thread runner that has priorities. its a thread with a subthread for state checking, then either delegate process to platform (in case of rendering) or executes the process
 *
 */
public class ThreadManager extends Thread {

	Queue<Runnable>
			process = new PriorityQueue<>(),
			render = new PriorityQueue<>();

	public ThreadManager() {
		start();
	}

	@Override
	public synchronized void start() {
		setDaemon(true);
		setName("Thread Manager");
		super.start();
	}

	@Override
	public synchronized void run() {

		do {
			if (!process.isEmpty()) {
				try {
					Thread thread = new Thread(render.poll());
					thread.start();
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if (!render.isEmpty()) Platform.runLater(render.poll());
		}
		while (!isInterrupted());
	}


	public void addToQueue(boolean isRenderThread, Runnable runnable) {
		if (isRenderThread) render.add(runnable);
		else process.add(runnable);
	}

	public void removeFromQueue(boolean isRenderThread, Runnable runnable) {
		if (isRenderThread) render.remove(runnable);
		else process.remove(runnable);
	}
}
