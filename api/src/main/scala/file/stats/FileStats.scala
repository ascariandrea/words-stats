package file.stats

object FileStats {

  def countWords(text: String): Map[String, Int] =
    text.split("\\W+")
      .groupBy(s => s)
      .map(g => (g._1, g._2.length))

  def totalWordCount(text: String): Int = text.split("\\W+").length
}
