package lt.overdrive.trackparser.parsing.tcx

import java.io.File
import lt.overdrive.trackparser.domain.{Track, Trail}
import javax.xml.validation.Schema
import lt.overdrive.trackparser.utils.ResourceUtils.loadSchema
import lt.overdrive.trackparser.parsing.GpsFileParser

class TcxParser extends GpsFileParser {

  def getSchema: Schema = loadSchema("tcx/TrainingCenterDatabasev2.xsd").get

  def loadTrail(file: File): Trail = {
    val gpx = scala.xml.XML.loadFile(file)

    val tracks: Seq[Track] = (gpx \\ "Activity").map(_ => new Track)

    Trail(tracks)
  }
}
