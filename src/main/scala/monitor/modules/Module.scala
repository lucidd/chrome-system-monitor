package monitor.modules

import japgolly.scalajs.react.vdom.TagMod


trait Module {

  val name: String

  def iconUrl: String
  def component: TagMod

}
