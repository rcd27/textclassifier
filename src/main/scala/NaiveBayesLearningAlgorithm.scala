import NaiveBayesLearningAlgorithm.tokenize

/**
 * Learning algorithm
 */

class NaiveBayesLearningAlgorithm(input: Vector[(Document, DocClass)]) {
  /* Words in text */
  def tokenizeTuple(token: (Document, DocClass)): Array[Word] = {
    tokenize(token._1.text)
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

object NaiveBayesLearningAlgorithm {
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
}
