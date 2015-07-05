package com.daodecode.scalaj.collection

import scala.reflect.ClassTag

package object test {

  def jb(b: Byte): JByte = b

  def js(s: Short): JShort = s

  def ji(i: Int): JInt = i

  def newInstance[A: ClassTag] =
    implicitly[ClassTag[A]].runtimeClass.newInstance().asInstanceOf[A]
}
