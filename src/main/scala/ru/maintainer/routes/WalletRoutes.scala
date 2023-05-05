package ru.maintainer.routes

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedRequest, AuthedRoutes, HttpRoutes, Request, Response}
import org.typelevel.ci.CIString
import ru.maintainer.Decoder.{account3Decoder, accountDecoder}
import ru.maintainer.auth.{Admin, AuthUser}
import ru.maintainer.data.{Account, Account3, AccountRepo}
import ru.maintainer.wallet.WalletRepo

object WalletRoutes {

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

  def walletRoutes(walletRepo: WalletRepo): AuthedRoutes[AuthUser,IO] = {
    AuthedRoutes.of {

      case request@GET -> Root / "balance" as user =>
        if (user.login != "unregistered") request.req.as[Account]
          .flatMap{req =>
            println(walletRepo.getBalance(req.login))
            walletRepo.getBalance(req.login)
          .flatMap(res => Ok(res))}
        else Forbidden("You're anonymous'\n'")

      case request@PUT -> Root / "top_up" as user =>
        if (user.login != "unregistered") request.req.as[Account3]
          .flatMap(req =>
            walletRepo.topUp(req.login, req.balance))
          .flatMap(res => Ok(res))
        else Forbidden("You're anonymous'\n'")

    }
  }

  def authMiddleware(accountRepo: AccountRepo): AuthMiddleware[IO,AuthUser] = AuthMiddleware(authUser(accountRepo), onAuthFailure)

  def routes(accountRepo: AccountRepo, walletRepo: WalletRepo): HttpRoutes[IO] = authMiddleware(accountRepo).apply(walletRoutes(walletRepo))
}
