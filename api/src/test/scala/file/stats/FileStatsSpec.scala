package file.stats

import akka.http.scaladsl.model._
import org.scalatest.{Assertions, AsyncFunSuite, Matchers}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import file.stats.models.WordStats

class FileStatsSpec
    extends AsyncFunSuite
    with Matchers
    with Assertions
    with ScalatestRouteTest {

  test("counts of each occurrence of a word") {
    val map = List(("hello", 2), ("world", 2)).toMap
    val wordsCount = FileStats.countWords(map, "World")
    wordsCount("hello") shouldBe 2
    wordsCount("world") shouldBe 3
  }

  test("Should parse the file and return a proper json object") {

    val multipartForm =
      Multipart.FormData(
        Multipart.FormData.BodyPart.Strict(
          "doc",
          HttpEntity(
            ContentTypes.`text/plain(UTF-8)`,
            """
            |this is the first line
            |this is the second line
            |this is the third line
          """.stripMargin
          ),
          Map("filename" -> "doc.txt")
        ))

    Post("/fileStats", multipartForm) ~> WebServer.routes ~> check {

      import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
      import io.circe.generic.auto._

      status shouldEqual StatusCodes.Created
      responseAs[WordStats].totalWords shouldBe 15
      responseAs[WordStats].words shouldBe Map(
        "this" -> 3,
        "is" -> 3,
        "line" -> 3,
        "second" -> 1,
        "third" -> 1,
        "first" -> 1,
        "the" -> 3
      )
    }
  }

}
