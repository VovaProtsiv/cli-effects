package dev.protsiv.app

import cats.implicits.toShow
import dev.protsiv.app.Configuration.Configuration.{AppOp, readEnv}
import dev.protsiv.app.Syntax.IOOps

object App {
  def printOptions: AppOp[Unit] = {
    def mkOptionsString(commands: List[Command]): String = {
      val header: String = "Please select an option: \n"
      val commandsList: List[String] = commands.zipWithIndex.map {
        case (c, i) => s"($i) ${c.show}"
      }
      (header :: commandsList).mkString("\n") + "\n"
    }

    for {
      env <- readEnv
      allCommands = env.controller.getAllCommands
      options = mkOptionsString(allCommands)
      _ <- env.console.printLine(options).toAppOp
    } yield ()
  }
}
