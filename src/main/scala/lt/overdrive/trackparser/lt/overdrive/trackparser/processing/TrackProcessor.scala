package lt.overdrive.trackparser.lt.overdrive.trackparser.processing

import _root_.lt.overdrive.trackparser.domain.Track

case class TrackProcessor(track: Track) {
  def calculateTotals(): TrackTotals = ???
}
