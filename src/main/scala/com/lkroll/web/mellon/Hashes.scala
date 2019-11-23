package com.lkroll.web.mellon

object Hashes {
  lazy val SHA1 = new Hasher(new facade.SHA1());
  lazy val MD5 = new Hasher(new facade.MD5());
  lazy val SHA256 = new Hasher(new facade.SHA256());
  lazy val SHA512 = new Hasher(new facade.SHA512());
  lazy val RMD160 = new Hasher(new facade.RMD160());

  def sha1(upperCase: Boolean = false, pad: String = "=", utf8: Boolean = true): Hasher = {
    val h = new facade.SHA1();
    h.setUpperCase(upperCase);
    h.setPad(pad);
    h.setUTF8(utf8);
    new Hasher(h)
  }
}

class Hasher(private val inner: facade.JSHashes) {
  def hash(s: String): Array[Byte] = {
    inner.raw(s).toCharArray().map(_.toByte)
  }
}
