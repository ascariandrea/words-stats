package file.stats.models

import io.circe.generic.JsonCodec

@JsonCodec case class WordStats(
    totalWords: Int,
    words: Map[String, Int]
)
