package com.daodecode.scalaj.collection.test

import java.util

import scala.reflect.ClassTag

import com.daodecode.scalaj.collection._

trait JListBuilder {
  def listOf[A, JL <: JList[A] : ClassTag](as: A*): JL = {
    val list = newInstance[JL]
    as.foreach(list.add)
    list
  }

  def JList[A](as: A*): JList[A] = listOf[A, util.ArrayList[A]](as: _*)

}

trait JSetBuilder {
  def setOf[A, JS <: JSet[A] : ClassTag](as: A*): JS = {
    val set = newInstance[JS]
    as.foreach(set.add)
    set
  }

  def JSet[A](as: A*): JSet[A] = setOf[A, util.HashSet[A]](as: _*)
}

trait JMapBuilder {
  def mapOf[A, B, JM <: JMap[A, B] : ClassTag](pairs: (A, B)*): JM = {
    val jm = newInstance[JM]
    pairs.foreach { case (k, v) => jm.put(k, v)}
    jm
  }

  def JMap[A, B](pairs: (A, B)*): JMap[A, B] = mapOf[A, B, util.HashMap[A, B]](pairs: _*)
}
