package ua.ucu.edu

object Threads {

  object ThreadsCreation extends App {
    class MyThread extends Thread {
      override def run(): Unit = {
        println("New thread running.")
      }
    }
    val t = new MyThread
    t.start()
    t.join()
    println("New thread joined.")
  }
}

object Pools {
  import java.util.concurrent.ExecutorService
  import java.util.concurrent.Executors
  import java.util.concurrent.Future

  val executor: ExecutorService = Executors.newFixedThreadPool(10)
  val ret: Future[_] = executor.submit(new Runnable {
    def run(): Unit = {
      val threadName = Thread.currentThread.getName
      System.out.println("Hello " + threadName)
    }
  })
}

object ForkJoin extends App {

  import java.util.concurrent.RecursiveTask

  class FibonacciComputation(val number: Int) extends RecursiveTask[Int] {

    override def compute: Int = {
      ???
    }
  }
}
