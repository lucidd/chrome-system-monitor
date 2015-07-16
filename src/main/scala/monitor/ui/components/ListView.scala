package ui.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.ReactComponentC.ReqProps

object ListView {


  case class State[T](items: List[T], selected: Option[T] = None)
  case class Props[T](items: List[T], selected: Option[T], itemView: (T, Boolean) => ReactNode, onSelect: (T) => Unit)

  class Backend[T](scope: BackendScope[Props[T], State[T]]) {

    def select(item: T) = {
      scope.modState(_.copy(selected = Some(item)))
      scope.props.onSelect(item)
    }

    def isSelected(item: T): Boolean = {
      scope.state.selected.exists(_ == item)
    }

  }

    def apply[T]() = ReactComponentB[Props[T]]("ListView")
      .initialStateP(p => State(p.items, p.selected))
      .backend(new Backend(_))
      .render { (p, s, b) => {
          ul()(
            for (item <- s.items) yield {
              li(onClick ==> ((e: ReactEvent) => b.select(item)))(
                p.itemView(item, b.isSelected(item))
              )
            }
          )
        }
      }
      .build



}
