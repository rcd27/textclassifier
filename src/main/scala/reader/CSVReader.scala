package reader

import java.io.FileReader
import java.util

import com.opencsv.bean.CsvToBeanBuilder

// FIXME: вернуть Vector[Document]
object CSVReader {
  def read(documentName: String): util.List[CsvDocument] = {
    new CsvToBeanBuilder[CsvDocument](new FileReader(documentName))
      .withType(classOf[CsvDocument])
      .build().parse()
  }
}
