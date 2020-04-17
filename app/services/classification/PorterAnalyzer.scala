package services.classification

import org.apache.lucene.analysis.ru.RussianAnalyzer
import org.apache.lucene.analysis.tokenattributes.{CharTermAttribute, OffsetAttribute}

import scala.collection.mutable.ArrayBuffer
import scala.util.{Try, Using}

object PorterAnalyzer {

  val analyzer = new RussianAnalyzer()

  def tokenize(input: String): Try[Vector[Term]] = {
    Using(analyzer.tokenStream("text", input)) { ts =>
      val out = new ArrayBuffer[Term]()
      ts.reset()
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
}
