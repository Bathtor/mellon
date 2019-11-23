package com.lkroll.web.mellon

object Utils {
  def byteToUnsignedInt(b: Byte): Int = b.toInt & 0xff;
}
