package file.stats

import org.scalatest.{Assertions, AsyncFunSuite, Matchers}

class FileStatsSpec
  extends AsyncFunSuite
    with Matchers
    with Assertions {

  test("count all the words in a text") {
    FileStats.totalWordCount("hello world") shouldBe 2
  }

  test("counts of each occurrence of a word") {
    val wordsCount = FileStats.countWords("hello world, hello world, world")
    wordsCount("hello") shouldBe 2
    wordsCount("world") shouldBe 3
  }
}
