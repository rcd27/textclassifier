import java.util.function.Consumer

import org.specs2.mutable.Specification

class ClassifierSpec extends Specification {

  "Classifier " should {
    "be able to classify SPAM" in {
      val alg = NaiveBayesLearningAlgorithm
      alg.addExample("предоставляю услуги бухгалтера проститутки", "SPAM")
      alg.addExample("спешите купить виагру", "SPAM")
      alg.addExample("надо купить молоко", "HAM")

      val bestClass = alg.classifier.classify("спешите купить молоко")
      bestClass equals "SPAM" must beTrue
    }
    "be able to classify HAM" in {
      val alg = NaiveBayesLearningAlgorithm
      alg.addExample("предоставляю услуги бухгалтера проститутки", "SPAM")
      alg.addExample("спешите купить виагру", "SPAM")
      alg.addExample("надо прикупить молоко", "HAM")
      alg.addExample("надо держаться", "HAM")

      val bestClass = alg.classifier.classify("надо купить сигареты")
      bestClass equals "HAM" must beTrue
    }
    "be able to classify negative text" in {
      val alg = NaiveBayesLearningAlgorithm

      val addDocToAlg: Consumer[CsvDocument] = { doc => alg.addExample(doc.getText, doc.getCategory.toString) }

      val negatives = CSVReader.read("negative.csv")
      negatives.forEach(addDocToAlg)

      CSVReader.read("positive.csv").forEach(addDocToAlg)

      // FIXME: это стопроцентно должно быть "-1", возможно дело в чистке от всяких ненужных знаков
      val bestClass = alg.classifier.classify(negatives.get(40).getText)
      bestClass equals "-1" must beTrue
    }
  }
}
