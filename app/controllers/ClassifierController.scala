package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.reader.{CSVReader, CsvDocument}
import services.{DocClass, Document, NaiveBayesLearningAlgorithm}

@Singleton
class ClassifierController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  // TODO: inject
  val toDocumentTuple: CsvDocument => (Document, DocClass) = csv => (new Document(csv.getText), new DocClass(csv.getCategory.toString))
  val negatives: Vector[(Document, DocClass)] = CSVReader.read("negative.csv").map(toDocumentTuple)
  val positives: Vector[(Document, DocClass)] = CSVReader.read("positive.csv").map(toDocumentTuple)
  val alg = new NaiveBayesLearningAlgorithm(negatives ++ positives)
  /////

  def index(inputText: Option[String]): Action[AnyContent] = Action {
    val classificationResult = alg.classifier.classify(inputText.getOrElse("")) //FIXME: будет пытаться классифицировать пустой текст
    val docClass = Some(classificationResult.docClass.pretty())
    val highlightedText = Some(classificationResult.highlightedText.get)
    Ok(views.html.classifier(inputText, docClass, highlightedText))
  }
}
