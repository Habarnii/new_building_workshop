package ru.maintainer

import cats.effect._
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import ru.maintainer.data._

object Decoder {
  implicit val accountDecoder: EntityDecoder[IO,Account] = jsonOf[IO, Account]
  implicit val account1Decoder: EntityDecoder[IO,Account1] = jsonOf[IO, Account1]
  implicit val account2Decoder: EntityDecoder[IO,Account2] = jsonOf[IO, Account2]
  implicit val account3Decoder: EntityDecoder[IO,Account3] = jsonOf[IO, Account3]
  implicit val lotsDecoder: EntityDecoder[IO,Lots] = jsonOf[IO, Lots]
}
