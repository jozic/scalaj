package com.daodecode.scalaj

import scala.collection.convert.{DecorateAsJava, DecorateAsScala}
import scala.collection.mutable.{Buffer => MBuffer, Map => MMap, Seq => MSeq, Set => MSet}
import scala.reflect.ClassTag

package object collection extends DecorateAsJava with DecorateAsScala with JavaAliases {

  /** deepAsJava converters **/

  implicit class DeepMutableBufferAsJavaList[A](val scalaBuffer: MBuffer[A]) extends AnyVal {
    def deepAsJava[B](implicit converter: JConverter[A, B]): JList[B] = converter match {
      case _: JCastConverter[_, _] => scalaBuffer.asJava.asInstanceOf[JList[B]]
      case _ => scalaBuffer.map(converter.convert).asJava
    }
  }

  implicit class DeepMutableSeqAsJavaList[A](val scalaSeq: MSeq[A]) extends AnyVal {
    def deepAsJava[B](implicit converter: JConverter[A, B]): JList[B] = converter match {
      case _: JCastConverter[_, _] => scalaSeq.asJava.asInstanceOf[JList[B]]
      case _ => scalaSeq.map(converter.convert).asJava
    }
  }

  implicit class DeepSeqAsJavaList[A](val scalaSeq: Seq[A]) extends AnyVal {
    def deepAsJava[B](implicit converter: JConverter[A, B]): JList[B] = converter match {
      case _: JCastConverter[_, _] => scalaSeq.asJava.asInstanceOf[JList[B]]
      case _ => scalaSeq.map(converter.convert).asJava
    }
  }

  implicit class DeepSetAsJavaSet[A](val scalaSet: scala.collection.Set[A]) extends AnyVal {
    def deepAsJava[B](implicit converter: JConverter[A, B]): JSet[B] = converter match {
      case _: JCastConverter[_, _] => scalaSet.asJava.asInstanceOf[JSet[B]]
      case _ => scalaSet.map(converter.convert).asJava
    }
  }

  implicit class DeepMutableSetAsJavaSet[A](val scalaSet: MSet[A]) extends AnyVal {
    def deepAsJava[B](implicit converter: JConverter[A, B]): JSet[B] = converter match {
      case _: JCastConverter[_, _] => scalaSet.asJava.asInstanceOf[JSet[B]]
      case _ => scalaSet.map(converter.convert).asJava
    }
  }

  implicit class DeepArrayAsJavaArray[A](val array: Array[A]) extends AnyVal {
    def deepAsJava[B: ClassTag](implicit converter: JConverter[A, B]): Array[B] = converter match {
      case _: JCastConverter[_, _] => array.asInstanceOf[Array[B]]
      case _ => array.map(converter.convert)
    }
  }

  implicit class DeepMapAsJavaMap[A, B](val scalaMap: scala.collection.Map[A, B]) extends AnyVal {
    def deepAsJava[C, D](implicit keyConverter: JConverter[A, C], valueConverter: JConverter[B, D]): JMap[C, D] = (keyConverter, valueConverter) match {
      case (_: JCastConverter[_, _], _: JCastConverter[_, _]) => scalaMap.asJava.asInstanceOf[JMap[C, D]]
      case _ => scalaMap.map { case (k, v) =>
        keyConverter.convert(k) -> valueConverter.convert(v)
      }.asJava
    }
  }

  implicit class DeepMutableMapAsJavaMap[A, B](val scalaMap: MMap[A, B]) extends AnyVal {
    def deepAsJava[C, D](implicit keyConverter: JConverter[A, C], valueConverter: JConverter[B, D]): JMap[C, D] = (keyConverter, valueConverter) match {
      case (_: JCastConverter[_, _], _: JCastConverter[_, _]) => scalaMap.asJava.asInstanceOf[JMap[C, D]]
      case _ => scalaMap.map { case (k, v) =>
        keyConverter.convert(k) -> valueConverter.convert(v)
      }.asJava
    }
  }


  /** deepAsScala converters **/

  implicit class DeepJavaListAsMutableBuffer[A](val javaList: JList[A]) extends AnyVal {
    def deepAsScala[B](implicit converter: SConverter[A, B]): MBuffer[B] = converter match {
      case _: SCastConverter[_, _] => javaList.asScala.asInstanceOf[MBuffer[B]]
      case _ => javaList.asScala.map(converter.convert)
    }
  }

  implicit class DeepJavaSetAsMutableSet[A](val javaSet: JSet[A]) extends AnyVal {
    def deepAsScala[B](implicit converter: SConverter[A, B]): MSet[B] = converter match {
      case _: SCastConverter[_, _] => javaSet.asScala.asInstanceOf[MSet[B]]
      case _ => javaSet.asScala.map(converter.convert)
    }
  }

  implicit class DeepArrayAsScalaArray[A](val array: Array[A]) extends AnyVal {
    def deepAsScala[B: ClassTag](implicit converter: SConverter[A, B]): Array[B] = converter match {
      case _: SCastConverter[_, _] => array.asInstanceOf[Array[B]]
      case _ => array.map(converter.convert)
    }
  }

  implicit class DeepJavaMapAsMutableMap[A, B](val javaMap: JMap[A, B]) extends AnyVal {
    def deepAsScala[C, D](implicit keyConverter: SConverter[A, C], valueConverter: SConverter[B, D]): MMap[C, D] = (keyConverter, valueConverter) match {
      case (_: SCastConverter[_, _], _: SCastConverter[_, _]) => javaMap.asScala.asInstanceOf[MMap[C, D]]
      case _ => javaMap.asScala.map { case (k, v) =>
        keyConverter.convert(k) -> valueConverter.convert(v)
      }
    }
  }

}
