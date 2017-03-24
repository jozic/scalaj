name := "scalaj-javaoptional"

description := "Conversions for java optional"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")


initialCommands in console :=
  """
  import com.daodecode.scalaj.javaoptional._
  """
