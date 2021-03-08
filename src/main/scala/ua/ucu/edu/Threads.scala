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

object TestFuture extends App {

  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  def fib(n: Int): Future[Int] = n match {
    case 0 => Future.successful(0)
    case 1 => Future.successful(1)
    case _ => fib(n - 1).flatMap(f => fib(n - 2).map(_ + f))
  }

  println(fib(10).onComplete { v =>
    println(v)
  })
}

object ForkJoin extends App {

  import java.util.concurrent.RecursiveTask

  class FibonacciComputation(val number: Int) extends RecursiveTask[Int] {
    override def compute: Int = {
      if (number <= 2) {
        1
      } else {
        val comp1 = new FibonacciComputation(number - 1)
        val comp2 = new FibonacciComputation(number - 2)
        comp1.fork()
        comp2.compute() + comp1.join()
      }
    }
  }

  val l = new ForkJoinPool().submit(new FibonacciComputation(3))
  println(l.get())
}
