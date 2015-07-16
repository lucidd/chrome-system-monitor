package ui.components

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.ReactTag
import japgolly.scalajs.react.vdom.all._

import scala.concurrent.Future
import scala.util.{Failure, Success}


object Loading {

    case class Props(future: Future[ReactTag], loading: ReactTag)

    def loading = ReactComponentB[Props]("AsyncComponent")
    .render(props => {
      props.future.value match {
        case Some(Success(value)) => value
        case Some(Failure(error)) => div("failed")
        case None => props.loading
      }
    })


}
