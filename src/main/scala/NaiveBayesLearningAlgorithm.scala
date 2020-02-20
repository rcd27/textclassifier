/**
 * Learning algorithm
 */

object NaiveBayesLearningAlgorithm {

  /* Collection of examples, where pair is (Document, Classifier) */
  private var examples: Vector[(Document, DocClass)] = Vector.empty

  /* Words in document */
  private val tokenizeTuple: ((Document, DocClass)) => Array[Word] = (v: (Document, DocClass)) => tokenize(v._1.get)

  /* Number of words in document */
  private val calculateWords: Vector[(Document, DocClass)] => Int = (l: Vector[(Document, DocClass)]) => l.map(tokenizeTuple(_).length).sum

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

  def dictionary: Set[Word] = examples.map(tokenizeTuple).flatten.toSet

  def model: NaiveBayesModel = {
    val docsByClass = examples.groupBy(_._2)
    val lengths = docsByClass.view.mapValues(calculateWords).toMap
    val docCounts = docsByClass.view.mapValues(_.length).toMap
    val wordsCount = docsByClass.view
      .mapValues(
        _.map(tokenizeTuple)
          .flatten
          .groupBy(x => x)
          .view.mapValues(_.length).toMap
      ).toMap

    NaiveBayesModel(lengths, docCounts, wordsCount, dictionary.size)
  }

  def classifier = new NaiveBayesClassifier(model)
}
