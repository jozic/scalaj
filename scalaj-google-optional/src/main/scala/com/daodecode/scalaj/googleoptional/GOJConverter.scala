package com.daodecode.scalaj.googleoptional

import com.daodecode.scalaj.collection.{CollectionsJConverter, JConverter, PrimitivesJConverter}

import scala.language.implicitConversions

trait GOJConverter[-A, +B] extends JConverter[A, B]

object GOJConverter extends PrimitivesJConverter with CollectionsJConverter {

  def apply[A, B](c: A => B) = new GOJConverter[A, B] {
    override def convert(a: A): B = c(a)
  }

  implicit def JConverterToGOJConverter[A, B](implicit jConv: JConverter[A, B]): GOJConverter[A, B] =
    GOJConverter[A, B](jConv.convert)

}


