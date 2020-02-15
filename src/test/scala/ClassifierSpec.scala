import org.specs2.mutable.Specification

class ClassifierSpec extends Specification {

  "Classifier " should {
    "be able to classify SPAM" in {
      val alg = new NaiveBayesLearningAlgorithm()
      alg.addExample("предоставляю услуги бухгалтера проститутки", "SPAM")
      alg.addExample("спешите купить виагру", "SPAM")
      alg.addExample("надо купить молоко", "HAM")

      val bestClass = alg.classifier.classify("спешите купить молоко")
      bestClass equals "SPAM" must beTrue
    }
    "be able to classify HAM" in {
      val alg = new NaiveBayesLearningAlgorithm()
      alg.addExample("предоставляю услуги бухгалтера проститутки", "SPAM")
      alg.addExample("спешите купить виагру", "SPAM")
      alg.addExample("надо прикупить молоко", "HAM")
      alg.addExample("надо держаться", "HAM")

      val bestClass = alg.classifier.classify("надо купить сигареты")
      bestClass equals "HAM" must beTrue
    }
  }
}
