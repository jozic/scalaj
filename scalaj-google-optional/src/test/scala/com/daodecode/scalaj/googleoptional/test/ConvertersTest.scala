package com.daodecode.scalaj.googleoptional.test

import com.daodecode.scalaj.collection._
import com.daodecode.scalaj.googleoptional._
import com.google.common.base.Optional
import org.scalatest.{Matchers, WordSpec}

class ConvertersTest extends WordSpec with Matchers {

  def jb(b: Byte): JByte = b

  def js(s: Short): JShort = s

  "AsJava Converter" should {

    class A

    "convert Somes with primitives and any non collection types properly" in {

      def checkPresent[T](optional: Optional[T], expected: T): Unit = {
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

      def checkAbsent[T](optional: Optional[T]): Unit = {
        optional should be(Optional.absent[T])
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
      Some[A](null).deepAsJava should be(Optional.absent[A])
    }

    "convert nested options" in {
      Option(Option(2D)).deepAsJava: GOption[GOption[JDouble]]
    }

    "convert collections in options" in {
      Option(Set(2D)).deepAsJava: GOption[JSet[JDouble]]
    }

    "convert options in collections" in {
      Set(Option(2D)).deepAsJava: JSet[GOption[JDouble]]
      Option(Map(Option(2L) -> Set(Option('a')))).deepAsJava: GOption[JMap[GOption[JLong], JSet[GOption[JChar]]]]
    }
  }

  "AsScala Converter" should {

    class A

    "convert Presents with primitives and any non-collection types properly" in {

      def checkSome[T](option: Option[T], expected: T): Unit = {
        option.get should be(expected)
      }

      checkSome[Byte](Optional.of(jb(1)).deepAsScala, expected = 1: Byte)
      checkSome[Short](Optional.of(js(1)).deepAsScala, expected = 1: Short)
      checkSome[Int](Optional.of(1: JInt).deepAsScala, expected = 1)
      checkSome[Long](Optional.of(1L: JLong).deepAsScala, expected = 1L)
      checkSome[Float](Optional.of(1F: JFloat).deepAsScala, expected = 1F)
      checkSome[Double](Optional.of(1D: JDouble).deepAsScala, expected = 1D)
      checkSome[Char](Optional.of('a': JChar).deepAsScala, expected = 'a')
      checkSome[Boolean](Optional.of(false: JBoolean).deepAsScala, expected = false)

      val a = new A
      checkSome[A](Optional.of(a).deepAsScala, expected = a)
    }

    "convert Absents with primitives and any non collection types properly" in {

      def checkNone[T](option: Option[T]): Unit = {
        option should be(None)
      }

      checkNone[Byte](Optional.absent[JByte].deepAsScala)
      checkNone[Short](Optional.absent[JShort].deepAsScala)
      checkNone[Int](Optional.absent[JInt].deepAsScala)
      checkNone[Long](Optional.absent[JLong].deepAsScala)
      checkNone[Float](Optional.absent[JFloat].deepAsScala)
      checkNone[Double](Optional.absent[JDouble].deepAsScala)
      checkNone[Char](Optional.absent[JChar].deepAsScala)
      checkNone[Boolean](Optional.absent[JBoolean].deepAsScala)

      checkNone[A](Optional.absent[A].deepAsScala)
    }
  }

}
