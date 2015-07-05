package com.daodecode.scalaj.collection

import scala.collection.mutable.{Buffer => MBuffer, Map => MMap, Set => MSet}
import scala.language.implicitConversions
import scala.reflect.ClassTag

/**
 * Base trait for convertions of Java classes to Scala classes.
 * All deepAsScala methods require implicit parameter of this type
 * @tparam A Type convert from (represents Java side)
 * @tparam B Type convert to (represents Scala side)
 */
trait SConverter[-A, +B] {
  def convert(a: A): B
}

/**
 * Implementation of [[SConverter]] that casts A to B.
 * @tparam A Type convert from (represents Java side)
 * @tparam B Type convert to (represents Scala side)
 */
class SCastConverter[A, B] extends SConverter[A, B] {
  override def convert(a: A): B = a.asInstanceOf[B]
}

/**
 * Converter that "converts" A to A,
 * used as a fallback when there are no other implicit converters in scope
 */
object SelfSConverter extends SCastConverter[Any, Any]

trait LowImplicitSelfSConverter {

  implicit def selfConverter[A]: SConverter[A, A] = SelfSConverter.asInstanceOf[SConverter[A, A]]
}

/**
 * Java to Scala converters for "primitive types
 */
trait PrimitivesSConverter extends LowImplicitSelfSConverter {

  implicit object BytesConverter extends SCastConverter[JByte, Byte]

  implicit object ShortsConverter extends SCastConverter[JShort, Short]

  implicit object IntsConverter extends SCastConverter[JInt, Int]

  implicit object LongsConverter extends SCastConverter[JLong, Long]

  implicit object FloatConverter extends SCastConverter[JFloat, Float]

  implicit object DoublesConverter extends SCastConverter[JDouble, Double]

  implicit object CharConverter extends SCastConverter[JChar, Char]

  implicit object BooleanConverter extends SCastConverter[JBoolean, Boolean]

}

/**
 * Companion object of [[SConverter]] to bring implicit converters in following order
 *  - collection converters defined here
 *  - converters for primitive types defined in [[PrimitivesSConverter]]
 *  - "self" converter for fallback
 */
object SConverter extends PrimitivesSConverter {

  def apply[A, B](c: A => B): SConverter[A, B] = new SConverter[A, B] {
    override def convert(a: A): B = c(a)
  }

  /**
   * @return converter for converting Java [[JList]] to mutable Scala [[scala.collection.mutable.Buffer]].
   *         Given `converter` used to convert elements of Java list
   */
  implicit def jListConverter[A, B](implicit converter: SConverter[A, B]): SConverter[JList[A], MBuffer[B]] =
    SConverter[JList[A], MBuffer[B]](_.deepAsScala)

  /**
   * @return converter for converting Java [[JSet]] to mutable Scala [[scala.collection.mutable.Set]].
   *         Given `converter` used to convert elements of Java set
   */
  implicit def jSetConverter[A, B](implicit converter: SConverter[A, B]): SConverter[JSet[A], MSet[B]] =
    SConverter[JSet[A], MSet[B]](_.deepAsScala)

  /**
   * @return converter for converting Java [[scala.Array]] to Java [[scala.Array]] :).
   *         Given `converter` used to convert elements of the array
   */
  implicit def arrayConverter[A, B: ClassTag](implicit converter: SConverter[A, B]): SConverter[Array[A], Array[B]] =
    SConverter[Array[A], Array[B]](_.deepAsScala[B])

  /**
   * @return converter for converting Java [[JMap]] to mutable Scala [[scala.collection.mutable.Map]].
   *         Given `converters` used to convert keys and values of Java map
   */
  implicit def jMapConverter[A, B, C, D](implicit keyConverter: SConverter[A, C],
                                         valueConverter: SConverter[B, D]): SConverter[JMap[A, B], MMap[C, D]] =
    SConverter[JMap[A, B], MMap[C, D]](_.deepAsScala[C, D])
}
