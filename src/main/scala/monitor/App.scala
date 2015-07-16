package monitor

import styles.Default
import japgolly.scalajs.react.{ReactComponentB, BackendScope}
import japgolly.scalajs.react.vdom.all._
import monitor.modules._
import org.scalajs.dom
import scalacss.ScalaCssReact._

object App {

  val style = Default

  case class State(currentView: Option[Module])


  case class Backend(scope: BackendScope[_, State]) {
    def select(module: Module): Unit = {
      scope.modState(_.copy(currentView = Some(module)))
    }
  }

  case class Props(views: List[() => ReactTag])

  def centered(element: TagMod) = div(
    display := "flex",
    width := "100%",
    alignItems.center,
    justifyContent.center
  )(element)

  def empty = centered("nothing selected")

  def webgl = {
    val canvas = dom.document.createElement("canvas").asInstanceOf[dom.html.Canvas]
    val gl = canvas.getContext("webgl").asInstanceOf[dom.webgl.RenderingContext]
    div(
      for (ext <- gl.getSupportedExtensions()) yield {
        div(ext)
      }
    )
  }

  val modules = List(
    CPU,
    Memory,
    Network,
    Display,
    About
  )

  val component = ReactComponentB[Unit]("App")
    .initialState(State(Some(CPU)))
    .backend(new Backend(_))
    .render((p, s, b) => {
    div(style.app)(
      div(style.sidebar)(
        for (module <- modules) yield {
          div(
            onClick --> {
              b.select(module)
            },
            style.menuItem(s.currentView.map(_ == module).getOrElse(false))
          )(
              img(style.menuItemIcon)(src := module.iconUrl)
          )
        }
      ),
      div(style.viewStyle)(
        s.currentView.map(_.component).getOrElse(empty)
      )
    )
  }).buildU

}
