package com.daodecode.scalaj.collection

import scala.collection.{Map => GenMap, Seq => GenSeq, Set => GenSet}
import scala.reflect.ClassTag

/**
  * Base trait for convertions of Scala classes to Java classes.
  * All deepAsJava methods require implicit parameter of this type
  * @tparam A Type convert from (represents Scala side)
  * @tparam B Type convert to (represents Java side)
  */
trait JConverter[-A, +B] {
  def convert(a: A): B
}

/**
  * Implementation of [[JConverter]] that casts A to B.
  * @tparam A Type convert from (represents Scala side)
  * @tparam B Type convert to (represents Java side)
  */
class JCastConverter[A, B] extends JConverter[A, B] {
  override def convert(a: A): B = a.asInstanceOf[B]
}

/**
  * Converter that "converts" A to A,
  * used as a fallback when there are no other implicit converters in scope
  */
object SelfJConverter extends JCastConverter[Any, Any]

trait LowImplicitSelfJConverter {

  implicit def selfConverter[A]: JConverter[A, A] = SelfJConverter.asInstanceOf[JConverter[A, A]]
}

/**
  * Scala to Java converters for "primitive types
  */
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

/**
  * Companion object of [[JConverter]] to bring implicit converters in following order
  *  - collection converters defined here
  *  - converters for primitive types defined in [[PrimitivesJConverter]]
  *  - "self" converter for fallback
  */
object JConverter extends PrimitivesJConverter {

  def apply[A, B](c: A => B): JConverter[A, B] = new JConverter[A, B] {
    override def convert(a: A): B = c(a)
  }

  /**
    * @return converter for converting Scala [[scala.collection.Seq]] to Java [[JList]].
    *         Given `converter` used to convert elements of Scala seq
    */
  implicit def seqConverter[A, B](implicit converter: JConverter[A, B]): JConverter[GenSeq[A], JList[B]] =
    JConverter[GenSeq[A], JList[B]](_.deepAsJava)

  /**
    * @return converter for converting Scala [[scala.collection.Set]] to Java [[JSet]].
    *         Given `converter` used to convert elements of Scala set
    */
  implicit def setConverter[A, B](implicit converter: JConverter[A, B]): JConverter[GenSet[A], JSet[B]] =
    JConverter[GenSet[A], JSet[B]](_.deepAsJava)

  /**
    * @return converter for converting Java [[scala.Array]] to Java [[scala.Array]] :).
    *         Given `converter` used to convert elements of the array
    */
  implicit def arrayConverter[A, B: ClassTag](implicit converter: JConverter[A, B]): JConverter[Array[A], Array[B]] =
    JConverter[Array[A], Array[B]](_.deepAsJava[B])

  /**
    * @return converter for converting Scala [[scala.collection.Map]] to Java [[JMap]].
    *         Given `converters` used to convert keys and values of Scala map
    */
  implicit def mapConverter[A, B, C, D](
      implicit keyConverter: JConverter[A, C],
      valueConverter: JConverter[B, D]
  ): JConverter[GenMap[A, B], JMap[C, D]] =
    JConverter[GenMap[A, B], JMap[C, D]](_.deepAsJava[C, D])
}
