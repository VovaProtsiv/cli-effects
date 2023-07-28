package dev.protsiv

import cats.effect.{ExitCode, IO, IOApp}
import dev.protsiv.app.App
import dev.protsiv.app.Configuration.liveEnv
import dev.protsiv.app.Syntax.AppOps


object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    App
      .run()
      .runApp(liveEnv)
      .as(ExitCode.Success)
  }
}
