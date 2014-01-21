package lt.overdrive.trackparser.parsing.tcx

import java.io.File
import lt.overdrive.trackparser.domain.Trail
import javax.xml.validation.Schema
import lt.overdrive.trackparser.utils.ResourceUtils.loadSchema
import lt.overdrive.trackparser.parsing.GpsFileParser

class TcxParser extends GpsFileParser {
  def parse(file: File): Trail = Trail(Seq())

  def getSchema: Schema = loadSchema("tcx/TrainingCenterDatabasev2.xsd").get
}
