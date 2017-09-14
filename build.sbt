import scoverage._
import ReleaseTransformations._
import xerial.sbt.Sonatype._

val coverageSettings = Seq(

  CoverallsKeys.coverallsTokenFile := Some("./token.txt"),

  ScoverageKeys.coverageMinimum := 95,

  ScoverageKeys.coverageFailOnMinimum := true,

  ScoverageKeys.coverageHighlighting := true
)

val releaseSettings = Seq(

  releaseCrossBuild := true,

  releasePublishArtifactsAction := PgpKeys.publishSigned.value,

  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
)

val publishSettings = sonatypeSettings ++ Seq(

  startYear := Some(2015),

  homepage := Some(url("http://github.com/jozic/scalaj")),

  developers := List(
    Developer("jozic", "Eugene Platonov", "jozic@live.com", url("http://github.com/jozic"))
  ),

  scmInfo := homepage.value.map(ScmInfo(_, "scm:git:git@github.com:jozic/scalaj.git")),

  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/BSD-3-Clause"))
)

val commonSettings = Seq(
  organization := "com.daodecode",
  crossScalaVersions := Seq("2.11.11", "2.12.3")
) ++ releaseSettings

val moduleSettings = commonSettings ++ Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-Xlint",
    "-Xfatal-warnings"
  ),

  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
) ++ publishSettings ++ coverageSettings

lazy val scalaj =
  project.in(file("."))
    .aggregate(
      `scalaj-collection`,
      `scalaj-google-optional`,
      `scalaj-java-optional`)
    .settings(
      publishArtifact := false,
      publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo"))),
      commonSettings
    )

lazy val `scalaj-collection` = project.settings(moduleSettings)

lazy val `scalaj-google-optional` = project.settings(moduleSettings).
  dependsOn(`scalaj-collection` % "compile->compile;test->test")

lazy val `scalaj-java-optional` = project.settings(moduleSettings).
  dependsOn(`scalaj-collection` % "compile->compile;test->test")
