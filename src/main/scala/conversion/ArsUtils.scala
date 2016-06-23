package conversion

import java.net.{HttpURLConnection, URL}
import play.api.libs.json._
import scala.io.Source._


object ArsUtils {

  implicit class getArs(init: String) {

    val pattern = "\\{\"resolveAvailabilityResponse.+\\}\\}".r
    def toArs: Option[String] = {
      pattern.findFirstIn(init)
    }
  }

  implicit class getJson(jsonString: String) {

    val menuLocInArs = 6
    def arsToTag(): List[String] = {
      val obj = Json.parse(jsonString).as[JsObject]

      // to do: handle case where "menu" blob is not in ars i.e. index out of bound
      // to do: double check that 6 is the way to do it i.e. other blobs not removed
      val availList = (obj \ "resolveAvailabilityResponse").\("availabilityGroups")(menuLocInArs).
        \("availabilities").as[List[String]]
      availList
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