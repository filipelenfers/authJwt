import sbt.Keys._
import sbt._
import sbtrelease.Version

name := "AuthJwt"

resolvers += Resolver.sonatypeRepo("public")
scalaVersion := "2.12.8"

val circeVersion = "0.10.0"

releaseNextVersion := { ver => Version(ver).map(_.bumpMinor.string).getOrElse("Error") }
assemblyJarName in assembly := "authJwt.jar"


libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-events" % "2.2.2",
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
  "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.426",
  "com.auth0" % "java-jwt" % "3.4.0",
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-optics" % circeVersion
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings")
