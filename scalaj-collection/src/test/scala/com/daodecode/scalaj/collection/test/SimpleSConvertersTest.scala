package com.daodecode.scalaj.collection.test

import java.util
import java.util.Arrays.asList

import com.daodecode.scalaj.collection._
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.{Buffer => MBuffer, Map => MMap, Set => MSet}
import scala.language.higherKinds
import scala.reflect.ClassTag

class SimpleSConvertersTest extends WordSpec with Matchers
with JListBuilder with JSetBuilder with JMapBuilder {

  "JListConverters" should {

    def acceptBufferOf[A](sb: MBuffer[A]) = {
      val clazz = sb.getClass
      clazz
    }

    def checkMutableBuffer[JL <: JList[Int] : ClassTag](): Unit = {
      val jList: JL = listOf(2)
      jList should be(JList(2))

      jList.deepAsScala += 5
      jList should be(JList(2, 5))
    }

    def checkSameInstance(javaList: JList[_]): Unit = {
      javaList.deepAsScala.asJava should be theSameInstanceAs javaList
    }

    "convert lists of primitives properly" in {
      acceptBufferOf[Byte](asList[JByte](jb(1), jb(2), jb(3)).deepAsScala)
      acceptBufferOf[Short](asList[JShort](js(1), js(2), js(3)).deepAsScala)
      acceptBufferOf[Int](asList[JInt](1, 2, 3).deepAsScala)
      acceptBufferOf[Long](asList[JLong](1L, 2L, 3L).deepAsScala)
      acceptBufferOf[Float](asList[JFloat](1F, 2F, 3F).deepAsScala)
      acceptBufferOf[Double](asList[JDouble](1D, 2D, 3D).deepAsScala)
      acceptBufferOf[Char](asList[JChar]('a', 'b').deepAsScala)
      acceptBufferOf[Boolean](asList[JBoolean](true, false).deepAsScala)
    }

    "convert lists of non-primitives properly" in {
      case class Boo(i: Int)
      acceptBufferOf[Boo](asList(Boo(3), Boo(5)).asScala)
      acceptBufferOf[Boo](asList(Boo(3), Boo(5)).deepAsScala)
    }

    "allow custom converters" in {
      implicit val intToString = SConverter[JInt, String](_ + 1.toString)
      val asScala: MBuffer[String] = asList[JInt](1, 2, 3).deepAsScala
      asScala(0) should be("11")
      asScala(1) should be("21")
      asScala(2) should be("31")
    }

    "support all standard JList subclasses" in {
      acceptBufferOf(new util.ArrayList[JLong]().deepAsScala)
      acceptBufferOf(new util.LinkedList[JLong]().deepAsScala)
    }

    "keep lists mutable" in {
      checkMutableBuffer[util.ArrayList[Int]]()
      checkMutableBuffer[util.LinkedList[Int]]()
    }

    "return same java list with primitives and self conversions" in {
      checkSameInstance(new util.ArrayList[JLong]())
      checkSameInstance(new util.LinkedList[JLong]())

      class A
      checkSameInstance(new util.ArrayList[A]())
    }
  }

  "ArrayConverters" should {

    def acceptArrayOf[A](ar: Array[A]) = ()

    def checkSameInstance(javaArray: Array[_]): Unit = {
      javaArray.deepAsScala should be theSameInstanceAs javaArray
    }

    "convert arrays of primitives properly" in {
      acceptArrayOf[Byte](Array[JByte](jb(1), jb(2), jb(3)).deepAsScala)
      acceptArrayOf[Short](Array[JShort](js(1), js(2), js(3)).deepAsScala)
      acceptArrayOf[Int](Array[JInt](1, 2, 3).deepAsScala)
      acceptArrayOf[Long](Array[JLong](1L, 2L, 3L).deepAsScala)
      acceptArrayOf[Float](Array[JFloat](1F, 2F, 3F).deepAsScala)
      acceptArrayOf[Double](Array[JDouble](1D, 2D, 3D).deepAsScala)
      acceptArrayOf[Char](Array[JChar]('a', 'b').deepAsScala)
      acceptArrayOf[Boolean](Array[JBoolean](true, false).deepAsScala)
    }

    "convert arrays of non-primitives properly" in {
      case class Boo(i: Int)
      acceptArrayOf[Boo](Array(Boo(3), Boo(5)).deepAsScala)
    }

    "allow custom converters" in {
      implicit val intToString = SConverter[Int, String](_ + 1.toString)
      val asScala: Array[String] = Array(1, 2, 3).deepAsScala
      asScala(0) should be("11")
      asScala(1) should be("21")
      asScala(2) should be("31")
    }

    "return same array with primitives and self conversions" in {
      checkSameInstance(Array(1))
      checkSameInstance(Array('s'))
      checkSameInstance(Array(12D))
      class A
      checkSameInstance(Array(new A))
    }

  }


  "JSetConverters" should {

    def acceptMSetOf[A](ms: MSet[A]) = ()

    def checkMutableSet[JS <: JSet[Int] : ClassTag](): Unit = {
      val mSet: JS = setOf(2)
      mSet should be(JSet(2))

      mSet.deepAsScala += 5
      mSet should be(JSet(2, 5))
    }

    def checkSameInstance(javaSet: JSet[_]): Unit = {
      javaSet.deepAsScala.asJava should be theSameInstanceAs javaSet
    }

    "convert sets of primitives properly" in {
      acceptMSetOf[Byte](JSet[JByte](jb(1), jb(2), jb(3)).deepAsScala)
      acceptMSetOf[Short](JSet[JShort](js(1), js(2), js(3)).deepAsScala)
      acceptMSetOf[Int](JSet(1, 2, 3).deepAsScala)
      acceptMSetOf[Long](JSet(1L, 2L, 3L).deepAsScala)
      acceptMSetOf[Float](JSet(1F, 2F, 3F).deepAsScala)
      acceptMSetOf[Double](JSet(1D, 2D, 3D).deepAsScala)
      acceptMSetOf[Char](JSet('a', 'b').deepAsScala)
      acceptMSetOf[Boolean](JSet(true, false).deepAsScala)
    }

    "convert sets of non-primitives properly" in {
      case class Boo(i: Int)
      acceptMSetOf[Boo](JSet(Boo(3), Boo(5)).asScala)
      acceptMSetOf[Boo](JSet(Boo(3), Boo(5)).deepAsScala)
    }

    "allow custom converters" in {
      implicit val intToString = SConverter[Int, String](_ + 1.toString)
      val asScala: MSet[String] = JSet(1, 2, 3).deepAsScala

      asScala should contain("11")
      asScala should contain("21")
      asScala should contain("31")
    }

    "support all standard JSet subclasses" in {
      acceptMSetOf(new util.HashSet[JInt]().deepAsScala)
      acceptMSetOf(new util.TreeSet[JInt]().deepAsScala)
      acceptMSetOf(new util.LinkedHashSet[JInt]().deepAsScala)
    }

    "keep sets mutable" in {
      checkMutableSet[util.HashSet[Int]]()
      checkMutableSet[util.TreeSet[Int]]()
      checkMutableSet[util.LinkedHashSet[Int]]()
    }

    "return same java set with primitives and self conversions" in {
      checkSameInstance(setOf[JInt, util.HashSet[JInt]](1, 2))
      checkSameInstance(setOf[JInt, util.TreeSet[JInt]](1, 2))
      checkSameInstance(setOf[JInt, util.LinkedHashSet[JInt]](1, 2))

      class A
      checkSameInstance(setOf[A, util.HashSet[A]](new A))
    }

  }

  "JMapConverters" should {

    def acceptMMapOf[A, B](sm: MMap[A, B]) = ()

    def checkMutableMap[JM <: JMap[Int, String] : ClassTag](): Unit = {
      val jMap = mapOf[Int, String, JM](2 -> "two")
      jMap should be(JMap(2 -> "two"))

      jMap.deepAsScala update(5, "five")
      jMap should be(JMap(2 -> "two", 5 -> "five"))
    }

    def checkSameInstance(javaMap: JMap[_, _]): Unit = {
      javaMap.deepAsScala.asJava should be theSameInstanceAs javaMap
    }

    "convert maps of primitives properly" in {
      acceptMMapOf[Byte, Int](JMap[JByte, JInt](jb(1) -> 2).deepAsScala)
      acceptMMapOf[Short, Long](JMap[JShort, JLong](js(1) -> 2L).deepAsScala)
      acceptMMapOf[Float, Double](JMap[JFloat, JDouble]((1F: JFloat) -> (2D: JDouble)).deepAsScala)
      acceptMMapOf[Boolean, Char](JMap[JBoolean, JChar]((true: JBoolean) -> ('t': JChar)).deepAsScala)
    }

    "convert maps of non-primitives properly" in {
      case class Boo(i: Int)
      acceptMMapOf[Boo, String](JMap(Boo(3) -> "3", Boo(5) -> "5").asScala)
      acceptMMapOf[Boo, String](JMap(Boo(3) -> "3", Boo(5) -> "5").deepAsScala)
    }

    "allow custom converters" in {
      implicit val intToString = SConverter[Int, String](_ + 1.toString)
      val asScala: MMap[String, String] = JMap[String, Int]("one" -> 1, "two" -> 2, "three" -> 3).deepAsScala

      asScala("one") should be("11")
      asScala("two") should be("21")
      asScala("three") should be("31")
    }

    "support all standard Map subclasses" in {
      acceptMMapOf(new util.HashMap[JInt, String]().deepAsScala)
      acceptMMapOf(new util.IdentityHashMap[JInt, String]().deepAsScala)
      acceptMMapOf(new util.LinkedHashMap[JInt, String]().deepAsScala)
      acceptMMapOf(new util.TreeMap[JInt, String]().deepAsScala)
      acceptMMapOf(new util.WeakHashMap[JInt, String]().deepAsScala)
    }

    "keep mutable maps mutable" in {
      checkMutableMap[util.HashMap[Int, String]]()
      checkMutableMap[util.IdentityHashMap[Int, String]]()
      checkMutableMap[util.LinkedHashMap[Int, String]]()
      checkMutableMap[util.TreeMap[Int, String]]()
      checkMutableMap[util.WeakHashMap[Int, String]]()
    }

    "return same mutable scala map with primitives and self conversions" in {
      checkSameInstance(mapOf[Int, String, util.HashMap[Int, String]](1 -> "one"))
      checkSameInstance(mapOf[Int, String, util.IdentityHashMap[Int, String]](1 -> "one"))
      checkSameInstance(mapOf[Int, String, util.LinkedHashMap[Int, String]](1 -> "one"))
      checkSameInstance(mapOf[Int, String, util.TreeMap[Int, String]](1 -> "one"))
      checkSameInstance(mapOf[Int, String, util.WeakHashMap[Int, String]](1 -> "one"))

      class A
      checkSameInstance(mapOf[A, String, util.HashMap[A, String]](new A -> "a"))
    }

  }

}
