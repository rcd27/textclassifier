import java.io.FileReader
import java.util

import com.opencsv.bean.CsvToBeanBuilder

// FIXME: вернуть Vector[Document]
class CSVReader extends Reader[String, util.List[Document]] {
  override def read(documentName: String): util.List[Document] = {
    new CsvToBeanBuilder[Document](new FileReader(documentName))
      .withType(classOf[Document])
      .build().parse()
  }
}
