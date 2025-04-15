import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DataProcessingSystem {
    public static void main(String[] args) {
        final int NUM_WORKERS = 3;
        BlockingTaskQueue<String> taskQueue = new BlockingTaskQueue<>();

        // Create a thread pool for worker threads
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_WORKERS);

        // Submit worker tasks to the executor
        List<Future<Void>> futures = new ArrayList<>();
        for (int i = 0; i < NUM_WORKERS; i++) {
            Worker worker = new Worker(taskQueue, i);
            futures.add(executorService.submit(worker));
        }

        // Add tasks to the queue
        for (int i = 1; i <= 10; i++) {
            String task = "Task-" + i;
            taskQueue.addTask(task);
        }

        // Signal the workers to stop
        for (int i = 0; i < NUM_WORKERS; i++) {
            taskQueue.addTask("END");
        }

        // Shutdown the executor gracefully
        executorService.shutdown();

        // Wait for all workers to finish (optional for demonstration)
        /* 
        for (Future<Void> f : futures) {
            try {
                f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */

        System.out.println("All tasks added. Main thread exiting...");
    }
}
