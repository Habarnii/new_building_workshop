import Dependencies._

//ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / scalaVersion     := "2.11.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "Habarnii"

lazy val root = (project in file("."))
  .settings(
    name := "new_building_workshop",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Dependencies.catsEffect,
    //libraryDependencies += catsCore,
    //libraryDependencies += catsEffect,
    //libraryDependencies += catsEffectStd,
    //libraryDependencies += catsEffectKernel,
    libraryDependencies ++= Dependencies.circe,
    libraryDependencies ++= Dependencies.doobie,
    libraryDependencies ++= Dependencies.http4s//,
    //libraryDependencies ++= Dependencies.scalapb
  )