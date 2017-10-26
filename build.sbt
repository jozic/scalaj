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

  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    releaseStepCommand("publishSigned"),
    setNextVersion,
    commitNextVersion,
    releaseStepCommand("sonatypeReleaseAll"),
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

  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/BSD-3-Clause")),

  publishTo := Some(
    if (isSnapshot.value)
      Opts.resolver.sonatypeSnapshots
    else
      Opts.resolver.sonatypeStaging
  )
)

val commonSettings = Seq(
  organization := "com.daodecode",
  scalaVersion := "2.11.11",
  crossScalaVersions := Seq(scalaVersion.value, "2.12.4")
) ++ releaseSettings

val moduleSettings = commonSettings ++ Seq(
  scalacOptions := Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-Xfatal-warnings",
    "-Ywarn-inaccessible",
    "-Ywarn-dead-code",
    "-Ywarn-adapted-args",
    "-Ywarn-nullary-unit",
    "-feature",
    "-Ywarn-unused",
    "-Ywarn-unused-import",
    "-encoding", "UTF-8"
  ),
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"
) ++ publishSettings ++ coverageSettings

lazy val scalaj =
  project.in(file("."))
    .aggregate(
      `scalaj-collection`,
      `scalaj-google-optional`)
    .settings(
      publishArtifact := false,
      publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo"))),
      commonSettings
    )

lazy val `scalaj-collection` = project.settings(moduleSettings)

lazy val `scalaj-google-optional` = project.settings(moduleSettings).
  dependsOn(`scalaj-collection` % "compile->compile;test->test")

addCommandAlias("scoverage", ";clean;coverage;test;coverageReport")