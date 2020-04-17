package services.feed

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import org.specs2.matcher.Matchers

class FeedSpec()
  extends TestKit(ActorSystem("MySpec"))
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "An Echo actor" must {

    "send back messages unchanged" in {
      val echo = system.actorOf(TestActors.echoActorProps)
      echo ! "hello world"
      expectMsg("hello world")
    }

  }

  "Feed actor" must {
    "get feed from Pikabu" in {
      // TODO CLA-10: implement

      //      val feed = system.actorOf(Props[FeedActor]())
      //      feed ! GetFeed
      //
      //      expectTerminated(feed)
    }
  }
}
