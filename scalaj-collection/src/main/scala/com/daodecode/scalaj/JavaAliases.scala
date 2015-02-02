package com.daodecode.scalaj

trait JavaAliases {
  type JList[A] = java.util.List[A]
  type JSet[A] = java.util.Set[A]
  type JMap[A, B] = java.util.Map[A, B]

  type JByte = java.lang.Byte
  type JShort = java.lang.Short
  type JInt = java.lang.Integer
  type JLong = java.lang.Long
  type JFloat = java.lang.Float
  type JDouble = java.lang.Double
  type JChar = java.lang.Character
  type JBoolean = java.lang.Boolean
}

object JavaAliases extends JavaAliases
