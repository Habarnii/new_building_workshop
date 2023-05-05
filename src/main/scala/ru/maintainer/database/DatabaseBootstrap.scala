package ru.maintainer.database

import cats.effect.{IO, Resource}
import doobie.Transactor
import doobie.implicits._

object DatabaseBootstrap {

  def bootstrap(xa: Transactor[IO]): Resource[IO, Int] = {
    for {
      schema <- Resource.eval(DatabaseQuery.createSchema.run.transact(xa))
      acc <- Resource.eval(DatabaseQuery.createTableAccounts.run.transact(xa))
      lots <- Resource.eval(DatabaseQuery.createTableLots.run.transact(xa))
      adm <- Resource.eval(DatabaseQuery.createTableAdmins.run.transact(xa))
      crt <- Resource.eval(DatabaseQuery.createAdmin.run.transact(xa))
    } yield schema + acc + lots + adm + crt
  }

}
