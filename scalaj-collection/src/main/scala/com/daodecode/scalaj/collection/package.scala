package com.daodecode.scalaj

import scala.collection.convert.DecorateAsJava
import scala.collection.mutable
import scala.reflect.ClassTag

package object collection extends DecorateAsJava {

  type JList[A] = java.util.List[A]
  type JSet[A] = java.util.Set[A]
  type JMap[A, B] = java.util.Map[A, B]

  type JByte = java.lang.Byte
  type JShort = java.lang.Short
  type JInt = java.lang.Integer
  type JLong = java.lang.Long
  type JFloat = java.lang.Float
  type JDouble = java.lang.Double
  type JChar = java.lang.Character
  type JBoolean = java.lang.Boolean


  implicit class DeepAsJavaList[A](val scalaSeq: Seq[A]) extends AnyVal {

    def deepAsJava[B](implicit converter: Converter[A, B]): JList[B] = converter match {
      case _: CastConverter[_, _] => scalaSeq.asJava.asInstanceOf[JList[B]]
      case _ => scalaSeq.map(converter.convert).asJava
    }
  }

  implicit class DeepAsJavaMutableList[A](val scalaSeq: mutable.Seq[A]) extends AnyVal {

    def deepAsJava[B](implicit converter: Converter[A, B]): JList[B] = converter match {
      case _: CastConverter[_, _] => scalaSeq.asJava.asInstanceOf[JList[B]]
      case _ => scalaSeq.map(converter.convert).asJava
    }
  }

  implicit class DeepAsJavaSet[A](val scalaSet: scala.collection.Set[A]) extends AnyVal {

    def deepAsJava[B](implicit converter: Converter[A, B]): JSet[B] = converter match {
      case _: CastConverter[_, _] => scalaSet.asJava.asInstanceOf[JSet[B]]
      case _ => scalaSet.map(converter.convert).asJava
    }
  }

  implicit class DeepAsJavaArray[A](val array: Array[A]) extends AnyVal {

    def deepAsJava[B: ClassTag](implicit converter: Converter[A, B]): Array[B] = converter match {
      case _: CastConverter[_, _] => array.asInstanceOf[Array[B]]
      case _ => array.map(converter.convert).toArray
    }
  }

  implicit class DeepAsJavaMap[A, B](val scalaMap: scala.collection.Map[A, B]) extends AnyVal {

    def deepAsJava[C, D](implicit keyConverter: Converter[A, C], valueConverter: Converter[B, D]): JMap[C, D] = (keyConverter, valueConverter) match {
      case (_: CastConverter[_, _], _: CastConverter[_, _]) => scalaMap.asJava.asInstanceOf[JMap[C, D]]
      case _ => scalaMap.map { case (k, v) =>
        keyConverter.convert(k) -> valueConverter.convert(v)
      }.asJava
    }
  }


}
