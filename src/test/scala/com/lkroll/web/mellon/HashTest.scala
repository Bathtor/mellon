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

  test("Alphabet Length") {
    println(s"Extended: ${PassphraseHashNew.EXTENDED_ALPHA.length}");
    println(s"Minimal: ${PassphraseHashNew.MINIMAL_ALPHA.length}");
  }

  test("New Hasher (Default)") {
    val settings = PassphraseHashNew.Settings();
    println("=== Defaults ===");
    testNewHasher(settings);
  }

  test("New Hasher (Minimal)") {
    val settings = PassphraseHashNew.Settings(extendedAlpha = false);
    println("=== Minimal ===");
    testNewHasher(settings);
  }

  test("New Hasher (Exclude $,&)") {
    val settings = PassphraseHashNew.Settings(excludedCharacters = "$&");
    println("=== Exclude $,& ===");
    testNewHasher(settings);
  }

  test("New Hasher (Require *,_)") {
    val settings = PassphraseHashNew.Settings(extendedAlpha = false, requiredCharacters = "*_");
    println("=== Require *,_ ===");
    testNewHasher(settings);
  }

  private def testNewHasher(settings: PassphraseHashNew.Settings): Unit = {
    val keys = testData.keySet.toArray;
    val hash0 = PassphraseHashNew.hash(keys(0), settings);
    println(hash0);
    val hash1 = PassphraseHashNew.hash(keys(1), settings);
    println(hash1);
    val hash2 = PassphraseHashNew.hash(keys(2), settings);
    println(hash2);
    val hash3 = PassphraseHashNew.hash(keys(3), settings);
    println(hash3);

    hash0 should not equal hash1;
    hash1 should not equal hash2;
    hash2 should not equal hash3;
    hash3 should not equal hash0;
  }
}
