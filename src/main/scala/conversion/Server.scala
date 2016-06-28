package conversion

import akka.NotUsed
import akka.actor._

import scala.concurrent._
import akka.http.scaladsl._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.ws.{Message, TextMessage}
// streaming tools
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.StdIn


object Server {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    //import system.dispatcher


    //minimal server will accept Get requests and respond with message
    val route = get {
      path("getInfo") {
        val src = Source.fromIterator(() => Iterator.continually(StdIn.readLine().takeWhile(_ != null)).map(i => TextMessage(i)))
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
