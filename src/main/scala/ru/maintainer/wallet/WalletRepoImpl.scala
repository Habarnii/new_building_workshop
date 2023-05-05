package ru.maintainer.wallet

import cats.effect.IO
import doobie.util.transactor.Transactor
import doobie.implicits._

class WalletRepoImpl(xa: Transactor[IO]) extends WalletRepo {

  override def getBalance(login: String): IO[Option[Double]] = {
    WalletQuery.getBalance(login).option.transact(xa)
  }

  override def topUp(login: String, balance: Double) = {
    for {
      i <- WalletQuery.topUp(login, balance).run.transact(xa)
      s <- IO {
        if (i == 1) "Ok" else "False"
      }
    } yield s
  }

  override def withdraw(login: String, balance: Double): IO[String] = {
    for {
      i <- WalletQuery.withdraw(login, balance).run.transact(xa)
      s <- IO {
        if (i == 1) "Ok" else "False"
      }
    } yield s
  }

}


