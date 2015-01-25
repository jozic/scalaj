package com.daodecode.scalaj

import com.daodecode.scalaj.collection._
import com.google.common.base.{Optional => GOption}

import scala.language.implicitConversions

package object googleoptional {

  type GOption[A] = com.google.common.base.Optional[A]

  implicit class DeepOptionAsOptional[A](val option: Option[A]) extends AnyVal {
    def deepAsJava[B](implicit converter: GOJConverter[A, B]): GOption[B] =
      option.fold(GOption.absent[B])(a => GOption.fromNullable(converter.convert(a)))
  }

  implicit class DeepOptionalAsOption[A](val optional: GOption[A]) extends AnyVal {
    def deepAsScala[B](implicit converter: SConverter[A, B]): Option[B] =
      if (optional.isPresent)
        Some(converter.convert(optional.get()))
      else
        None
  }

  implicit def optionConverter[A, B](implicit converter: GOJConverter[A, B]): GOJConverter[Option[A], GOption[B]] =
    GOJConverter[Option[A], GOption[B]](_.deepAsJava)

  implicit def optionalConverter[A, B](implicit converter: GOSConverter[A, B]): GOSConverter[GOption[A], Option[B]] =
    GOSConverter[GOption[A], Option[B]](_.deepAsScala)

}
