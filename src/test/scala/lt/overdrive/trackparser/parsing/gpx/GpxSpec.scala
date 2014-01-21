package lt.overdrive.trackparser.parsing.gpx

import org.specs2.mutable.Specification
import lt.overdrive.trackparser.utils.ResourceUtils.getFile
import scala.util.Try
import lt.overdrive.trackparser.domain.Trail
import lt.overdrive.trackparser.parsing.ParserException

class GpxSpec extends Specification {
  "gpx parser" should {
    "load one track" in {
      val trail: Trail = new GpxParser().parse(getFile("gpx/test_with_ele.gpx").get)

      trail.tracks must have size 1
    }

    "load 2 tracks" in {
      val trail: Trail = new GpxParser().parse(getFile("gpx/2tracks.gpx").get)

      trail.tracks must have size 2
    }
  }
}
