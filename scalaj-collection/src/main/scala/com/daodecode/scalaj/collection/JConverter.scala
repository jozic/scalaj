package com.daodecode.scalaj.collection

import scala.collection.{Map => GenMap, Seq => GenSeq, Set => GenSet}
import scala.language.implicitConversions
import scala.reflect.ClassTag

trait JConverter[-A, +B] {
  def convert(a: A): B
}

class JCastConverter[A, B] extends JConverter[A, B] {
  override def convert(a: A): B = a.asInstanceOf[B]
}

trait LowImplicitSelfJConverter {

  object SelfConverter extends JCastConverter[Any, Any]

  implicit def selfConverter[A]: JConverter[A, A] = SelfConverter.asInstanceOf[JConverter[A, A]]
}

trait PrimitivesJConverter extends LowImplicitSelfJConverter {

  implicit object BytesConverter extends JCastConverter[Byte, JByte]

  implicit object ShortsConverter extends JCastConverter[Short, JShort]

  implicit object IntsConverter extends JCastConverter[Int, JInt]

  implicit object LongsConverter extends JCastConverter[Long, JLong]

  implicit object FloatConverter extends JCastConverter[Float, JFloat]

  implicit object DoublesConverter extends JCastConverter[Double, JDouble]

  implicit object CharConverter extends JCastConverter[Char, JChar]

  implicit object BooleanConverter extends JCastConverter[Boolean, JBoolean]

}

object JConverter extends PrimitivesJConverter {

  def apply[A, B](c: A => B) = new JConverter[A, B] {
    override def convert(a: A): B = c(a)
  }

  implicit def seqConverter[A, B](implicit converter: JConverter[A, B]): JConverter[GenSeq[A], JList[B]] =
    JConverter[GenSeq[A], JList[B]](_.deepAsJava)

  implicit def setConverter[A, B](implicit converter: JConverter[A, B]): JConverter[GenSet[A], JSet[B]] =
    JConverter[collection.Set[A], JSet[B]](_.deepAsJava)

  implicit def arrayConverter[A, B: ClassTag](implicit converter: JConverter[A, B]): JConverter[Array[A], Array[B]] =
    JConverter[Array[A], Array[B]](_.deepAsJava[B])

  implicit def mapConverter[A, B, C, D](implicit keyConverter: JConverter[A, C],
                                        valueConverter: JConverter[B, D]): JConverter[GenMap[A, B], JMap[C, D]] =
    JConverter[GenMap[A, B], JMap[C, D]](_.deepAsJava[C, D])
}
