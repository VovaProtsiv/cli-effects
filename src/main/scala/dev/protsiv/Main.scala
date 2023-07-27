package dev.protsiv

import cats.effect.{ExitCode, IO, IOApp}
import dev.protsiv.app.App
import dev.protsiv.app.Configuration.liveEnv
import dev.protsiv.service.{Console, LiveClient, LiveConsole}
import dev.protsiv.app.Configuration.path

object Main extends IOApp{
  override def run(args: List[String]): IO[ExitCode] = {
    App.printOptions.run(liveEnv)
      .value
      .flatTap{
        _=>
          new LiveClient {}
            .call(path)
            .flatTap(msg => new LiveConsole {}.printLine(msg, Console.Success))
      }
      .as(ExitCode.Success)
  }
}
