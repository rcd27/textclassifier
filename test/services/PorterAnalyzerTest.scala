package services

import org.specs2.mutable.Specification

import scala.collection.immutable.VectorBuilder

class PorterAnalyzerTest extends Specification {

  "PorterAnalyzerTest" should {
    "analyze" in {
      val testData = "тестовая строка"
      val result = PorterAnalyzer.tokenize(testData)
      val expected = new VectorBuilder[Term]()
        .addOne(Term(new Word("тестов"), 0, 8))
        .addOne(Term(new Word("строк"), 9, 15)).result()
      result should beEqualTo(expected)
    }
  }
}
