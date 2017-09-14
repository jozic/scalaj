package com.daodecode.scalaj

import java.util.{Optional => JOption}

import com.daodecode.scalaj.collection._

/**
  * Importing `com.daodecode.scalaj.javaoptional._` allows to use "extension" methods
  * asJava/asScala, deepAsScala/deepAsJava.
  * The later handle all nested Options/Optionals as well as primitives.
  */
package object javaoptional {

  type JOption[A] = java.util.Optional[A]

  implicit class DeepOptionAsJOption[A](val option: Option[A]) extends AnyVal {

    def asJava: JOption[A] =
      option.fold(JOption.empty[A])(a => JOption.ofNullable(a))

    def deepAsJava[B](implicit converter: JConverter[A, B]): JOption[B] =
      option.fold(JOption.empty[B])(a => JOption.ofNullable(converter.convert(a)))
  }

  implicit class DeepJOptionAsOption[A](val optional: JOption[A]) extends AnyVal {

    def asScala: Option[A] =
      if (optional.isPresent)
        Some(optional.get())
      else
        None

    def deepAsScala[B](implicit converter: SConverter[A, B]): Option[B] =
      if (optional.isPresent)
        Some(converter.convert(optional.get))
      else
        None
  }

}