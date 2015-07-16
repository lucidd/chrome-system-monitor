package monitor

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import chrome.events.EventSource

class TickSource[T](val sampleInterval: FiniteDuration, fun: => Future[T]) extends EventSource[T] {

  class Subscription(fn: T => Unit) extends chrome.events.Subscription {
    def cancel(): Unit = {

    }
  }

  def listen(fn: T => Unit): Subscription = {
    ???
  }
}

object Timeline {

  trait Listener[T] {
    def update(value: T): Unit
  }

}

class Timeline[T](val sampleCount: Int, val sampleInterval: FiniteDuration)(fun: => Future[T]) {

  import Timeline._

  private var _samples: List[T] = List()
  private var listeners = collection.mutable.ListBuffer[Listener[Timeline[T]]]()
  private var intervalHandler: Option[js.timers.SetIntervalHandle] = None

  private def addSample(sample: T): Unit = {
    _samples = (sample :: _samples).take(sampleCount)
    listeners.map(_.update(this))
  }

  def samples = _samples

  def addListener(listener: Listener[Timeline[T]]) = {
    listeners += listener
  }

  def removeListener(listener: Listener[Timeline[T]]) = {
    listeners -= listener
  }

  private def tick(): Unit = {
    fun.onSuccess { case s => addSample(s) }
  }

  def start() = intervalHandler match {
    case None => intervalHandler = Some(js.timers.setInterval(sampleInterval)(tick))
    case _ =>
  }

  def stop() = intervalHandler foreach js.timers.clearInterval

}
