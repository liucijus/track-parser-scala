package lt.overdrive.trackparser.parsing.gpx

import org.specs2.mutable.Specification
import lt.overdrive.trackparser.utils.ResourceUtils.getFile
import lt.overdrive.trackparser.domain.{TrackPoint, Trail}
import lt.overdrive.trackparser.parsing.GpsTestDataHelper._

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

    val trackPoints: Seq[TrackPoint] = Seq(Point_1, Point_2, Point_3, Point_4, Point_5, Point_6)

    "load data correctly" in {
      val expectedTrail: Trail = prepareTrail(trackPoints)

      val trail: Trail = new GpxParser().parse(getFile("gpx/test_with_ele.gpx").get)

      trail must be equalTo expectedTrail
    }

    "load data correctly without altitude" in {
      val expectedTrail: Trail = prepareTrailWithoutAltitude(trackPoints)

      val trail: Trail = new GpxParser().parse(getFile("gpx/test_no_ele.gpx").get)

      trail must be equalTo expectedTrail
    }

    "load data correctly without time" in {
      val expectedTrail: Trail = prepareTrailWithoutTime(trackPoints)

      val trail: Trail = new GpxParser().parse(getFile("gpx/test_no_time.gpx").get)

      trail must be equalTo expectedTrail
    }
  }
}
