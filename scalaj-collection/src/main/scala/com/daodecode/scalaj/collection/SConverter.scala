package com.daodecode.scalaj.collection

import scala.language.implicitConversions

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

}
