package lt.overdrive.trackparser.parsing.tcx

import org.specs2.mutable.Specification
import lt.overdrive.trackparser.domain.{TrackPoint, Trail}
import lt.overdrive.trackparser.parsing.gpx.GpxParser
import lt.overdrive.trackparser.utils.ResourceUtils.getFile
import lt.overdrive.trackparser.parsing.GpsTestDataHelper._

class TcxSpec extends Specification {
  "parser" should {
    "load 1 track" in {
      val trail: Trail = new GpxParser().parse(getFile("tcx/valid.tcx").get)

      trail.tracks must have size 1
    }

    "load 2 tracks" in {
      val trail: Trail = new GpxParser().parse(getFile("tcx/2activities.tcx").get)

      trail.tracks must have size 2
    }

    "skip points without position" in {
      val expectedTrail = prepareTrail(Seq(Point_1, Point_2))

      val trail = new TcxParser().parse(getFile("tcx/missing_position.tcx").get)

      trail must be equalTo expectedTrail
    }

    val trackPoints: Seq[TrackPoint] = Seq(Point_1, Point_2, Point_3, Point_4, Point_5, Point_6)

    "load correct data with altitude" in {
      val expectedTrail: Trail = prepareTrail(trackPoints)

      val trail = new TcxParser().parse(getFile("tcx/test_with_ele.tcx").get)

      trail must be equalTo expectedTrail
    }

    "load correct data without altitude" in {
      val expectedTrail: Trail = prepareTrailWithoutAltitude(trackPoints)

      val trail = new TcxParser().parse(getFile("tcx/test_no_ele.tcx").get)

      trail must be equalTo expectedTrail
    }
  }

}
