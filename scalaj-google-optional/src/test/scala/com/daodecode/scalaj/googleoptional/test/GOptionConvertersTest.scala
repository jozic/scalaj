package com.daodecode.scalaj.googleoptional.test

import com.daodecode.scalaj.JavaAliases._
import com.daodecode.scalaj.collection.test.{JMapBuilder, JSetBuilder}
import com.daodecode.scalaj.googleoptional._
import com.google.common.base.{Optional => GOption}
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.{Map => MMap, Set => MSet}

class GOptionConvertersTest extends WordSpec with Matchers with JSetBuilder with JMapBuilder {

  def jb(b: Byte): JByte = b

  def sb(b: Byte): Byte = b

  def js(s: Short): JShort = s

  def ss(s: Short): Short = s

  "DeepAsJava Converter" should {

    class A

    "convert Somes with primitives and any non collection types properly" in {

      def checkPresent[T](optional: GOption[T], expected: T): Unit = {
        optional.get() should be(expected)
      }

      checkPresent(Option(1: Byte).deepAsJava, expected = jb(1))
      checkPresent(Option(1: Short).deepAsJava, expected = js(1))

      checkPresent(Option(1).deepAsJava, expected = 1: JInt)
      checkPresent(Option(1L).deepAsJava, expected = 1L: JLong)
      checkPresent(Option(1f).deepAsJava, expected = 1f: JFloat)
      checkPresent(Option(1d).deepAsJava, expected = 1d: JDouble)
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
      val asJava = Option(Option(2d)).deepAsJava
      asJava shouldBe a[GOption[_]]
      asJava.get shouldBe a[GOption[_]]

      Option(Option(2d)).deepAsJava: GOption[GOption[JDouble]]
    }

    "convert collections in options" in {
      val asJava = Option(Set(2d)).deepAsJava
      asJava shouldBe a[GOption[_]]
      asJava.get() shouldBe a[JSet[_]]

      Option(Set(2d)).deepAsJava: GOption[JSet[JDouble]]
    }

    "convert options in collections" in {
      import com.daodecode.scalaj.collection._

      val asJava = Set(Option(2d)).deepAsJava
      asJava shouldBe a[JSet[_]]
      asJava.iterator().next() shouldBe a[GOption[_]]

      val asJava2 = Option(Map(Option(2L) -> Set(Option('a')))).deepAsJava
      asJava2 shouldBe a[GOption[_]]
      asJava2.get shouldBe a[JMap[_, _]]
      asJava2.get.keySet().iterator().next() shouldBe a[GOption[_]]
      asJava2.get.values().iterator().next() shouldBe a[JSet[_]]
      asJava2.get.values().iterator().next().iterator().next() shouldBe a[GOption[_]]

      Option(Map(Option(2L) -> Set(Option('a')))).deepAsJava: GOption[JMap[GOption[JLong], JSet[GOption[JChar]]]]
    }
  }

  "AsJava Converter" should {

    class A

    "convert Somes without touching primitives " in {

      def checkPresent[T](optional: GOption[T], expected: T): Unit = {
        optional.get() should be(expected)
      }

      checkPresent(Option(sb(1)).asJava, expected = sb(1))
      checkPresent(Option(ss(1)).asJava, expected = ss(1))
      checkPresent(Option(1).asJava, expected = 1)
      checkPresent(Option(1L).asJava, expected = 1L)
      checkPresent(Option(1f).asJava, expected = 1f)
      checkPresent(Option(1d).asJava, expected = 1d)
      checkPresent(Option('a').asJava, expected = 'a')
      checkPresent(Option(true).asJava, expected = true)

      val a = new A
      checkPresent(Option(a).deepAsJava, expected = a)
    }

    "convert Nones properly" in {

      def checkAbsent[T](optional: GOption[T]): Unit = {
        optional should be(GOption.absent[T])
      }

      checkAbsent[Byte](Option.empty[Byte].asJava)
      checkAbsent[Short](Option.empty[Short].asJava)
      checkAbsent[Int](Option.empty[Int].asJava)
      checkAbsent[Long](Option.empty[Long].asJava)
      checkAbsent[Float](Option.empty[Float].asJava)
      checkAbsent[Double](Option.empty[Double].asJava)
      checkAbsent[Char](Option.empty[Char].asJava)
      checkAbsent[Boolean](Option.empty[Boolean].asJava)

      checkAbsent[A](Option.empty[A].asJava)
    }

    "convert Some of null to absent, as Optional can't have nulls" in {
      Some[A](null).asJava should be(GOption.absent[A])
    }

    "convert NOT nested options" in {
      val asJava = Option(Option(2d)).asJava
      asJava shouldBe a[GOption[_]]
      asJava.get shouldBe a[Option[_]]

      Option(Option(2d)).asJava: GOption[Option[Double]]
    }

    "convert NOT collections in options" in {
      val asJava = Option(Set(2d)).asJava
      asJava shouldBe a[GOption[_]]
      asJava.get() shouldBe a[Set[_]]

      Option(Set(2d)).asJava: GOption[Set[Double]]
    }
  }

  "DeepAsScala Converter" should {

    class A

    "convert Presents with primitives and any non-collection types properly" in {

      def checkSome[T](option: Option[T], expected: T): Unit = {
        option.get should be(expected)
      }

      checkSome[Byte](GOption.of(jb(1)).deepAsScala, expected = 1: Byte)
      checkSome[Short](GOption.of(js(1)).deepAsScala, expected = 1: Short)
      checkSome[Int](GOption.of(1: JInt).deepAsScala, expected = 1)
      checkSome[Long](GOption.of(1L: JLong).deepAsScala, expected = 1L)
      checkSome[Float](GOption.of(1f: JFloat).deepAsScala, expected = 1f)
      checkSome[Double](GOption.of(1d: JDouble).deepAsScala, expected = 1d)
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
      val asScala = GOption.of(GOption.of[JDouble](2d)).deepAsScala

      asScala shouldBe an[Option[_]]
      asScala.get shouldBe an[Option[_]]

      GOption.of(GOption.of[JDouble](2d)).deepAsScala: Option[Option[Double]]
    }

    "convert collections in options" in {
      //requires type ascription
      val asScala = GOption.of(JSet[JDouble](2d)).deepAsScala
      asScala shouldBe an[Option[_]]
      asScala.get shouldBe an[MSet[_]]

      GOption.of(JSet[JDouble](2d)).deepAsScala: Option[MSet[Double]]
    }

    "convert options in collections" in {
      import com.daodecode.scalaj.collection._

      val asScala = JSet(GOption.of[JDouble](2d)).deepAsScala
      asScala shouldBe an[MSet[_]]
      asScala.head shouldBe an[Option[_]]

      JSet(GOption.of[JDouble](2d)).deepAsScala: MSet[Option[Double]]

      val asScala2 = GOption.of(JMap(GOption.of[JLong](2L) -> JSet(GOption.of[JChar]('a')))).deepAsScala
      asScala2 shouldBe an[Option[_]]
      asScala2.get shouldBe an[MMap[_, _]]
      asScala2.get.head._1 shouldBe an[Option[_]]
      asScala2.get.head._2 shouldBe an[MSet[_]]
      asScala2.get.head._2.head shouldBe an[Option[_]]

      GOption
        .of(JMap(GOption.of[JLong](2L) -> JSet(GOption.of[JChar]('a'))))
        .deepAsScala: Option[MMap[Option[Long], MSet[Option[Char]]]]
    }
  }

  "DeepAsScalaImmutable Converter" should {
    "convert nested collections as immutable if asked" in {
      import com.daodecode.scalaj.collection.immutable._

      val asScala = GOption.of(JSet[JDouble](2d)).deepAsScala
      asScala shouldBe an[Option[_]]
      asScala.get shouldBe an[Set[_]]

      GOption.of(JSet[JDouble](2d)).deepAsScala: Option[Set[Double]]
    }

    "convert collections in options in collections as immutable if asked" in {
      import com.daodecode.scalaj.collection.immutable._

      val asScala = JSet(GOption.of[JDouble](2d)).deepAsScalaImmutable
      asScala shouldBe an[Set[_]]
      asScala.head shouldBe an[Option[_]]

      JSet(GOption.of[JDouble](2d)).deepAsScalaImmutable: Set[Option[Double]]

      val asScala2 = GOption.of(JMap(GOption.of[JLong](2L) -> JSet(GOption.of[JChar]('a')))).deepAsScala
      asScala2 shouldBe an[Option[_]]
      asScala2.get shouldBe an[Map[_, _]]
      asScala2.get.head._1 shouldBe an[Option[_]]
      asScala2.get.head._2 shouldBe an[Set[_]]
      asScala2.get.head._2.head shouldBe an[Option[_]]

      GOption
        .of(JMap(GOption.of[JLong](2L) -> JSet(GOption.of[JChar]('a'))))
        .deepAsScala: Option[Map[Option[Long], Set[Option[Char]]]]
    }

  }

  "AsScala Converter" should {

    class A

    "convert Presents without touching primitives" in {

      def checkSome[T](option: Option[T], expected: T): Unit = {
        option.get should be(expected)
      }

      checkSome[JByte](GOption.of(jb(1)).asScala, expected = jb(1))
      checkSome[JShort](GOption.of(js(1)).asScala, expected = js(1))
      checkSome[JInt](GOption.of(1: JInt).asScala, expected = 1: JInt)
      checkSome[JLong](GOption.of(1L: JLong).asScala, expected = 1L: JLong)
      checkSome[JFloat](GOption.of(1f: JFloat).asScala, expected = 1f: JFloat)
      checkSome[JDouble](GOption.of(1d: JDouble).asScala, expected = 1d: JDouble)
      checkSome[JChar](GOption.of('a': JChar).asScala, expected = 'a': JChar)
      checkSome[JBoolean](GOption.of(false: JBoolean).asScala, expected = false: JBoolean)

      val a = new A
      checkSome[A](GOption.of(a).asScala, expected = a)
    }

    "convert Absents properly" in {

      def checkNone[T](option: Option[T]): Unit = {
        option should be(None)
      }

      checkNone[JByte](GOption.absent[JByte].asScala)
      checkNone[JShort](GOption.absent[JShort].asScala)
      checkNone[JInt](GOption.absent[JInt].asScala)
      checkNone[JLong](GOption.absent[JLong].asScala)
      checkNone[JFloat](GOption.absent[JFloat].asScala)
      checkNone[JDouble](GOption.absent[JDouble].asScala)
      checkNone[JChar](GOption.absent[JChar].asScala)
      checkNone[JBoolean](GOption.absent[JBoolean].asScala)

      checkNone[A](GOption.absent[A].asScala)
    }

    "convert NOT nested options" in {
      val asScala = GOption.of(GOption.of[JDouble](2d)).asScala

      asScala shouldBe an[Option[_]]
      asScala.get shouldBe an[GOption[_]]

      GOption.of(GOption.of[JDouble](2d)).asScala: Option[GOption[JDouble]]
    }

    "convert NOT collections in options" in {
      val asScala = GOption.of(JSet[JDouble](2d)).asScala
      asScala shouldBe an[Option[_]]
      asScala.get shouldBe an[JSet[_]]

      GOption.of(JSet[JDouble](2d)).asScala: Option[JSet[JDouble]]
    }

  }

}
