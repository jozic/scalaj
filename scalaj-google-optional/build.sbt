name := "scalaj-googleoptional"

description := "Conversions for google guava optional"

libraryDependencies += "com.google.guava" % "guava" % "10.0" % "provided"

console / initialCommands := """
  import com.daodecode.scalaj.googleoptional._
                              """
