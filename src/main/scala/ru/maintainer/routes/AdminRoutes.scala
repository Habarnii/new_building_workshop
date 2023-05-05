package ru.maintainer.routes

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedRequest, AuthedRoutes, HttpRoutes, Request, Response, Status}
import org.typelevel.ci.CIString
import ru.maintainer.Decoder.{account1Decoder, account2Decoder}
import ru.maintainer.admin.AdminRepo
import ru.maintainer.auth.Admin
import ru.maintainer.data.{Account1, Account2}

object AdminRoutes {

  def authAdmin(adminRepo: AdminRepo): Kleisli[IO,Request[IO],Either[Error,Admin]] = Kleisli {request: Request[IO] =>
    // only for authenticated users
    request.headers.get(CIString("login")).map(_.head.value) match {
      case Some(login) =>
        adminRepo.getAdmin(login).map{ users =>
          if (users.getOrElse("") == login) Option(Admin(login)).toRight(AnyError)
          else Option(Admin("unregistered")).toRight(AnyError)
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
            status = Status.Unauthorized
          )
        )
    }
  }

  def authRoutes(adminRepo: AdminRepo): AuthedRoutes[Admin,IO] = {
    AuthedRoutes.of {

      case request@PUT -> Root / "register" as user =>
        if (user.login != "unregistered") request.req.as[Account1]
          .flatMap(req =>
            adminRepo.createAccount(
              req.login,
              req.first_name,
              req.last_name,
              req.apartment_number,
              req.is_active))
          .flatMap(res => Ok(res))
        else Forbidden("You're anonymous'\n'")

      case request@PUT -> Root / "renew_subscription" as user =>
        if (user.login != "unregistered") request.req.as[Account2]
          .flatMap(req =>
            adminRepo.updateSubscription(req.login, req.is_active))
          .flatMap(res => Ok(res))
        else Forbidden("You're anonymous'\n'")

    }
  }

  def authMiddleware(documentRepo: AdminRepo): AuthMiddleware[IO,Admin] = AuthMiddleware(authAdmin(documentRepo), onAuthFailure)

  def routes(documentRepo: AdminRepo): HttpRoutes[IO] = authMiddleware(documentRepo: AdminRepo).apply(authRoutes(documentRepo: AdminRepo))
}
