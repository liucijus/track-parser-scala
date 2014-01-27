name := "track-parser"

organization := "lt.overdrive"

val gitHeadCommitSha = settingKey[String]("SHA of HEAD commit")

gitHeadCommitSha in ThisBuild := Process("git rev-parse HEAD").lines.head

version in ThisBuild := "1.0-" + gitHeadCommitSha.value

sbtBinaryVersion := "0.13"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "0.6.0"

libraryDependencies += "org.specs2" %% "specs2" % "2.3.7" % "test"

scalacOptions in Test ++= Seq("-Yrangepos")
