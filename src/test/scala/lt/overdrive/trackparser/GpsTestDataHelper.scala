package lt.overdrive.trackparser

import _root_.lt.overdrive.trackparser.domain.{Track, Trail, TrackPoint}
import com.github.nscala_time.time.Imports._

object GpsTestDataHelper {
  val Point_1 = TrackPoint(54.709699, 25.245331, Some(165.282010), Some(new DateTime("2012-11-24T06:42:35Z")))
  val Point_2 = TrackPoint(54.709651, 25.245321, Some(165.064160), Some(new DateTime("2012-11-24T06:42:36Z")))
  val Point_3 = TrackPoint(54.709612, 25.245299, Some(164.895780), Some(new DateTime("2012-11-24T06:42:37Z")))
  val Point_4 = TrackPoint(54.709577, 25.245242, Some(164.769670), Some(new DateTime("2012-11-24T06:42:38Z")))
  val Point_5 = TrackPoint(54.709552, 25.245176, Some(164.748660), Some(new DateTime("2012-11-24T06:42:39Z")))
  val Point_6 = TrackPoint(54.709523, 25.245106, Some(164.775070), Some(new DateTime("2012-11-24T06:42:40Z")))

  def prepareTrail(points: Seq[TrackPoint]): Trail = Trail(Seq(Track(points)))

  def prepareTrailWithoutAltitude(points: Seq[TrackPoint]): Trail =
    prepareTrail(prepareTrackPointsWithoutAltitude(points))

  def prepareTrackPointsWithoutAltitude(points: Seq[TrackPoint]): Seq[TrackPoint] =
    points.map(p => new TrackPoint(p.latitude, p.longitude, None, p.date))

  def prepareTrailWithoutTime(points: Seq[TrackPoint]): Trail =
    prepareTrail(prepareTrackPointsWithoutTime(points))

  def prepareTrackPointsWithoutTime(points: Seq[TrackPoint]): Seq[TrackPoint] =
    points.map(p => TrackPoint(p.latitude, p.longitude, p.altitude, None))

  def trackOf(points: TrackPoint*) = Track(points.toList)

  def trackWithoutTimeOf(points: TrackPoint*) = Track(prepareTrackPointsWithoutTime(points.toList))

  def trackWithoutAltitudeOf(points: TrackPoint*) = Track(prepareTrackPointsWithoutAltitude(points.toList))
}
