package lt.overdrive.trackparser.parsing

import org.specs2.mutable.Specification
import scala.util.Try
import lt.overdrive.trackparser.domain.Trail
import lt.overdrive.trackparser.utils.ResourceUtils.getFile

class ParserSpec extends Specification {
  "parser" should {
    "parse TCX " in {
      val file = getFile("tcx/test_with_ele.tcx").get

      val trail: Try[Trail] = Parser.parserFile(file)

      trail must beSuccessfulTry //todo: .withValue(tracks must be not empty)
    }

    "parse GPX" in {
      val file = getFile("gpx/test_with_ele.gpx").get

      val trail: Try[Trail] = Parser.parserFile(file)

      trail must beSuccessfulTry[Trail].withValue(trail.get.tracks must be not empty) //todo: .withValue(tracks must be not empty)
    }

    "fail for invalid file" in {
      val file = getFile("tcx/invalid.tcx").get

      val trail: Try[Trail] = Parser.parserFile(file)

      trail must beFailedTry[Trail].withThrowable[UnrecognizedFileException]
    }
  }
}
