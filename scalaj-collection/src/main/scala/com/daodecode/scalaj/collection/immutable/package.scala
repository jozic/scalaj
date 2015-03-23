package com.daodecode.scalaj.collection

import scala.collection.immutable.{Seq => ImSeq}
import scala.language.implicitConversions

package object immutable extends ImmutableSConverters {

  implicit class DeepJavaListAsImmutableSeq[A](val javaList: JList[A]) extends AnyVal {
    def deepAsScalaImmutable[B](implicit converter: SConverter[A, B]): ImSeq[B] =
      javaList.deepAsScala[B].to[ImSeq]
  }

  implicit class DeepJavaSetAsImmutableSet[A](val javaSet: JSet[A]) extends AnyVal {
    def deepAsScalaImmutable[B](implicit converter: SConverter[A, B]): Set[B] =
      javaSet.deepAsScala[B].to[Set]
  }

  implicit class DeepJavaMapAsImmutableMap[A, B](val javaMap: JMap[A, B]) extends AnyVal {
    def deepAsScalaImmutable[C, D](implicit keyConverter: SConverter[A, C],
                                   valueConverter: SConverter[B, D]): Map[C, D] =
      javaMap.deepAsScala[C, D].toMap[C, D]
  }

}
