import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
import scoverage._
import xerial.sbt.Sonatype._

def scala213 = Def.setting(scalaVersion.value.startsWith("2.13"))
def scala3 = Def.setting(scalaVersion.value.startsWith("3."))

lazy val crossVersionSourcesSettings =
  Seq(Compile, Test).map { sc =>
    (sc / unmanagedSourceDirectories) ++= {
      (sc / unmanagedSourceDirectories).value.flatMap { dir =>
        if (dir.getPath.endsWith("scala"))
          Seq(new File(dir.getPath + (if (scala213.value || scala3.value) "_2.13+" else "_2.13-")))
        else
          Seq.empty
      }
    }
  }

val coverageSettings = Seq(
  CoverallsKeys.coverallsTokenFile := Some("./token.txt"),
  ScoverageKeys.coverageMinimumStmtTotal := 100,
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
    releaseStepCommandAndRemaining("+publishSigned"),
    releaseStepCommand("sonatypeBundleRelease"),
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
  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/BSD-3-Clause")),
  publishTo := sonatypePublishToBundle.value
)

val commonSettings = Seq(
  organization := "com.daodecode",
  scalaVersion := "2.13.11",
  crossScalaVersions := Seq(scalaVersion.value, "2.12.18", "3.3.0"),
  scalafmtConfig := Some(scalaj.base / "scalafmt-config/.scalafmt.conf")
) ++ releaseSettings ++ crossVersionSourcesSettings

val Scala212ScalacOptions = Seq(
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
)

val Scala213ScalacOptions = Seq(
  "-Xlint",
  "-unchecked",
  "-deprecation",
  "-Xfatal-warnings",
  "-Ywarn-dead-code",
  "-feature",
  "-Ywarn-unused",
  "-encoding", "UTF-8",
  "-Wconf:origin=scala.collection.compat.*:s"
)

val Scala3ScalacOptions = Seq(
  "-unchecked",
  "-deprecation",
  "-Werror",
  "-feature",
  "-encoding", "UTF-8"
)

val moduleSettings = commonSettings ++ Seq(
  scalacOptions := (
    if (scala3.value) Scala3ScalacOptions
    else if (scala213.value) Scala213ScalacOptions
    else Scala212ScalacOptions
    ),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.16" % "test"
  )
) ++ publishSettings ++ coverageSettings

lazy val scalaj: Project =
  project
    .in(file("."))
    .aggregate(`scalaj-collection`, `scalaj-google-optional`)
    .settings(
      publishArtifact := false,
      publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo"))),
      commonSettings
    )

lazy val `scalaj-collection` = project.settings(
  moduleSettings ++ Seq(
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.10.0"
    )
  )
)

lazy val `scalaj-google-optional` =
  project.settings(moduleSettings).dependsOn(`scalaj-collection` % "compile->compile;test->test")

addCommandAlias("scoverage", ";clean;coverage;test;coverageReport")

addCommandAlias("checkFormatting", ";scalafmtCheck;test:scalafmtCheck")
