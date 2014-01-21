package lt.overdrive.trackparser.parsing

import java.io.File
import scala.util.{Success, Failure, Try}
import lt.overdrive.trackparser.parsing.FileType.FileType
import javax.xml.validation.{Schema, Validator}
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import lt.overdrive.trackparser.parsing.gpx.GpxParser
import lt.overdrive.trackparser.parsing.tcx.TcxParser
import lt.overdrive.trackparser.domain.Trail

object Parser {
  def parserFile(file: File): Try[Trail] = {
    val parser = new Parser(file)

    val fileType = parser.guessFileType

    parser.parserForType(fileType) match {
      case Success(typeParser) => Success(typeParser.parse(file))
      case Failure(e) => Failure(e)
    }
  }
}

case class Parser(file: File) {
  def guessFileType: FileType = {
    def isTcxFile(file: File): Boolean = {
      val schema: Schema = new TcxParser().getSchema
      isValidSchemaDocument(schema)
    }

    def isGpxFile(file: File): Boolean = {
      val schema: Schema = new GpxParser().getSchema
      isValidSchemaDocument(schema)
    }

    def isValidSchemaDocument(schema: Schema): Boolean = {
      val validator: Validator = schema.newValidator
      val source: Source = new StreamSource(file)
      Try(validator.validate(source)).isSuccess
    }

    if (isTcxFile(file)) FileType.Tcx
    else if (isGpxFile(file)) FileType.Gpx
    else FileType.Unknown
  }

  def parserForType(fileType: FileType): Try[GpsFileParser] = fileType match {
    case FileType.Tcx => Success(new TcxParser())
    case FileType.Gpx => Success(new GpxParser())
    case _ => Failure(new UnrecognizedFileException(s"File $file was not recognized to be of supported type [TCX, GPX]"))
  }
}

object FileType extends Enumeration {
  type FileType = Value
  val Gpx, Tcx, Unknown = Value
}

trait GpsFileParser {
  def parse(file: File): Trail = {
    if (!file.exists) throw new ParserException(s"File $file does not exist.")

    loadTrail(file)
  }

  def loadTrail(file: File): Trail

  def getSchema: Schema
}


