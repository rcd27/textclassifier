class NaiveBayesClassifier(model: NaiveBayesModel) {

  def classify(text: String): DocClassification = {
    // FIXME: два раза tokenize, при чём из разных классов
    val tokenizedText: Vector[Term] = PorterAnalyzer.tokenize(text)

    val docClass: DocClass =
      model.classes
        .map(c => (c, calculateProbability(c, tokenizedText)))
        .maxBy(_._2)(Ordering.Double.TotalOrdering)
        ._1

    val wordsForCurrentClass: Seq[(Term, Double)] = wordProbability(docClass, tokenizedText)
    val top3words = wordsForCurrentClass
      .sortBy(pair => pair._2)
      .take(3)

    new DocClassification(docClass, new HighlightedText("*highlightedText*"))
  }

  /* Count a probability of document for a class */
  private def calculateProbability(`class`: DocClass, tokenizedText: Vector[Term]): Double = {
    val wordProbability = tokenizedText
      .map(term => model.wordLogProbability(`class`, term.word))
      .sum
    wordProbability + model.classLogProbability(`class`)
  }

  private def wordProbability(`class`: DocClass, tokenizedText: Vector[Term]): Vector[(Term, Double)] = {
    tokenizedText
      .map(term => (term, model.wordLogProbability(`class`, term.word)))
  }
}
