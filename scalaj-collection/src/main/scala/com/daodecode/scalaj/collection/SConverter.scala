package com.daodecode.scalaj.collection

import scala.collection.mutable.{Buffer => MBuffer, Map => MMap, Set => MSet}
import scala.language.implicitConversions
import scala.reflect.ClassTag

trait SConverter[-A, +B] {
  def convert(a: A): B
}

class SCastConverter[-A, +B] extends SConverter[A, B] {
  override def convert(a: A): B = a.asInstanceOf[B]
}

trait LowImplicitSelfSConverter {

  object SelfConverter extends SCastConverter[Any, Any]

  implicit def selfConverter[A]: SConverter[A, A] = SelfConverter.asInstanceOf[SConverter[A, A]]
}


object SConverter extends LowImplicitSelfSConverter {

  def apply[A, B](c: A => B) = new SConverter[A, B] {
    override def convert(a: A): B = c(a)
  }

  implicit object BytesConverter extends SCastConverter[JByte, Byte]

  implicit object ShortsConverter extends SCastConverter[JShort, Short]

  implicit object IntsConverter extends SCastConverter[JInt, Int]

  implicit object LongsConverter extends SCastConverter[JLong, Long]

  implicit object FloatConverter extends SCastConverter[JFloat, Float]

  implicit object DoublesConverter extends SCastConverter[JDouble, Double]

  implicit object CharConverter extends SCastConverter[JChar, Char]

  implicit object BooleanConverter extends SCastConverter[JBoolean, Boolean]

  implicit def jListConverter[A, B](implicit converter: SConverter[A, B]): SConverter[JList[A], MBuffer[B]] =
    SConverter[JList[A], MBuffer[B]](_.deepAsScala)

  implicit def jSetConverter[A, B](implicit converter: SConverter[A, B]): SConverter[JSet[A], MSet[B]] =
    SConverter[JSet[A], MSet[B]](_.deepAsScala)

  implicit def arrayConverter[A, B: ClassTag](implicit converter: SConverter[A, B]): SConverter[Array[A], Array[B]] =
    SConverter[Array[A], Array[B]](_.deepAsScala[B])

  implicit def jMapConverter[A, B, C, D](implicit keyConverter: SConverter[A, C],
                                         valueConverter: SConverter[B, D]): SConverter[JMap[A, B], MMap[C, D]] =
    SConverter[JMap[A, B], MMap[C, D]](_.deepAsScala[C, D])

}
