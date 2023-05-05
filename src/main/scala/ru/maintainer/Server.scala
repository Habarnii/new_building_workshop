package ru.maintainer

import cats.data.Kleisli
import cats.effect.kernel.Resource
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import doobie.util.transactor.Transactor
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{Request, Response}
import ru.maintainer.admin.AdminRepoImpl
import ru.maintainer.config.{Config, ServerConfig}
import ru.maintainer.data.AccountRepoImpl
import ru.maintainer.database.{DatabaseBootstrap, DatabaseTransactor}
import ru.maintainer.routes.{AdminRoutes, AuthRoutes, WalletRoutes}
import ru.maintainer.wallet.WalletRepoImpl


object Server extends IOApp {


  def makeRouter(transactor: Transactor[IO]): Kleisli[IO, Request[IO], Response[IO]] = {
    Router[IO](
      "/api/" -> AuthRoutes.routes(new AccountRepoImpl(transactor)),
      "/api/admin" -> AdminRoutes.routes(new AdminRepoImpl(transactor)),
      "/api/wallet" -> WalletRoutes.routes(new AccountRepoImpl(transactor), new WalletRepoImpl(transactor))
    ).orNotFound
  }

  def serveStream(transactor: Transactor[IO], serverConfig: ServerConfig): Resource[IO, ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(serverConfig.port, serverConfig.host)
      .withHttpApp(makeRouter(transactor))
      .resource
      .as(ExitCode.Success)

  override def run(args: List[String]): IO[ExitCode] = {

    val stream = for {
      config <- Config.load("workshop")
      xa <- DatabaseTransactor.pgTransactor(config.dbConfig)
      _ <- DatabaseBootstrap.bootstrap(xa)
      exitCode <- serveStream(xa, config.serverConfig)
    } yield exitCode

    stream.use(_ => IO.never).as(ExitCode.Success)
  }


  // curl -X PUT -H "login: admin" localhost:8080/api/admin/register -d '{"login":"test","first_name":"Test","last_name":"Test","apartment_number":10,"is_active":false}' -v
  // curl -X PUT -H "login: admin" localhost:8080/api/admin/renew_subscription -d '{"login":"test","is_active":true}' -v



  // curl -X PUT -H "login: test" localhost:8080/api/create/new_lot -d '{"name":"lightbulb","description":"200V","count":10.0,"price":10.0}' -v
  // curl -X PUT -H "login: alex02" localhost:8080/api/create/new_lot -d '{"name":"lightbulb","description":"200V","count":10.0,"price":10.0}' -v


  // curl -X GET -H "login: admin"  localhost:8080/api/wallet/balance -d {'"login":"admin"'} -v
  // curl -X PUT -H "login: admin"  localhost:8080/api/wallet/balance -d {'"login":"admin","balance":1000 '} -v
}




