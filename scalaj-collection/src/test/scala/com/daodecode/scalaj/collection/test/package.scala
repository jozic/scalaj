package com.daodecode.scalaj.collection

import scala.reflect.ClassTag

package object test {

  def jb(b: Byte): JByte = b

  def js(s: Short): JShort = s

  def ji(i: Int): JInt = i

  def newInstance[A: ClassTag]: A =
    implicitly[ClassTag[A]].runtimeClass.getDeclaredConstructor().newInstance().asInstanceOf[A]
}
