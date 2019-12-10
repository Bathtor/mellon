package com.lkroll.web.mellon

import org.scalajs.dom.html.Element

trait Page {
  def title(): String;
  def identifier(): String;
  //def symbol(): String;
  def render(target: Element): Unit;
  def show(): Unit;
  def hide(): Unit;
}

object Page {
  val hiddenClass: String = "hidden";
}
