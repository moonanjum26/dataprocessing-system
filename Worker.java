import java.util.concurrent.Callable;

public class Worker implements Callable<Void> {

    private final BlockingTaskQueue<String> taskQueue;
    private final int workerId;

    public Worker(BlockingTaskQueue<String> taskQueue, int workerId) {
        this.taskQueue = taskQueue;
        this.workerId = workerId;
    }

    @Override
    public Void call() {
        try {
            while (true) {
                // Retrieve a task from the queue (blocks if empty)
                String task = taskQueue.getTask();
                if (task.equals("END")) {
                    System.out.println("Worker " + workerId + " received END signal.");
                    break;
                }

                System.out.println("Worker " + workerId + " processing task: " + task);
                // Simulate work
                processTask(task);
                System.out.println("Worker " + workerId + " completed task: " + task);
            }
        } catch (InterruptedException e) {
            System.err.println("Worker " + workerId + " interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Worker " + workerId + " encountered an error: " + e.getMessage());
        }
        return null;
    }

    private void processTask(String task) throws InterruptedException {
        // Simulate computation by sleeping
        Thread.sleep(500); 
    }
}
