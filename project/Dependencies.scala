import sbt._

object Dependencies {

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.14"

  lazy val CatsEffectVersion = "3.3.14"
  lazy val catsCore = "org.typelevel" %% "cats-core" % "2.8.0"

  lazy val catsEffect: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-effect" % CatsEffectVersion,
    "org.typelevel" %% "cats-effect-kernel" % CatsEffectVersion,
    "org.typelevel" %% "cats-effect-std" % CatsEffectVersion
  )

  val doobieVersion = "1.0.0-RC1"
  lazy val http4sVersion = "0.23.12"
  lazy val circeVersion = "0.13.0"

  lazy val doobie: Seq[ModuleID] = Seq(
    "org.tpolecat"          %% "doobie-core"            % doobieVersion,
    "org.tpolecat"          %% "doobie-postgres"        % doobieVersion,
    "org.tpolecat"          %% "doobie-hikari"          % doobieVersion,
    "org.tpolecat"          %% "doobie-specs2"          % doobieVersion
  )

  lazy val http4s: Seq[ModuleID] = Seq(
    "org.http4s"            %% "http4s-blaze-server"    % http4sVersion,
    "org.http4s"            %% "http4s-blaze-client"    % http4sVersion,
    "org.http4s"            %% "http4s-circe"           % http4sVersion,
    "org.http4s"            %% "http4s-dsl"             % http4sVersion
  )

  lazy val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core"    % circeVersion,
    "io.circe" %% "circe-parser"  % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-literal" % circeVersion,
    "io.circe" %% "circe-config"  % "0.8.0"
  )

  lazy val slf4j = "org.slf4j" %% "slf4j-api" % "1.7.5"
}
