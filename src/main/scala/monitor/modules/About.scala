package monitor.modules

import japgolly.scalajs.react.vdom.all._

object About extends Module {

  val name = "About"
  val iconUrl = "/assets/icons/scalable/about.svg"

  val component = ui.components.infoTable (
    ("Source Code", a(target := "_blank", href := "https://github.com/lucidd/chrome-system-monitor")("Github"))
  )

}
