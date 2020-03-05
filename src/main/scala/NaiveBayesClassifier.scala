class NaiveBayesClassifier(model: NaiveBayesModel) {

  def classify(text: String): DocClassification = {
    val docClass: DocClass =
      model.classes
        .map(c => (c, calculateProbability(c, text)))
        .maxBy(_._2)(Ordering.Double.TotalOrdering)
        ._1
    val highlightedText: HighlightedText = new HighlightedText("some *words* highlighted")
    new DocClassification(docClass, highlightedText)
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
