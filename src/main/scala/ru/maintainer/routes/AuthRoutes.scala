package ru.maintainer.routes

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedRequest, AuthedRoutes, HttpRoutes, Request, Response}
import org.typelevel.ci.CIString
import ru.maintainer.data.{AccountRepo, Lots}
import ru.maintainer.Decoder.lotsDecoder
import ru.maintainer.auth.AuthUser

case object AnyError extends Error

object AuthRoutes {

  def authUser(accountRepo: AccountRepo): Kleisli[IO,Request[IO],Either[Error,AuthUser]] = Kleisli {request: Request[IO] =>
    // only for authenticated users
    request.headers.get(CIString("login")).map(_.head.value) match {
      case Some(login) =>
        accountRepo.getUser(login).map{ users =>
          if (users.getOrElse("") == login) Option(AuthUser(login)).toRight(AnyError)
          else Option(AuthUser("unregistered")).toRight(AnyError)
        }
      case None =>
        IO.pure(Left(AnyError))
    }
  }

  def onAuthFailure: AuthedRoutes[Error,IO] = Kleisli {req: AuthedRequest[IO,Error] =>
    // only for unauthenticated, return 401
    req.req match {
      case _ =>
        OptionT.pure[IO](
          Response[IO](
            status = Unauthorized
          )
        )
    }
  }

  def authRoutes(accountRepo: AccountRepo): AuthedRoutes[AuthUser,IO] = {
    AuthedRoutes.of {
      // Lists all lots for register users
      case GET -> Root / "lots" as user =>
        if (user.login != "unregistered") accountRepo.getLots()
          .flatMap{lots =>
            Ok(lots.mkString)}
        else Forbidden("You're anonymous'\n'")

      // Create new lot, only for active users
      case request@PUT -> Root / "create" / "new_lot" as user =>
        //TODO develop in for comprehension
        /*for {
            h1 <- IO(user.login != "unregistered")
            id <- accountRepo.checkActive(user.login).map(_.getOrElse(0)).unsafeRunSync()
            req <- request.req.as[Lots]
            ot <- accountRepo.createLot(id,
                                        req.name,
                                        req.description,
                                        req.count,
                                        req.price)
              } yield ???*/
        if (user.login != "unregistered") request.req.as[Lots]
          .flatMap {req =>
            // check if user have active subscription
            val check = accountRepo.checkActive(user.login).map(_.getOrElse(0)).unsafeRunSync()
            check match {
              case 0 => Forbidden("You havn`t got active subscription'\n'")
              case id => accountRepo.createLot(id,
                req.name,
                req.description,
                req.count,
                req.price
              ).flatMap(res => Ok(res))
            }
          }
        else Forbidden("You're anonymous'\n'")
    }
  }

  def authMiddleware(documentRepo: AccountRepo): AuthMiddleware[IO,AuthUser] = AuthMiddleware(authUser(documentRepo), onAuthFailure)

  def routes(documentRepo: AccountRepo): HttpRoutes[IO] = authMiddleware(documentRepo: AccountRepo).apply(authRoutes(documentRepo: AccountRepo))
}
