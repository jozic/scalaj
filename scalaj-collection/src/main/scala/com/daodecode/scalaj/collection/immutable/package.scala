package com.daodecode.scalaj.collection

import scala.collection.immutable.{Seq => ImSeq}
import scala.language.implicitConversions

package object immutable {

  implicit def jListImmutableConverter[A, B](implicit converter: SConverter[A, B]): SConverter[JList[A], ImSeq[B]] =
    SConverter[JList[A], ImSeq[B]](_.deepAsScalaImmutable)


  implicit class DeepJavaListAsImmutableSeq[A](val javaList: JList[A]) extends AnyVal {

    def deepAsScalaImmutable[B](implicit converter: SConverter[A, B]): ImSeq[B] =
      javaList.deepAsScala[B].to[ImSeq]
  }

}
