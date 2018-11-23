package com.daodecode.scalaj.collection

import scala.collection.immutable.{Seq => ImSeq}

/**
  * Importing `com.daodecode.scalaj.collection.immutable._` allows to use "extension" methods deepAsScalaImmutable,
  * which convert all supported nested Java collections to immutable versions of corresponding Scala collections
  * as well as primitive wrappers.
  */
package object immutable extends ImmutableSConverters {

  implicit class DeepJavaListAsImmutableSeq[A](val javaList: JList[A]) extends AnyVal {

    /**
      * Converts given Java [[JList]] to immutable Scala [[scala.collection.immutable.Seq]]
      * Elements inside list are converted using given implicit `converter`,
      * which allows to convert nested Java collections and primitive wrappers.
      * @param converter Implicit converter to convert from `A` to `B`.
      * @tparam B New type of resulting Buffer elements
      * @return Scala immutable seq.
      *
      *         Example:
      *         {{{
      *            scala> val jl = new java.util.ArrayList[java.util.Set[JInt]]()
      *            jl: java.util.ArrayList[java.util.Set[com.daodecode.scalaj.collection.JInt]] = []
      *
      *            scala> jl.add(new java.util.HashSet[JInt])
      *            res0: Boolean = true
      *
      *            scala> jl.get(0).add(23)
      *            res1: Boolean = true
      *
      *            scala> jl
      *            res2: java.util.ArrayList[java.util.Set[com.daodecode.scalaj.collection.JInt]] = [[23]]
      *
      *            scala> jl.deepAsScalaImmutable
      *            res3: scala.collection.immutable.Seq[Set[Int]] = Vector(Set(23))
      *         }}}
      */
    def deepAsScalaImmutable[B](implicit converter: SConverter[A, B]): ImSeq[B] =
      javaList.deepAsScala[B].to[ImSeq]
  }

  implicit class DeepJavaSetAsImmutableSet[A](val javaSet: JSet[A]) extends AnyVal {

    /**
      * Converts given Java [[JSet]] to immutable Scala [[scala.collection.immutable.Set]]
      * Elements inside set are converted using given implicit `converter`,
      * which allows to convert nested Java collections and primitive wrappers.
      * @param converter Implicit converter to convert from `A` to `B`.
      * @tparam B New type of resulting Set elements
      * @return Scala immutable set.
      *
      *         Example:
      *         {{{
      *            scala> val js = new java.util.HashSet[java.util.Set[JInt]]()
      *            js: java.util.HashSet[java.util.Set[com.daodecode.scalaj.collection.JInt]] = []
      *
      *            scala> js.add(new java.util.HashSet[JInt])
      *            res0: Boolean = true
      *
      *            scala> js.iterator.next.add(34)
      *            res1: Boolean = true
      *
      *            scala> js
      *            res2: java.util.HashSet[java.util.Set[com.daodecode.scalaj.collection.JInt]] = [[34]]
      *
      *            scala> js.deepAsScalaImmutable
      *            res3: Set[Set[Int]] = Set(Set(34))
      *         }}}
      */
    def deepAsScalaImmutable[B](implicit converter: SConverter[A, B]): Set[B] =
      javaSet.deepAsScala[B].to[Set]
  }

  implicit class DeepJavaMapAsImmutableMap[A, B](val javaMap: JMap[A, B]) extends AnyVal {

    /**
      * Converts given Java [[JMap]] to immutable Scala [[scala.collection.immutable.Map]].
      * Keys and values inside map are converted using given implicit `converters`,
      * which allow to convert nested Java collections and primitive wrappers.
      * @param keyConverter Implicit converter to convert from `A` to `C`
      * @param valueConverter Implicit converter to convert from `B` to `D`
      * @tparam C New type of Scala map key
      * @tparam D New type of Scala map value
      * @return Scala immutable map.
      *
      *         Example:
      *         {{{
      *            scala> val jm = new java.util.HashMap[JLong, JList[JChar]]()
      *            jm: java.util.HashMap[com.daodecode.scalaj.collection.JLong,com.daodecode.scalaj.collection.JList[com.daodecode.scalaj.collection.JChar]] = {}
      *
      *            scala> jm.put(3L, java.util.Arrays.asList('a', 'b', 'c'))
      *            res0: com.daodecode.scalaj.collection.JList[com.daodecode.scalaj.collection.JChar] = null
      *
      *            scala> jm
      *            res1: java.util.HashMap[com.daodecode.scalaj.collection.JLong,com.daodecode.scalaj.collection.JList[com.daodecode.scalaj.collection.JChar]] = {3=[a, b, c]}
      *
      *            scala> jm.deepAsScala
      *            res2: Map[Long,scala.collection.immutable.Seq[Char]] = Map(3 -> Vector(a, b, c))
      *         }}}
      */
    def deepAsScalaImmutable[C, D](
        implicit keyConverter: SConverter[A, C],
        valueConverter: SConverter[B, D]
    ): Map[C, D] =
      javaMap.deepAsScala[C, D].toMap[C, D]
  }

}
