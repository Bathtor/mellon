package com.lkroll.web.mellon.facade

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.annotation.JSImport

@js.native
trait JSHashes extends js.Object {

  /**
    * Hexadecimal hash encoding from string.
    */
  def hex(s: String): String = js.native;

  /**
    * Hexadecimal hash with HMAC salt key.
    */
  def hex_hmac(key: String, string: String): String = js.native;

  /**
    * Base64 hash encondig from string.
    */
  def b64(s: String): String = js.native;

  /**
    * Base64 hash with HMAC salt key.
    */
  def b64_hmac(key: String, string: String): String = js.native;

  /**
    * Custom hash algorithm values encoding.
    */
  def any(s: String, encoding: String): String = js.native;

  /**
    * Custom hash values encoding with HMAC salt key support.
    */
  def any_hmac(key: String, string: String, encoding: String): String = js.native;

  /**
    * Hashes without encoding the output.
    *
    * (Undocumented originally)
    */
  def raw(s: String): String = js.native;

  /**
    * Enable/disable uppercase hexadecimal returned string.
    *
    * @return this Object.
    */
  def setUpperCase(a: Boolean): this.type = js.native;

  /**
    * Defines a custom base64 pad string.
    *
    * Default is '=' according with the RFC standard.
    *
    * @return this Object.
    */
  def setPad(a: String): this.type = js.native;

  /**
    * Enable/disable UTF-8 character encoding.
    *
    * @return this Object.
    */
  def setUTF8(a: Boolean): this.type = js.native;

  /**
    * Simple self-test to see it is working.
    *
    * Note: This doesn't seem to work within SBT tests at least.
    *
    * @return this Object
    */
  def vm_test(): this.type = js.native;
}

/**
  * MD5 Algorithm Implementation
  */
@JSGlobal("Hashes.MD5")
@js.native
class MD5() extends JSHashes;

/**
  * SHA1 Algorithm Implementation
  */
@JSGlobal("Hashes.SHA1")
@js.native
class SHA1 extends JSHashes;

/**
  * SHA256 Algorithm Implementation
  */
@JSGlobal("Hashes.SHA256")
@js.native
class SHA256 extends JSHashes;

/**
  * SHA512 Algorithm Implementation
  */
@JSGlobal("Hashes.SHA512")
@js.native
class SHA512 extends JSHashes;

/**
  * RMD160 Algorithm Implementation
  */
@JSGlobal("Hashes.RMD160")
@js.native
class RMD160 extends JSHashes;
