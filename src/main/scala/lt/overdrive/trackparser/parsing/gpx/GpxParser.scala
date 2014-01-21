package lt.overdrive.trackparser.parsing.gpx

import javax.xml.validation.Schema
import lt.overdrive.trackparser.domain.Trail
import java.io.File
import lt.overdrive.trackparser.utils.ResourceUtils.loadSchema
import lt.overdrive.trackparser.parsing.GpsFileParser

class GpxParser extends GpsFileParser {
  def parse(file: File): Trail = Trail(Seq())

  def getSchema: Schema = loadSchema("gpx/gpx.xsd").get
}
