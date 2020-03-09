package reader;

import com.opencsv.bean.CsvBindByName;

// FIXME: переделать наскаловский класс
public class CsvDocument {

  @CsvBindByName(required = true)
  private String text;

  @CsvBindByName(required = true)
  private int category;

  public String getText() {
    return text;
  }

  public int getCategory() {
    return category;
  }
}
