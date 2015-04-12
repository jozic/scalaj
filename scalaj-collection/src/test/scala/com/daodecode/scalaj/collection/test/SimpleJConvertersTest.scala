package com.daodecode.scalaj.collection.test

import com.daodecode.scalaj.collection._
import org.scalatest.{Matchers, WordSpec}

import scala.collection.generic.CanBuildFrom
import scala.collection.{immutable, mutable}
import scala.language.{higherKinds, reflectiveCalls}

class SimpleJConvertersTest extends WordSpec with Matchers {

  "SeqConverters" should {

    def acceptJListOf[A](jl: JList[A]) = {
      val clazz = jl.getClass
      clazz
    }

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

    def checkSameInstance(scalaBuffer: mutable.Buffer[_]): Unit = {
      scalaBuffer.deepAsJava.asScala should be theSameInstanceAs scalaBuffer
    }

    "convert lists of primitives properly" in {

      //    acceptJListOf[JByte](List[Byte](1, 2, 3)) //doesn't compile
      //    acceptJListOf[JByte](List[Byte](1, 2, 3).asJava) // doesn't compile
      acceptJListOf[JByte](List[Byte](1, 2, 3).deepAsJava)

      //    acceptJListOf[JShort](List(1, 2, 3)) //doesn't compile
      //    acceptJListOf[JShort](List(1, 2, 3).asJava) // doesn't compile
      acceptJListOf[JShort](List[Short](1, 2, 3).deepAsJava)

      //    acceptJListOf[JInt](List(1, 2, 3)) //doesn't compile
      //    acceptJListOf[JInt](List(1, 2, 3).asJava) // doesn't compile
      acceptJListOf[JInt](List(1, 2, 3).deepAsJava)

      //    acceptJListOf[JLong](List(1L, 2L, 3L)) //doesn't compile
      //    acceptJListOf[JLong](List(1L, 2L, 3L).asJava) // doesn't compile
      acceptJListOf[JLong](List(1L, 2L, 3L).deepAsJava)

      //    acceptJListOf[JFloat](List(1F, 2F, 3F)) //doesn't compile
      //    acceptJListOf[JFloat](List(1F, 2F, 3F).asJava) // doesn't compile
      acceptJListOf[JFloat](List(1F, 2F, 3F).deepAsJava)

      //    acceptJListOf[JDouble](List(1D, 2D, 3D)) //doesn't compile
      //    acceptJListOf[JDouble](List(1D, 2D, 3D).asJava) // doesn't compile
      acceptJListOf[JDouble](List(1D, 2D, 3D).deepAsJava)

      //    acceptJListOf[JChar](List('a', 'b')) //doesn't compile
      //    acceptJListOf[JChar](List('a', 'b').asJava) // doesn't compile
      acceptJListOf[JChar](List('a', 'b').deepAsJava)

      //    acceptJListOf[JBoolean](List(true, false)) //doesn't compile
      //    acceptJListOf[JBoolean](List(true, false).asJava) // doesn't compile
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
      acceptJListOf(mutable.Stack(1).deepAsJava)
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
      checkMutableSeq[mutable.Stack[Int]]
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
      checkSameInstance(mutable.Buffer(1))
      checkSameInstance(mutable.ArrayBuffer(1))
      checkSameInstance(mutable.ListBuffer(1))
      checkSameInstance(mutable.UnrolledBuffer(1))

      class A
      checkSameInstance(mutable.Buffer(new A))
    }
  }

  "ArrayConverters" should {

    def acceptArrayOf[A](ar: Array[A]) = ()

    def checkSameInstance(scalaArray: Array[_]): Unit = {
      scalaArray.deepAsJava should be theSameInstanceAs scalaArray
    }


    "convert arrays of primitives properly" in {

      //          acceptArrayOf[JByte](Array[Byte](1, 2, 3)) //doesn't compile
      //          acceptArrayOf[JByte](Array[Byte](1, 2, 3).asJava) // doesn't compile
      acceptArrayOf[JByte](Array[Byte](1, 2, 3).deepAsJava)

      //    acceptArrayOf[JShort](Array(1, 2, 3)) //doesn't compile
      //    acceptArrayOf[JShort](Array(1, 2, 3).asJava) // doesn't compile
      acceptArrayOf[JShort](Array[Short](1, 2, 3).deepAsJava)

      //    acceptArrayOf[JInt](Array(1, 2, 3)) //doesn't compile
      //    acceptArrayOf[JInt](Array(1, 2, 3).asJava) // doesn't compile
      acceptArrayOf[JInt](Array(1, 2, 3).deepAsJava)

      //    acceptArrayOf[JLong](Array(1L, 2L, 3L)) //doesn't compile
      //    acceptArrayOf[JLong](Array(1L, 2L, 3L).asJava) // doesn't compile
      acceptArrayOf[JLong](Array(1L, 2L, 3L).deepAsJava)

      //    acceptArrayOf[JFloat](Array(1F, 2F, 3F)) //doesn't compile
      //    acceptArrayOf[JFloat](Array(1F, 2F, 3F).asJava) // doesn't compile
      acceptArrayOf[JFloat](Array(1F, 2F, 3F).deepAsJava)

      //    acceptArrayOf[JDouble](Array(1D, 2D, 3D)) //doesn't compile
      //    acceptArrayOf[JDouble](Array(1D, 2D, 3D).asJava) // doesn't compile
      acceptArrayOf[JDouble](Array(1D, 2D, 3D).deepAsJava)

      //    acceptArrayOf[JChar](Array('a', 'b')) //doesn't compile
      //    acceptArrayOf[JChar](Array('a', 'b').asJava) // doesn't compile
      acceptArrayOf[JChar](Array('a', 'b').deepAsJava)

      //    acceptArrayOf[JBoolean](Array(true, false)) //doesn't compile
      //    acceptArrayOf[JBoolean](Array(true, false).asJava) // doesn't compile
      acceptArrayOf[JBoolean](Array(true, false).deepAsJava)
    }

    "convert arrays of non-primitives properly" in {
      case class Boo(i: Int)
      //      acceptArrayOf[Boo](Array(Boo(3), Boo(5)).asJava) // doesn't compile
      acceptArrayOf[Boo](Array(Boo(3), Boo(5)).deepAsJava)
    }

    "allow custom converters" in {
      implicit val intToString = JConverter[Int, String](_ + 1.toString)
      val asJava: Array[String] = Array(1, 2, 3).deepAsJava
      asJava(0) should be("11")
      asJava(1) should be("21")
      asJava(2) should be("31")
    }

    "return same array with primitives and self conversions" in {
      checkSameInstance(Array(1))
      checkSameInstance(Array('s'))
      checkSameInstance(Array(12D))
      class A
      checkSameInstance(Array(new A))
    }

  }

  "SetConverters" should {

    def acceptJSetOf[A](js: JSet[A]) = ()

    def checkMutableSet[MS <: mutable.Set[Int]](implicit cbf: CanBuildFrom[MS, Int, MS]): Unit = {
      val mSet = (cbf() += 2).result()
      mSet should be(Set(2))
      mSet.deepAsJava[Int].add(5)
      mSet should be(Set(2, 5))
    }

    def checkSameInstance(scalaSet: mutable.Set[_]): Unit = {
      scalaSet.deepAsJava.asScala should be theSameInstanceAs scalaSet
    }

    "convert sets of primitives properly" in {

      //    acceptJSetOf[JByte](Set[Byte](1, 2, 3)) //doesn't compile
      //    acceptJSetOf[JByte](Set[Byte](1, 2, 3).asJava) // doesn't compile
      acceptJSetOf[JByte](Set[Byte](1, 2, 3).deepAsJava)

      //    acceptJSetOf[JShort](Set(1, 2, 3)) //doesn't compile
      //    acceptJSetOf[JShort](Set(1, 2, 3).asJava) // doesn't compile
      acceptJSetOf[JShort](Set[Short](1, 2, 3).deepAsJava)

      //    acceptJSetOf[JInt](Set(1, 2, 3)) //doesn't compile
      //    acceptJSetOf[JInt](Set(1, 2, 3).asJava) // doesn't compile
      acceptJSetOf[JInt](Set(1, 2, 3).deepAsJava)

      //    acceptJSetOf[JLong](Set(1L, 2L, 3L)) //doesn't compile
      //    acceptJSetOf[JLong](Set(1L, 2L, 3L).asJava) // doesn't compile
      acceptJSetOf[JLong](Set(1L, 2L, 3L).deepAsJava)

      //    acceptJSetOf[JFloat](Set(1F, 2F, 3F)) //doesn't compile
      //    acceptJSetOf[JFloat](Set(1F, 2F, 3F).asJava) // doesn't compile
      acceptJSetOf[JFloat](Set(1F, 2F, 3F).deepAsJava)

      //    acceptJSetOf[JDouble](Set(1D, 2D, 3D)) //doesn't compile
      //    acceptJSetOf[JDouble](Set(1D, 2D, 3D).asJava) // doesn't compile
      acceptJSetOf[JDouble](Set(1D, 2D, 3D).deepAsJava)

      //    acceptJSetOf[JChar](Set('a', 'b')) //doesn't compile
      //    acceptJSetOf[JChar](Set('a', 'b').asJava) // doesn't compile
      acceptJSetOf[JChar](Set('a', 'b').deepAsJava)

      //    acceptJSetOf[JBoolean](Set(true, false)) //doesn't compile
      //    acceptJSetOf[JBoolean](Set(true, false).asJava) // doesn't compile
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
      checkSameInstance(mutable.Set(1))
      checkSameInstance(mutable.HashSet(1))
      checkSameInstance(mutable.BitSet(1))
      checkSameInstance(mutable.SortedSet(1))
      checkSameInstance(mutable.TreeSet(1))

      class A
      checkSameInstance(mutable.Set(new A))
    }

  }

  "MapConverters" should {

    def acceptJMapOf[A, B](jm: JMap[A, B]) = ()

    def checkMutableMap[MM <: mutable.Map[Int, String]](implicit cbf: CanBuildFrom[MM, (Int, String), MM]): Unit = {
      val mMap = (cbf() += 2 -> "two").result()
      mMap should be(Map(2 -> "two"))
      mMap.deepAsJava.put(5, "five")
      mMap should be(Map(2 -> "two", 5 -> "five"))
    }

    def checkSameInstance(scalaMap: mutable.Map[_, _]): Unit = {
      scalaMap.deepAsJava.asScala should be theSameInstanceAs scalaMap
    }

    "convert maps of primitives properly" in {
      // acceptJMapOf[JByte, JInt](Map[Byte, Int]((1: Byte) -> 2) //doesn't compile
      // acceptJMapOf[JByte, JInt](Map[Byte, Int]((1: Byte) -> 2).asJava) // doesn't compile
      acceptJMapOf[JByte, JInt](Map[Byte, Int]((1: Byte) -> 2).deepAsJava)

      // acceptJMapOf[JShort, JLong](Map[Short, Long]((1: Short) -> 2L) //doesn't compile
      // acceptJMapOf[JShort, JLong](Map[Short, Long]((1: Short) -> 2L).asJava // doesn't compile
      acceptJMapOf[JShort, JLong](Map[Short, Long]((1: Short) -> 2L).deepAsJava)

      // acceptJMapOf[JFloat, JDouble](Map[Float, Double](1F -> 2D) //doesn't compile
      // acceptJMapOf[JFloat, JDouble](Map[Float, Double](1F -> 2D).asJava // doesn't compile
      acceptJMapOf[JFloat, JDouble](Map[Float, Double](1F -> 2D).deepAsJava)

      // acceptJMapOf[JBoolean, JChar](Map[Boolean, Char](true -> 't') //doesn't compile
      // acceptJMapOf[JBoolean, JChar](Map[Boolean, Char](true -> 't').asJava // doesn't compile
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
      checkSameInstance(mutable.Map(1 -> "one"))
      checkSameInstance(mutable.HashMap(1 -> "one"))
      checkSameInstance(mutable.ListMap(1 -> "one"))
      checkSameInstance(mutable.LinkedHashMap(1 -> "one"))
      checkSameInstance(mutable.OpenHashMap(1 -> "one"))
      //      checkSameInstance(mutable.WeakHashMap(1 -> "one")) // doesn't hold for WeakHashMap, as it's a wrapper itself

      class A
      checkSameInstance(mutable.Map(new A -> "a"))
    }

  }
}
