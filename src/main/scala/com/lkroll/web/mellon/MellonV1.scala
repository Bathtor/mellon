package com.lkroll.web.mellon

import org.scalajs.dom
import scalatags.JsDom.all._

object MellonV1 extends Page {

  private val speakFriend =
    PasswordField(idField = "speakFriend",
                  placeholderText = "Speak Friend",
                  editable = true,
                  maxLength = 100,
                  onChange = calculateHash);
  private val enterShort =
    PasswordField(idField = "andEnterShort", placeholderText = "and enter...", editable = false, maxLength = 8);
  private val enterFull =
    PasswordField(idField = "andEnter", placeholderText = "and enter...", editable = false, maxLength = 20);
  private val container =
    div(
      id := this.identifier(), //
      h2(this.title()), //
      div(
        id := "mask", //
        form(
          p(label(`class` := "input-label", "Passphrase"), speakFriend.render), //
          hr, //
          div(`class` := "result-box",
              p(label(`class` := "short-label", "Shortened"), enterShort.render), //
              p(label(`class` := "full-label", "Full"), enterFull.render) //
          )
        )
      )
    ).render;

  override def title(): String = "Version 1 Compatibility Mode";
  override def identifier(): String = "mellon-v1";

  override def render(target: dom.html.Element): Unit = {
    target.appendChild(container);
  }

  override def show(): Unit = {
    container.classList.remove(Page.hiddenClass);
  }

  override def hide(): Unit = {
    container.classList.add(Page.hiddenClass);
  }

  private def calculateHash(inputString: String): Unit = {
    val hash = PassphraseHashOld.hash(inputString);
    val shortHash = hash.substring(0, 8);
    enterFull.updateValue(hash);
    enterShort.updateValue(shortHash);
  }
}
