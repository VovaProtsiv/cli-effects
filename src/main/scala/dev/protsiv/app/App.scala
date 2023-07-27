package dev.protsiv.app

import cats.MonadError
import cats.implicits.toShow
import dev.protsiv.app.Configuration.{AppOp, readEnv}
import dev.protsiv.app.Syntax.IOOps

object App {
  val ME = MonadError[AppOp, String]
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

  def readCommandNumber(): AppOp[Int] = {
    for {
      env <- readEnv
      option <- env.console.readLine("Your option: ").toAppOp
      cmdNumber <- ME.fromOption(option.toIntOption, "Invalid option selected")
    } yield cmdNumber
  }
}
