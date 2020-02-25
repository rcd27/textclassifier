import org.specs2.mutable.Specification

import scala.collection.immutable.VectorBuilder

class PorterAnalyzerTest extends Specification {

  "PorterAnalyzerTest" should {
    "analyze" in {
      val testData = "тестовая строка"
      val result = PorterAnalyzer.tokenize(testData)
      val expected = new VectorBuilder[Term]()
        .addOne(Term("тестов", 0, 8))
        .addOne(Term("строк", 9, 15)).result()
      result should beEqualTo(expected)
    }
  }
}
