package com.lkroll.web.mellon

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.window
import scalatags.JsDom.all._

object Main {

  val pages: List[Page] = List(OldMellon);

  def main(args: Array[String]) {
    println(s"Args: ${args.mkString}");

    val nojs = document.body.children.namedItem("nojs");
    document.body.removeChild(nojs);
    val content = div(id := "content", h1("Mellon")).render;
    document.body.appendChild(content);
    for (page <- pages) {
      page.render(content);
    }
  }
}
