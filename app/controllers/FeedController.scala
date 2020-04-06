package controllers

import akka.actor.ActorSystem
import javax.inject.{Inject, Singleton}
import org.jsoup.select.Elements
import org.jsoup.{Jsoup, nodes}
import play.api.libs.ws.StandaloneWSRequest
import play.api.libs.ws.ahc.{AhcCurlRequestLogger, StandaloneAhcWSClient}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.CollectionConverters._

@Singleton
class FeedController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val actorSystem: ActorSystem = ActorSystem()

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
