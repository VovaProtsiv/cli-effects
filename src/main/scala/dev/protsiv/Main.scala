package dev.protsiv

import cats.effect.{ExitCode, IO, IOApp}
import dev.protsiv.service.{Console,LiveClient, LiveConsole}
import dev.protsiv.app.Configuration.path

object Main extends IOApp{
  override def run(args: List[String]): IO[ExitCode] =
    new LiveClient{}
      .call(path)
      .flatTap(msg=>new LiveConsole {}.printLine(msg,Console.Success))
      .as(ExitCode.Success)
}
