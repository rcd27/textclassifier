package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.reader.{CSVReader, CsvDocument}
import services.{DocClass, DocClassification, Document, NaiveBayesLearningAlgorithm}

@Singleton
class ClassifierController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  // TODO: inject to constructor
  val toDocumentTuple: CsvDocument => (Document, DocClass) = csv => (new Document(csv.getText), new DocClass(csv.getCategory.toString))
  val negatives: Vector[(Document, DocClass)] = CSVReader.read("negative.csv").map(toDocumentTuple)
  val positives: Vector[(Document, DocClass)] = CSVReader.read("positive.csv").map(toDocumentTuple)

  val alg = new NaiveBayesLearningAlgorithm(negatives ++ positives)

  val tuple: (Document, DocClass) = negatives.apply(40)
  val expectedClass: String = tuple._2.get
  assert(expectedClass == (-1).toString)

  val docClassification: DocClassification = alg.classifier.classify(tuple._1.text)
  val actualClass: String = docClassification.docClass.get

  def index(inputText: String): Action[AnyContent] = Action {
    val classificationResult = alg.classifier.classify(inputText)
    val docClass = Some(classificationResult.docClass.get)
    val highlightedText = Some(classificationResult.highlightedText.get)
    Ok(views.html.classifier(Some(inputText), docClass, highlightedText))
  }

//  def classify(inputText: Option[String]): Action[AnyContent] = Action {
//    val classificationResult = alg.classifier.classify(inputText.getOrElse(""))
//    val docClass = Some(classificationResult.docClass.get)
//    val highlightedText = Some(classificationResult.highlightedText.get)
//    Ok(views.html.classifier(inputText, docClass, highlightedText))
//  }
}
