import org.apache.lucene.analysis.ru.RussianAnalyzer
import org.apache.lucene.analysis.tokenattributes.{CharTermAttribute, OffsetAttribute}

import scala.collection.mutable.ArrayBuffer

object PorterAnalyzer {

  val analyzer = new RussianAnalyzer()

  def analyze(input: String): Vector[Term] = {

    val ts = analyzer.tokenStream("text", input)
    ts.reset()

    val out = new ArrayBuffer[Term]()

    while (ts.incrementToken()) {

      val word = ts.getAttribute(classOf[CharTermAttribute]).toString

      val offsets = ts.getAttribute(classOf[OffsetAttribute])

      out.addOne(Term(word, offsets.startOffset(), offsets.endOffset()))
    }

    out.toVector
  }
}

case class Term(word: String, start: Int, end: Int)
