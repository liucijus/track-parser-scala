package lt.overdrive.trackparser.parsing

import lt.overdrive.trackparser.domain.Trail
import java.io.File
import javax.xml.validation.Schema
import scala.xml.NodeSeq

trait GpsFileParser {
  implicit def enrichNodeSeq(nodeSeq: NodeSeq) = new AnyRef {
    def textOption: Option[String] = {
      val text = nodeSeq.text
      if (text == null || text.length == 0) None else Some(text)
    }
  }

  def parse(file: File): Trail = {
    if (!file.exists) throw new ParserException(s"File $file does not exist.")

    loadTrail(file)
  }

  def loadTrail(file: File): Trail

  def getSchema: Schema
}
