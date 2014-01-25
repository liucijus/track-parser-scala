package lt.overdrive.trackparser.lt.overdrive.trackparser.processing

import lt.overdrive.trackparser.domain.{TrackPoint, Track}
import org.joda.time.Seconds
import scala.annotation.tailrec

case class TrackProcessor(track: Track) {
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

    if (points.isEmpty)
      TrackTotals(0, None, Some(TimeTotals(Seconds.ZERO, 0, 0, 0)))
    else if (points.size == 1)
      TrackTotals(0, Some(AltitudeTotals(0, 0)), Some(TimeTotals(Seconds.ZERO, 0, 0, 0)))
    else {
      val pointsWithoutTime = points.filter(_.date.isEmpty)
      val pointsWithoutAltitude = points.filter(_.altitude.isEmpty)

      val distance = segments.foldLeft[Double](0)((d, s) => d + Haversine.calculateDistance(s.point1, s.point2))

      val timeTotals = if (pointsWithoutTime.isEmpty) {
        val time = Seconds.secondsBetween(points.head.date.get, points.last.date.get)
        val speed = distance / time.getSeconds

        val (minSpeed, maxSpeed) = segments.foldLeft[(Double, Double)]((Double.MaxValue, 0))((totals, segment) => {
          val segmentTime = Seconds.secondsBetween(segment.point1.date.get, segment.point2.date.get)
          val segmentDistance = Haversine.calculateDistance(segment.point1, segment.point2)
          val segmentSpeed = segmentDistance / segmentTime.getSeconds

          val minSpeed = if (totals._1 > segmentSpeed) segmentSpeed else totals._1
          val maxSpeed = if (totals._2 < segmentSpeed) segmentSpeed else totals._2
          (minSpeed, maxSpeed)
        })

        Some(TimeTotals(time, speed, minSpeed, maxSpeed))
      } else None

      val altitudeTotals = if (pointsWithoutAltitude.isEmpty) {
        val (descent, ascent) = segments.foldLeft[(Double, Double)]((0, 0))(
          (totals, segment) => {
            val difference = segment.point1.altitude.get - segment.point2.altitude.get
            if (difference > 0) (totals._1, totals._2 + difference)
            else (totals._1 - difference, totals._2)
          }
        )
        Some(AltitudeTotals(descent, ascent))
      } else None

      TrackTotals(distance, altitudeTotals, timeTotals)
    }
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
