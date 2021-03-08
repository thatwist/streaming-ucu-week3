package ua.ucu.edu.hidden

import java.util.concurrent.{ForkJoinPool, ForkJoinWorkerThread, RecursiveTask}

object Par1 {

}

trait Task[A] {
  def join: A // blocks and returns when ready
}
object Task {

  // https://github.com/rohgar/scala-parallel-programming-3/blob/master/resources/source/package-parallel.pdf
  // todo
  val forkJoinPool = new ForkJoinPool

  def task[T](e: => T): Task[T] = {
    val t = new RecursiveTask[T] {
      def compute: T = e
    }
    // schedule
    Thread.currentThread match {
      case worker: ForkJoinWorkerThread =>
        t.fork
      case _ => forkJoinPool.execute(t)
    }
    // return the scheduled task
    new Task[T] {
      def join: T = t.join
    }
  }
}


/*
Consider a square of side length = 2 and a circle of diameter = 2.
The circle is inside the square.
Ratio between the surfaces of 1/4 of a circle and 1/4 of a square:
λ = ( pi*(1^2)/2 / (2^2)/4 )
λ = pi/4
Estimating λ: randomly sample points inside the square.
Count how many fall inside the circle. Multiply this ratio by 4 for an estimate of pi
*/
object MC {
  import scala.util.Random

  def mcCount(iter: Int): Int = {
    val randomX = new Random
    val randomY = new Random
    var hits = 0
    for (i <- 0 until iter) {
      // since we are in the quarter of the cicle/square we get coordinates from 0 to 1
      val x = randomX.nextDouble // in [0,1]
      val y = randomY.nextDouble // in [0,1]
      if (x*x + y*y < 1) hits= hits + 1
    }
    hits
  }

  def parallel[A, B](cA: => A, cB: => B): (A, B) = {
    val tB: Task[B] = Task.task { cB }
    val tA: A = cA
    (tA, tB.join)
  }

  def parallelWrong[A, B](cA: => A, cB: => B): (A, B) = {
    val tB: B = (Task.task { cB }).join
    val tA: A = cA
    (tA, tB)
  }
  // Here join is called on tb where we are creating the task to be executed in parallel.
  // So first we wait until tb is calculated. Then we calculated ta.
  // So we are not doing these tasks in parallel.



  def monteCarloPiSeq(iter: Int): Double = 4.0 * mcCount(iter) / iter

  def monteCarloPiPar(iter: Int): Double = {
    val ((pi1, pi2), (pi3, pi4)) = parallel( parallel(mcCount(iter/4), mcCount(iter/4)),
      parallel(mcCount(iter/4), mcCount(iter - 3*(iter/4))) )
    4.0 * (pi1 + pi2 + pi3 + pi4) / iter
  }
}