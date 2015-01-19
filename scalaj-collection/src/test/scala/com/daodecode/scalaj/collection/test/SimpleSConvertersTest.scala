package com.daodecode.scalaj.collection.test

import java.util
import java.util.Arrays.asList

import com.daodecode.scalaj.collection._
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.{Buffer => MBuffer, Set => MSet}
import scala.language.higherKinds
import scala.reflect.ClassTag

class SimpleSConvertersTest extends WordSpec with Matchers {

  private def newInstance[JC: ClassTag] =
    implicitly[ClassTag[JC]].runtimeClass.newInstance().asInstanceOf[JC]

  "JListConverters" should {

    def acceptBufferOf[A](sb: MBuffer[A]) = {
      val clazz = sb.getClass
      clazz
    }

    def checkMutableBuffer[JL <: JList[Int] : ClassTag](): Unit = {
      val jList = newInstance[JL]

      jList.add(2)

      jList.toArray should be(Array(2))
      jList.deepAsScala += 5
      jList.toArray should be(Array(2, 5))
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

  "JSetConverters" should {

    def setOf[A, JS <: JSet[A]](as: A*)(implicit ct: ClassTag[JS]): JS = {
      val set: JS = ct.runtimeClass.newInstance().asInstanceOf[JS]
      as.foreach(set.add)
      set
    }

    def JSet[A](as: A*): JSet[A] = setOf[A, util.HashSet[A]](as: _*)

    def acceptMSetOf[A](ms: MSet[A]) = ()

    def checkMutableSet[JS <: JSet[Int] : ClassTag](): Unit = {
      val mSet = newInstance[JS]
      mSet.add(2)
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
}
