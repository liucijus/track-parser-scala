package lt.overdrive.trackparser.parsing.gpx

import javax.xml.validation.Schema
import lt.overdrive.trackparser.domain.{Track, Trail}
import java.io.File
import lt.overdrive.trackparser.utils.ResourceUtils.loadSchema
import lt.overdrive.trackparser.parsing.{ParserException, GpsFileParser}

class GpxParser extends GpsFileParser {
  def getSchema: Schema = loadSchema("gpx/gpx.xsd").get

  def loadTrail(file: File): Trail = {
    val gpx = scala.xml.XML.loadFile(file)

    val tracks: Seq[Track] = (gpx \ "trk").map(_ => new Track)

    Trail(tracks)
  }
}
