import org.specs2.mutable.Specification
import reader.{CSVReader, CsvDocument}

class NaiveBayesClassifierTest extends Specification {

  "NaiveBayesClassifier " should {
    "be able to classify negative text" in {
      val toDocumentTuple: CsvDocument => (Document, DocClass) = csv => (new Document(csv.getText), new DocClass(csv.getCategory.toString))
      val negatives = CSVReader.read("negative.csv").map(toDocumentTuple)
      val positives = CSVReader.read("positive.csv").map(toDocumentTuple)

      val alg = new NaiveBayesLearningAlgorithm(negatives ++ positives)

      val tuple = negatives.apply(40)
      val expected: String = tuple._2.get
      assert(expected == (-1).toString)

      val bestClass: DocClassification = alg.classifier.classify(tuple._1.text)
      val actual: String = bestClass.docClass.get

      actual.shouldEqual(expected)
    }
  }
}