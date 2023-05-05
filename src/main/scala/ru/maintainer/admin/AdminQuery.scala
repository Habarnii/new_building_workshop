package ru.maintainer.admin

import doobie.implicits._

object AdminQuery {
  // insert query
  def insert(login: String,
             first_name: String,
             last_name: Option[String],
             apartment_number: Int,
             is_active: Boolean): doobie.Update0 = {
    sql"""
         |INSERT INTO workshop.accounts (
         |  login,
         |  first_name,
         |  last_name,
         |  apartment_number,
         |  is_active
         |)
         |VALUES (
         |  $login,
         |  $first_name,
         |  $last_name,
         |  $apartment_number,
         |  $is_active
         |)
        """.stripMargin
      .update
  }

  def update(login: String, is_active: Boolean): doobie.Update0 = {
    sql"""
         |UPDATE workshop.accounts
         |SET is_active = $is_active
         |WHERE login = $login
        """.stripMargin
      .update
  }

  // find Admin
  def find(login: String): doobie.Query0[String] = {
    sql"""
         |SELECT login FROM workshop.accounts acc
         |INNER JOIN workshop.admins ad
         |ON acc.id = ad.id
         |WHERE login = $login
       """.stripMargin
      .query[String]
  }



}
