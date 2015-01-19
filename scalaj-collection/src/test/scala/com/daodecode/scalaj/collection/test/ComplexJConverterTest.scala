package com.daodecode.scalaj.collection.test

import com.daodecode.scalaj.collection._
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable

class ComplexJConverterTest extends WordSpec with Matchers {

  "Collection Converters" should {

    "convert nested collections and primitives" in {

      List(List(List('a'))).deepAsJava: JList[JList[JList[JChar]]]

      List(Set(Array(12D))).deepAsJava: JList[JSet[Array[JDouble]]]

      List(Array(Set(12D))).deepAsJava: JList[Array[JSet[JDouble]]]

      Array(List(Set(12L))).deepAsJava: Array[JList[JSet[JLong]]]

      Array(Set(List(false))).deepAsJava: Array[JSet[JList[JBoolean]]]

      Set(List(Array('a'))).deepAsJava: JSet[JList[Array[JChar]]]
    }

    "keep nested mutable collections as mutable" in {

      val sm: mutable.Set[mutable.Buffer[Int]] = mutable.Set(mutable.Buffer(1, 2))
      val jm = sm.deepAsJava(JConverter.bufferConverter)
      //todo: try to make it work without explicit converter
      //      val jm = sm.deepAsJava

      val jList: JList[JInt] = jm.iterator().next()

      jList.add(3)
      sm should be(mutable.Set(mutable.Buffer(1, 2, 3)))
    }
  }

}

