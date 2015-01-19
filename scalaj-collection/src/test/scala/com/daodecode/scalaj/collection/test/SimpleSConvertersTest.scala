package com.daodecode.scalaj.collection.test

import java.util
import java.util.Arrays.asList

import com.daodecode.scalaj.collection._
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable.{Buffer => MBuffer}
import scala.reflect.ClassTag

class SimpleSConvertersTest extends WordSpec with Matchers {

  "JListConverters" should {

    def acceptBufferOf[A](sb: MBuffer[A]) = {
      val clazz = sb.getClass
      clazz
    }

    def checkMutableBuffer[JL <: JList[Int] : ClassTag](): Unit = {
      val jList = implicitly[ClassTag[JL]].runtimeClass.newInstance().asInstanceOf[JL]

      jList.add(2)

      jList.toArray should be(Array(2))
      jList.deepAsScala += 5
      jList.toArray should be(Array(2, 5))
    }

    def checkSameInstance(javaList: JList[_]): Unit = {
      import scala.collection.JavaConverters._
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

    "support all JList subclasses" in {
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
}
