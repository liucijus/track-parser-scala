name := "track-parser"

organization := "lt.overdrive"

//N.B.!  Version is generated upon sbt startup. To get unique build versions reload sbt.
version in ThisBuild := "1.0-" + new java.text.SimpleDateFormat("yyyyMMdd.kkmmss.SSS").format(new java.util.Date())

sbtBinaryVersion := "0.13"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "0.6.0"

libraryDependencies += "org.specs2" %% "specs2" % "2.3.7" % "test"

scalacOptions in Test ++= Seq("-Yrangepos")
