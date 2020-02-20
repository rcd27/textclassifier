/**
 * Learning algorithm
 */

object NaiveBayesLearningAlgorithm {

  /* Collection of examples, where pair is (Document, Classifier) */
  private var examples: Vector[(Document, DocClass)] = Vector.empty

  def addExample(ex: Document, `class`: DocClass): Unit = {
    examples = (ex, `class`) +: examples
  }

  // TODO: вынести в отдельный класс, отвечающий за обработку текста
  def tokenize(inputText: String): Array[Word] = {
    inputText.split(' ')
      .view
      .map(s =>
        // TODO: implement implicit String => Word
        new Word(s.to(LazyList)
          .filter(c => c.isValidChar && c.isLetter)
          .map(_.toLower)
          .mkString)
      )
      .toArray
  }

  /* Words in text */
  def tokenizeTuple(token: (Document, DocClass)): Array[Word] = {
    tokenize(token._1.text)
  }

  /* Number of words in document */
  def calculateWords(input: Vector[(Document, DocClass)]): Int = {
    input.map(tokenizeTuple(_).length).sum
  }

  def dictionary: Set[Word] = examples.flatMap(tokenizeTuple).toSet

  def model: NaiveBayesModel = {
    val docsByClass = examples.groupBy(_._2)
    val lengths = docsByClass.view.mapValues(calculateWords).toMap
    val docCounts = docsByClass.view.mapValues(_.length).toMap
    val wordsCount = docsByClass.view
      .mapValues(
        _.flatMap(tokenizeTuple)
          .groupBy(x => x)
          .view.mapValues(_.length).toMap
      ).toMap

    NaiveBayesModel(lengths, docCounts, wordsCount, dictionary.size)
  }

  def classifier = new NaiveBayesClassifier(model)
}
