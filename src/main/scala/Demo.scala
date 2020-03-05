import akka.actor.ActorSystem
import org.jsoup.select.Elements
import org.jsoup.{Jsoup, nodes}
import play.api.libs.ws.DefaultBodyReadables._
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.ExecutionContext.Implicits._
import scala.jdk.CollectionConverters._

object Demo extends App {
  implicit val actorSystem: ActorSystem = ActorSystem()

  val wsClient: StandaloneAhcWSClient = StandaloneAhcWSClient()
  wsClient.url("https://pikabu.ru/best").get().map { response =>
    val body: String = response.body[String]

    val document: nodes.Document = Jsoup.parse(body)
    val storyBlocks: Elements = document.select("div.story-block")
    val posts: Vector[String] = storyBlocks.eachText().asScala.toVector

    posts.filter(_.split(" ").length > 4) foreach (println(_))
  }
}
