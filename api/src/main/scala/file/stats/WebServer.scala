package file.stats

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{HttpOrigin, HttpOriginRange}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import ch.megard.akka.http.cors.javadsl.settings.CorsSettings

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.control.NonFatal
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

object WebServer extends App {
  val settings = CorsSettings.defaultSettings.withAllowedOrigins(HttpOriginRange("localhost"))

  def routes = cors() {
    path("fileStats") {
      fileUpload("doc") {
        case (_, byteSource) =>
          val fileF = FileStats.getForFile(byteSource)

          onSuccess(fileF) { wordStats => {
            complete(
              HttpResponse(StatusCodes.Created,
                entity =
                  HttpEntity(ContentTypes.`application/json`,
                    wordStats.toString())))
          }
          }
      }
    }
  }

  override def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    implicit def myExceptionHandler: ExceptionHandler =
      ExceptionHandler {
        case NonFatal(e) =>
          e.printStackTrace()
          complete(
            HttpResponse(StatusCodes.InternalServerError,
                         entity = "Internal Server Error"))
      }

    val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())

  }
}
