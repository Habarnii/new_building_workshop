package ru.maintainer.data

import cats.effect.{Async, IO}
import doobie.implicits._
import doobie.util.transactor.Transactor

class AccountRepoImpl(xa: Transactor[IO]) extends AccountRepo {
  override def createLot(user_id: Int,
                         name: String,
                         description: String,
                         count: Double,
                         price: Double): IO[String] = {
    for {
      i <- AccountQuery.insert(user_id, name, description, count, price).run.transact(xa)
      s <- IO {
        if (i == 1) "Ok" else "False"
      }
    } yield s
  }

  override def getLots(): IO[List[Lots]] = {
    AccountQuery.searchAllLots.to[List].transact(xa)
  }

  override def getUser(name: String): IO[Option[String]] = {
    AccountQuery.getUser(name).option.transact(xa)
  }
  override def checkActive(login: String): IO[Option[Int]] = {
    AccountQuery.check(login).option.transact(xa)
  }




}


