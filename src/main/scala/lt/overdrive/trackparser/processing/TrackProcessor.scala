package lt.overdrive.trackparser.lt.overdrive.trackparser.processing

import lt.overdrive.trackparser.domain.{TrackPoint, Track}
import org.joda.time.Seconds
import scala.annotation.tailrec
import lt.overdrive.trackparser.processing.TrackRectangle

case class TrackProcessor(track: Track) {
  def calculateRectangle(): Option[TrackRectangle] = {
    None
  }

  private val segments: Seq[Segment] = convert(track)

  private def convert(track: Track): Seq[Segment] = track.points match {
    case Nil => List()
    case head :: Nil => List()
    case head :: tail => {
      @tailrec
      def makeSegments(first: TrackPoint, rest: Seq[TrackPoint], accu: Seq[Segment]): Seq[Segment] = {
        val segments = accu :+ Segment(first, rest.head)
        rest match {
          case head :: Nil => segments
          case head :: tail => makeSegments(head, tail, segments)
        }
      }

      makeSegments(head, tail, List())
    }
  }

  def calculateTotals(): TrackTotals = {
    val points = track.points

    def calculateAltitudeTotalsIfHasAltitude(pointsWithoutAltitude: Seq[TrackPoint]): Option[AltitudeTotals] = {
      def hasAltitude: Boolean = pointsWithoutAltitude.isEmpty

      def calculateTotals: Some[AltitudeTotals] = {
        val (descent, ascent) = segments.foldLeft((0d, 0d))(
          (totals, segment) => {
            val difference = segment.point1.altitude.get - segment.point2.altitude.get
            val (descent, ascent) = totals
            if (difference > 0) (descent, ascent + difference) else (descent - difference, ascent)
          }
        )
        Some(AltitudeTotals(descent, ascent))
      }

      if (hasAltitude) calculateTotals else None
    }

    def calculateTimeTotalsIfHasTime(pointsWithoutTime: Seq[TrackPoint], distance: Double): Option[TimeTotals] = {
      def hasTime: Boolean = pointsWithoutTime.isEmpty

      def calculateTimeTotals: Some[TimeTotals] = {
        val time = Seconds.secondsBetween(points.head.date.get, points.last.date.get)
        val speed = distance / time.getSeconds

        val (minSpeed, maxSpeed) = segments.foldLeft((Double.MaxValue, 0d))((totals, segment) => {
          val time = Seconds.secondsBetween(segment.point1.date.get, segment.point2.date.get)
          val distance = Haversine.calculateDistance(segment.point1, segment.point2)
          val speed = distance / time.getSeconds

          val (minSpeed, maxSpeed) = totals

          (if (minSpeed > speed) speed else minSpeed, if (maxSpeed < speed) speed else maxSpeed)
        })

        Some(TimeTotals(time, speed, minSpeed, maxSpeed))
      }

      if (hasTime) calculateTimeTotals else None
    }

    def calculateTotals = {
      val distance = segments.foldLeft(0d)((distance, segment) => distance + Haversine.calculateDistance(segment.point1, segment.point2))

      val pointsWithoutTime = points.filter(_.date.isEmpty)
      val timeTotals = calculateTimeTotalsIfHasTime(pointsWithoutTime, distance)

      val pointsWithoutAltitude = points.filter(_.altitude.isEmpty)
      val altitudeTotals = calculateAltitudeTotalsIfHasAltitude(pointsWithoutAltitude)

      (distance, altitudeTotals, timeTotals)
    }

    val defaultTimeTotals = Some(TimeTotals(Seconds.ZERO, 0, 0, 0))
    
    val (distance, altitudeTotals, timeTotals) = if (points.isEmpty)
      (0d, None, defaultTimeTotals)
    else if (points.size == 1)
      (0d, Some(AltitudeTotals(0, 0)), defaultTimeTotals)
    else {
      calculateTotals
    }

    TrackTotals(distance, altitudeTotals, timeTotals)
  }
}

/**
 * Calculates distance using Haversine formula.
 *
 * @see [[http://en.wikipedia.org/wiki/Haversine_formula Haversine formula]]
 */
object Haversine {
  private val EarthRadiusInM = 6372.8 * 1000

  def calculateDistance(point1: TrackPoint, point2: TrackPoint): Double = {
    import math.{toRadians, pow, sin, asin, sqrt, cos}

    val latitude1 = point1.latitude
    val latitude2 = point2.latitude
    val latitudeDistance = toRadians(latitude2 - latitude1)

    val longitude1 = point1.longitude
    val longitude2 = point2.longitude
    val longitudeDistance = toRadians(longitude2 - longitude1)

    val a = pow(sin(latitudeDistance / 2), 2) +
      pow(sin(longitudeDistance / 2), 2) *
        cos(toRadians(latitude1)) *
        cos(toRadians(latitude2))

    val c = 2 * asin(sqrt(a))

    EarthRadiusInM * c
  }
}

case class Segment(point1: TrackPoint, point2: TrackPoint)
