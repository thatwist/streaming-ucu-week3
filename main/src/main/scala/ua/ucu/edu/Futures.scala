package ua.ucu.edu

import scala.collection.immutable
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object Futures {

  trait Egg
  trait Cracked extends Egg

  trait Chicken
  object Chicken extends Chicken

  def chickenMagic(): Egg = ???
  def crack(e: Egg): Cracked = ???

  // ExecutionContext decides how to schedule and on what thread
  val executionContext: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

  def eggF: Future[Egg] =
    Future {
      chickenMagic()
    }

  def eggF(chicken: Chicken): Future[Egg] =
    Future {
      chickenMagic()
    }

  val crackedEggF: Future[Cracked] = eggF.map(crack)(executionContext)

  crackedEggF.onComplete{
    case Success(crackedEgg) => ???
    case Failure(exception)  => ???
  }(executionContext)


  val list: immutable.Seq[Future[Egg]] = List(eggF, eggF.map(crack), eggF)
  val ret: Future[immutable.Seq[Egg]] = Future.sequence(list)

  for {
    e1 <- eggF(Chicken)
    e2 <- eggF(Chicken)
    e3 <- eggF(Chicken)
  } yield (e1, e2, e3)

}
