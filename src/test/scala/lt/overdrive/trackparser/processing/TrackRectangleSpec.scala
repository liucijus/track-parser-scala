package lt.overdrive.trackparser.processing

import org.specs2.mutable.Specification
import lt.overdrive.trackparser.GpsTestDataHelper._
import lt.overdrive.trackparser.domain.{TrackPoint, Track}

class TrackRectangleSpec extends Specification {
  "track rectangle" should {
    "be None for empty track" in {
      val track: Track = trackOf()

      val rectangle = TrackProcessor(track).calculateRectangle()

      rectangle must beNone
    }

    "coordinates should be equal for 1 point track" in {
      val track: Track = trackOf(Point_1)

      val rectangle = TrackProcessor(track).calculateRectangle()

      rectangle.get.northEast.latitude must beEqualTo(Point_1.latitude)
      rectangle.get.northEast.longitude must beEqualTo(Point_1.longitude)
      rectangle.get.southWest.latitude must beEqualTo(Point_1.latitude)
      rectangle.get.southWest.longitude must beEqualTo(Point_1.longitude)
    }

    "coordinates correct" in {
      val track: Track = trackOf(Point_1, Point_2, Point_3)
      val expectedNorthEast = TrackPoint(Point_1.latitude, Point_1.longitude)
      val expectedSouthWest = TrackPoint(Point_3.latitude, Point_3.longitude)

      val rectangle = TrackProcessor(track).calculateRectangle()

      rectangle.get.northEast must beEqualTo(expectedNorthEast)
      rectangle.get.southWest must beEqualTo(expectedSouthWest)
    }

    "center coordinates correct" in {
      val track: Track = trackOf(Point_1, Point_2, Point_3)
      val expectedCenter = TrackPoint(54.7096555, 25.245314999999998)

      val rectangle = TrackProcessor(track).calculateRectangle()

      rectangle.get.centerPoint must beEqualTo(expectedCenter)
    }

    "correct rectangle for multiple tracks" in {
      val tracks: Seq[Track] = Seq(trackOf(Point_1, Point_2, Point_3), trackOf(Point_4, Point_5, Point_6))
      val expectedSouthWest = TrackPoint(Point_6.latitude, Point_6.longitude)
      val expectedNorthEast = TrackPoint(Point_1.latitude, Point_1.longitude)
      val expectedCenter = TrackPoint(54.709610999999995, 25.2452185)

      val rectangle = TrackProcessor.calculateRectangle(tracks)

      rectangle.get.southWest must beEqualTo(expectedSouthWest)
      rectangle.get.northEast must beEqualTo(expectedNorthEast)
      rectangle.get.centerPoint must beEqualTo(expectedCenter)
    }
  }
}
