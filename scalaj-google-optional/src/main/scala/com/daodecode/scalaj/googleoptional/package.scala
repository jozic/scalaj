package com.daodecode.scalaj

import com.daodecode.scalaj.collection._
import com.google.common.base.Optional

package object googleoptional {

  implicit class DeepOptionAsOptional[A](val option: Option[A]) extends AnyVal {
    def deepAsJava[B](implicit converter: JConverter[A, B]): Optional[B] =
      option.fold(Optional.absent[B])(a => Optional.fromNullable(converter.convert(a)))
  }

  implicit class DeepOptionalAsOption[A](val optional: Optional[A]) extends AnyVal {
    def deepAsScala[B](implicit converter: SConverter[A, B]): Option[B] =
      if (optional.isPresent)
        Some(converter.convert(optional.get()))
      else
        None
  }

}
