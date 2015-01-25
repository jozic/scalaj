package com.daodecode.scalaj.googleoptional.test

import com.daodecode.scalaj.collection._
import com.daodecode.scalaj.collection.test.{JMapBuilder, JSetBuilder}
import com.daodecode.scalaj.googleoptional._
import com.google.common.base.{Optional => GOption}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.{Map => MMap, Set => MSet}

class ConvertersTest extends WordSpec with Matchers with JSetBuilder with JMapBuilder {

  def jb(b: Byte): JByte = b

  def js(s: Short): JShort = s

  "AsJava Converter" should {

    class A

    "convert Somes with primitives and any non collection types properly" in {

      def checkPresent[T](optional: GOption[T], expected: T): Unit = {
        optional.get() should be(expected)
      }

      checkPresent(Option(1: Byte).deepAsJava, expected = jb(1))
      checkPresent(Option(1: Short).deepAsJava, expected = js(1))

      checkPresent(Option(1).deepAsJava, expected = 1: JInt)
      checkPresent(Option(1L).deepAsJava, expected = 1L: JLong)
      checkPresent(Option(1F).deepAsJava, expected = 1F: JFloat)
      checkPresent(Option(1D).deepAsJava, expected = 1D: JDouble)
      checkPresent(Option('a').deepAsJava, expected = 'a': JChar)
      checkPresent(Option(true).deepAsJava, expected = true: JBoolean)

      val a = new A
      checkPresent(Option(a).deepAsJava, expected = a)
    }

    "convert Nones with primitives and any non-collection types properly" in {

      def checkAbsent[T](optional: GOption[T]): Unit = {
        optional should be(GOption.absent[T])
      }

      checkAbsent[JByte](Option.empty[Byte].deepAsJava)
      checkAbsent[JShort](Option.empty[Short].deepAsJava)
      checkAbsent[JInt](Option.empty[Int].deepAsJava)
      checkAbsent[JLong](Option.empty[Long].deepAsJava)
      checkAbsent[JFloat](Option.empty[Float].deepAsJava)
      checkAbsent[JDouble](Option.empty[Double].deepAsJava)
      checkAbsent[JChar](Option.empty[Char].deepAsJava)
      checkAbsent[JBoolean](Option.empty[Boolean].deepAsJava)

      checkAbsent[A](Option.empty[A].deepAsJava)
    }

    "convert Some of null to absent, as Optional can't have nulls" in {
      Some[A](null).deepAsJava should be(GOption.absent[A])
    }

    "convert nested options" in {
      Option(Option(2D)).deepAsJava
      Option(Option(2D)).deepAsJava: GOption[GOption[JDouble]]
    }

    "convert collections in options" in {
      //requires type ascription
      //      Option(Set(2D)).deepAsJava // doesn't compile
      Option(Set(2D)).deepAsJava: GOption[JSet[JDouble]]
    }

    "convert options in collections" in {
      Set(Option(2D)).deepAsJava
      Set(Option(2D)).deepAsJava: JSet[GOption[JDouble]]

      // following compiles, but infers wrong type
      Option(Map(Option(2L) -> Set(Option('a')))).deepAsJava

      Option(Map(Option(2L) -> Set(Option('a')))).deepAsJava: GOption[JMap[GOption[JLong], JSet[GOption[JChar]]]]
    }
  }

  "AsScala Converter" should {

    class A

    "convert Presents with primitives and any non-collection types properly" in {

      def checkSome[T](option: Option[T], expected: T): Unit = {
        option.get should be(expected)
      }

      checkSome[Byte](GOption.of(jb(1)).deepAsScala, expected = 1: Byte)
      checkSome[Short](GOption.of(js(1)).deepAsScala, expected = 1: Short)
      checkSome[Int](GOption.of(1: JInt).deepAsScala, expected = 1)
      checkSome[Long](GOption.of(1L: JLong).deepAsScala, expected = 1L)
      checkSome[Float](GOption.of(1F: JFloat).deepAsScala, expected = 1F)
      checkSome[Double](GOption.of(1D: JDouble).deepAsScala, expected = 1D)
      checkSome[Char](GOption.of('a': JChar).deepAsScala, expected = 'a')
      checkSome[Boolean](GOption.of(false: JBoolean).deepAsScala, expected = false)

      val a = new A
      checkSome[A](GOption.of(a).deepAsScala, expected = a)
    }

    "convert Absents with primitives and any non collection types properly" in {

      def checkNone[T](option: Option[T]): Unit = {
        option should be(None)
      }

      checkNone[Byte](GOption.absent[JByte].deepAsScala)
      checkNone[Short](GOption.absent[JShort].deepAsScala)
      checkNone[Int](GOption.absent[JInt].deepAsScala)
      checkNone[Long](GOption.absent[JLong].deepAsScala)
      checkNone[Float](GOption.absent[JFloat].deepAsScala)
      checkNone[Double](GOption.absent[JDouble].deepAsScala)
      checkNone[Char](GOption.absent[JChar].deepAsScala)
      checkNone[Boolean](GOption.absent[JBoolean].deepAsScala)

      checkNone[A](GOption.absent[A].deepAsScala)
    }

    "convert nested options" in {
      GOption.of(GOption.of[JDouble](2D)).deepAsScala
      GOption.of(GOption.of[JDouble](2D)).deepAsScala: Option[Option[Double]]
    }

    "convert collections in options" in {
      //requires type ascription
      //      GOption.of(JSet[JDouble](2D)).deepAsScala // doesn't compile
      GOption.of(JSet[JDouble](2D)).deepAsScala: Option[MSet[Double]]
    }

    "convert options in collections" in {
      JSet(GOption.of[JDouble](2D)).deepAsScala
      JSet(GOption.of[JDouble](2D)).deepAsScala: MSet[Option[Double]]

      // following compiles, but infers wrong type
      GOption.of(JMap(GOption.of[JLong](2L) -> JSet(GOption.of[JChar]('a')))).deepAsScala

      GOption.of(JMap(GOption.of[JLong](2L) -> JSet(GOption.of[JChar]('a'))))
        .deepAsScala: Option[MMap[Option[Long], MSet[Option[Char]]]]
    }

  }

}
