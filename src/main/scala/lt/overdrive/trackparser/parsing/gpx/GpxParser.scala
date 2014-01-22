package lt.overdrive.trackparser.parsing.gpx

import javax.xml.validation.Schema
import lt.overdrive.trackparser.domain.{TrackPoint, Track, Trail}
import java.io.File
import lt.overdrive.trackparser.utils.ResourceUtils.loadSchema
import lt.overdrive.trackparser.parsing.{ParserException, GpsFileParser}
import org.joda.time.DateTime

class GpxParser extends GpsFileParser {
  def getSchema: Schema = loadSchema("gpx/gpx.xsd").get

  def loadTrail(file: File): Trail = {
    val gpx = scala.xml.XML.loadFile(file)

    val tracks: Seq[Track] = (gpx \ "trk").map(
      t => {
        val points: Seq[TrackPoint] = (t \\ "trkpt").map({
          p =>
            val latitude: Double = (p \ "@lat").text.toDouble
            val longitude: Double = (p \ "@lon").text.toDouble
            val altitude: Option[Double] = (p \ "ele").textOption match {
              case Some(value) => Some(value.toDouble)
              case None => None
            }

            val time: Option[DateTime] = (p \ "time").textOption match {
              case Some(xmldate) => Some(new DateTime(xmldate))
              case None => None
            }
            TrackPoint(latitude, longitude, altitude, time)
        })
        new Track(points)
      })

    Trail(tracks)
  }
}
