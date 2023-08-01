package dev.protsiv.app

import cats.MonadError
import cats.implicits.{toFunctorOps, toShow}
import dev.protsiv.app.Configuration.{AppOp, readEnv}
import dev.protsiv.app.Syntax.IOOps
import dev.protsiv.service.Console

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

  def executeCommand: AppOp[Boolean] =
    for {
      env <- readEnv
      _ <- printOptions
      commandNumber <- readCommandNumber()
      command <- ME.fromOption(env.controller.getCommandByNumber(commandNumber), "Command not found")
      successMsg <- command.execute()
      _ <- env.console.printLine(s"\n$successMsg\n", Console.Success).toAppOp
    } yield command.isExit



  def run(): AppOp[Unit] = {
    def executeCommandWithRecovery: AppOp[Boolean] =
      ME.handleErrorWith(executeCommand) { e =>
        for {
          env <- readEnv
          _ <- env.console.printLine(s"\n$e\n", Console.Error).toAppOp
        } yield false
      }

    ME.iterateUntil(executeCommandWithRecovery)(identity).void
  }
}
