package ru.maintainer.config

import cats.effect
import cats.effect.IO
import cats.effect.kernel.Resource
import io.circe.config.parser
import io.circe.generic.auto._
import com.typesafe.config._

case class ServerConfig(port: Int, host: String)

case class DbConfig(driver: String, url: String, username: String, password: String, poolSize: Int)

case class Config(serverConfig: ServerConfig, dbConfig: DbConfig)

object  Config {

 def load(path: String): Resource[IO, Config] = {
   for {
     dbConf <- effect.Resource.eval(parser.decodePathF[IO, DbConfig](path+".db"))
     serverConf <- effect.Resource.eval(parser.decodePathF[IO, ServerConfig](path+".server"))
   } yield Config(serverConf, dbConf)
 }

}

