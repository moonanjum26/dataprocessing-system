package main

import (
    "fmt"
    "sync"
    "time"
)

func main() {
    // Create an unbuffered channel for tasks
    tasks := make(chan string)
    // Create a WaitGroup to wait for all workers to finish
    var wg sync.WaitGroup

    numWorkers := 3

    // Start worker goroutines
    for i := 1; i <= numWorkers; i++ {
        wg.Add(1)
        go worker(i, tasks, &wg)
    }

    // Add tasks
    for i := 1; i <= 10; i++ {
        task := fmt.Sprintf("Task-%d", i)
        tasks <- task
    }

    // Send termination signals
    for i := 1; i <= numWorkers; i++ {
        tasks <- "END"
    }

    // Close the tasks channel (optional if we rely only on "END" signals)
    // close(tasks)

    // Wait for all workers to finish
    wg.Wait()
    fmt.Println("All tasks have been processed. Main goroutine exiting.")
}

func worker(id int, tasks <-chan string, wg *sync.WaitGroup) {
    defer wg.Done()

    for {
        task, ok := <-tasks
        // If channel is closed (ok=false) or we receive "END", break
        if !ok || task == "END" {
            fmt.Printf("Worker %d received END signal or channel closed.\n", id)
            return
        }

        fmt.Printf("Worker %d processing %s\n", id, task)
        err := processTask(task)
        if err != nil {
            fmt.Printf("Worker %d encountered an error processing %s: %v\n", id, task, err)
            // Optionally, continue or break based on the severity
        } else {
            fmt.Printf("Worker %d completed %s\n", id, task)
        }
    }
}

func processTask(task string) error {
    // Simulate processing delay
    time.Sleep(500 * time.Millisecond)
    // Intentionally no error, but if you need to simulate:
    // if task == "Task-5" {
    //     return fmt.Errorf("simulated error on %s", task)
    // }
    return nil
}
