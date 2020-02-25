import scala.math.log

/**
 * Classifier model which includes all statistics which is needed for classification
 *
 * @param lengths        sum of docs(their words) lengths and there class
 * @param docCount       sum of docs according to class
 * @param wordsCount     word repeat stats in certain class
 * @param dictionarySize size of the dictionary of the learning algorithm
 */
case class NaiveBayesModel(lengths: Map[DocClass, Int],
                           docCount: Map[DocClass, Int],
                           wordsCount: Map[DocClass, Map[Word, Int]],
                           dictionarySize: Int) {

  /* log of probability to find a word in a certain class */
  def wordLogProbability(`class`: DocClass, word: Word): Double = {
    val wordsCountForCurrentClass = wordsCount.getOrElse(`class`, Map.empty)
    val currentWordCountInCurrentClass = wordsCountForCurrentClass
      .getOrElse(word, 0) + 1.0
    val i = lengths(`class`)
    log(currentWordCountInCurrentClass / (i.toDouble + dictionarySize))
  }

  /* log of probability of a class */
  def classLogProbability(`class`: DocClass): Double = log(docCount(`class`).toDouble / docCount.values.sum)

  /* A Set of all existent classes */
  def classes: Set[DocClass] = docCount.keySet
}

class Word(val get: String) extends AnyVal

class Length(val get: Int) extends AnyVal

class DocClass(val get: String) extends AnyVal

class Count(val get: Int) extends AnyVal

class Document(val text: String) extends AnyVal

case class Term(word: Word, start: Int, end: Int)
