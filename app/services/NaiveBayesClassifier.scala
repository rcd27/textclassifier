package services

import scala.collection.mutable.ArrayBuffer

class NaiveBayesClassifier(model: NaiveBayesModel) {

  def classify(inputRawText: String): DocClassification = {
    // FIXME: два раза tokenize, при чём из разных классов
    val tokenizedText: Vector[Term] = PorterAnalyzer.tokenize(inputRawText).get

    val docClass: DocClass =
      model.classes
        .map(c => (c, calculateProbability(c, tokenizedText)))
        .maxBy(_._2)(Ordering.Double.TotalOrdering)
        ._1

    val top3words: Seq[Term] = calculateTop3words(docClass, tokenizedText)
    val arr: ArrayBuffer[Char] = ArrayBuffer.empty
    inputRawText.toIndexedSeq
      .zipWithIndex
      .foreach { cd =>
        val (currentChar, currentId) = cd
        // search for current index in top3word
        val currentCharIsStart: Option[Term] = top3words.find(_.start == currentId)
        val currentCharIsEnd: Option[Term] = top3words.find(_.end == currentId + 1)

        // TODO: можно ли переделать на pattern matching?
        if (currentCharIsStart.nonEmpty) {
          arr.addOne('*')
          arr.addOne(currentChar)
        } else if (currentCharIsEnd.nonEmpty) {
          arr.addOne(currentChar)
          arr.addOne('*')
        } else { // чтобы это был дефолтный кейс "_"
          arr.addOne(currentChar)
        }
      }

    val result = arr.mkString

    new DocClassification(docClass, new HighlightedText(result))
  }

  /* services.Count a probability of document for a class */
  private def calculateProbability(`class`: DocClass, tokenizedText: Vector[Term]): Double = {
    val wordProbability = tokenizedText
      .map(term => model.wordLogProbability(`class`, term.word))
      .sum
    wordProbability + model.classLogProbability(`class`)
  }

  private def calculateTop3words(`class`: DocClass, tokenizedText: Vector[Term]): Vector[Term] = {
    tokenizedText
      .map(term => (term, model.wordLogProbability(`class`, term.word)))
      .sortBy(pair => pair._2)
      .take(3)
      .map(pair => pair._1)
  }
}
