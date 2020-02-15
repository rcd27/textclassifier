import scala.collection.MapView
import scala.math.log

/**
 * Classifier model which includes all statistics which is needed for classification
 *
 * @param lengths        sum of docs(there words) lengths and there class
 * @param docCount       sum of docs according to class
 * @param wordsCount     word repeat stats in certain class
 * @param dictionarySize size of the dictionary of the learning algorithm
 */
case class NaiveBayesModel(lengths: Map[String, Int],
                           docCount: Map[String, Int],
                           // FIXME: понять, почему такая структура данных
                           // FIXME: переделать MapView в Map
                           wordsCount: Map[String, MapView[String, Int]],
                           dictionarySize: Int) {

  /* log of probability to find a word in a certain class */
  def wordLogProbability(`class`: String, word: String): Double =
    log((wordsCount(`class`).getOrElse(word, 0) + 1.0) / (lengths(`class`).toDouble + dictionarySize))

  // FIXME: translate it right
  /* log of probability of a class */
  def classLogProbability(`class`: String): Double = log(docCount(`class`).toDouble / docCount.values.sum)

  /* A Set of all existent classes */
  def classes: Set[String] = docCount.keySet
}
