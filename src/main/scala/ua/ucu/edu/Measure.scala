package ua.ucu.edu

object Measure extends App {
  import org.scalameter._

  val time = measure {
    (0 until 1000000).toArray // returns a double (milli seconds)
  }
  println(s"Array initialization time: $time ms")
}
