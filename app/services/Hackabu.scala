package services

import akka.actor.ActorSystem
import org.jsoup.select.Elements
import org.jsoup.{Jsoup, nodes}
import play.api.libs.ws.StandaloneWSRequest
import play.api.libs.ws.ahc.{AhcCurlRequestLogger, StandaloneAhcWSClient}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.jdk.CollectionConverters._

object Hackabu extends App {
  implicit val actorSystem: ActorSystem = ActorSystem()

  val wsClient: StandaloneAhcWSClient = StandaloneAhcWSClient()
  val superToken: Long = System.currentTimeMillis()
  val request: StandaloneWSRequest =
    wsClient.url(s"http://pikabu.ru/best")
      .withRequestFilter(AhcCurlRequestLogger())
  val complexRequest = request
    .addQueryStringParameters("of" -> "v2")
    .addQueryStringParameters("page" -> "1")
    .addQueryStringParameters("_" -> s"$superToken")
  complexRequest.get().map { response =>
    val body: String = response.body[String]

    val document: nodes.Document = Jsoup.parse(body)
    val storyBlocks: Elements = document.select("div.story-block")
    val posts: Vector[String] = storyBlocks.eachText().asScala.toVector

    posts.filter(_.split(" ").length > 4) foreach (println(_))
  }.onComplete { _ =>
    System.exit(0)
  }
}
