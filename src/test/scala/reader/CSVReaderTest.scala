package reader

import org.specs2.mutable.Specification

class CSVReaderTest extends Specification {

  "CSVReaderTest" should {
    "read" in {
      val result: Vector[CsvDocument] = CSVReader.read("positive.csv")
      // TODO: проверить поля каждого элемента, что они не пустые
      result should not be empty
    }
  }
}
