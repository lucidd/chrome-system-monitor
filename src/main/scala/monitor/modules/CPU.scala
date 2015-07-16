package monitor.modules

import chrome.system.cpu.bindings.{CPUInfo, Processor}
import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import monitor.Timeline
import monitor.Timeline.Listener
import monitor.math._
import monitor.ui.components.SVGDiagram

import scala.concurrent.duration._

object CPU extends Module {

  val name = "CPU"
  val iconUrl = "/assets/icons/scalable/cpu.svg"

  val cpuTimeline = {
    val timeline = new Timeline[chrome.system.cpu.bindings.CPUInfo](61, 1.second)({
      chrome.system.cpu.CPU.getInfo
    })
    timeline.start()
    timeline
  }


  def deltas(history: List[CPUInfo]): List[List[UsageDelta]] = {
    for (prev :: next :: Nil <- history.sliding(2)) yield {
      for ((prev, next) <- prev.processors.zip(next.processors))
        yield calculateDelta(prev.usage, next.usage)
    }.toList
  }.toList

  def total(cores: List[UsageDelta]): UsageDelta = {
    cores.foldLeft(UsageDelta(0, 0, 0)) { (acc, item) =>
      UsageDelta(
        acc.user + item.user,
        acc.kernel + item.kernel,
        acc.idle + item.idle
      )
    }
  }

  case class UsageDelta(user: Double, kernel: Double, idle: Double) {

    def total = user + kernel + idle

    def active = user + kernel

    def percentActive = active / total

    def percentIdle = idle / total

  }

  def calculateDelta(prev: Processor.Usage, next: Processor.Usage): UsageDelta = {
    UsageDelta(
      next.user - prev.user,
      next.kernel - prev.kernel,
      next.idle - prev.idle
    )
  }

  class Backend[T](scope: BackendScope[Timeline[T], _]) extends Listener[Timeline[T]] {
    def update(timeline: Timeline[T]) = {
      scope.forceUpdate()
    }
  }

  val cpuInfoStyle = Seq(
    display.flex,
    flexDirection := "column",
    flex := "1"
  )

  val comp = ReactComponentB[Timeline[CPUInfo]]("CPU")
    .stateless
    .backend(new Backend(_))
    .render(scope => {
    val graphData = deltas(scope.props.samples).map(total)
    div(cpuInfoStyle)(
      SVGDiagram.component(
        SVGDiagram.Props(
          for ((d, i) <- graphData.reverse.zipWithIndex) yield {
            Point(i, d.percentActive)
          },
          (data: List[Point]) => new Rectangle(0, 0, scope.props.sampleCount - 2, 1)
        )
      ),
      for (info <- scope.props.samples.headOption) yield {
        ui.components.infoTable(
          ("Model:", info.modelName),
          ("Architecture:", info.archName),
          ("Features:", info.features.mkString(", ")),
          ("Number of Processors:", info.numOfProcessors)
        )
      }
    )
  }
    ).componentDidMount(scope => {
    scope.props.addListener(scope.backend)
  })
    .componentWillUnmount(scope => {
    scope.props.removeListener(scope.backend)
  })
    .build


  override val component: TagMod = comp(cpuTimeline)


}
