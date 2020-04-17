package services.feed

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.stream.Materializer
import javax.inject.Singleton
import org.jsoup.select.Elements
import org.jsoup.{Jsoup, nodes}
import play.api.libs.ws.StandaloneWSRequest
import play.api.libs.ws.ahc.{AhcCurlRequestLogger, StandaloneAhcWSClient}

import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.util.Try

final case class GetFeed(replyTo: ActorRef[FeedReceived])

final case class FeedReceived(data: Vector[String], from: ActorRef[GetFeed])

@Singleton
class FeedActor()(
  implicit val ec: ExecutionContext,
  implicit val as: ActorSystem) {


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

  def apply(): Behavior[GetFeed] = Behaviors.receive { (context, message) =>
    context.log.info("GetFeed")
    val response: Option[Try[complexRequest.Response]] = eventualResponse.value
    if (response.nonEmpty) {
      val value: Try[complexRequest.Response] = response.get
      if (value.isSuccess) {
        val data = value.get
        val body: String = data.body[String]

        val document: nodes.Document = Jsoup.parse(body)
        val storyBlocks: Elements = document.select("div.story-block")
        val posts: Vector[String] = storyBlocks.eachText().asScala.toVector

        val filteredPosts: Vector[String] = posts.filter(_.split(" ").length > 4)
        message.replyTo ! FeedReceived(filteredPosts, context.self)
      }
    } else {
      context.log.info("GetFeed FAILED")

      // FIXME CLA-10: переделать актор на работу с фьючей

      //      eventualResponse.map { response =>
      //      val body: String = response.body[String]
      //
      //      val document: nodes.Document = Jsoup.parse(body)
      //      val storyBlocks: Elements = document.select("div.story-block")
      //      val posts: Vector[String] = storyBlocks.eachText().asScala.toVector
      //
      //      val filteredPosts: Vector[String] = posts.filter(_.split(" ").length > 4)
      //      message.replyTo ! FeedReceived(filteredPosts, context.self)
      //
    }
    Behaviors.same
  }
}
