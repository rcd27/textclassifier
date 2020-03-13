package services

import akka.actor.ActorSystem
import org.jsoup.select.Elements
import org.jsoup.{Jsoup, nodes}
import play.api.libs.ws.StandaloneWSRequest
import play.api.libs.ws.ahc.{AhcCurlRequestLogger, StandaloneAhcWSClient}
import scala.jdk.CollectionConverters._
import scala.concurrent.ExecutionContext.Implicits.global

object Hackabu extends App {
  implicit val actorSystem: ActorSystem = ActorSystem()

  val wsClient: StandaloneAhcWSClient = StandaloneAhcWSClient()
  val superToken: Long = System.currentTimeMillis()
  val request: StandaloneWSRequest =
    wsClient.url(s"http://pikabu.ru/best")
      .withRequestFilter(AhcCurlRequestLogger())
  val complexRequest = request
    .addHttpHeaders("Accept" -> "application/json, text/javascript, */*; q=0.01")
    .addHttpHeaders("x-csrf-token" -> "2ei8t5vd7qg3f28nms7c1nkcfsotvlug")
    .addHttpHeaders("referer" -> "pikabu.ru")
    .addHttpHeaders("x-requested-with" -> "XMLHttpRequest")
    .addHttpHeaders("dnt" -> "1")
    .addQueryStringParameters("twitmode" -> "1")
    .addQueryStringParameters("of" -> "v2")
    .addQueryStringParameters("page" -> "1")
    .addQueryStringParameters("_" -> s"$superToken")
  complexRequest.get().map { response =>
    val body: String = response.body[String]

    val document: nodes.Document = Jsoup.parse(body)
    val storyBlocks: Elements = document.select("div.story-block")
    val posts: Vector[String] = storyBlocks.eachText().asScala.toVector

    posts.filter(_.split(" ").length > 4) foreach (println(_))
  }
}
