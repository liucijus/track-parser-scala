package lt.overdrive.trackparser.lt.overdrive.trackparser.processing

import lt.overdrive.trackparser.domain.{TrackPoint, Track}

case class TrackProcessor(track: Track) {
  def calculateTotals(): TrackTotals = {
    def calculateDistance(points: Seq[TrackPoint], left: TrackPoint): Double = points match {
      case Nil => 0
      case head :: tail => Haversine.calculateDistance(left, head) + calculateDistance(tail, head)
    }

    TrackTotals(calculateDistance(), None, None, None, None, None)
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
