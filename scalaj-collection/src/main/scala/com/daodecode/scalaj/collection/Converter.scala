package com.daodecode.scalaj.collection

import scala.language.implicitConversions
import scala.reflect.ClassTag

trait Converter[-A, +B] {
  def convert(a: A): B
}

class CastConverter[-A, +B] extends Converter[A, B] {
  override def convert(a: A): B = ???
}

trait LowImplicitSelfConverter {

  //todo use same trick as in toMap with type constraint
  object SelfConverter extends CastConverter[Any, Any]

  implicit def selfConverter[A]: Converter[A, A] = SelfConverter.asInstanceOf[Converter[A, A]]
}


object Converter extends LowImplicitSelfConverter {

  def apply[A, B](c: A => B) = new Converter[A, B] {
    override def convert(a: A): B = c(a)
  }

  implicit object BytesConverter extends CastConverter[Byte, JByte]

  implicit object ShortsConverter extends CastConverter[Short, JShort]

  implicit object IntsConverter extends CastConverter[Int, JInt]

  implicit object LongsConverter extends CastConverter[Long, JLong]

  implicit object FloatConverter extends CastConverter[Float, JFloat]

  implicit object DoublesConverter extends CastConverter[Double, JDouble]

  implicit object CharConverter extends CastConverter[Char, JChar]

  implicit object BooleanConverter extends CastConverter[Boolean, JBoolean]

  implicit def seqConverter[A, B](implicit converter: Converter[A, B]): Converter[Seq[A], JList[B]] =
    Converter[Seq[A], JList[B]](_.deepAsJava)

  implicit def setConverter[A, B](implicit converter: Converter[A, B]): Converter[collection.Set[A], JSet[B]] =
    Converter[collection.Set[A], JSet[B]](_.deepAsJava)

  implicit def arrayConverter[A, B: ClassTag](implicit converter: Converter[A, B]): Converter[Array[A], Array[B]] =
    Converter[Array[A], Array[B]](_.deepAsJava[B])
}
