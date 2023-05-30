package com.daodecode.scalaj.collection.test

import com.daodecode.scalaj.collection._

import java.util
import java.util.Arrays.asList
import scala.collection.immutable.{Seq => ImSeq}
import scala.collection.mutable.{Buffer => MBuffer, Map => MMap, Set => MSet}
import scala.reflect.ClassTag

class SimpleSConvertersTest extends ScalaJSpec with JListBuilder with JSetBuilder with JMapBuilder {

  "JListConverters" should {

    def acceptBufferOf[A](sb: MBuffer[A]): MBuffer[A] = sb

    def checkMutableBuffer[JL <: JList[Int]: ClassTag](): Unit = {
      val jList: JL = listOf(2)
      jList should be(JList(2))

      jList.deepAsScala += 5
      jList should be(JList(2, 5))
    }

    "convert lists of primitives properly" in {
      "acceptBufferOf[Byte](asList[JByte](jb(1), jb(2), jb(3)))" shouldNot compile
      "acceptBufferOf[Byte](asList[JByte](jb(1), jb(2), jb(3)).asScala)" shouldNot compile
      acceptBufferOf[Byte](asList[JByte](jb(1), jb(2), jb(3)).deepAsScala)

      acceptBufferOf[Short](asList[JShort](js(1), js(2), js(3)).deepAsScala)
      acceptBufferOf[Int](asList[JInt](1, 2, 3).deepAsScala)
      acceptBufferOf[Long](asList[JLong](1L, 2L, 3L).deepAsScala)
      acceptBufferOf[Float](asList[JFloat](1f, 2f, 3f).deepAsScala)
      acceptBufferOf[Double](asList[JDouble](1d, 2d, 3d).deepAsScala)
      acceptBufferOf[Char](asList[JChar]('a', 'b').deepAsScala)
      acceptBufferOf[Boolean](asList[JBoolean](true, false).deepAsScala)
    }

    "convert lists of non-primitives properly" in {
      case class Boo(i: Int)
      "acceptBufferOf[Boo](asList(Boo(3), Boo(5)))" shouldNot compile
      acceptBufferOf[Boo](asList(Boo(3), Boo(5)).asScala)
      acceptBufferOf[Boo](asList(Boo(3), Boo(5)).deepAsScala)
    }

    "allow custom converters" in {
      implicit val intToString = SConverter[JInt, String](i => s"${i}1")
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
      {
        val javaList = new util.ArrayList[JLong]()
        javaList.deepAsScala.asJava should be theSameInstanceAs javaList
      }
      {
        val javaList = new util.LinkedList[JLong]()
        javaList.deepAsScala.asJava should be theSameInstanceAs javaList
      }
      {
        class A
        val javaList = new util.LinkedList[A]()
        javaList.deepAsScala.asJava should be theSameInstanceAs javaList
      }
    }

    "return immutable seq if asked" in {
      import com.daodecode.scalaj.collection.immutable._

      asList[JLong](1L, 2L, 3L).deepAsScalaImmutable: ImSeq[Long]
    }
  }

  "ArrayConverters" should {

    def acceptArrayOf[A](ar: Array[A]) = ar

    "convert arrays of primitives properly" in {
      acceptArrayOf[Byte](Array[JByte](jb(1), jb(2), jb(3)).deepAsScala)
      acceptArrayOf[Short](Array[JShort](js(1), js(2), js(3)).deepAsScala)
      acceptArrayOf[Int](Array[JInt](1, 2, 3).deepAsScala)
      acceptArrayOf[Long](Array[JLong](1L, 2L, 3L).deepAsScala)
      acceptArrayOf[Float](Array[JFloat](1f, 2f, 3f).deepAsScala)
      acceptArrayOf[Double](Array[JDouble](1d, 2d, 3d).deepAsScala)
      acceptArrayOf[Char](Array[JChar]('a', 'b').deepAsScala)
      acceptArrayOf[Boolean](Array[JBoolean](true, false).deepAsScala)
    }

    "convert arrays of non-primitives properly" in {
      case class Boo(i: Int)
      acceptArrayOf[Boo](Array(Boo(3), Boo(5)).deepAsScala)
    }

    "allow custom converters" in {
      implicit val intToString = SConverter[Int, String](i => s"${i}1")
      val asScala: Array[String] = Array(1, 2, 3).deepAsScala
      asScala(0) should be("11")
      asScala(1) should be("21")
      asScala(2) should be("31")
    }

    "return same array with primitives and self conversions" in {
      {
        val javaArray = Array[Int](1)
        javaArray.deepAsScala should be theSameInstanceAs javaArray
      }
      {
        val javaArray = Array[Char]('s')
        javaArray.deepAsScala should be theSameInstanceAs javaArray
      }
      {
        val javaArray = Array[Double](12d)
        javaArray.deepAsScala should be theSameInstanceAs javaArray
      }
      {
        class A
        val javaArray = Array[A](new A)
        javaArray.deepAsScala should be theSameInstanceAs javaArray
      }
    }
  }

  "JSetConverters" should {

    def acceptMSetOf[A](ms: MSet[A]) = ms

    def checkMutableSet[JS <: JSet[Int]: ClassTag](): Unit = {
      val mSet: JS = setOf(2)
      mSet should be(JSet(2))

      mSet.deepAsScala += 5
      mSet should be(JSet(2, 5))
    }

    "convert sets of primitives properly" in {
      acceptMSetOf[Byte](JSet[JByte](jb(1), jb(2), jb(3)).deepAsScala)
      acceptMSetOf[Short](JSet[JShort](js(1), js(2), js(3)).deepAsScala)
      acceptMSetOf[Int](JSet(1, 2, 3).deepAsScala)
      acceptMSetOf[Long](JSet(1L, 2L, 3L).deepAsScala)
      acceptMSetOf[Float](JSet(1f, 2f, 3f).deepAsScala)
      acceptMSetOf[Double](JSet(1d, 2d, 3d).deepAsScala)
      acceptMSetOf[Char](JSet('a', 'b').deepAsScala)
      acceptMSetOf[Boolean](JSet(true, false).deepAsScala)
    }

    "convert sets of non-primitives properly" in {
      case class Boo(i: Int)
      acceptMSetOf[Boo](JSet(Boo(3), Boo(5)).asScala)
      acceptMSetOf[Boo](JSet(Boo(3), Boo(5)).deepAsScala)
    }

    "allow custom converters" in {
      implicit val intToString = SConverter[Int, String](i => s"${i}1")
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
      {
        val javaSet = setOf[JInt, util.HashSet[JInt]](1, 2)
        javaSet.deepAsScala.asJava should be theSameInstanceAs javaSet
      }
      {
        val javaSet = setOf[JInt, util.TreeSet[JInt]](1, 2)
        javaSet.deepAsScala.asJava should be theSameInstanceAs javaSet
      }
      {
        val javaSet = setOf[JInt, util.LinkedHashSet[JInt]](1, 2)
        javaSet.deepAsScala.asJava should be theSameInstanceAs javaSet
      }
      {
        class A
        val javaSet = setOf[A, util.LinkedHashSet[A]](new A)
        javaSet.deepAsScala.asJava should be theSameInstanceAs javaSet
      }
    }

    "return immutable set if asked" in {
      import com.daodecode.scalaj.collection.immutable._

      JSet[JLong](1L, 2L, 3L).deepAsScalaImmutable: Set[Long]
    }

  }

  "JMapConverters" should {

    def acceptMMapOf[A, B](sm: MMap[A, B]) = sm

    def checkMutableMap[JM <: JMap[JInt, String]: ClassTag](): Unit = {
      val jMap = mapOf[JInt, String, JM](ji(2) -> "two")
      jMap should be(JMap(2 -> "two"))

      jMap.deepAsScala.update(5, "five")
      jMap should be(JMap(2 -> "two", 5 -> "five"))
    }

    "convert maps of primitives properly" in {
      acceptMMapOf[Byte, Int](JMap[JByte, JInt](jb(1) -> 2).deepAsScala)
      acceptMMapOf[Short, Long](JMap[JShort, JLong](js(1) -> 2L).deepAsScala)
      acceptMMapOf[Float, Double](JMap[JFloat, JDouble]((1f: JFloat) -> (2d: JDouble)).deepAsScala)
      acceptMMapOf[Boolean, Char](JMap[JBoolean, JChar]((true: JBoolean) -> ('t': JChar)).deepAsScala)
    }

    "convert maps of non-primitives properly" in {
      case class Boo(i: Int)
      acceptMMapOf[Boo, String](JMap(Boo(3) -> "3", Boo(5) -> "5").asScala)
      acceptMMapOf[Boo, String](JMap(Boo(3) -> "3", Boo(5) -> "5").deepAsScala)
    }

    "allow custom converters" in {
      implicit val intToString = SConverter[Int, String](i => s"${i}1")
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
      checkMutableMap[util.HashMap[JInt, String]]()
      checkMutableMap[util.IdentityHashMap[JInt, String]]()
      checkMutableMap[util.LinkedHashMap[JInt, String]]()
      checkMutableMap[util.TreeMap[JInt, String]]()
      checkMutableMap[util.WeakHashMap[JInt, String]]()
    }

    "return same mutable scala map with primitives and self conversions" in {
      {
        val javaMap = mapOf[JInt, String, util.HashMap[JInt, String]](ji(1) -> "one")
        javaMap.deepAsScala.asJava should be theSameInstanceAs javaMap
      }
      {
        val javaMap = mapOf[JInt, String, util.IdentityHashMap[JInt, String]](ji(1) -> "one")
        javaMap.deepAsScala.asJava should be theSameInstanceAs javaMap
      }
      {
        val javaMap = mapOf[JInt, String, util.LinkedHashMap[JInt, String]](ji(1) -> "one")
        javaMap.deepAsScala.asJava should be theSameInstanceAs javaMap
      }
      {
        val javaMap = mapOf[JInt, String, util.TreeMap[JInt, String]](ji(1) -> "one")
        javaMap.deepAsScala.asJava should be theSameInstanceAs javaMap
      }
      {
        val javaMap = mapOf[JInt, String, util.WeakHashMap[JInt, String]](ji(1) -> "one")
        javaMap.deepAsScala.asJava should be theSameInstanceAs javaMap
      }
      {
        class A
        val javaMap = mapOf[A, String, util.HashMap[A, String]](new A -> "a")
        javaMap.deepAsScala.asJava should be theSameInstanceAs javaMap
      }
    }

    "return immutable map if asked" in {
      import com.daodecode.scalaj.collection.immutable._

      JMap[String, JDouble]("1" -> 1.0d).deepAsScalaImmutable: Map[String, Double]
    }
  }

}
