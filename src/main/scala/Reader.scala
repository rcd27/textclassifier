/**
 * Reader that is able to read documents
 *
 * @tparam I input document type
 * @tparam O output data
 */
trait Reader[-I, +O] {
  def read(input: I): O
}