class NaiveBayesClassifier(model: NaiveBayesModel) {

  def classify(text: String): DocClass = {
    model
      .classes
      .map(c => (c, calculateProbability(c, text)))
      .maxBy(_._2)(Ordering.Double.TotalOrdering)
      ._1
  }

  /* Count a probability of document for a class */
  private def calculateProbability(`class`: DocClass, text: String): Double = {
    // FIXME: два раза tokenize, при чём из разных классов
    val wordProbability = PorterAnalyzer.tokenize(text)
      .map(term => model.wordLogProbability(`class`, term.word))
      .sum
    wordProbability + model.classLogProbability(`class`)
  }
}
