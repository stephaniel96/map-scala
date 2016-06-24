package conversion

import akka.NotUsed
import akka.actor._
import akka.http.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.StdIn

object Server {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()


    val echoService: Flow[Message, Message, _] = Flow[Message].map {
      case TextMessage.Strict(txt) => TextMessage("ECHO: " + txt)
      case _ => TextMessage("Message type unsupported")
    }


    // minimal server will accept Get requests and respond with message
    val route = get {
      pathEndOrSingleSlash {
          complete("Such HTTP response")
        }
      } ~
    path("ws-echo") {
      get {
        handleWebSocketMessages(echoService)
      }
    }
    val requestHandler: HttpRequest => HttpResponse = {
      case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
        HttpResponse(entity = HttpEntity(ContentTypes.`text/html(UTF-8)`,
          "<html><body>Hello world!</body></html>"))

      case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
        HttpResponse(entity = "PONG!")

      case HttpRequest(GET, Uri.Path("/crash"), _, _, _) =>
        sys.error("BOOM!")

      case _: HttpRequest =>
        HttpResponse(404, entity = "Unknown resource!")
    }

    val interface = "localhost"
    val port = 8080
    val binding = Await.result(Http().bindAndHandle(route, interface, port), 3.seconds)
    //val binding = Http().bindAndHandleSync(requestHandler, interface, port)


    println("Started server, press enter to kill")
    StdIn.readLine()
    system.terminate()
  }
}
