package com.daodecode.scalaj.collection.test

import org.scalatest.{Matchers, WordSpec}

import com.daodecode.scalaj.collection._

class ComplexConverterTest extends WordSpec with Matchers {

  "Collection Converters" should {

    "convert nested collections and primitives" in {

      List(List(List('a'))).deepAsJava: JList[JList[JList[JChar]]]

      List(Set(Array(12D))).deepAsJava: JList[JSet[Array[JDouble]]]

      List(Array(Set(12D))).deepAsJava: JList[Array[JSet[JDouble]]]

      Array(List(Set(12L))).deepAsJava: Array[JList[JSet[JLong]]]

      Array(Set(List(false))).deepAsJava: Array[JSet[JList[JBoolean]]]

      Set(List(Array(false))).deepAsJava: JSet[JList[Array[JBoolean]]]
    }
  }

}

