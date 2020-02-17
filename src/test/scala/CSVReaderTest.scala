import java.util

import org.specs2.mutable.Specification

class CSVReaderTest extends Specification {

  "CSVReaderTest" should {
    "read" in {
      val result: util.List[Document] = new CSVReader().read("positive.csv")
      // TODO: проверить поля каждого элемента, что они не пустые
      result should not be empty
    }
  }
}
