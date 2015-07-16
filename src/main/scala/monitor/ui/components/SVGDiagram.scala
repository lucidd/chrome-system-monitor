package monitor.ui.components

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.all._
import monitor.math._


object SVGDiagram {

  case class Props(data: List[Point], view: (List[Point] => Rectangle), style: Style = defaultStyle)

  case class State(bounds: Rectangle)

  trait Style {

    import svg._

    def gridLine: TagMod = Seq(
      vectorEffect.nonScalingStroke,
      strokeWidth := "2px"
    )

    def grid: TagMod = Seq(
      strokeOpacity := 0.1,
      stroke := "#2A2A2A"
    )

    def path: TagMod = Seq(
      vectorEffect.nonScalingStroke,
      strokeWidth := "2px"
    )

    def root: TagMod = Seq(
      flex := 1,
      backgroundColor := "#ffffff",
      shapeRendering := "geometricPrecision",
      borderRadius := "5px"
    )

  }


  def fit(data: List[Point]): Rectangle = {
    data.foldLeft(Rectangle.ZERO)(Rectangle.union)
  }

  object defaultStyle extends Style

  //def barChart(p: Props): ReactTag = {
  //  val bounds = p.data.foldLeft(Rectangle.ZERO)(Rectangle.union)
  //  import svg._
  //  svg(width := "100%", height := "100%", viewBox := s"0 0 ${bounds.max.x} ${bounds.max.y}", preserveAspectRatio := "none")(
  //    g(transform := "translate(0, 1)", fill := "black", stroke := "none", strokeWidth := 0)(
  //      g(transform := "scale(1,-1)", fill := "#bada55")(
  //        for ((data, index) <- p.data.zipWithIndex) yield {
  //          rect(x := index, y := 0, width := 1, height := data)
  //        }
  //      )
  //    )
  //  )
  //}

  def grid(p: Props, bounds: Rectangle): ReactTag = {
    import svg._
    g(p.style.grid)(
      for (index <- 0 to 5) yield {
        line(p.style.gridLine)(x1 := 0, y1 := index / 5.0, x2 := bounds.max.x, y2 := index / 5.0)
      }
    )
  }

  def lineChart(p: Props): ReactTag = {
    val bounds = p.view(p.data)
    import svg._
    val pathData = p.data.foldLeft(new StringBuilder("M0 0")) { case (acc, data) =>
      acc.append(s" ${data.x} ${data.y}")
    }.append(s"V0 ").toString

    svg(p.style.root)(width := "100%", height := "100%", viewBox := s"0 0 ${bounds.max.x} ${bounds.max.y}", preserveAspectRatio := "none")(
      g(transform := "translate(0, 1)", fill := "black", stroke := "#bada55", strokeWidth := 0.01)(
        g(transform := "scale(1,-1)", fill := "#bada55", fillOpacity := 0.3)(
          path(p.style.path)(d := pathData),
          grid(p, bounds)
        )
      )
    )
  }

  val component = ReactComponentB[Props]("SVGDiagram")
    .render { p =>
    lineChart(p)
  }
    .build

}
