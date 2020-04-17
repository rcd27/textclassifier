package services.classification

/**
 * Learning algorithm
 */

class NaiveBayesLearningAlgorithm(input: Vector[(Document, DocClass)]) {
  /* Words in text */
  def tokenizeTuple(token: (Document, DocClass)): Vector[Word] = {
    PorterAnalyzer.tokenize(token._1.text).get.map(_.word)
  }

  /* Number of words in document */
  def calculateWords(input: Vector[(Document, DocClass)]): Int = {
    input.map(tokenizeTuple(_).length).sum
  }

  def dictionary: Set[Word] = input.flatMap(tokenizeTuple).toSet

  def model: NaiveBayesModel = {
    val docsByClass = input.groupBy(_._2)
    val lengths = docsByClass.view.mapValues(calculateWords).toMap
    val docCounts = docsByClass.view.mapValues(_.length).toMap
    val wordsCount = docsByClass.view
      .mapValues(
        _.flatMap(tokenizeTuple)
          .groupBy(identity)
          .view.mapValues(_.length).toMap
      ).toMap

    NaiveBayesModel(lengths, docCounts, wordsCount, dictionary.size)
  }

  def classifier = new NaiveBayesClassifier(model)
}