package conversion
import scala.io.Source._
import java.io._
import ArsUtils._

object MapApplication {

  def main(args: Array[String]): Unit = {
    // initialize maphttp://www.tableau.com/products/desktop/download
    //val map = collection.mutable.Map[String, Int]().withDefaultValue(0)
    val filename = "../test4.txt"
    val writer = new PrintWriter(new File("markers.json"))
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
      .foreach(x => writer.write(x + ","))
    writer.close()

      //.foreach(key => map.update(key, map(key) + 1))

      //println(map)
  }


}
