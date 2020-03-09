package reader

import play.api.libs.json._

object JsonReader {

  def read(input: String): Option[JsonDocument] = {
    implicit val docReads: Reads[JsonDocument] = Json.reads[JsonDocument]

    val docFromJson: JsResult[JsonDocument] = Json.fromJson[JsonDocument](Json.parse(input))

    docFromJson match {
      case JsSuccess(value, _) => Some(value)
      case JsError(_) => None
    }
  }
}
