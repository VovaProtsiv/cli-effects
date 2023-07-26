package dev.protsiv.app

import cats.data.{EitherT, ReaderT}
import cats.effect.IO
import dev.protsiv.service.{Client, Console, Controller, LiveClient, LiveConsole,LiveController}

object Configuration {
  val path:String = sys.env.getOrElse("CLI_EFFECTS_API_PATH","Path not found")


  object Configuration {
    type Error = String
    type ErrorOr[A] = EitherT[IO, Error, A]
    type SuccessMsg = String
    type AppOp[A] = ReaderT[ErrorOr, Environment, A]

    val liveEnv: Environment = Environment(new LiveConsole {}, LiveController(List.empty), new LiveClient {})

    def readEnv: AppOp[Environment] = ReaderT.ask[ErrorOr, Environment]
  }

  case class Environment(console: Console, controller: Controller, client: Client)
}
