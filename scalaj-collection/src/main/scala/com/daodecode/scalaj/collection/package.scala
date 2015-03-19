package com.daodecode.scalaj

import scala.collection.convert.{DecorateAsJava, DecorateAsScala}
import scala.collection.mutable.{Buffer => MBuffer, Map => MMap, Seq => MSeq, Set => MSet}
import scala.collection.{Seq => GenSeq, Set => GenSet, Map => GenMap}
import scala.reflect.ClassTag

package object collection extends DecorateAsJava with DecorateAsScala with JavaAliases {

  /** deepAsJava converters **/
  implicit class DeepSeqAsJavaList[A](val scalaSeq: GenSeq[A]) extends AnyVal {
    private def toJava[T](genSeq: GenSeq[T]): JList[T] = genSeq match {
      case buffer: MBuffer[T] => buffer.asJava
      case mSeq: MSeq[T] => mSeq.asJava
      case _ => genSeq.asJava
    }

    def deepAsJava[B](implicit converter: JConverter[A, B]): JList[B] = {
      converter match {
        case _: JCastConverter[_, _] => toJava(scalaSeq).asInstanceOf[JList[B]]
        case _ => toJava(scalaSeq.map(converter.convert))
      }
    }
  }

  implicit class DeepSetAsJavaSet[A](val scalaSet: GenSet[A]) extends AnyVal {
    private def toJava[T](genSet: GenSet[T]): JSet[T] = genSet match {
      case mSet: MSet[T] => mSet.asJava
      case _ => genSet.asJava
    }

    def deepAsJava[B](implicit converter: JConverter[A, B]): JSet[B] = converter match {
      case _: JCastConverter[_, _] => toJava(scalaSet).asInstanceOf[JSet[B]]
      case _ => toJava(scalaSet.map(converter.convert))
    }
  }

  implicit class DeepArrayAsJavaArray[A](val array: Array[A]) extends AnyVal {
    def deepAsJava[B: ClassTag](implicit converter: JConverter[A, B]): Array[B] = converter match {
      case _: JCastConverter[_, _] => array.asInstanceOf[Array[B]]
      case _ => array.map(converter.convert)
    }
  }

  implicit class DeepMapAsJavaMap[A, B](val scalaMap: GenMap[A, B]) extends AnyVal {
    private def toJava[T, U](genMap: GenMap[T, U]): JMap[T, U] = genMap match {
      case mMap: MMap[T, U] => mMap.asJava
      case _ => genMap.asJava
    }

    def deepAsJava[C, D](implicit keyConverter: JConverter[A, C], valueConverter: JConverter[B, D]): JMap[C, D] = (keyConverter, valueConverter) match {
      case (_: JCastConverter[_, _], _: JCastConverter[_, _]) => toJava(scalaMap).asInstanceOf[JMap[C, D]]
      case _ => toJava(scalaMap.map { case (k, v) =>
        keyConverter.convert(k) -> valueConverter.convert(v)
      })
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
