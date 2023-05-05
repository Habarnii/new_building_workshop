package ru.maintainer.admin

import cats.effect.IO
import doobie.implicits._
import doobie.util.transactor.Transactor

class AdminRepoImpl(xa: Transactor[IO]) extends AdminRepo {
  override def createAccount(login: String, first_name: String, last_name: Option[String], apartment_number: Int, is_active: Boolean): IO[Int] = {
    for {
      i <- AdminQuery.insert(login, first_name, last_name, apartment_number, is_active).run.transact(xa)
      s <- IO {
        if (i == 1) i else 0
      }
    } yield s
  }

  override def updateSubscription(login: String, is_active: Boolean): IO[Int] = AdminQuery.update(login, is_active).run.transact(xa)

  override def getAdmin(login: String): IO[Option[String]] = AdminQuery.find(login).option.transact(xa)

}