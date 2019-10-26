package ua.ucu.edu

import java.util.concurrent.ForkJoinPool

object Threads1 extends App {

    class MyThread extends Thread {
      override def run(): Unit = {
        println("New thread running.")
      }
    }
    val t = new MyThread
    t.start()
    Thread.sleep(1000)
    println("New thread joined.")
    t.join()
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
      if (number <= 1) {
        1
      } else {
        val comp1 = new FibonacciComputation(number - 1)
        val comp2 = new FibonacciComputation(number - 2)
        comp1.fork()
        comp2.compute() + comp1.join()
      }
    }
  }

  val l = new ForkJoinPool().submit(new FibonacciComputation(10))
  println(l.get())
}
