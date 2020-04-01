package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

@Singleton
class FeedController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def feed(category: Option[String]): Action[AnyContent] = Action {
    // TODO: pass category from query
    Ok(views.html.feed.apply("Лучшее", Vector.fill[String](3)("Опа")))
  }
}
