package ru.maintainer.data

import doobie.implicits._

object AccountQuery {

  // insert query
  def insert(user_id: Int,
             name: String,
             description: String,
             count: Double,
             price: Double): doobie.Update0 = {
    sql"""
         |INSERT INTO workshop.lots (
         |  user_id,
         |  name,
         |  description,
         |  count,
         |  price
         |)
         |VALUES (
         |  $user_id,
         |  $name,
         |  $description,
         |  $count,
         |  $price
         |)
        """.stripMargin
      .update
  }

  // update query
  def update(id: Int, name: String): doobie.Update0 = {
    sql"""
         |UPDATE accounts
         |SET name = $name
         |WHERE id = $id
       """.stripMargin
      .update
  }

  // search account
  def searchAllLots: doobie.Query0[Lots] = {
    sql"""
         |SELECT
         |  name,
         |  description,
         |  count,
         |  price
         |FROM workshop.lots
       """.stripMargin
      .query[Lots]
  }

  // delete query
  def delete(id: String): doobie.Update0 = {
    sql"""
         |DELETE FROM workshop.accounts
         |WHERE id=$id
       """.stripMargin
      .update
  }

  // find User
  def getUser(login: String): doobie.Query0[String] = {
    sql"""
         |SELECT login FROM workshop.accounts
         |WHERE login = $login
       """.stripMargin
      .query[String]
  }

  // check if user is active
  def check(login: String): doobie.Query0[Int] = {
    sql"""
         |SELECT id FROM workshop.accounts acc
         |WHERE login = $login
         |  AND is_active IS TRUE
       """.stripMargin
      .query[Int]
  }


}
