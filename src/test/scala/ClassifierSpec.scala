import java.util.function.Consumer

import org.specs2.mutable.Specification
import reader.{CSVReader, CsvDocument}

class ClassifierSpec extends Specification {

  "Classifier " should {
    "be able to classify negative text" in {
      val alg = NaiveBayesLearningAlgorithm

      val addDocToAlg: Consumer[CsvDocument] = { doc => alg.addExample(doc.getText, doc.getCategory.toString) }

      val negatives = CSVReader.read("negative.csv")
      negatives.forEach(addDocToAlg)

      CSVReader.read("positive.csv").forEach(addDocToAlg)

      val document = negatives.get(40)
      // FIXME: это стопроцентно должно быть "-1"
      val bestClass = alg.classifier.classify(document.getText)
      assert(document.getCategory == -1)
      bestClass equals "-1" must beTrue
    }
  }
}
