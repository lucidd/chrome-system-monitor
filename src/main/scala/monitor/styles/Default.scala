package monitor.styles

import scalacss.Defaults._


object Default extends StyleSheet.Inline {

  import dsl._

  val color1 = c"#3F3F3F"
  val color2 = c"#2A2A2A"

  val app = style(
    display.flex,
    flexDirection.row,
    height(100.%%),
    width(100.%%)
  )

  val viewStyle = style(
    backgroundColor(color1),
    color(c"#ffffff"),
    display.flex,
    flexGrow(1),
    padding(20.px),
    overflow.auto
  )

  val menuItem =  styleF.bool(selected =>
    styleS(
      display.flex,
      flexDirection.row,
      alignItems.center,
      minHeight(60.px),
      minWidth(60.px),
      width(60.px),
      height(60.px),
      transition := "150ms linear all",
      cursor.pointer,
      backgroundColor(if (selected) color1 else color2),
      color(if (selected) c"#ffffff" else c"#000000")
    )
  )

  val sidebar = style(
    minWidth(60.px),
    width(60.px),
    backgroundColor(color2)
  )


  val menuItemLabel = style(
    flexGrow(1),
    display.flex,
    justifyContent.center
  )

  val menuItemIcon = style(
    height(100.%%)
  )

  val boxHeader = style(
    padding(10.px, 15.px),
    backgroundColor.rgb(42, 42, 42),
    borderRadius(5.px, 5.px, 0.px, 0.px),
    fontSize.large
  )

  val boxContent = style(
    padding(10.px),
    backgroundColor(c"#303535"),
    borderRadius(0.px, 0.px, 5.px, 5.px),
    border(1.px, solid, rgb(42, 42, 42)),
    boxShadow := "inset 0px 0px 10px 0px rgba(40, 40, 40, 0.75)",
    overflow.auto
  )


}

