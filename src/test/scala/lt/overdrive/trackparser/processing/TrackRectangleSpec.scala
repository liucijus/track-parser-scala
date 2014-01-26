package lt.overdrive.trackparser.processing

import org.specs2.mutable.Specification
import lt.overdrive.trackparser.GpsTestDataHelper._
import lt.overdrive.trackparser.domain.Track
import lt.overdrive.trackparser.lt.overdrive.trackparser.processing.TrackProcessor

class TrackRectangleSpec extends Specification {
  "track rectangle" should {
    "be None for empty track" in {
      val track: Track = trackOf()

      val rectangle = TrackProcessor(track).calculateRectangle()

      rectangle must beNone
    }
  }
}
