package lt.overdrive.trackparser.parsing

import org.specs2.mutable.Specification
import scala.util.{Failure, Try}
import lt.overdrive.trackparser.domain.Trail
import lt.overdrive.trackparser.utils.ResourceUtils.getFile

class ParserSpec extends Specification {
  "parser" should {
    "parse TCX " in {
      val file = getFile("tcx/test_with_ele.tcx").get

      val trail: Try[Trail] = Parser.parserFile(file)

      trail must beSuccessfulTry
      trail.get.tracks must not be empty
    }

    "parse GPX" in {
      val file = getFile("gpx/test_with_ele.gpx").get

      val trail: Try[Trail] = Parser.parserFile(file)

      trail must beSuccessfulTry[Trail]
      trail.get.tracks must not be empty
    }

    "fail for invalid file" in {
      val file = getFile("tcx/invalid.tcx").get
      //val expectedExceptionMessage = s"File $file was not recognized to be of supported type [TCX, GPX]"

      val trail: Try[Trail] = Parser.parserFile(file)

      trail must beFailedTry //[Trail].withThrowable[UnrecognizedFileException](expectedExceptionMessage)
    }
  }
}
