package ua.ucu.edu.hidden

import scala.collection.parallel.{Combiner, SeqSplitter, immutable}
import ua.ucu.edu._

object Par2 {


  class ParString(val str: String)
    extends immutable.ParSeq[Char] {
    def apply(i: Int): Char = str.charAt(i)

    def length: Int = str.length

    def seq = new collection.immutable.WrappedString(str)

    def splitter = new ParStringSplitter(str, 0, str.length)

    override def newCombiner = new ParStringCombiner

  }


  class ParStringSplitter(
    private val s: String,
    private var i: Int,
    private val limit: Int
  ) extends SeqSplitter[Char] {
    final def hasNext: Boolean = i < limit
    final def next: Char = {
      val r = s.charAt(i)
      i += 1
      r
    }
    def remaining: Int = limit - i
    def dup = new ParStringSplitter(s, i, limit)
    def split: Seq[ParStringSplitter] = {
      val rem = remaining
      if (rem >= 2) psplit(rem / 2, rem - rem / 2)
      else Seq(this)
    }
    def psplit(sizes: Int*): Seq[ParStringSplitter] = {
      val ss = for (sz <- sizes) yield {
        val nlimit = (i + sz) min limit
        val ps = new ParStringSplitter(s, i, nlimit)
        i = nlimit
        ps
      }
      if (i == limit) ss else ss :+ new ParStringSplitter(s, i, limit)
    }
  }


  class ParStringCombiner extends Combiner[Char, ParString] {
    import scala.collection.mutable.ArrayBuffer
    private var sz = 0
    private val chunks = new ArrayBuffer += new StringBuilder
    private var lastc = chunks.last

    def size: Int = sz

    def +=(elem: Char): this.type = {
      lastc += elem
      sz += 1
      this
    }

    def clear(): Unit = {
      chunks.clear
      chunks += new StringBuilder
      lastc = chunks.last
      sz = 0
    }

    def result: ParString = {
      val rsb = new StringBuilder
      for (sb <- chunks) rsb.append(sb)
      new ParString(rsb.toString)
    }

    def combine[U <: Char, NewTo >: ParString](that: Combiner[U, NewTo]): ParStringCombiner =
      if (that eq this) this else that match {
        case that: ParStringCombiner =>
          sz += that.sz
          chunks ++= that.chunks
          lastc = chunks.last
          this
      }
  }


  object CustomCharCount extends App {
    val txt = "A custom text " * 250000
    val partxt = new ParString(txt)

    val seqtime = warmedTimed(50) {
      txt.foldLeft(0)((n, c) => if (Character.isUpperCase(c)) n + 1 else n)
    }

    println(s"Sequential time - $seqtime ms")

    val partime = warmedTimed(50) {
      partxt.aggregate(0)((n, c) => if (Character.isUpperCase(c)) n + 1 else n, _ + _)
    }

    println(s"Parallel time   - $partime ms")

  }

  object CustomCharFilter extends App {
    val txt = "A custom txt" * 25000
    val partxt = new ParString(txt)

    val seqtime = warmedTimed(250) {
      txt.filter(_ != ' ')
    }

    println(s"Sequential time - $seqtime ms")

    val partime = warmedTimed(250) {
      partxt.filter(_ != ' ')
    }

    println(s"Parallel time   - $partime ms")

  }
}
