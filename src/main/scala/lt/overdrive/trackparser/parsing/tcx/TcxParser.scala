package lt.overdrive.trackparser.parsing.tcx

import java.io.File
import lt.overdrive.trackparser.domain.{TrackPoint, Track, Trail}
import javax.xml.validation.Schema
import lt.overdrive.trackparser.utils.ResourceUtils.loadSchema
import lt.overdrive.trackparser.parsing.GpsFileParser
import org.joda.time.DateTime

class TcxParser extends GpsFileParser {
  def getSchema: Schema = loadSchema("tcx/TrainingCenterDatabasev2.xsd").get

  def loadTrail(file: File): Trail = {
    val tcx = scala.xml.XML.loadFile(file)

    val tracks: Seq[Track] = (tcx \\ "Track").map{
      t => {
        val points: Seq[TrackPoint] = (t \ "Trackpoint").map {
          p => {
            val latitude = (p \\ "LatitudeDegrees").text.toDouble

            val longitude = (p \\ "LongitudeDegrees").text.toDouble

            val altitude: Option[Double] = (p \ "AltitudeMeters").textOption match {
              case Some(value) => Some(value.toDouble)
              case None => None
            }

            val time: Option[DateTime] = (p \ "Time").textOption match {
              case Some(xmldate) => Some(new DateTime(xmldate))
              case None => None
            }

            TrackPoint(latitude, longitude, altitude, time)
          }
        }
        Track(points)
      }
    }

    Trail(tracks)
  }
}
