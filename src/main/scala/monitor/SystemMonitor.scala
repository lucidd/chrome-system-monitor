package monitor

import styles.Default
import chrome.app.runtime.bindings.LaunchData
import chrome.app.window.bindings.{BoundsSpecification, CreateWindowOptions}
import chrome.app.window._
import japgolly.scalajs.react.React
import org.scalajs.dom.raw.HTMLStyleElement
import org.scalajs.dom
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scalacss.Defaults._
import scalacss.ScalaCssReact._

object SystemMonitor extends utils.ChromeApp {

  override def onLaunched(launchData: LaunchData): Unit = {
    val options = CreateWindowOptions(id = "MainWindow")
    Window.create("assets/html/App.html", options).foreach { window =>
      window.contentWindow.onload = (e: dom.Event) => {
        val style = Default.render[HTMLStyleElement]
        window.contentWindow.document.head.appendChild(style)
        React.render(App.component(), window.contentWindow.document.body)
      }
    }
  }

}
