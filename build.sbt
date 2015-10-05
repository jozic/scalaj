import org.scoverage.coveralls.CoverallsPlugin._
import scoverage._
import ReleaseTransformations._
import sbtrelease._
import xerial.sbt.Sonatype._
import scala.xml.{Node => XmlNode, NodeSeq => XmlNodeSeq, _}
import scala.xml.transform.{RewriteRule, RuleTransformer}

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

  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/BSD-3-Clause")),

  pomPostProcess := { (node: XmlNode) =>
    new RuleTransformer(new RewriteRule {
      override def transform(node: XmlNode): XmlNodeSeq = node match {
        case e: Elem
          if e.label == "dependency" && e.child.exists(child => child.label == "groupId" && child.text == "org.scoverage") => XmlNodeSeq.Empty
        case _ => node
      }
    }).transform(node).head
  }

)

val commonSettings = Seq (
  organization := "com.daodecode",

  scalaVersion := "2.11.7",

  crossScalaVersions := Seq("2.10.6", "2.11.7")
) ++ releaseSettings

val moduleSettings = commonSettings ++ Seq(
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint", "-Xfatal-warnings"),

  javacOptions ++= Seq("-source", "1.6", "-target", "1.6"),

  libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % "test"
) ++ publishSettings ++ coverageSettings

lazy val scalaj = project.in(file(".")).aggregate(`scalaj-collection`, `scalaj-google-optional`)
  .settings(
    publishArtifact := false,
    publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo"))),
    commonSettings
  )

lazy val `scalaj-collection` = project.settings(moduleSettings)

lazy val `scalaj-google-optional` = project.settings(moduleSettings).
  dependsOn(`scalaj-collection` % "compile->compile;test->test")
