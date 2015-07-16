package monitor.modules

import chrome.system.memory.bindings.MemoryInfo
import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import monitor.Timeline
import monitor.Timeline.Listener
import monitor.math._
import monitor.ui.components.SVGDiagram

import scala.concurrent.duration._

object Memory extends Module {

  val name = "Memory"
  val iconUrl = "/assets/icons/scalable/ram.svg"

  val memoryInfoStyle = Seq(
    display.flex,
    flexDirection := "column",
    flex := "1"
  )

  class Backend[T](scope: BackendScope[Timeline[T], _]) extends Listener[Timeline[T]] {
    def update(timeline: Timeline[T]) = {
      scope.forceUpdate()
    }
  }

  val memoryTimeline = {
    val timeline = new Timeline[MemoryInfo](60, 1.second)({
      chrome.system.memory.Memory.getInfo
    })
    timeline.start()
    timeline
  }

  val comp = ReactComponentB[Timeline[MemoryInfo]]("Memory")
    .stateless
    .backend(new Backend(_))
    .render(scope =>
    div(memoryInfoStyle)(
      SVGDiagram.component(SVGDiagram.Props(
        for ((d, i) <- scope.props.samples.reverse.zipWithIndex) yield {
          Point(i, (d.capacity - d.availableCapacity) / d.capacity)
        },
        (data: List[Point]) => new Rectangle(0, 0, scope.props.sampleCount - 1, 1)
      )),
      for (info <- scope.props.samples.headOption) yield {
        val used = (info.capacity - info.availableCapacity) / (1024 * 1014 * 1024)
        val capacity = info.capacity / (1024 * 1014 * 1024)
        ui.components.infoTable(
          ("Memory", f"$used%3.1fGB / $capacity%3.1fGB")
        )
      }
    )
    )
    .componentDidMount(scope => {
    scope.props.addListener(scope.backend)
  })
    .componentWillUnmount(scope => {
    scope.props.removeListener(scope.backend)
  })
    .build

  override val component: TagMod = comp(memoryTimeline)


}
