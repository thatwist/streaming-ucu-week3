package ua.ucu.edu

import akka.actor.{Actor, ActorRef}
import akka.routing.{DefaultResizer, RoundRobinPool, RoundRobinRoutingLogic, Router}

object Actors {

  // Message Protocol
  case class Greeting(who: String)
  case class Respond(gr: Greeting)

  class GreetingActor extends Actor {
    // Put mutable state in here..

    def receive: Receive = {
      // Define the behavior here..
      case Greeting(who) => println(s"Hello $who")
    }
  }

  import akka.actor.ActorSystem
  import akka.actor.Props

  val system: ActorSystem = ActorSystem.create("MySystem")
  val greeter: ActorRef = system.actorOf(Props[Greeting], "greeter")

  greeter ! ""

  import akka.actor.Props

  val actorRef = ???

}
