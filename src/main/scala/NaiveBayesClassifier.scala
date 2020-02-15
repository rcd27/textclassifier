class NaiveBayesClassifier(model: NaiveBayesModel) {

  def classify(s: String): String = {
    model
      .classes
      .toList
      .map(c => (c, calculateProbability(c, s)))
      .maxBy(_._2)(Ordering.Double.TotalOrdering)
      ._1
  }

  /* Count a probability of document for a class */
  private def calculateProbability(c: String, s: String): Double = {
    // FIXME: опять разделение по пробелам, повторяющийся код
    val tokenize: String => Array[String] = _.split(' ')

    tokenize(s)
      .map(model.wordLogProbability(c, _))
      .sum
    +model.classLogProbability(c)
  }
}
