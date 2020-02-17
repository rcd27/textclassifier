import org.specs2.mutable.Specification

class NaiveBayesLearningAlgorithmTest extends Specification {

  "NaiveBayesLearningAlgorithmTest" should {
    "tokenize" in {

      val input = "Что, там такое?8( НичЁ не понимаю.."
      val expected = "что там такое ничё не понимаю".split(' ')

      val result = NaiveBayesLearningAlgorithm.tokenize(input)

      result should be equalTo expected
    }
  }
}
