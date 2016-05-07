package monitor.modules

import chrome.system.network._
import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import org.scalajs.dom

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object Network extends Module {

  val name = "Network"
  val iconUrl = "/assets/icons/scalable/network.svg"


  case class State(interfaces: List[Interface] = List(), online: Boolean = false)
  case class Backend(scope: BackendScope[_, State]) {

    def init() = {
      scope.modState(_.copy(online = dom.window.navigator.onLine))
      dom.window.addEventListener("online", (e: dom.Event) => {
        scope.modState(_.copy(online = dom.window.navigator.onLine))
      })
      dom.window.addEventListener("offline", (e: dom.Event) => {
        scope.modState(_.copy(online = dom.window.navigator.onLine))
      })
      chrome.system.network.Network.getNetworkInterfaces.foreach(ifaces => {
        scope.modState(_.copy(interfaces = ifaces))
      })
    }

  }



  def interfaceView(iface: Interface): ReactTag = {
    div(
      div(
        padding := "15px 10px",
        backgroundColor := "rgb(42, 42, 42)",
        borderRadius := "5px 5px 0px 0px",
        fontSize.large
      )(iface.name),
      ul(
        padding := "10px",
        backgroundColor := "#303535",
        borderRadius := "0px 0px 5px 5px",
        border := "1px solid rgb(42, 42, 42)",
        boxShadow := "inset 0px 0px 10px 0px rgba(40, 40, 40, 0.75)",
        overflow.auto
      )(
        for(configs <- iface.configurations) yield {
          li(s"${configs.address}/${configs.prefixLength}")
        }
      )
    )
  }

  val comp = ReactComponentB[Unit]("NetworkComponent")
      .initialState(State())
      .backend(new Backend(_))
      .render((p, s, b) => {
        div(width := "100%")(
          for(iface <- s.interfaces) yield {
            interfaceView(iface)
          }
        )
      })
      .componentWillMount((s) => s.backend.init())
      .buildU

  val component: TagMod = comp()



}
