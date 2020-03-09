import org.apache.lucene.analysis.ru.RussianAnalyzer
import org.apache.lucene.analysis.tokenattributes.{CharTermAttribute, OffsetAttribute}

import scala.collection.mutable.ArrayBuffer

object PorterAnalyzer {

  val analyzer = new RussianAnalyzer()

  def tokenize(input: String): Vector[Term] = {

    val ts = analyzer.tokenStream("text", input)
    ts.reset()

    val out = new ArrayBuffer[Term]()

    while (ts.incrementToken()) {

      val word = ts.getAttribute(classOf[CharTermAttribute]).toString

      val offsets = ts.getAttribute(classOf[OffsetAttribute])

      out.addOne(Term(new Word(word), offsets.startOffset(), offsets.endOffset()))
    }
    ts.end()
    ts.close()

    out.toVector
  }
}