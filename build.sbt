name := "scalaj"

organization := "com.daodecode"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4", "2.11.5")

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint", "-Xfatal-warnings")

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

val settings = Seq(
  libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.2" % "test"
)

lazy val scalaj = project.in(file(".")).aggregate(`scalaj-collection`, `scalaj-google-optional`)

lazy val `scalaj-collection` = project.settings(settings: _*)

lazy val `scalaj-google-optional` = project.settings(settings: _*).
  dependsOn(`scalaj-collection` % "compile->compile;test->test")
