package com.daodecode.scalaj

import com.daodecode.scalaj.collection._
import com.google.common.base.{Optional => GOption}

import scala.language.implicitConversions

package object googleoptional {

  type GOption[A] = com.google.common.base.Optional[A]

  implicit class DeepOptionAsOptional[A](val option: Option[A]) extends AnyVal {
    def asJava: GOption[A] =
      option.fold(GOption.absent[A])(a => GOption.fromNullable(a))

    def deepAsJava[B](implicit converter: JConverter[A, B]): GOption[B] =
      option.fold(GOption.absent[B])(a => GOption.fromNullable(converter.convert(a)))
  }

  implicit class DeepOptionalAsOption[A](val optional: GOption[A]) extends AnyVal {
    def asScala: Option[A] =
      if (optional.isPresent)
        Some(optional.get())
      else
        None

    def deepAsScala[B](implicit converter: SConverter[A, B]): Option[B] =
      if (optional.isPresent)
        Some(converter.convert(optional.get()))
      else
        None
  }

  implicit def optionConverter[A, B](implicit converter: JConverter[A, B]): JConverter[Option[A], GOption[B]] =
    JConverter[Option[A], GOption[B]](_.deepAsJava)

  implicit def optionalConverter[A, B](implicit converter: SConverter[A, B]): SConverter[GOption[A], Option[B]] =
    SConverter[GOption[A], Option[B]](_.deepAsScala)

}
