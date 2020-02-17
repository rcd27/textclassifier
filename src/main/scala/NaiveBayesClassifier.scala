class NaiveBayesClassifier(model: NaiveBayesModel) {

  def classify(text: String): String = {
    model
      .classes
      .toList
      .map(c => (c, calculateProbability(c, text)))
      .maxBy(_._2)(Ordering.Double.TotalOrdering)
      ._1
  }

  /* Count a probability of document for a class */
  private def calculateProbability(`class`: String, text: String): Double = {
    // FIXME: опять разделение по пробелам, повторяющийся код
    val tokenize: String => Array[String] = _.split(' ')

    tokenize(text)
      .map(model.wordLogProbability(`class`, _))
      .sum
    +model.classLogProbability(`class`)
  }
}
