package lt.overdrive.trackparser.lt.overdrive.trackparser.processing

import org.joda.time.Seconds

case class TrackTotals(distance: Double, altitudeTotals: Option[AltitudeTotal], timeTotals: Option[TimeTotals])

case class AltitudeTotal(ascent: Double, descent: Double)

case class TimeTotals(time: Seconds, maxSpeed: Double, minSpeed: Double, speed: Double)