package com.lkroll.web.mellon

import com.lkroll.web.mellon.facade.SHA1;
import org.scalatest._

object HashTest {

  val testString = "abcdefg";
  val testStringSha1 = "2fb5e13419fc89246865e7a324f476ec624e8740";

  val testData: Map[String, String] = Map(
    testString -> "}Jg@o(x.08m2..dr5Ku7",
    "test" -> "8G?k7F#5rI8a3=ou[}P3",
    "1" -> "<2o_giEIQTJnX,Cl0Q+0",
    "A large one walks in a silly Park" -> "i+0%p=ddz5S7H=ESqN0h"
  );
}

class HashTest extends FunSuite with Matchers {
  import HashTest._;

  test("Basic jshashes Crypto") {
    val hasher = new SHA1();
    //hasher.vm_test();
    val hashed = hasher.hex(testString);
    hashed shouldBe testStringSha1;
    // Make sure the state for a hasher is not maintained
    val hashed2 = hasher.hex(testString);
    hashed2 shouldBe testStringSha1;
  }

  test("Raw Strings") {
    val raw = Hashes.SHA1.hash(testString);
    println(s"Raw: ${raw.map(b => Utils.byteToUnsignedInt(b)).mkString(",")}");
    raw.length shouldBe 20; // SHA1 is 20 bytes
  }

  test("Old Mellon Compatibility") {
    testData.foreach {
      case (input, expected) => {
        val output = PassphraseHashOld.hash(input);
        // println(s"""
        // |${expected.toCharArray().zipWithIndex.map(_._2).mkString(" ")}
        // |${output.toCharArray().mkString(" ")}
        // |${expected.toCharArray().mkString(" ")}
        // """.stripMargin)
        output shouldBe expected;
      }
    }
  }
}
