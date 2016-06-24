package conversion

import akka.NotUsed
import akka.actor._
import akka.http.scaladsl._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.ws.Message
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.StdIn

object Server {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    // minimal server will accept Get requests and respond with message
    val route =
      path("example") {
        get {
          complete("Such HTTP response")
        }
      }
    val interface = "localhost"
    val port = 8080
    val binding = Await.result(Http().bindAndHandle(route, interface, port), 3.seconds)

    println("Started server, press enter to kill")
    StdIn.readLine()
    system.terminate()
  }
}
