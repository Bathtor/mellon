package com.lkroll.web.mellon

object PassphraseHashOld {
  val ALPHA = Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
    'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'w', 'x', 'y', 'z', '.', ',', '!', '?', '+', '=', '-',
    '_', '(', ')', '{', '}', '[', ']', '%', '#', '@', '<', '>', ':', ';', '0', '1', '2', '3', '4', '5', '6', '7', '8',
    '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
    'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0');

  def hash(passphrase: String): String = {
    val hashBytes = Hashes.SHA1.hash(passphrase);
    val encoded = hashBytes.map(b => {
      val i = Utils.byteToUnsignedInt(b);
      ALPHA(i % ALPHA.length).toByte
    });
    //String.valueOf(encoded)
    new String(encoded, "UTF-8")
  }
}
