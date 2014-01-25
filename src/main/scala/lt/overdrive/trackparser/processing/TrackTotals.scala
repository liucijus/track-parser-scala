package lt.overdrive.trackparser.lt.overdrive.trackparser.processing

import org.joda.time.Seconds

case class TrackTotals(distance: Double, altitudeTotals: Option[AltitudeTotals], timeTotals: Option[TimeTotals])

case class AltitudeTotals(ascent: Double, descent: Double)

case class TimeTotals(time: Seconds, speed: Double, minSpeed: Double, maxSpeed: Double)