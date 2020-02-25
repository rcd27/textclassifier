class NaiveBayesClassifier(model: NaiveBayesModel) {

  def classify(text: String): DocClass = {
    model
      .classes
      .map(c => (c, calculateProbability(c, text)))
      // FIXME: если поменять на minBy(_._2), то всё заработает
      .maxBy(_._2)(Ordering.Double.TotalOrdering)
      ._1
  }

  /* Count a probability of document for a class */
  private def calculateProbability(`class`: DocClass, text: String): Double = {
    // FIXME: два раза tokenize, при чём из разных классов
    PorterAnalyzer.tokenize(text)
      .map(model.wordLogProbability(`class`, _))
      .sum
    +model.classLogProbability(`class`)
  }
}
