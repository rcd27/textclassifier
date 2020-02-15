class NaiveBayesClassifier(model: NaiveBayesModel) {

  def calculateProbability(`class`: String, s: String): Double = {
    // FIXME: опять разделение по пробелам, повторяющийся код
    val tokenize: String => Array[String] = _.split(' ')
    tokenize(`class`)
      .map(model.wordLogProbability(`class`, _))
      .sum
    +model.classLogProbability(`class`)
  }

  def classify(s: String): String = {
    model
      .classes
      .toList
      .map(c => (c, calculateProbability(c, s)))
      .maxBy(_._2)
      ._1
  }
}
