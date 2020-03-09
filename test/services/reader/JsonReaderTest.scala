package services.reader

import org.specs2.mutable.Specification

class JsonReaderTest extends Specification {

  "JsonReaderTest" should {
    "read" in {
      val input =
        """{
          "text" : "раз раз",
          "category" :  -1
          }"""

      val expected = Some(JsonDocument("раз раз", -1))

      val actual = JsonReader.read(input)

      actual should be equalTo expected
    }
  }
}
