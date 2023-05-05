package ru.maintainer.database

import cats.effect.{IO, Resource}
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import ru.maintainer.config.DbConfig

object DatabaseTransactor{

  def pgTransactor(dbConfig: DbConfig): Resource[IO, HikariTransactor[IO]] = for {
    ce <- ExecutionContexts.fixedThreadPool[IO](dbConfig.poolSize)
    xa <- HikariTransactor.newHikariTransactor[IO](
      dbConfig.driver,
      dbConfig.url,
      dbConfig.username,
      dbConfig.password,
      ce,
    )
  } yield xa
}
