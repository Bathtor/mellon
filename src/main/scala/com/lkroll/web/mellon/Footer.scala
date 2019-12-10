package com.lkroll.web.mellon

import org.scalajs.dom.html.Element
import scalatags.JsDom.all._

object Footer {
  def render(target: Element): Unit = {
    target.appendChild(content.render);
  }

  val content = div(
    id := "footer",
    label("Version"),
    span(s"${BuildInfo.version}"),
    span(cls := "horssep", "|"),
    a(href := "https://github.com/Bathtor/mellon", "Sources", rel := "external")
  );
}
