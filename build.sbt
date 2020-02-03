import sbt.Keys.libraryDependencies

lazy val commonSettings = Seq(
  organization := "com.stulsoft",
  version := "1.0.1",
  javacOptions ++= Seq("-source", "11"),
  scalaVersion := "2.13.1",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-language:implicitConversions",
    "-language:postfixOps")
)

lazy val tools = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "tools"
  )