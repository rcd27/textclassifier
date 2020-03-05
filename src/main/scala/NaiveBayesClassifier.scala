import scala.collection.mutable.ArrayBuffer

class NaiveBayesClassifier(model: NaiveBayesModel) {

  def classify(inputRawText: String): DocClassification = {
    // FIXME: два раза tokenize, при чём из разных классов
    val tokenizedText: Vector[Term] = PorterAnalyzer.tokenize(inputRawText)

    val docClass: DocClass =
      model.classes
        .map(c => (c, calculateProbability(c, tokenizedText)))
        .maxBy(_._2)(Ordering.Double.TotalOrdering)
        ._1

    val wordsForCurrentClass: Seq[(Term, Double)] = wordProbability(docClass, tokenizedText)
    val top3words = wordsForCurrentClass
      .sortBy(pair => pair._2)
      .take(3)

    val arr: ArrayBuffer[Char] = ArrayBuffer.empty
    inputRawText.toIndexedSeq
      .zipWithIndex
      .foreach(cd => {
        val currentChar = cd._1
        val currentId = cd._2
        // search for current index in top3word
        val currentCharIsStart: Option[(Term, Double)] = top3words.find(td => td._1.start == currentId)
        val currentCharIsEnd: Option[(Term, Double)] = top3words.find(td => td._1.end == currentId + 1)

        // TODO: переделать на pattern matching
        if (currentCharIsStart.nonEmpty) {
          arr.addOne('*')
          arr.addOne(currentChar)
        } else if (currentCharIsEnd.nonEmpty) {
          arr.addOne(currentChar)
          arr.addOne('*')
        } else {
          arr.addOne(currentChar)
        }
      })

    val result = arr.mkString

    new DocClassification(docClass, new HighlightedText(result))
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
