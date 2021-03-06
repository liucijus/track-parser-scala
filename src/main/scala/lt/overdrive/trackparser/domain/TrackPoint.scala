package lt.overdrive.trackparser.domain

import org.joda.time.DateTime

case class TrackPoint(latitude: Double, longitude: Double, altitude: Option[Double] = None, date: Option[DateTime] = None)