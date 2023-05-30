package com.daodecode.scalaj.collection.test

import com.daodecode.scalaj.collection._

import scala.collection.immutable.{Seq => ImSeq}
import scala.collection.mutable.{Buffer => MBuffer, Map => MMap, Set => MSet}

class ComplexSConvertersTest extends ScalaJSpec with JListBuilder with JSetBuilder with JMapBuilder {

  "AsScala collection converters" should {

    "convert nested collections and primitives" in {

      JList(JList(JList[JChar]('a'))).deepAsScala: MBuffer[MBuffer[MBuffer[Char]]]

      JList(JSet(Array[JDouble](12d))).deepAsScala: MBuffer[MSet[Array[Double]]]

      Array(JList(JSet[JLong](12L))).deepAsScala: Array[MBuffer[MSet[Long]]]

      Array(JSet(JList[JBoolean](false))).deepAsScala: Array[MSet[MBuffer[Boolean]]]

      JSet(JList(Array[JChar]('a'))).deepAsScala: MSet[MBuffer[Array[Char]]]

      JSet(JMap((1: JInt) -> ('a': JChar))).deepAsScala: MSet[MMap[Int, Char]]

      JMap("set1" -> JSet[JInt](23, 24)).deepAsScala: MMap[String, MSet[Int]]

      JMap(Array[JFloat](1f, 2f) -> JList(JSet[JShort](js(23), js(24)))).deepAsScala: MMap[Array[Float], MBuffer[
        MSet[Short]
      ]]
    }

    "keep nested mutable collections as mutable" in {

      val jm: JSet[JList[JInt]] = JSet(JList[JInt](1, 2))
      val sm = jm.deepAsScala

      val buffer: MBuffer[Int] = sm.head
      buffer += 3

      jm.iterator().next() should be(JList[JInt](1, 2, 3))
    }

    "return nested collections as immutable if asked" in {
      import com.daodecode.scalaj.collection.immutable._

      val jm: JList[JList[JInt]] = JList(JList[JInt](1, 2))
      jm.deepAsScalaImmutable: ImSeq[ImSeq[Int]]

      JList(JSet[JChar]('a')).deepAsScalaImmutable: ImSeq[Set[Char]]

      JMap((1L: JLong) -> JList(JSet[JInt](1, 2, 3))).deepAsScalaImmutable: Map[Long, ImSeq[Set[Int]]]
    }
  }

}
