import org.specs2.mutable.Specification

class ClassifierSpec extends Specification {


  "Classifier " should {
    "be able to classify" in {
      val alg = new NaiveBayesLearningAlgorithm()
      alg.addExample("предоставляю услуги бухгалтера", "SPAM")
      alg.addExample("спешите купить виагру", "SPAM")
      alg.addExample("надо купить молоко", "HAM")

      val bestClass = alg.classifier.classify("надо купить сигареты")
      bestClass equals "HAM" must beTrue
    }
  }
}
