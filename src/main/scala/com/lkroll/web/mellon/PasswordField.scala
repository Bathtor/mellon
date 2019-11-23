package com.lkroll.web.mellon

import org.scalajs.dom
import org.scalajs.dom.document
import scalatags.JsDom.all._

class PasswordField(val idField: String,
                    val placeholderText: String,
                    val editable: Boolean = true,
                    val maxLength: Int,
                    val onChange: String => Unit) {

  import PasswordField._;

  private val readOnly = if (editable) {
    None
  } else {
    Some(readonly)
  };

  private val inner =
    input(`type` := "password",
          `class` := "pw-field",
          id := idField,
          maxlength := maxLength,
          placeholder := placeholderText,
          readOnly,
          autocomplete := "off").render;
  private val showHide = i(`class` := showClasses).render;
  private val copyMe = i(`class` := copyClasses).render;
  private val hiddenField = input(`type` := "hidden").render;

  private val wrapper = span(`class` := "pw-field-wrapper", inner, showHide, copyMe, hiddenField).render;

  def render = {
    inner.oninput = (_e: dom.Event) => {
      onChange(inner.value)
    };
    inner.onfocus = (_e: dom.Event) => {
      inner.setSelectionRange(0, inner.value.length());
    };
    showHide.onclick = (_e: dom.Event) => {
      val shouldShow = showHide.classList.contains("fa-eye");
      if (shouldShow) {
        inner.`type` = "text";
        val classes = showHide.classList;
        classes.remove("fa-eye");
        classes.add("fa-eye-slash");
      } else {
        inner.`type` = "password";
        val classes = showHide.classList;
        classes.remove("fa-eye-slash");
        classes.add("fa-eye");
      }
    }
    copyMe.onclick = (_e: dom.Event) => {
      val content = inner.value;
      hiddenField.value = content;
      hiddenField.`type` = "input";
      hiddenField.select(); //.setSelectionRange(0, inner.value.length());
      document.execCommand("copy");
      println("Copied text!");
      hiddenField.value = "";
      hiddenField.`type` = "hidden";
    };
    wrapper
  };

  def updateValue(newValue: String): Unit = {
    inner.value = newValue;
  }
}
object PasswordField {

  val showClasses = "fas fa-eye input-command";
  val hideClasses = "fas fa-eye-slash input-command";
  val copyClasses = "fas fa-clipboard input-command";

  def apply(idField: String,
            placeholderText: String,
            editable: Boolean = true,
            maxLength: Int = 100,
            onChange: String => Unit = (_: String) => ()): PasswordField =
    new PasswordField(idField, placeholderText, editable, maxLength, onChange);
}
