package lt.overdrive.trackparser.processing

import lt.overdrive.trackparser.domain.TrackPoint

case class TrackRectangle(southWest: TrackPoint, northEast: TrackPoint) {
  val centerPoint = {
    val northLatitude = northEast.latitude
    val eastLongitude = northEast.longitude

    val latitude = northLatitude - ((northLatitude - southWest.latitude) / 2)
    val longitude = eastLongitude - ((eastLongitude - southWest.longitude) / 2)

    TrackPoint(latitude, longitude)
  }
}