import org.specs2.mutable.Specification
import reader.{CSVReader, CsvDocument}

class NaiveBayesClassifierTest extends Specification {

  "NaiveBayesClassifier " should {
    val toDocumentTuple: CsvDocument => (Document, DocClass) = csv => (new Document(csv.getText), new DocClass(csv.getCategory.toString))
    val negatives = CSVReader.read("negative.csv").map(toDocumentTuple)
    val positives = CSVReader.read("positive.csv").map(toDocumentTuple)

    val alg = new NaiveBayesLearningAlgorithm(negatives ++ positives)

    val tuple = negatives.apply(40)
    val expectedClass: String = tuple._2.get
    assert(expectedClass == (-1).toString)

    val docClassification: DocClassification = alg.classifier.classify(tuple._1.text)
    val actualClass: String = docClassification.docClass.get

    "be able to classify negative text" in {
      actualClass.shouldEqual(expectedClass)
    }

    "return highlighted text" in {
      docClassification.highlightedText.get shouldEqual "*highlightedText*"
    }
  }
}