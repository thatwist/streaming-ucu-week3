package ua.ucu.edu

import java.util.concurrent.{ForkJoinPool, ForkJoinWorkerThread, RecursiveTask}

trait Task[A] {
  def join: A // blocks and returns when ready
}

object Task {
  val pool = new ForkJoinPool(4)
  def apply[T](e: T): Task[T] = {
    val task = new RecursiveTask[T] {
      override def compute(): T = e
    }
    pool.execute(task)
    task.fork()
    new Task[T] {
      override def join: T = task.join()
    }
  }
}

object Test extends App {
  Parallel.parallel(
    Parallel.parallel(
      println("split1"),
      println("split1.2")),
    println("split2")
  )
}

object T extends App {
  val l = (1 to 10000000).toList
  println(org.scalameter.measure(l.map(_*2)))
  println(org.scalameter.measure(l.par.map(_*2)))
}

object Tt extends App {
  implicit class S(s: String) {
    def *(ss: String): String = "1"
    def -(ss: String): String = "2"
  }

  val l = "6" * "2" - "3"

  println(l)
}

object Parallel {


  def parallel[A, B](cA: => A, cB: => B): (A, B) = {
    val tB: Task[B] = Task { cB }
    val tA: A = cA
    (tA, tB.join)
  }

  def parallelWrong[A, B](cA: => A, cB: => B): (A, B) = {
    val tB: B = Task {
      cB
    }.join
    val tA: A = cA
    (tA, tB)
  }
  // Here join is called on tb where we are creating the task to be executed in parallel.
  // So first we wait until tb is calculated. Then we calculated ta.
  // So we are not doing these tasks in parallel
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

  def monteCarloPiSeq(iter: Int): Double = 4.0 * mcCount(iter) / iter

  import Parallel._
  def monteCarloPiPar(iter: Int): Double = {
    val ((pi1, pi2), (pi3, pi4)) = parallel( parallel(mcCount(iter/4), mcCount(iter/4)),
      parallel(mcCount(iter/4), mcCount(iter - 3*(iter/4))) )
    4.0 * (pi1 + pi2 + pi3 + pi4) / iter
  }
}
