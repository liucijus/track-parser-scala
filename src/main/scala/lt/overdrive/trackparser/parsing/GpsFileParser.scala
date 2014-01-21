package lt.overdrive.trackparser.parsing

import lt.overdrive.trackparser.domain.Trail
import java.io.File
import javax.xml.validation.Schema

trait GpsFileParser {
  def parse(file: File): Trail = {
    if (!file.exists) throw new ParserException(s"File $file does not exist.")

    loadTrail(file)
  }

  def loadTrail(file: File): Trail

  def getSchema: Schema
}
