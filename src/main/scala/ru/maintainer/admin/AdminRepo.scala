package ru.maintainer.admin

import cats.effect.IO

trait AdminRepo {
  def createAccount(login: String, first_name: String, last_name: Option[String], apartment_number: Int, is_active: Boolean): IO[Int]
  def updateSubscription(login: String, is_active: Boolean): IO[Int]
  def getAdmin(name: String): IO[Option[String]]
}
