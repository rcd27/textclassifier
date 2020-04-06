package services

import java.lang.Math.{E, pow}

import scala.collection.immutable.SortedSet
import scala.collection.mutable.ArrayBuffer

class NaiveBayesClassifier(model: NaiveBayesModel) {

  def classify(inputRawText: String): DocClassification = {
    // FIXME: два раза tokenize, при чём из разных классов
    val tokenizedText: Vector[Term] = PorterAnalyzer.tokenize(inputRawText).get

    val docClassWithProbability: Set[(DocClass, Double)] = model.classes
      .map(c => (c, calculateProbability(c, tokenizedText)))

    val sortedProbabilities =
      SortedSet.from(
        docClassWithProbability
          .map { f: (DocClass, Double) => f._2 })((x: Double, y: Double) => -java.lang.Double.compare(x, y)) // revert sorting
    val head = sortedProbabilities.head
    val tail = sortedProbabilities.tail

    val tailPowSum: Double = tail.map {
      pow(E, _)
    }.sum
    val headPow = pow(E, head)
    val classificationAccuracy = headPow / (headPow + tailPowSum)

    val mostLikelyClass: DocClass =
      docClassWithProbability.maxBy(_._2)(Ordering.Double.TotalOrdering)._1

    val top3words: Seq[Term] = calculateTop3words(mostLikelyClass, tokenizedText)
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

    new DocClassification(mostLikelyClass, classificationAccuracy, new HighlightedText(result))
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
      .sortBy(pair => pair._2)(Ordering.Double.TotalOrdering)
      .take(3)
      .map(pair => pair._1)
  }
}
