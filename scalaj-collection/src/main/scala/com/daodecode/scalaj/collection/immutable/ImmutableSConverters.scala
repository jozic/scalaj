package com.daodecode.scalaj.collection.immutable

import com.daodecode.scalaj.collection._

import scala.collection.immutable.{Seq => ImSeq}

/**
  * Trait for bringing in implicit [[SCastConverter]]s for converting between Java and immutable Scala collections.
  * Importing com.daodecode.scalaj.collection.immutable._ will bring them in the scope.
  */
trait ImmutableSConverters {

  /**
    * @return converter for converting Java [[JList]] to immutable Scala [[scala.collection.immutable.Seq]].
    *         Given `converter` used to convert elements of Java list
    */
  implicit def jListImmutableConverter[A, B](implicit converter: SConverter[A, B]): SConverter[JList[A], ImSeq[B]] =
    SConverter[JList[A], ImSeq[B]](_.deepAsScalaImmutable)

  /**
    * @return converter for converting Java [[JSet]] to immutable Scala [[scala.collection.immutable.Set]].
    *         Given `converter` used to convert elements of Java set
    */
  implicit def jSetImmutableConverter[A, B](implicit converter: SConverter[A, B]): SConverter[JSet[A], Set[B]] =
    SConverter[JSet[A], Set[B]](_.deepAsScalaImmutable)

  /**
    * @return converter for converting Java [[JMap]] to immutable Scala [[scala.collection.immutable.Map]].
    *         Given `converters` used to convert keys and values of Java map
    */
  implicit def jMapImmutableConverter[A, B, C, D](implicit keyConverter: SConverter[A, C],
                                                  valueConverter: SConverter[B, D]): SConverter[JMap[A, B], Map[C, D]] =
    SConverter[JMap[A, B], Map[C, D]](_.deepAsScalaImmutable[C, D])
}
