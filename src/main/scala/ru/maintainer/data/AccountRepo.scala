package ru.maintainer.data

import cats.effect.IO

trait AccountRepo {
  def createLot(user_id: Int,
                name: String,
                description: String,
                count: Double,
                price: Double): IO[String]
  def getLots(): IO[List[Lots]]

  def getUser(name: String): IO[Option[String]]
  def checkActive(login: String): IO[Option[Int]]
}


