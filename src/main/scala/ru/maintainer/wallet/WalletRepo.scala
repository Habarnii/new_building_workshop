package ru.maintainer.wallet

import cats.effect.IO

trait WalletRepo {
  def getBalance(login: String): IO[Option[Double]]
  def topUp(login: String, balance: Double): IO[String]
  def withdraw(login: String, balance: Double): IO[String]
}


