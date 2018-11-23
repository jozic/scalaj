package com.daodecode.scalaj.collection.test

import com.daodecode.scalaj.collection._
import org.scalatest.{Matchers, WordSpec}

import scala.collection.generic.CanBuildFrom
import scala.collection.{immutable, mutable}

class SimpleJConvertersTest extends WordSpec with Matchers {

  "SeqConverters" should {

    def acceptJListOf[A](jl: JList[A]) = jl

    def checkMutableSeq[MS <: mutable.Seq[Int]](implicit cbf: CanBuildFrom[MS, Int, MS]): Unit = {
      val mSeq = (cbf() += 2).result()
      mSeq should be(Seq(2))
      mSeq.deepAsJava[Int].set(0, 5)
      mSeq should be(Seq(5))
    }

    def checkMutableBuffer[MB <: mutable.Buffer[Int]](implicit cbf: CanBuildFrom[MB, Int, MB]): Unit = {
      val mBuf = (cbf() += 2).result()
      mBuf should be(mutable.Buffer(2))
      val jlist = mBuf.deepAsJava[Int]
      jlist.set(0, 5)
      mBuf should be(mutable.Buffer(5))
      jlist.add(10)
      mBuf should be(mutable.Buffer(5, 10))

    }

    "convert lists of primitives properly" in {

      "acceptJListOf[JByte](List[Byte](1, 2, 3))" shouldNot compile
      "acceptJListOf[JByte](List[Byte](1, 2, 3).asJava)" shouldNot compile
      acceptJListOf[JByte](List[Byte](1, 2, 3).deepAsJava)

      "acceptJListOf[JShort](List(1, 2, 3))" shouldNot compile
      "acceptJListOf[JShort](List(1, 2, 3).asJava)" shouldNot compile
      acceptJListOf[JShort](List[Short](1, 2, 3).deepAsJava)

      "acceptJListOf[JInt](List(1, 2, 3))" shouldNot compile
      "acceptJListOf[JInt](List(1, 2, 3).asJava)" shouldNot compile
      acceptJListOf[JInt](List(1, 2, 3).deepAsJava)

      "acceptJListOf[JLong](List(1L, 2L, 3L))" shouldNot compile
      "acceptJListOf[JLong](List(1L, 2L, 3L).asJava)" shouldNot compile
      acceptJListOf[JLong](List(1L, 2L, 3L).deepAsJava)

      "acceptJListOf[JFloat](List(1F, 2F, 3F))" shouldNot compile
      "acceptJListOf[JFloat](List(1F, 2F, 3F).asJava)" shouldNot compile
      acceptJListOf[JFloat](List(1f, 2f, 3f).deepAsJava)

      "acceptJListOf[JDouble](List(1D, 2D, 3D))" shouldNot compile
      "acceptJListOf[JDouble](List(1D, 2D, 3D).asJava)" shouldNot compile
      acceptJListOf[JDouble](List(1d, 2d, 3d).deepAsJava)

      "acceptJListOf[JChar](List('a', 'b'))" shouldNot compile
      "acceptJListOf[JChar](List('a', 'b').asJava)" shouldNot compile
      acceptJListOf[JChar](List('a', 'b').deepAsJava)

      "acceptJListOf[JBoolean](List(true, false))" shouldNot compile
      "acceptJListOf[JBoolean](List(true, false).asJava)" shouldNot compile
      acceptJListOf[JBoolean](List(true, false).deepAsJava)
    }

    "convert lists of non-primitives properly" in {
      case class Boo(i: Int)
      acceptJListOf[Boo](List(Boo(3), Boo(5)).asJava)
      acceptJListOf[Boo](List(Boo(3), Boo(5)).deepAsJava)
    }

    "allow custom converters" in {
      implicit val intToString = JConverter[Int, String](_ + 1.toString)
      val asJava: JList[String] = List(1, 2, 3).deepAsJava
      asJava.get(0) should be("11")
      asJava.get(1) should be("21")
      asJava.get(2) should be("31")
    }

    "support all Seq subclasses" in {
      acceptJListOf(List.empty[Int].deepAsJava)
      acceptJListOf(List(1).deepAsJava)
      acceptJListOf(Vector(1).deepAsJava)
      acceptJListOf(Stream(1).deepAsJava)
      acceptJListOf(Seq(1).deepAsJava)
      acceptJListOf(immutable.Seq(1).deepAsJava)
      acceptJListOf(immutable.IndexedSeq(1).deepAsJava)
      acceptJListOf(immutable.LinearSeq(1).deepAsJava)
      acceptJListOf(immutable.Queue(1).deepAsJava)

      acceptJListOf(mutable.Seq(1).deepAsJava)
      acceptJListOf(mutable.IndexedSeq(1).deepAsJava)
      acceptJListOf(mutable.LinearSeq(1).deepAsJava)

      acceptJListOf(mutable.Buffer(1).deepAsJava)
      acceptJListOf(mutable.ArrayBuffer(1).deepAsJava)
      acceptJListOf(mutable.ListBuffer(1).deepAsJava)
      acceptJListOf(mutable.UnrolledBuffer(1).deepAsJava)

      acceptJListOf(mutable.Queue(1).deepAsJava)
      acceptJListOf(mutable.ArrayStack(1).deepAsJava)
      acceptJListOf(mutable.ArraySeq(1).deepAsJava)
      acceptJListOf(mutable.MutableList(1).deepAsJava)
      acceptJListOf(mutable.ResizableArray(1).deepAsJava)
    }

    "keep mutable seqs mutable" in {
      checkMutableSeq[mutable.Seq[Int]]
      checkMutableSeq[mutable.ArraySeq[Int]]
      checkMutableSeq[mutable.LinearSeq[Int]]
      checkMutableSeq[mutable.IndexedSeq[Int]]
      checkMutableSeq[mutable.MutableList[Int]]
      checkMutableSeq[mutable.ResizableArray[Int]]

      checkMutableSeq[mutable.Queue[Int]]
      checkMutableSeq[mutable.ArrayStack[Int]]

      checkMutableSeq[mutable.Buffer[Int]]
      checkMutableSeq[mutable.ArrayBuffer[Int]]
      checkMutableSeq[mutable.ListBuffer[Int]]
      checkMutableSeq[mutable.UnrolledBuffer[Int]]
    }

    "keep mutable buffers mutable" in {
      checkMutableBuffer[mutable.Buffer[Int]]
      checkMutableBuffer[mutable.ArrayBuffer[Int]]
      checkMutableBuffer[mutable.ListBuffer[Int]]
      checkMutableBuffer[mutable.UnrolledBuffer[Int]]
    }

    "return same scala buffer with primitives and self conversions" in {
      {
        val scalaBuffer = mutable.Buffer(1)
        scalaBuffer.deepAsJava.asScala should be theSameInstanceAs scalaBuffer
      }
      {
        val scalaBuffer = mutable.ArrayBuffer(1)
        scalaBuffer.deepAsJava.asScala should be theSameInstanceAs scalaBuffer
      }
      {
        val scalaBuffer = mutable.ListBuffer(1)
        scalaBuffer.deepAsJava.asScala should be theSameInstanceAs scalaBuffer
      }
      {
        val scalaBuffer = mutable.UnrolledBuffer(1)
        scalaBuffer.deepAsJava.asScala should be theSameInstanceAs scalaBuffer
      }
      {
        class A
        val scalaBuffer = mutable.Buffer(new A)
        scalaBuffer.deepAsJava.asScala should be theSameInstanceAs scalaBuffer
      }
    }
  }

  "ArrayConverters" should {

    def acceptArrayOf[A](ar: Array[A]) = ar

    "convert arrays of primitives properly" in {
      "acceptArrayOf[JByte](Array[Byte](1, 2, 3))" shouldNot compile
      "acceptArrayOf[JByte](Array[Byte](1, 2, 3).asJava)" shouldNot compile
      acceptArrayOf[JByte](Array[Byte](1, 2, 3).deepAsJava)

      "acceptArrayOf[JShort](Array(1, 2, 3))" shouldNot compile
      "acceptArrayOf[JShort](Array(1, 2, 3).asJava)" shouldNot compile
      acceptArrayOf[JShort](Array[Short](1, 2, 3).deepAsJava)

      "acceptArrayOf[JInt](Array[Int](1, 2, 3))" shouldNot compile
      "acceptArrayOf[JInt](Array[Int](1, 2, 3).asJava)" shouldNot compile
      acceptArrayOf[JInt](Array[Int](1, 2, 3).deepAsJava)

      "acceptArrayOf[JLong](Array[Long](1L, 2L, 3L))" shouldNot compile
      "acceptArrayOf[JLong](Array[Long](1L, 2L, 3L).asJava)" shouldNot compile
      acceptArrayOf[JLong](Array[Long](1L, 2L, 3L).deepAsJava)

      "acceptArrayOf[JFloat](Array[Float](1F, 2F, 3F))" shouldNot compile
      "acceptArrayOf[JFloat](Array[Float](1F, 2F, 3F).asJava)" shouldNot compile
      acceptArrayOf[JFloat](Array[Float](1f, 2f, 3f).deepAsJava)

      "acceptArrayOf[JDouble](Array[Double](1D, 2D, 3D))" shouldNot compile
      "acceptArrayOf[JDouble](Array[Double](1D, 2D, 3D).asJava)" shouldNot compile
      acceptArrayOf[JDouble](Array[Double](1d, 2d, 3d).deepAsJava)

      "acceptArrayOf[JChar](Array[Char]('a', 'b'))" shouldNot compile
      "acceptArrayOf[JChar](Array[Char]('a', 'b').asJava)" shouldNot compile
      acceptArrayOf[JChar](Array[Char]('a', 'b').deepAsJava)

      "acceptArrayOf[JBoolean](Array[Boolean](true, false))" shouldNot compile
      "acceptArrayOf[JBoolean](Array[Boolean](true, false).asJava)" shouldNot compile
      acceptArrayOf[JBoolean](Array[Boolean](true, false).deepAsJava)
    }

    "convert arrays of non-primitives properly" in {
      case class Boo(i: Int)
      "acceptArrayOf[Boo](Array(Boo(3), Boo(5)).asJava)" shouldNot compile
      acceptArrayOf[Boo](Array(Boo(3), Boo(5)).deepAsJava)
    }

    "allow custom converters" in {
      implicit val intToString = JConverter[Int, String](_ + 1.toString)
      val asJava: Array[String] = Array(1, 2, 3).deepAsJava
      asJava(0) should be("11")
      asJava(1) should be("21")
      asJava(2) should be("31")
    }

    "return same array with self conversions" in {
      {
        val scalaArray = Array("ss")
        scalaArray.deepAsJava should be theSameInstanceAs scalaArray
      }
      {
        class A
        val scalaArray = Array(new A)
        scalaArray.deepAsJava should be theSameInstanceAs scalaArray
      }
    }

  }

  "SetConverters" should {

    def acceptJSetOf[A](js: JSet[A]) = js

    def checkMutableSet[MS <: mutable.Set[Int]](implicit cbf: CanBuildFrom[MS, Int, MS]): Unit = {
      val mSet = (cbf() += 2).result()
      mSet should be(Set(2))
      mSet.deepAsJava[Int].add(5)
      mSet should be(Set(2, 5))
    }

    "convert sets of primitives properly" in {

      "acceptJSetOf[JByte](Set[Byte](1, 2, 3))" shouldNot compile
      "acceptJSetOf[JByte](Set[Byte](1, 2, 3).asJava)" shouldNot compile
      acceptJSetOf[JByte](Set[Byte](1, 2, 3).deepAsJava)

      "acceptJSetOf[JShort](Set(1, 2, 3))" shouldNot compile
      "acceptJSetOf[JShort](Set(1, 2, 3).asJava)" shouldNot compile
      acceptJSetOf[JShort](Set[Short](1, 2, 3).deepAsJava)

      "acceptJSetOf[JInt](Set(1, 2, 3))" shouldNot compile
      "acceptJSetOf[JInt](Set(1, 2, 3).asJava)" shouldNot compile
      acceptJSetOf[JInt](Set(1, 2, 3).deepAsJava)

      "acceptJSetOf[JLong](Set(1L, 2L, 3L))" shouldNot compile
      "acceptJSetOf[JLong](Set(1L, 2L, 3L).asJava)" shouldNot compile
      acceptJSetOf[JLong](Set(1L, 2L, 3L).deepAsJava)

      "acceptJSetOf[JFloat](Set(1F, 2F, 3F))" shouldNot compile
      "acceptJSetOf[JFloat](Set(1F, 2F, 3F).asJava)" shouldNot compile
      acceptJSetOf[JFloat](Set(1f, 2f, 3f).deepAsJava)

      "acceptJSetOf[JDouble](Set(1D, 2D, 3D))" shouldNot compile
      "acceptJSetOf[JDouble](Set(1D, 2D, 3D).asJava)" shouldNot compile
      acceptJSetOf[JDouble](Set(1d, 2d, 3d).deepAsJava)

      "acceptJSetOf[JChar](Set('a', 'b'))" shouldNot compile
      "acceptJSetOf[JChar](Set('a', 'b').asJava)" shouldNot compile
      acceptJSetOf[JChar](Set('a', 'b').deepAsJava)

      "acceptJSetOf[JBoolean](Set(true, false))" shouldNot compile
      "acceptJSetOf[JBoolean](Set(true, false).asJava)" shouldNot compile
      acceptJSetOf[JBoolean](Set(true, false).deepAsJava)
    }

    "convert sets of non-primitives properly" in {
      case class Boo(i: Int)
      acceptJSetOf[Boo](Set(Boo(3), Boo(5)).asJava)
      acceptJSetOf[Boo](Set(Boo(3), Boo(5)).deepAsJava)
    }

    "allow custom converters" in {
      implicit val intToString = JConverter[Int, String](_ + 1.toString)
      val asJava: JSet[String] = Set(1, 2, 3).deepAsJava

      asJava should contain("11")
      asJava should contain("21")
      asJava should contain("31")
    }

    "support all Set subclasses" in {
      acceptJSetOf(immutable.Set(1).deepAsJava)
      acceptJSetOf(immutable.HashSet(1).deepAsJava)
      acceptJSetOf(immutable.BitSet(1).deepAsJava)
      acceptJSetOf(immutable.ListSet(1).deepAsJava)
      acceptJSetOf(immutable.SortedSet(1).deepAsJava)
      acceptJSetOf(immutable.TreeSet(1).deepAsJava)

      acceptJSetOf(mutable.Set(1).deepAsJava)
      acceptJSetOf(mutable.HashSet(1).deepAsJava)
      acceptJSetOf(mutable.BitSet(1).deepAsJava)
      acceptJSetOf(mutable.SortedSet(1).deepAsJava)
      acceptJSetOf(mutable.TreeSet(1).deepAsJava)
    }

    "keep mutable sets mutable" in {
      checkMutableSet[mutable.Set[Int]]
      checkMutableSet[mutable.HashSet[Int]]
      checkMutableSet[mutable.BitSet]
      checkMutableSet[mutable.SortedSet[Int]]
      checkMutableSet[mutable.TreeSet[Int]]
    }

    "return same mutable scala set with primitives and self conversions" in {
      {
        val scalaSet = mutable.Set(1)
        scalaSet.deepAsJava.asScala should be theSameInstanceAs scalaSet
      }
      {
        val scalaSet = mutable.HashSet(1)
        scalaSet.deepAsJava.asScala should be theSameInstanceAs scalaSet
      }
      {
        val scalaSet = mutable.BitSet(1)
        scalaSet.deepAsJava.asScala should be theSameInstanceAs scalaSet
      }
      {
        val scalaSet = mutable.SortedSet(1)
        scalaSet.deepAsJava.asScala should be theSameInstanceAs scalaSet
      }
      {
        val scalaSet = mutable.TreeSet(1)
        scalaSet.deepAsJava.asScala should be theSameInstanceAs scalaSet
      }
      {
        class A
        val scalaSet = mutable.Set(new A)
        scalaSet.deepAsJava.asScala should be theSameInstanceAs scalaSet
      }
    }
  }

  "MapConverters" should {

    def acceptJMapOf[A, B](jm: JMap[A, B]) = jm

    def checkMutableMap[MM <: mutable.Map[Int, String]](implicit cbf: CanBuildFrom[MM, (Int, String), MM]): Unit = {
      val mMap = (cbf() += 2 -> "two").result()
      mMap should be(Map(2 -> "two"))
      mMap.deepAsJava.put(5, "five")
      mMap should be(Map(2 -> "two", 5 -> "five"))
    }

    "convert maps of primitives properly" in {
      "acceptJMapOf[JByte, JInt](Map[Byte, Int]((1: Byte) -> 2)" shouldNot compile
      "acceptJMapOf[JByte, JInt](Map[Byte, Int]((1: Byte) -> 2).asJava)" shouldNot compile
      acceptJMapOf[JByte, JInt](Map[Byte, Int]((1: Byte) -> 2).deepAsJava)

      "acceptJMapOf[JShort, JLong](Map[Short, Long]((1: Short) -> 2L)" shouldNot compile
      "acceptJMapOf[JShort, JLong](Map[Short, Long]((1: Short) -> 2L).asJava" shouldNot compile
      acceptJMapOf[JShort, JLong](Map[Short, Long]((1: Short) -> 2L).deepAsJava)

      "acceptJMapOf[JFloat, JDouble](Map[Float, Double](1F -> 2D)" shouldNot compile
      "acceptJMapOf[JFloat, JDouble](Map[Float, Double](1F -> 2D).asJava" shouldNot compile
      acceptJMapOf[JFloat, JDouble](Map[Float, Double](1f -> 2d).deepAsJava)

      "acceptJMapOf[JBoolean, JChar](Map[Boolean, Char](true -> 't')" shouldNot compile
      "acceptJMapOf[JBoolean, JChar](Map[Boolean, Char](true -> 't').asJava" shouldNot compile
      acceptJMapOf[JBoolean, JChar](Map[Boolean, Char](true -> 't').deepAsJava)
    }

    "convert maps of non-primitives properly" in {
      case class Boo(i: Int)
      acceptJMapOf[Boo, String](Map(Boo(3) -> "3", Boo(5) -> "5").asJava)
      acceptJMapOf[Boo, String](Map(Boo(3) -> "3", Boo(5) -> "5").deepAsJava)
    }

    "allow custom converters" in {
      implicit val intToString = JConverter[Int, String](_ + 1.toString)
      val asJava: JMap[String, String] = Map("one" -> 1, "two" -> 2, "three" -> 3).deepAsJava

      asJava.get("one") should be("11")
      asJava.get("two") should be("21")
      asJava.get("three") should be("31")
    }

    "support all Map subclasses" in {
      acceptJMapOf(immutable.Map(1 -> "one").deepAsJava)
      acceptJMapOf(immutable.HashMap(1 -> "one").deepAsJava)
      acceptJMapOf(immutable.TreeMap(1 -> "one").deepAsJava)
      acceptJMapOf(immutable.SortedMap(1L -> "one").deepAsJava)
      acceptJMapOf(immutable.IntMap(1 -> "one").deepAsJava)
      acceptJMapOf(immutable.LongMap(1L -> "one").deepAsJava)
      acceptJMapOf(immutable.ListMap(1L -> "one").deepAsJava)

      acceptJMapOf(mutable.Map(1 -> "one").deepAsJava)
      acceptJMapOf(mutable.HashMap(1 -> "one").deepAsJava)
      acceptJMapOf(mutable.ListMap(1L -> "one").deepAsJava)
      acceptJMapOf(mutable.LinkedHashMap(1L -> "one").deepAsJava)
      acceptJMapOf(mutable.OpenHashMap(1L -> "one").deepAsJava)
      acceptJMapOf(mutable.WeakHashMap(1L -> "one").deepAsJava)
    }

    "keep mutable maps mutable" in {
      checkMutableMap[mutable.Map[Int, String]]
      checkMutableMap[mutable.HashMap[Int, String]]
      checkMutableMap[mutable.ListMap[Int, String]]
      checkMutableMap[mutable.LinkedHashMap[Int, String]]
      checkMutableMap[mutable.WeakHashMap[Int, String]]

      //OpenHashMap doesn't provide CanBuildFrom
      val openHashMap: mutable.OpenHashMap[Int, String] = mutable.OpenHashMap(1 -> "one")
      openHashMap.deepAsJava.put(2, "two")
      openHashMap should be(mutable.OpenHashMap(1 -> "one", 2 -> "two"))
    }

    "return same mutable scala map with primitives and self conversions" in {
      {
        val scalaMap = mutable.Map(1 -> "one")
        scalaMap.deepAsJava.asScala should be theSameInstanceAs scalaMap
      }
      {
        val scalaMap = mutable.HashMap(1 -> "one")
        scalaMap.deepAsJava.asScala should be theSameInstanceAs scalaMap
      }
      {
        val scalaMap = mutable.ListMap(1 -> "one")
        scalaMap.deepAsJava.asScala should be theSameInstanceAs scalaMap
      }
      {
        val scalaMap = mutable.LinkedHashMap(1 -> "one")
        scalaMap.deepAsJava.asScala should be theSameInstanceAs scalaMap
      }
      {
        val scalaMap = mutable.OpenHashMap(1 -> "one")
        scalaMap.deepAsJava.asScala should be theSameInstanceAs scalaMap
      }
      {
        class A
        val scalaMap = mutable.Map(new A -> "a")
        scalaMap.deepAsJava.asScala should be theSameInstanceAs scalaMap
      }
      // doesn't hold for WeakHashMap, as it's a wrapper itself
    }
  }
}
