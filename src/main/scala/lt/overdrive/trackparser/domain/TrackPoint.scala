package lt.overdrive.trackparser.domain

import org.joda.time.DateTime

case class TrackPoint(longitude: Double, latitude: Double, altitude: Option[Double], date: Option[DateTime])