package conversion

import ArsUtils._
import akka.actor._
import akka.http.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.ws.TextMessage
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.duration._
import scala.concurrent.Await
import java.net.URLDecoder


object Server {


  def getData(str: String): TextMessage = {
      val line =
        str.toArs.getOrElse("")
          .arsToTag()
          .map(_.takeRight(19))
          .flatMap(_.toLinear)
          .map(_.toLocation)
          .mkString
      TextMessage(line)
  }

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    // outActor can be used to send messages to client(s)
    val (outActor, publisher) = Source.actorRef[TextMessage](500, OverflowStrategy.dropNew)
      .toMat(Sink.asPublisher(true))(Keep.both).run()

    val source = Source.fromPublisher(publisher)

      val handler: HttpRequest => HttpResponse = {
      HttpRequest =>
        val uri = HttpRequest.uri.toString()
        val info = getData(URLDecoder.decode(uri, "UTF-8"))
        // send to the actor
        outActor ! info
        HttpResponse(200)

    }

    val route = get {
      path("getInfo") {
        extractUpgradeToWebSocket {
          upgrade => complete(upgrade.handleMessagesWithSinkSource(Sink.ignore, source)) }
      }
    } ~
    pathPrefix("rex") {
        handleWith(handler)
    }

    val interface = "localhost"
    val port = 8080
    // Wait for Actor to "complete" the Future
    val binding = Await.result(Http().bindAndHandle(route, interface, port), 3.seconds)

    println("Started server, press enter to kill")
    //StdIn.readLine()
    //system.terminate()
  }
}
