package conversion

import java.net.{HttpURLConnection, URL}
import play.api.libs.json._
import scala.io.Source._

object ArsUtils {

  // gets ars json
  implicit class getArs(init: String) {
    val pattern = "\\{\"resolveAvailabilityResponse.+\\}\\}".r
    def toArs: Option[String] = {
      pattern.findFirstIn(init)
    }
  }

  // get list of tags
  implicit class getTag(jsonString: String) {
    def arsToTag(): List[String] = {
      // if statement necessary to check in case getArs produced empty string
      if (jsonString.isEmpty()) List()
      else {
        val obj = Json.parse(jsonString).as[JsObject]
        val availList = (obj \ "resolveAvailabilityResponse").\("availabilityGroups").as[JsArray]
        // assumes menu availability is always last
        availList(availList.value.size - 1).\("availabilities").as[List[String]]
      }
    }
  }

  implicit class tagConversion(availTag: String) {
    val timeout: Int = 5000
    val linearURL = "http://ccpmer-po-v003-p.po.ccp.cable.comcast.com:9003/linearDataService/data/Location?" +
      "schema=1.6.0&form=cjson&pretty=true&byId="
    val locationURL = "http://ccpmer-po-v003-p.po.ccp.cable.comcast.com:9037/locationDataService/data/GeoLocation?" +
      "schema=1.3.0&form=cjson&pretty=true&byId="

    @throws(classOf[java.io.IOException])
    @throws(classOf[java.net.SocketTimeoutException])
    def toLinear: List[String] = {
      val connection = new URL(linearURL + availTag).openConnection.asInstanceOf[HttpURLConnection]
      connection.setConnectTimeout(timeout)
      connection.setReadTimeout(timeout)
      connection.setRequestMethod("GET")
      val inputStream = connection.getInputStream
      val jsonString = fromInputStream(inputStream).mkString
      if (inputStream != null) inputStream.close()

      val jsonObj = Json.parse(jsonString).as[JsObject]
      val geoLocList = (jsonObj \ "entries").as[List[JsValue]]
      if (geoLocList.nonEmpty)
        geoLocList(0).\("geoLocationIds").as[List[String]]
      else List()

    }

    // formats JSON
    case class Location(city: String, lat: Double, long: Double)
    implicit val locationWrites = new Writes[Location] {
      def writes(location:Location) = Json.obj(
        "city" -> location.city,
        "lat" -> location.lat,
        "lng" -> location.long
      )
    }

    @throws(classOf[java.io.IOException])
    @throws(classOf[java.net.SocketTimeoutException])
    def toLocation: JsValue = {
      val connection = new URL(locationURL + availTag).openConnection.asInstanceOf[HttpURLConnection]
      connection.setConnectTimeout(timeout)
      connection.setReadTimeout(timeout)
      connection.setRequestMethod("GET")
      val inputStream = connection.getInputStream
      val jsonString = fromInputStream(inputStream).mkString
      if (inputStream != null) inputStream.close()
      val jsonObj = Json.parse(jsonString)
      val path = (jsonObj \ "entries") (0)

      val loc = Location(path.\("city").as[String], path.\("latitude").as[Double],
        path.\("longitude").as[Double])
      Json.toJson(loc)
    }
  }
}