package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import javax.inject.{Inject, Singleton}
import org.jsoup.select.Elements
import org.jsoup.{Jsoup, nodes}
import play.api.libs.ws.StandaloneWSRequest
import play.api.libs.ws.ahc.{AhcCurlRequestLogger, StandaloneAhcWSClient}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.reader.{CSVReader, CsvDocument}
import services.classification.{DocClass, DocClassification, Document, NaiveBayesLearningAlgorithm}

import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters._

@Singleton
class Application @Inject()(val cc: ControllerComponents,
                            implicit val ec: ExecutionContext,
                            implicit val ac: ActorSystem) extends AbstractController(cc) {

  def menu: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  // CLASSIFICATOR
  // TODO: inject
  val toDocumentTuple: CsvDocument => (Document, DocClass) = csv => (new Document(csv.getText), new DocClass(csv.getCategory.toString))
  val negatives: Vector[(Document, DocClass)] = CSVReader.read("negative.csv").map(toDocumentTuple)
  val positives: Vector[(Document, DocClass)] = CSVReader.read("positive.csv").map(toDocumentTuple)
  val alg = new NaiveBayesLearningAlgorithm(negatives ++ positives)
  /////

  def classify(inputText: Option[String]): Action[AnyContent] = Action {
    val classificationResult: DocClassification = alg.classifier.classify(inputText.getOrElse("")) //FIXME: будет пытаться классифицировать пустой текст
    val docClass: Some[String] = Some(classificationResult.docClass.pretty())
    val highlightedText: Some[String] = Some(classificationResult.highlightedText.get)

    // TODO: забиндить действие на странице и не обновлять вьюху целиком
    if (classificationResult.classificationAccuracy >= 0.7) {
      Ok(views.html.classifier(inputText, docClass, highlightedText))
    } else {
      Ok(views.html.classifier(inputText, Some("Нейтральный"), highlightedText))
    }
  }

  // FEED
  implicit val materializer: Materializer = akka.stream.Materializer.matFromSystem
  private val wsClient: StandaloneAhcWSClient = StandaloneAhcWSClient()
  private val superToken: Long = System.currentTimeMillis()
  private val request: StandaloneWSRequest =
    wsClient.url(s"http://pikabu.ru/best")
      .withRequestFilter(AhcCurlRequestLogger())
  private val complexRequest = request
    .addQueryStringParameters("of" -> "v2")
    .addQueryStringParameters("page" -> "1")
    .addQueryStringParameters("_" -> s"$superToken")

  private val eventualResponse: Future[complexRequest.Response] = complexRequest.get()

  def feed(category: Option[String]): Action[AnyContent] = Action.async {
    eventualResponse.map { response =>
      val body: String = response.body[String]

      val document: nodes.Document = Jsoup.parse(body)
      val storyBlocks: Elements = document.select("div.story-block")
      val posts: Vector[String] = storyBlocks.eachText().asScala.toVector

      val filteredPosts = posts.filter(_.split(" ").length > 4)
      Ok(views.html.feed.apply("Лучшее", filteredPosts))
    }
  }
}
