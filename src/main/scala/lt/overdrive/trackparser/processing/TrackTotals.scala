package lt.overdrive.trackparser.lt.overdrive.trackparser.processing

import org.joda.time.Seconds

case class TrackTotals(distance: Double,
                       time: Option[Seconds],
                       ascent: Option[Double],
                       descent: Option[Double],
                       maxSpeed: Option[Double],
                       minSpeed: Option[Double],
                       speed: Option[Double] = time match {
                         case Some(t) => Some(distance / t.getSeconds)
                         case None => _
                       })