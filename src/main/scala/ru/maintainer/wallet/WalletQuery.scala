package ru.maintainer.wallet

import doobie.implicits._

object WalletQuery {

  // get actual balance
  def getBalance(login: String): doobie.Query0[Double] = {
    sql"""
         |SELECT
         |  balance
         |FROM workshop.accounts
         |WHERE login=$login
        """.stripMargin
      .query[Double]
  }

  // top up money for user
  def topUp(login: String, balance: Double): doobie.Update0 = {
    sql"""
         |UPDATE workshop.accounts
         |SET balance = balance + $balance
         |WHERE login = $login
       """.stripMargin
      .update
  }

  // withdraw money for user
  def withdraw(login: String, balance: Double): doobie.Update0 = {
    sql"""
         |UPDATE workshop.accounts
         |SET balance = balance - $balance
         |WHERE login = $login
       """.stripMargin
      .update
  }

}
