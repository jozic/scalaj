package com.daodecode.scalaj.collection.immutable

import com.daodecode.scalaj.collection._

import scala.collection.immutable.{Seq => ImSeq}

trait ImmutableSConverters {
  implicit def jListImmutableConverter[A, B](implicit converter: SConverter[A, B]): SConverter[JList[A], ImSeq[B]] =
    SConverter[JList[A], ImSeq[B]](_.deepAsScalaImmutable)

  implicit def jSetImmutableConverter[A, B](implicit converter: SConverter[A, B]): SConverter[JSet[A], Set[B]] =
    SConverter[JSet[A], Set[B]](_.deepAsScalaImmutable)

  implicit def jMapImmutableConverter[A, B, C, D](implicit keyConverter: SConverter[A, C],
                                                  valueConverter: SConverter[B, D]): SConverter[JMap[A, B], Map[C, D]] =
    SConverter[JMap[A, B], Map[C, D]](_.deepAsScalaImmutable[C, D])
}
