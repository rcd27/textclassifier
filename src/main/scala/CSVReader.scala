import java.io.FileReader
import java.util

import com.opencsv.bean.CsvToBeanBuilder

// FIXME: вернуть Vector[Document]
object CSVReader extends Reader[String, util.List[CsvDocument]] {
  override def read(documentName: String): util.List[CsvDocument] = {
    new CsvToBeanBuilder[CsvDocument](new FileReader(documentName))
      .withType(classOf[CsvDocument])
      .build().parse()
  }
}
