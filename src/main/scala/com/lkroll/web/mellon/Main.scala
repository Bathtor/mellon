package com.lkroll.web.mellon

import org.scalajs.dom
import org.scalajs.dom.html.Element
import scalatags.JsDom.all._
import org.scalajs.dom.raw.HashChangeEvent

object Main {

  val pages: List[Page] = List(MellonV1, MellonV2);
  val defaultPage = MellonV2;

  val activeClass = "active";

  private var navMap = Map.empty[String, (Element, Page)];
  private var currentPage: Page = defaultPage;

  def main(_args: Array[String]) {
    //println(s"Args: ${args.mkString}");

    val nojs = dom.document.body.children.namedItem("nojs");
    dom.document.body.removeChild(nojs);
    val navigation = div(id := "navi").render;
    val main = div(id := "main", h1("Mellon"), navigation).render;
    val content = div(id := "content", main).render;
    dom.document.body.appendChild(content);
    val navList = for (page <- pages) yield {
      page.render(main);
      page.hide();
      val navEntry = li(a(href := s"#${page.identifier()}", page.title())).render;
      navMap += (page.identifier() -> (navEntry, page));
      navEntry
    };
    navigation.appendChild(ul(navList).render);
    Footer.render(content);

    val urlPage = {
      val id = dom.window.location.hash;
      val idNoHash = id.replace("#", "");
      this.navMap.get(idNoHash) match {
        case Some((_, page)) => page
        case None            => defaultPage
      }
    };
    setActive(Left(urlPage));

    //dom.window.onhashchange = this.updateActivePage(_);
    dom.window.addEventListener("hashchange", this.updateActivePage(_), false);
    // = (e: HashChangeEvent) => {
    //   scribe.info(s"Got a HashChangeEvent $e");
    //   this.updateActivePage()
    // }
  }

  private def updateActivePage(e: HashChangeEvent): Unit = {
    val id = dom.window.location.hash;
    scribe.info(s"New anchor is ${id}");
    val idNoHash = id.replace("#", "");
    setInactive(this.currentPage);
    setActive(Right(idNoHash));
  }

  private def setActive(pageOrId: Either[Page, String]): Unit = {
    val id = pageOrId match {
      case Left(page) => page.identifier()
      case Right(id)  => id
    };
    navMap.get(id) match {
      case Some((entry, page)) => {
        entry.classList.add(activeClass);
        page.show();
        this.currentPage = page;
      }
      case None => scribe.error(s"Could not find navList entry for $id");
    }
  }

  private def setInactive(page: Page): Unit = {
    val id = page.identifier();
    navMap.get(id) match {
      case Some((entry, page)) => {
        entry.classList.remove(activeClass);
        page.hide();
      }
      case None => scribe.error(s"Could not find navList entry for $id");
    }
  }
}
