package ru.maintainer.data

case class Account(login: String)
case class Account1(login: String, first_name: String, last_name: Option[String], apartment_number: Int, is_active: Boolean)
case class Account2(login: String, is_active: Boolean)
case class Account3(login: String, balance: Double)