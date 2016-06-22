package conversion
import scala.io.Source._
import ArsUtils._


object MapApplication {

  def main(args: Array[String]): Unit = {
    // initialize map
    val map = collection.mutable.Map[String, Int]().withDefaultValue(0)
    val filename = "/home/sliu203/Scala/test2.txt"

    fromFile(filename)
      .getLines()
      .filter(_.nonEmpty)
      .map(_.toArs)
      .collect{ case Some(x) => x}
      .flatMap(_ arsToTag())
      //.collect{ case x if x.length > 0 => x} // removes ars with no menu tags
      //.flatten
      .map(_.takeRight(19))
      .flatMap(_.toLinear)
      .map(_.toLocation)
      .foreach(key => map.update(key, map(key) + 1))

    println(map)



//    for (line <- fromFile(filename).getLines().filter(_.nonEmpty)) {
//
//      val arsStr = line.toArs()
//
//      val arsStringList = arsStr.arsToTag()
//      val arsTagList = arsStringList.map(str => str.takeRight(19))
//      println(arsTagList)
//
//      val linearStringList = arsTagList.flatMap(tag => tag.toLinear)
//      println(linearStringList)
//
//      val locationList = linearStringList.map(str => str.toLocation)
//      locationList.map(key => map.update(key, map(key) + 1))
//      println(map)
//    }

    // testing for if we need to retrieve ars
//    val testStr = "2016-06-20 13:56:50,999 INFO  Component=\"xQE\" Version=\"2.1.15-201603\" Application=\"5.0|browse\" EntryCount=\"1\" Returned=\"1\" StatusCode=\"200\" SessionId=\"comcast:compass:session:4258498651553968338\" DiscoveryId=\"browse2115201603-172.20.184.108-94735648-1466431010986579\" Query=\"http://rex.ccp.xcal.tv:10124/rex/browse/program?id=m%246115786120257849112&schema=5.0&showings.sort.linear=window.start.ASC%2Cformat.DESC&client=XRE%3AX2&filter.image.gid=8580885346555644161%2C7020167223608802161%2C7171039184798784161&showings.linear=1&filters=%28catalog.type%3Alinear+AND+%28tag.encoding%3Ampeg_qam+OR+tag.encoding%3AM3U%29%29&fields=episode.programId%2Cepisode.title%2Cepisode.ratings%2Cepisode.synopsis%2Cepisode.originalAirDate%2Cepisode.duration%2Cepisode.programType%2Cepisode.starRating%2Cepisode.language%2Cepisode.year%2Cepisode.tags%2Cepisode.sportsSubtitle%2Cepisode.audience%2Cepisode.seasons%2Cepisode.companyIds%2Cepisode.partNumber%2Cepisode.totalParts%2Cepisode.facets%2Cepisode.offers%2Cepisode.images%2Cepisode.seriesId%2Cepisode.episodeTitle%2Cepisode.tvSeasonEpisodeNumber%2Cepisode.tvSeasonNumber%2Cshowing.programId%2Cshowing.rating%2Cshowing.cc%2Cshowing.sap%2Cshowing.dvs%2Cshowing.providerCompanyId%2Cshowing.listingId%2Cshowing.airingType%2Cshowing.audioType%2Cshowing.captionType%2Cshowing.hdLevel%2Cshowing.endTime%2Cshowing.startTime%2Cshowing.stationId%2Cshowing.threeD%2Cshowing.hd%2Cshowing.onDvr%2Cshowing.dvrScheduleId%2Cshowing.ppv%2Cshowing.ratings%2Cshowing.mediaId%2Cshowing.providerId%2Cshowing.titleAssetId%2Cshowing.provider%2Cshowing.duration%2Cshowing.price%2Cshowing.trickModesRestricted%2Cshowing.availableDate%2Cshowing.expirationDate%2Cshowing.purchasable%2Cshowing.ultraViolet%2Cshowing.mediaGuid%2Cshowing.locatorUrl%2Cshowing.mediaContent%2Cshowing.purchase%2Cprogram.programId%2Cprogram.title%2Cprogram.ratings%2Cprogram.synopsis%2Cprogram.originalAirDate%2Cprogram.duration%2Cprogram.programType%2Cprogram.starRating%2Cprogram.language%2Cprogram.year%2Cprogram.tags%2Cprogram.sportsSubtitle%2Cprogram.audience%2Cprogram.seasons%2Cprogram.companyIds%2Cprogram.partNumber%2Cprogram.totalParts%2Cprogram.facets%2Cprogram.offers%2Cprogram.images%2Cprogram.seriesId%2Cprogram.episodeTitle%2Cprogram.episodeNumber%2Cprogram.tvSeasonNumber\" PostBody=\"ars={\"resolveAvailabilityResponse\":{\"adZone\":\"comcast:merlin:Region:6592196767705772172\",\"availabilityGroups\":[{\"availabilities\":[\"comcast:merlin:Location:9161470516141458110\"],\"productContext\":\"comcast:merlin:ProductContext:7013566886215080174\",\"scope\":\"station\"},{\"availabilities\":[\"comcast:merlin:Location:5931695747941653110\"],\"productContext\":\"comcast:merlin:ProductContext:6036846939271877174\",\"scope\":\"station\"},{\"availabilities\":[\"comcast:merlin:Location:5931695747941653110\",\"comcast:merlin:Location:9161470516141458110\"],\"scope\":\"channel\"},{\"availabilities\":[\"comcast:merlin:Region:4776139640285072172\",\"comcast:merlin:Region:5650196334098113172\",\"comcast:merlin:Region:6027041135917990172\",\"comcast:merlin:Region:6208841666954814172\",\"comcast:merlin:Region:6285903049383680172\",\"comcast:merlin:Region:6422963363306761172\",\"comcast:merlin:Region:6719620344054441172\",\"comcast:merlin:Region:6867185822233621172\",\"comcast:merlin:Region:6918664617018091172\",\"comcast:merlin:Region:7157702964120104172\",\"comcast:merlin:Region:7588074218130803172\",\"comcast:merlin:Region:7712465536556739172\",\"comcast:merlin:Region:7719477545354136172\",\"comcast:merlin:Region:9051839054532940172\"],\"scope\":\"stream\"},{\"availabilities\":[\"comcast:merlin:AvailabilityTag:5342498276680043143\",\"comcast:merlin:AvailabilityTag:6720525474512723143\",\"comcast:merlin:AvailabilityTag:6896282672196793143\",\"comcast:merlin:Region:4934208498678832172\",\"comcast:merlin:Region:5398141814757545172\",\"comcast:merlin:Region:6012513910855154172\",\"comcast:merlin:Region:6895293134966264172\",\"comcast:merlin:Region:7368260017605679172\"],\"productContext\":\"comcast:merlin:ProductContext:7013566886215080174\",\"scope\":\"vod\"},{\"availabilities\":[\"comcast:merlin:AvailabilityTag:5342498276680043143\",\"comcast:merlin:AvailabilityTag:6720525474512723143\",\"comcast:merlin:AvailabilityTag:6896282672196793143\",\"comcast:merlin:Region:4934208498678832172\",\"comcast:merlin:Region:5398141814757545172\",\"comcast:merlin:Region:6012513910855154172\",\"comcast:merlin:Region:6895293134966264172\",\"comcast:merlin:Region:7368260017605679172\"],\"productContext\":\"comcast:merlin:ProductContext:7013566886215080174\",\"scope\":\"offer\"},{\"availabilities\":[\"comcast:merlin:Location:4681152428903034110\",\"comcast:merlin:Location:6537074518960321110\",\"comcast:merlin:Location:8249791229493755110\"],\"scope\":\"menu\"}],\"distributionModeZone\":\"IP\",\"recorderManager\":\"comcast:RecorderManager:cdvr-x-rmID-sfld01\",\"version\":\"2.3.16\",\"timeRegion\":\"US/Eastern\",\"schema\":\"1.5\"}}\" Client=\"XRE:X2\" PagingStartIndex=\"1\" StatusMessage=\"OK\" SolrTime=\"7\" spluginTime=\"2\" voomSearcherTime=\"\" linearSearcherTime=\"\"  ecoreLookupTime=\"0\" ohsCollectorTime=\"\" rollUpCollectorTime=\"\" voomFqsTime=\"\" linearFqsTime=\"\" scoringTime=\"\" voomShowingsCount=\"\"  linearShowingsCount=\"\" scoringInitTime=\"0\" entitiesCollected=\"\"  eCoreSearcherTime=\"\" resultDocSetrTime=\"\" CreateRequestContext=\"1\" personAvailibilityTime=\"\" FetchImagesFromMembase=\"\" imageMDSWithoutGroupIds=\"\" imageMDSByGroupIds=\"\" FetchEditorialCollectionsFromCouchbase=\"\" searchRelatedShowingsMDS=\"\" TotalTime=\"13\" writeResponse=\"4\" MetadataServiceTotalTime=\"\" MoltServiceCallTime=\"\" getPersonalizedMoltResultsTime=\"\" FavProgramsServiceCallTime=\"\" FavTeamsServiceCallTime=\"\" FavStationsServiceCallTime=\"\"\n          vodShowingMDS=\"\" FetchVodShowingsFromCouchbase=\"\" odolShowingMDS=\"\" FetchOdolShowingsFromCouchbase=\"\" LinearPopularityServiceCall=\"0\" TopLevelResults=\"6115786120257849112,\"  "
//    val result = testStr.toArs()

//    try {
//    } catch {
//      case ioe: java.io.IOException => // handle this
//      case ste: java.net.SocketTimeoutException => // handle this
//    }

  }


}
