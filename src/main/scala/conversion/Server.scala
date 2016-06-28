package conversion


import akka.actor._
import akka.http.scaladsl._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.ws.TextMessage
// streaming tools
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.StdIn
import ArsUtils._

object Server {

  def getData(): String = {
    val line =
      StdIn.readLine().takeWhile(_ != null)
        .toArs.getOrElse("")
        .arsToTag()
        .map(_.takeRight(19))
        .flatMap(_.toLinear)
        .map(_.toLocation)
      .mkString
    line
  }

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    //minimal server will accept Get requests and respond with message
    val route = get {
      path("getInfo") {
        val src = Source.fromIterator(() => Iterator.continually(getData()).map(i => TextMessage(i)))
        extractUpgradeToWebSocket { upgrade => complete(upgrade.handleMessagesWithSinkSource(Sink.ignore, src)) }
      }
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
