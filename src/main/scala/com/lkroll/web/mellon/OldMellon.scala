package com.lkroll.web.mellon

import org.scalajs.dom
import scalatags.JsDom.all._

object OldMellon extends Page {

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
      id := "old-mellon", //
      h2("Version 1 Compatibility Mode"), //
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

  override def render(target: dom.html.Element): Unit = {
    target.appendChild(container);
  }

  override def show(): Unit = {}

  override def hide(): Unit = {}

  private def calculateHash(inputString: String): Unit = {
    val hash = PassphraseHashOld.hash(inputString);
    val shortHash = hash.substring(0, 8);
    enterFull.updateValue(hash);
    enterShort.updateValue(shortHash);
  }
}
