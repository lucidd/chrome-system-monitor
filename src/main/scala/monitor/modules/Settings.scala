package monitor.modules

import japgolly.scalajs.react.vdom.all._

object Settings extends Module {

   val name = "Settings"
   val iconUrl = "/assets/icons/scalable/settings.svg"

   val component = ui.components.infoTable (
     ("Source Code", a(target := "_blank", href := "https://github.com/lucidd/chrome-system-monitor")("Github"))
   )

 }
