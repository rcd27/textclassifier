package services.reader

import java.io.FileReader

import com.opencsv.bean.CsvToBeanBuilder

import scala.jdk.CollectionConverters._

object CSVReader {
  def read(documentName: String): Vector[CsvDocument] = {
    new CsvToBeanBuilder[CsvDocument](new FileReader(documentName))
      .withType(classOf[CsvDocument])
      .build().parse().asScala.toVector
  }
}
