package lt.overdrive.trackparser.processing

import org.specs2.mutable.Specification
import lt.overdrive.trackparser.domain.Track
import lt.overdrive.trackparser.GpsTestDataHelper
import GpsTestDataHelper._

class TrackTotalsSpec extends Specification {
  "track totals" should {
    "have distance equal 0 for empty track" in {
      val track: Track = trackOf()

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.distance must beCloseTo(0, 0.005)
    }

    "have distance equal 0 for 1 point track" in {
      val track: Track = trackOf(Point_1)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.distance must beCloseTo(0, 0.005)
    }

    "have correct distance" in {
      val track: Track = trackOf(Point_1, Point_2, Point_3)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.distance must beCloseTo(9.94, 0.005)
    }

    "have total time 0 for empty track" in {
      val track: Track = trackOf()

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.timeTotals.get.time.getSeconds must be_==(0)
    }

    "have total time 0 for 1 point track" in {
      val track: Track = trackOf(Point_1)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.timeTotals.get.time.getSeconds must be_==(0)
    }

    "have correct time" in {
      val track: Track = trackOf(Point_1, Point_2, Point_3)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.timeTotals.get.time.getSeconds must be_==(2)
    }

    "have no time totals for timeless track" in {
      val track: Track = trackWithoutTimeOf(Point_1, Point_2, Point_3)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.timeTotals must beNone
    }

    "have correct average speed" in {
      val track: Track = trackOf(Point_1, Point_2, Point_3)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.timeTotals.get.speed must be beCloseTo(4.97, 0.005)
    }

    "have correct ascent" in {
      val track: Track = trackWithoutTimeOf(Point_1, Point_2, Point_3)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.altitudeTotals.get.ascent must closeTo(0, 0.005)
    }

    "have no altitude totals for track without altitude" in {
      val track: Track = trackWithoutAltitudeOf(Point_1, Point_2, Point_3)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.altitudeTotals must beNone
    }

    "have correct descent" in {
      val track: Track = trackWithoutTimeOf(Point_1, Point_2, Point_3)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.altitudeTotals.get.descent must beCloseTo(0.39, 0.005)
    }

    "have 0 ascent for 1 point track" in {
      val track: Track = trackOf(Point_1)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.altitudeTotals.get.ascent must beCloseTo(0, 0.005)
    }

    "have 0 descent for 1 point track" in {
      val track: Track = trackOf(Point_1)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.altitudeTotals.get.descent must beCloseTo(0, 0.005)
    }

    "have max speed 0 for empty track" in {
      val track: Track = trackOf()

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.timeTotals.get.maxSpeed must beCloseTo(0, 0.005)
    }

    "have max speed 0 for 1 point track" in {
      val track: Track = trackOf(Point_1)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.timeTotals.get.maxSpeed must beCloseTo(0, 0.005)
    }

    "have correct max speed" in {
      val track: Track = trackOf(Point_1, Point_2, Point_3)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.timeTotals.get.maxSpeed must beCloseTo(5.38, 0.005)
    }

    "have min speed 0 for empty track" in {
      val track: Track = trackOf()

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.timeTotals.get.minSpeed must beCloseTo(0, 0.005)
    }

    "have min speed 0 for 1 point track" in {
      val track: Track = trackOf(Point_1)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.timeTotals.get.minSpeed must beCloseTo(0, 0.005)
    }

    "have correct min speed" in {
      val track: Track = trackOf(Point_1, Point_2, Point_3)

      val totals: TrackTotals = TrackProcessor(track).calculateTotals()

      totals.timeTotals.get.minSpeed must beCloseTo(4.56, 0.005)
    }
  }
}
