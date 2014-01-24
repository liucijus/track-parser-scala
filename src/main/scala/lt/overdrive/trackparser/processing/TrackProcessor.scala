package lt.overdrive.trackparser.lt.overdrive.trackparser.processing

import lt.overdrive.trackparser.domain.{TrackPoint, Track}
import org.joda.time.Seconds

case class TrackProcessor(track: Track) {
  def calculateTotals(): TrackTotals = {
    val points = track.points

    if (points.isEmpty || points.size == 1)
      TrackTotals(0, Some(AltitudeTotal(0, 0)), Some(TimeTotals(Seconds.ZERO, 0, 0, 0)))
    else {
      val pointsWithoutTime = points.filter(_.date.isEmpty)
      val pointsWitoutAltitude = points.filter(_.altitude.isEmpty)

      TrackTotals(0, None, None)
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
