package com.daodecode.scalaj.collection.test

import scala.collection.{mutable, immutable}
import scala.language.{higherKinds, reflectiveCalls}

import com.daodecode.scalaj.collection._
import org.scalatest.{Matchers, WordSpec}

class SimpleConverterTest extends WordSpec with Matchers {

  "SeqConverter" should {

    def acceptJListOf[A](jl: JList[A]) = {
      val clazz = jl.getClass
      clazz
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
      implicit val intToString = Converter[Int, String](_ + 1.toString)
      val asJava: JList[String] = List(1, 2, 3).deepAsJava
      asJava.get(0) should be("11")
      asJava.get(1) should be("21")
      asJava.get(2) should be("31")
    }

    "support all Seq subclasses" in {
      acceptJListOf(Nil.deepAsJava)
      acceptJListOf(List(1).deepAsJava)
      acceptJListOf(Vector(1).deepAsJava)
      acceptJListOf(Stream(1).deepAsJava)
      acceptJListOf(Seq(1).deepAsJava)
      acceptJListOf(immutable.Seq(1).deepAsJava)
      acceptJListOf(immutable.IndexedSeq(1).deepAsJava)
      acceptJListOf(immutable.LinearSeq(1).deepAsJava)
      acceptJListOf(immutable.Queue(1).deepAsJava)

      acceptJListOf(mutable.Seq(1).deepAsJava)
      acceptJListOf(mutable.Buffer(1).deepAsJava)
      acceptJListOf(mutable.ArrayBuffer(1).deepAsJava)
      acceptJListOf(mutable.ListBuffer(1).deepAsJava)
      acceptJListOf(mutable.IndexedSeq(1).deepAsJava)
      acceptJListOf(mutable.LinearSeq(1).deepAsJava)
      acceptJListOf(mutable.Queue(1).deepAsJava)
      acceptJListOf(mutable.Stack(1).deepAsJava)
      acceptJListOf(mutable.ArrayStack(1).deepAsJava)
      acceptJListOf(mutable.DoubleLinkedList(1).deepAsJava)
      acceptJListOf(mutable.ArraySeq(1).deepAsJava)
      acceptJListOf(mutable.LinkedList(1).deepAsJava)
      acceptJListOf(mutable.MutableList(1).deepAsJava)
      acceptJListOf(mutable.ResizableArray(1).deepAsJava)
      acceptJListOf(mutable.UnrolledBuffer(1).deepAsJava)
    }

    "keep mutable lists mutable" in {

      val mtbl = mutable.Seq(1)
      mtbl.deepAsJava.add(23)
      mtbl should be(mutable.Seq(1, 23))
    }
  }

  "ArrayConverter" should {

    def acceptArrayOf[A](jl: Array[A]) = ()

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
      implicit val intToString = Converter[Int, String](_ + 1.toString)
      val asJava: Array[String] = Array(1, 2, 3).deepAsJava
      asJava(0) should be("11")
      asJava(1) should be("21")
      asJava(2) should be("31")
    }
  }

  "SetConverter" should {

    def acceptJSetOf[A](jl: JSet[A]) = ()

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
      implicit val intToString = Converter[Int, String](_ + 1.toString)
      val asJava: JSet[String] = Set(1, 2, 3).deepAsJava
      asJava.contains("11") should be(true)
      asJava.contains("21") should be(true)
      asJava.contains("31") should be(true)
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
  }
}
