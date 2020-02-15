/**
 * Learning algorithm
 */

class NaiveBayesLearningAlgorithm {

  /* Collection of examples, where pair is (Document, Classifier) */
  private var examples: Vector[(String, String)] = Vector.empty

  // FIXME: разделение документа по пробелам
  private val tokenize = (v: String) => v.split(' ')
  /* Words in document */
  private val tokenizeTuple = (v: (String, String)) => tokenize(v._1)
  /* Number of words in document */
  private val calculateWords = (l: Vector[(String, String)]) => l.map(tokenizeTuple(_).length).sum

  def addExample(ex: String, `class`: String): Unit = {
    examples = (ex, `class`) +: examples
  }

  def dictionary: Set[String] = examples.map(tokenizeTuple).flatten.toSet

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
