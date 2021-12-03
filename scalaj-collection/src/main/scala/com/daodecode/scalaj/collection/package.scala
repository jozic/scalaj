package com.daodecode.scalaj

import scala.collection.mutable.{Buffer => MBuffer, Map => MMap, Seq => MSeq, Set => MSet}
import scala.collection.{Map => GenMap, Seq => GenSeq, Set => GenSet}
import scala.reflect.ClassTag

/**
  * Importing `com.daodecode.scalaj.collection._` allows to use "extension" methods deepAsScala/deepAsJava,
  * which convert all supported nested Java/Scala collections as well as primitives.
  */
package object collection extends StdLibDecorators with JavaAliases {

  /******************************************** deepAsJava converters ********************************************/
  implicit class DeepSeqAsJavaList[A](val scalaSeq: GenSeq[A]) extends AnyVal {
    private def toJava[T](genSeq: GenSeq[T]): JList[T] = genSeq match {
      case buffer: MBuffer[T] => buffer.asJava
      case mSeq: MSeq[T] => mSeq.asJava
      case _ => genSeq.asJava
    }

    /**
      * Converts given Scala [[scala.collection.Seq]] to Java [[JList]].
      * Elements inside seq are converted using given implicit `converter`,
      * which allows to convert nested Scala collections and primitives.
      * If given seq is mutable, then returned Java list is mutable as well.
      * @param converter Implicit converter to convert from `A` to `B`.
      * @tparam B New type of resulting JList elements
      * @return Java list wrapper around given seq if given seq elements are primitives
      *         or anything but supported Scala collections. Wrapper around new seq otherwise.
      *
      *         Example:
      *         {{{
      *            scala> val s = Seq(Seq(1,2), Seq(), Seq(3))
      *            s: Seq[Seq[Int]] = List(List(1, 2), List(), List(3))
      *
      *            scala> s.deepAsJava
      *            res0: com.daodecode.scalaj.collection.package.JList[com.daodecode.scalaj.collection.JList[com.daodecode.scalaj.collection.JInt]] = [[1, 2], [], [3]]
      *         }}}
      */
    def deepAsJava[B](implicit converter: JConverter[A, B]): JList[B] = {
      converter match {
        case _: JCastConverter[_, _] => toJava(scalaSeq).asInstanceOf[JList[B]]
        case _ => toJava(scalaSeq.map(converter.convert))
      }
    }
  }

  implicit class DeepSetAsJavaSet[A](val scalaSet: GenSet[A]) extends AnyVal {
    private def toJava[T](genSet: GenSet[T]): JSet[T] = genSet match {
      case mSet: MSet[T] => mSet.asJava
      case _ => genSet.asJava
    }

    /**
      * Converts given Scala [[scala.collection.Set]] to Java [[JSet]].
      * Elements inside set are converted using given implicit `converter`,
      * which allows to convert nested Scala collections and primitives.
      * If given set is mutable, then returned Java set is mutable as well.
      * @param converter Implicit converter to convert from `A` to `B`
      * @tparam B New type of resulting JSet elements
      * @return Java set wrapper around given set if given set elements are primitives
      *         or anything but supported Scala collections. Wrapper around new set otherwise
      *
      *         Example:
      *         {{{
      *            scala> val s = Set(Seq(1,2), Seq(), Seq(3))
      *            s: scala.collection.immutable.Set[Seq[Int]] = Set(List(1, 2), List(), List(3))
      *
      *            scala> s.deepAsJava
      *            res0: com.daodecode.scalaj.collection.package.JSet[com.daodecode.scalaj.collection.JList[com.daodecode.scalaj.collection.JInt]] = [[1, 2], [], [3]]
      *         }}}
      */
    def deepAsJava[B](implicit converter: JConverter[A, B]): JSet[B] = converter match {
      case _: JCastConverter[_, _] => toJava(scalaSet).asInstanceOf[JSet[B]]
      case _ => toJava(scalaSet.map(converter.convert))
    }
  }

  implicit class DeepArrayAsJavaArray[A](val array: Array[A]) extends AnyVal {

    /**
      * Converts given array of A to array of B.
      * Elements inside array are converted using given implicit `converter`,
      * which allows to convert nested Scala collections and primitives.
      * @param converter Implicit converter to convert from `A` to `B`
      * @tparam B New type of resulting array elements
      * @return Same array if elements of given array are primitives
      *         or anything but supported Scala collections, new array otherwise
      *
      *         Example:
      *         {{{
      *            scala> val la = Array(Set(1L), Set(2L, 3L))
      *            la: Array[scala.collection.immutable.Set[Long]] = Array(Set(1), Set(2, 3))
      *
      *            scala> la.deepAsJava
      *            res0: Array[com.daodecode.scalaj.collection.JSet[com.daodecode.scalaj.collection.JLong]] = Array([1], [2, 3])
      *         }}}
      */
    def deepAsJava[B: ClassTag](implicit converter: JConverter[A, B]): Array[B] = converter match {
      case SelfJConverter => array.asInstanceOf[Array[B]]
      case _ => array.map(converter.convert)
    }
  }

  implicit class DeepMapAsJavaMap[A, B](val scalaMap: GenMap[A, B]) extends AnyVal {
    private def toJava[T, U](genMap: GenMap[T, U]): JMap[T, U] = genMap match {
      case mMap: MMap[T, U] => mMap.asJava
      case _ => genMap.asJava
    }

    /**
      * Converts given Scala [[scala.collection.Map]] to Java [[JMap]].
      * Keys and values inside map are converted using given implicit `converters`,
      * which allow to convert nested Scala collections and primitives.
      * If given map is mutable, then returned Java map is mutable as well.
      * @param keyConverter Implicit converter to convert from `A` to `C`
      * @param valueConverter Implicit converter to convert from `B` to `D`
      * @tparam C New type of Java map key
      * @tparam D New type of Java map value
      * @return Java map wrapper around given map if given map keys and values are primitives
      *         or anything but supported Scala collections. Wrapper around new map otherwise.
      *
      *         Example:
      *         {{{
      *            scala> val m = Map(3L -> Set('3'))
      *            m: scala.collection.immutable.Map[Long,scala.collection.immutable.Set[Char]] = Map(3 -> Set(3))
      *
      *            scala> m.deepAsJava
      *            res0: com.daodecode.scalaj.collection.package.JMap[com.daodecode.scalaj.collection.JLong,com.daodecode.scalaj.collection.JSet[com.daodecode.scalaj.collection.JChar]] = {3=[3]}
      *         }}}
      */
    def deepAsJava[C, D](implicit keyConverter: JConverter[A, C], valueConverter: JConverter[B, D]): JMap[C, D] =
      (keyConverter, valueConverter) match {
        case (_: JCastConverter[_, _], _: JCastConverter[_, _]) => toJava(scalaMap).asInstanceOf[JMap[C, D]]
        case _ => toJava(scalaMap.map { case (k, v) => keyConverter.convert(k) -> valueConverter.convert(v) })
      }
  }

  /******************************************** deepAsScala converters ********************************************/
  implicit class DeepJavaListAsMutableBuffer[A](val javaList: JList[A]) extends AnyVal {

    /**
      * Converts given Java [[JList]] to mutable Scala [[scala.collection.mutable.Buffer]]
      * Elements inside list are converted using given implicit `converter`,
      * which allows to convert nested Java collections and primitives.
      * @param converter Implicit converter to convert from `A` to `B`.
      * @tparam B New type of resulting Buffer elements
      * @return Scala wrapper around given list if given list elements are primitives
      *         or anything but supported Java collections. Wrapper around new list otherwise.
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
      *            scala> jl.deepAsScala
      *            res3: scala.collection.mutable.Buffer[scala.collection.mutable.Set[Int]] = ArrayBuffer(Set(23))
      *         }}}
      */
    def deepAsScala[B](implicit converter: SConverter[A, B]): MBuffer[B] = converter match {
      case _: SCastConverter[_, _] => javaList.asScala.asInstanceOf[MBuffer[B]]
      case _ => javaList.asScala.map(converter.convert)
    }
  }

  implicit class DeepJavaSetAsMutableSet[A](val javaSet: JSet[A]) extends AnyVal {

    /**
      * Converts given Java [[JSet]] to mutable Scala [[scala.collection.mutable.Set]]
      * Elements inside set are converted using given implicit `converter`,
      * which allows to convert nested Java collections and primitives.
      * @param converter Implicit converter to convert from `A` to `B`.
      * @tparam B New type of resulting Set elements
      * @return Scala wrapper around given Java set if given set elements are primitives
      *         or anything but supported Java collections. Wrapper around new set otherwise.
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
      *            scala> js.deepAsScala
      *            res3: scala.collection.mutable.Set[scala.collection.mutable.Set[Int]] = Set(Set(34))
      *         }}}
      */
    def deepAsScala[B](implicit converter: SConverter[A, B]): MSet[B] = converter match {
      case _: SCastConverter[_, _] => javaSet.asScala.asInstanceOf[MSet[B]]
      case _ => javaSet.asScala.map(converter.convert)
    }
  }

  implicit class DeepArrayAsScalaArray[A](val array: Array[A]) extends AnyVal {

    /**
      * Converts given array of A to array of B.
      * Elements inside array are converted using given implicit `converter`,
      * which allows to convert nested Java collections and primitives.
      * @param converter Implicit converter to convert from `A` to `B`
      * @tparam B New type of resulting array elements
      * @return Same array if elements of given array are primitives
      *         or anything but supported Java collections or primitive wrappers, new array otherwise
      *
      *         Example:
      *         {{{
      *            scala> val ja = Array(java.util.Arrays.asList[JInt](1,2,3))
      *            ja: Array[java.util.List[com.daodecode.scalaj.collection.JInt]] = Array([1, 2, 3])
      *
      *            scala> ja.deepAsScala
      *            res0: Array[scala.collection.mutable.Buffer[Int]] = Array(Buffer(1, 2, 3))
      *         }}}
      */
    def deepAsScala[B: ClassTag](implicit converter: SConverter[A, B]): Array[B] = converter match {
      case SelfSConverter => array.asInstanceOf[Array[B]]
      case _ => array.map(converter.convert)
    }
  }

  implicit class DeepJavaMapAsMutableMap[A, B](val javaMap: JMap[A, B]) extends AnyVal {

    /**
      * Converts given Java [[JMap]] to mutable Scala [[scala.collection.mutable.Map]].
      * Keys and values inside map are converted using given implicit `converters`,
      * which allow to convert nested Java collections and primitive wrappers.
      * @param keyConverter Implicit converter to convert from `A` to `C`
      * @param valueConverter Implicit converter to convert from `B` to `D`
      * @tparam C New type of Scala map key
      * @tparam D New type of Scala map value
      * @return Scala map wrapper around given map if given map keys and values are primitives
      *         or anything but supported Scala collections or primitive wrappers. Wrapper around new map otherwise.
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
      *            res2: scala.collection.mutable.Map[Long,scala.collection.mutable.Buffer[Char]] = Map(3 -> Buffer(a, b, c))
      *         }}}
      */
    def deepAsScala[C, D](implicit keyConverter: SConverter[A, C], valueConverter: SConverter[B, D]): MMap[C, D] =
      (keyConverter, valueConverter) match {
        case (_: SCastConverter[_, _], _: SCastConverter[_, _]) => javaMap.asScala.asInstanceOf[MMap[C, D]]
        case _ => javaMap.asScala.map { case (k, v) => keyConverter.convert(k) -> valueConverter.convert(v) }
      }
  }

}
