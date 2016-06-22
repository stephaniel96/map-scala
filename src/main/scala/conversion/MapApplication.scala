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
  }


}
