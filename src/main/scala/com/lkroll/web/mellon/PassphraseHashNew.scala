package com.lkroll.web.mellon

import java.util.UUID;
import java.nio.{ByteBuffer, ByteOrder};
import java.{util => ju}

import upickle.default.{ReadWriter => RW, macroRW}

object PassphraseHashNew {
  val EXTENDED_ALPHA = Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
    'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'w', 'x', 'y', 'z', '.', ',', '!', '?', '+',
    '=', '-', '_', '(', ')', '{', '}', '[', ']', '%', '#', '@', '<', '>', ':', ';', '*', '&', '^', '$', '@', '~', '`',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'X',
    'Y', 'Z');

  val MINIMAL_ALPHA = Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
    'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'w', 'x', 'y', 'z', '-', '_', '?', 'A', 'B',
    'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z');

  def hash(passphrase: String, settings: Settings): String = {
    val salted = settings.salt match {
      case Some(salt) => s"${salt}|${passphrase}"
      case None       => passphrase
    };
    val hashBytes = Hashes.RMD160.hash(salted);
    val alphabet: Array[Char] = {
      val baseAlpha = if (settings.extendedAlpha) {
        EXTENDED_ALPHA
      } else {
        MINIMAL_ALPHA
      };
      if (settings.excludedCharacters.isEmpty()) {
        baseAlpha
      } else {
        val exclusions = settings.excludedCharacters.toCharArray().toSet;
        val limitedAlpha = Array.newBuilder[Char];
        for (c <- baseAlpha) {
          if (!exclusions.contains(c)) {
            limitedAlpha += c;
          }
        }
        limitedAlpha.result()
      }
    };
    val encoded = encode(hashBytes, alphabet);
    if (settings.requiredCharacters.isEmpty()) {
      encoded
    } else {
      var required = settings.requiredCharacters.toCharArray().toSet;
      for (c <- encoded) {
        required -= c;
      }
      val remaining = new String(required.toArray);
      s"${encoded}${remaining}"
    }
  }

  def generateSalt(): String = {
    val id = UUID.randomUUID();
    val buffer = ByteBuffer.allocate(16);
    buffer.order(ByteOrder.BIG_ENDIAN);
    val idUpper = id.getMostSignificantBits();
    buffer.putLong(idUpper);
    val idLower = id.getLeastSignificantBits();
    buffer.putLong(idLower);
    val bytes = buffer.array;
    encode(bytes, EXTENDED_ALPHA)
  }

  private def encode(bytes: Array[Byte], alphabet: Array[Char]): String = {
    val encoded = bytes.map(b => {
      val i = Utils.byteToUnsignedInt(b);
      alphabet(i % alphabet.length).toByte
    });
    new String(encoded, "UTF-8")
  }

  case class Settings(
      val extendedAlpha: Boolean = true,
      val excludedCharacters: String = "",
      val requiredCharacters: String = "",
      val salt: Option[String] = None
  )
  object Settings {
    implicit val rw: RW[Settings] = macroRW;
  }
}
