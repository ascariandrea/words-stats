package file.stats

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Framing, Source}
import akka.util.ByteString
import file.stats.models.WordStats
import io.circe.Json
import scala.concurrent.{ExecutionContextExecutor, Future}
import io.circe.syntax._

object FileStats {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  def countWords(map: Map[String, Int], word: String): Map[String, Int] =
    map + (word.toLowerCase -> (map.getOrElse(word.toLowerCase, 0) + 1))

  def getForFile(byteSource: Source[ByteString, Any]): Future[Json] = {

    byteSource
      .via(Framing.delimiter(ByteString("\n"), 1024, true))
      .mapConcat(_.utf8String.toLowerCase().split("\\W+").filter(_.isEmpty == false).toVector)
      .runFold((0, Map.empty[String, Int]))(
        (acc, w) =>
          (
            acc._1 + 1,
            countWords(acc._2, w)
        ))
      .map(stats => WordStats(stats._1, stats._2).asJson)
  }
}
