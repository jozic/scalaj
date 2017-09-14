package com.daodecode.scalaj

import com.daodecode.scalaj.collection._
import com.google.common.base.{Optional => GOption}

/**
 * Importing `com.daodecode.scalaj.googleoptional._` allows to use "extension" methods
 * asJava/asScala, deepAsScala/deepAsJava.
 * The later handle all nested Options/Optionals as well as primitives.
 */
package object googleoptional {

  type GOption[A] = com.google.common.base.Optional[A]

  implicit class DeepOptionAsGOption[A](val option: Option[A]) extends AnyVal {

    /**
     * Converts given [[scala.Option]] to [[com.google.common.base.Optional]].
     * Object inside Option is NOT converted.
     * @return `Absent` if option is `None` or `Some(null)`, `Present` otherwise
     *
     *         Example:
     *         {{{
     *            scala> Option(12).asJava
     *            res0: com.daodecode.scalaj.googleoptional.package.GOption[Int] = Optional.of(12)
     *         }}}
     */
    def asJava: GOption[A] =
      option.fold(GOption.absent[A])(a => GOption.fromNullable(a))

    /**
     * Converts given [[scala.Option]] to [[com.google.common.base.Optional]].
     * Object inside Option is converted using implicit `converter`.
     * All scala primitives will be implicitly converted to corresponding java primitive type wrappers.
     * This method allows for many level of conversions as long as proper implicit converter is in scope.
     * @param converter Implicit converter to convert from `A` to `B`
     * @tparam B New type of Optional
     * @return `Absent` if option is `None` or if converted value is `null`, `Present` otherwise
     *
     *         Example:
     *         {{{
     *            scala> Option(12).deepAsJava
     *            res0: com.daodecode.scalaj.googleoptional.package.GOption[com.daodecode.scalaj.collection.JInt] = Optional.of(12)
     *
     *            scala> Option(Option(12)).deepAsJava
     *            res1: com.daodecode.scalaj.googleoptional.package.GOption[
     *                       com.daodecode.scalaj.googleoptional.GOption[com.daodecode.scalaj.collection.JInt]] =
     *                       Optional.of(Optional.of(12))
     *         }}}
     */
    def deepAsJava[B](implicit converter: JConverter[A, B]): GOption[B] =
      option.fold(GOption.absent[B])(a => GOption.fromNullable(converter.convert(a)))
  }

  implicit class DeepGOptionAsOption[A](val optional: GOption[A]) extends AnyVal {

    /**
     * Converts [[com.google.common.base.Optional]] to [[scala.Option]].
     * Object inside `optional` is NOT converted
     * @return `Some` if `optional` is `Present`, `None` otherwise
     *
     *         Example:
     *         {{{
     *            scala> Optional.of(12: JInt).asScala
     *            res0: Option[com.daodecode.scalaj.JavaAliases.JInt] = Some(12)
     *         }}}
     */
    def asScala: Option[A] =
      if (optional.isPresent)
        Some(optional.get())
      else
        None

    /**
     * Converts given [[com.google.common.base.Optional]] to [[scala.Option]].
     * Object inside Option is converted using implicit `converter`.
     * All java primitive type wrappers will be implicitly converted to corresponding scala primitives.
     * This method allows for many level of conversions as long as proper implicit converter is in scope.
     * @param converter Implicit converter to convert from `A` to `B`
     * @tparam B New type of Option
     * @return `Some` if optional is `Absent`, `None` otherwise
     *
     *         Example:
     *         {{{
     *            scala> Optional.of(12: JInt).deepAsScala
     *            res0: Option[Int] = Some(12)
     *
     *            scala> Optional.of(Optional.of(12: JInt)).deepAsScala
     *            res1: Option[Option[Int]] = Some(Some(12))
     *         }}}
     */
    def deepAsScala[B](implicit converter: SConverter[A, B]): Option[B] =
      if (optional.isPresent)
        Some(converter.convert(optional.get()))
      else
        None
  }

  /**
   * Implicit converter that lifts `JConverter[A, B]` to `JConverter[Option[A], GOption[B]]`
   * @param converter Implicit converter to convert from `A` to `B`
   * @tparam A Original type
   * @tparam B Type after conversion
   * @return New `JConverter` that can convert `Option[A]` to `GOption[B]`
   */
  implicit def optionConverter[A, B](implicit converter: JConverter[A, B]): JConverter[Option[A], GOption[B]] =
    JConverter[Option[A], GOption[B]](_.deepAsJava)

  /**
   * Implicit converter that lifts `SConverter[A, B]` to `SConverter[GOption[A], Option[B]]`
   * @param converter Implicit converter to convert from `A` to `B`
   * @tparam A Original type
   * @tparam B Type after conversion
   * @return New `SConverter` that can convert `GOption[A]` to `Option[B]`
   */
  implicit def optionalConverter[A, B](implicit converter: SConverter[A, B]): SConverter[GOption[A], Option[B]] =
    SConverter[GOption[A], Option[B]](_.deepAsScala)
}
