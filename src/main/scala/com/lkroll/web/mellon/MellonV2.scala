package com.lkroll.web.mellon

import org.scalajs.dom
import org.scalajs.dom.document
import scalatags.JsDom.all._
import org.scalajs.dom.raw.Element
import scala.util.{Failure, Success, Try}

import upickle.default.{ReadWriter => RW, macroRW}

case class PhrasebookEntry(website: String, version: Int, settings: PassphraseHashNew.Settings)
object PhrasebookEntry {
  implicit val rw: RW[PhrasebookEntry] = macroRW;
}

object MellonV2 extends Page {

  private var settings = PassphraseHashNew.Settings();

  private val extendedAlpha = input(`type` := "checkbox", id := "extended-alpha", checked).render;
  private val excludedCharacters =
    input(`type` := "text",
          `class` := "input-field",
          id := "excluded-chars",
          placeholder := "e.g., a^$",
          autocomplete := "off").render;
  private val requiredCharacters =
    input(`type` := "text",
          `class` := "input-field",
          id := "required-chars",
          placeholder := "e.g., a^$",
          autocomplete := "off").render;
  private val salt =
    input(`type` := "text",
          `class` := "input-field",
          id := "salt",
          placeholder := "generated or leave empty for none",
          autocomplete := "off").render;
  private val generateSalt = i(`class` := "fas fa-random input-command").render;
  private val website =
    input(`type` := "text",
          `class` := "input-field",
          id := "website",
          placeholder := "google.com",
          autocomplete := "off").render;
  private val phraseVersion =
    input(`type` := "number",
          `class` := "input-field",
          id := "phrase-version",
          min := "0",
          max := "9999",
          step := "1",
          value := "1",
          autocomplete := "off").render;
  private val settingsJson =
    input(`type` := "text",
          `class` := "input-field",
          id := "settings-json",
          placeholder := "paste to load or copy and save",
          autocomplete := "off").render;
  private val settingsCopyMe = i(`class` := PasswordField.copyClasses).render;
  private val passphrase =
    PasswordField(idField = "passphrase-v2",
                  placeholderText = "Speak Friend",
                  editable = true,
                  maxLength = 100,
                  onChange = calculateHash);
  private val passwordShort =
    PasswordField(idField = "password-short", placeholderText = "and enter...", editable = false, maxLength = 8);
  private val passwordFull =
    PasswordField(idField = "password-full", placeholderText = "and enter...", editable = false, maxLength = 26);
  private val container =
    div(
      id := this.identifier(), //
      h2(this.title()), //
      div(
        id := "mask", //
        form(
          h3(`class` := "titlesep", "Settings"),
          div(
            `class` := "input-box",
            p(label(`class` := "input-label", "Extended Alphabet"), extendedAlpha),
            p(label(`class` := "input-label", "Exclude"), span(`class` := "pw-field-wrapper", excludedCharacters)),
            p(label(`class` := "input-label", "Require"), span(`class` := "pw-field-wrapper", requiredCharacters)),
            p(label(`class` := "input-label", "Salt"), span(`class` := "pw-field-wrapper", salt, generateSalt))
          ),
          h3(`class` := "titlesep", "Export"),
          div(
            `class` := "input-box",
            p(`class` := "input-row",
              label(`class` := "input-label", "Website"),
              span(`class` := "pw-field-wrapper", website.render)),
            p(`class` := "input-row",
              label(`class` := "input-label", "Phrase Version"),
              span(`class` := "pw-field-wrapper", phraseVersion.render)),
            p(`class` := "input-row",
              label(`class` := "input-label", "Settings JSON"),
              span(`class` := "pw-field-wrapper", settingsJson.render, settingsCopyMe))
          ),
          h3(`class` := "titlesep", "Input"),
          p(`class` := "input-row", label(`class` := "input-label", "Passphrase"), passphrase.render), //
          h3(`class` := "titlesep", "Results"),
          div(
            `class` := "result-box",
            p(`class` := "input-row", label(`class` := "short-label", "Shortened"), passwordShort.render), //
            p(`class` := "input-row", label(`class` := "full-label", "Full"), passwordFull.render) //
          )
        )
      )
    ).render;

  override def title(): String = "Version 2";
  override def identifier(): String = "mellon-v2";

  override def render(target: dom.html.Element): Unit = {
    extendedAlpha.onchange = (_) => settingsChange();
    excludedCharacters.oninput = (_) => settingsChange();
    requiredCharacters.oninput = (_) => settingsChange();
    salt.oninput = (_) => settingsChange();
    generateSalt.onclick = (_) => insertRandomSalt();
    website.oninput = (_) => settingsChange();
    phraseVersion.oninput = (_) => settingsChange();
    settingsJson.oninput = (_) => loadJson();
    settingsCopyMe.onclick = (_e: dom.Event) => {
      settingsJson.select(); //.setSelectionRange(0, inner.value.length());
      document.execCommand("copy");
      scribe.info("Copied text!");
    };

    target.appendChild(container);
  }

  override def show(): Unit = {
    container.classList.remove(Page.hiddenClass);
  }

  override def hide(): Unit = {
    container.classList.add(Page.hiddenClass);
  }

  private def insertRandomSalt(): Unit = {
    val generatedSalt = PassphraseHashNew.generateSalt();
    salt.value = generatedSalt;
    settingsChange();
  }

  private def produceJson(): Unit = {
    import upickle.default._

    val websiteS = website.value;
    if (websiteS.isEmpty()) {
      highlight(website);
      scribe.error("No JSON produced, as website is missing!");
      return;
    }
    Try(phraseVersion.value.toInt) match {
      case Success(version) => {
        val metadata = PhrasebookEntry(websiteS, version, settings);
        val json = write(metadata);
        //scribe.info(s"Produced JSON: $json");
        settingsJson.value = json;
      }
      case Failure(ex) => {
        highlight(phraseVersion);
        scribe.error(ex);
        return;
      }
    }

    normalise(website);
    normalise(phraseVersion);
  }

  private def loadJson(): Unit = {
    import upickle.default._

    val jsonString = settingsJson.value;
    if (jsonString.isEmpty()) {
      highlight(settingsJson);
      return;
    }

    Try(read[PhrasebookEntry](jsonString)) match {
      case Success(metadata) => {
        this.settings = metadata.settings;
        this.extendedAlpha.checked = metadata.settings.extendedAlpha;
        this.excludedCharacters.value = metadata.settings.excludedCharacters;
        this.requiredCharacters.value = metadata.settings.requiredCharacters;
        metadata.settings.salt match {
          case Some(saltV) => this.salt.value = saltV
          case None        => this.salt.value = ""
        }

        this.phraseVersion.value = metadata.version.toString();
        this.website.value = metadata.website;

        val inputString = passphrase.getValue();
        calculateHash(inputString)
      }
      case Failure(ex) => {
        highlight(settingsJson);
        scribe.error(ex);
        return;
      }
    }

    normalise(settingsJson);
  }

  private def settingsChange(): Unit = {
    val extended = this.extendedAlpha.checked;
    val excluded = this.excludedCharacters.value;
    val required = this.requiredCharacters.value;
    val saltOpt: Option[String] = if (this.salt.value.isEmpty()) {
      None
    } else {
      Some(this.salt.value)
    };
    this.settings = PassphraseHashNew.Settings(
      extendedAlpha = extended,
      excludedCharacters = excluded,
      requiredCharacters = required,
      salt = saltOpt
    );

    produceJson();

    val inputString = passphrase.getValue();
    calculateHash(inputString)
  }

  private def calculateHash(inputString: String): Unit = {
    if (inputString.isEmpty()) {
      passwordFull.updateValue("");
      passwordShort.updateValue("");
      return;
    }
    val hash = PassphraseHashNew.hash(inputString, settings);
    val shortHash = hash.substring(0, 8);
    passwordFull.updateValue(hash);
    passwordShort.updateValue(shortHash);
  }

  private def highlight(el: Element): Unit = {
    el.classList.add("highlighted");
  }
  private def normalise(el: Element): Unit = {
    el.classList.remove("highlighted");
  }
}
