import org.specs2.mutable.Specification
import reader.{CSVReader, CsvDocument}

import scala.jdk.CollectionConverters._

class ClassifierSpec extends Specification {

  "Classifier " should {
    "be able to classify negative text" in {
      val toDocumentTuple: CsvDocument => (Document, DocClass) = csv => (new Document(csv.getText), new DocClass(csv.getCategory.toString))
      val negatives = CSVReader.read("negative.csv").asScala.toVector.map(toDocumentTuple)
      val positives = CSVReader.read("positive.csv").asScala.toVector.map(toDocumentTuple)

      val alg = new NaiveBayesLearningAlgorithm(negatives ++ positives)

      val tuple = negatives.apply(40)
      // FIXME: это стопроцентно должно быть "-1"
      val bestClass = alg.classifier.classify(tuple._1.text)
      assert(tuple._2.get == (-1).toString)
      bestClass equals "-1" must beTrue
    }
  }
}