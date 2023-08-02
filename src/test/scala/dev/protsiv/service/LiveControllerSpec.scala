package dev.protsiv.service

import cats.effect.IO
import dev.protsiv.app.Command
import dev.protsiv.app.Syntax._
import dev.protsiv.app.Configuration.{AppOp, SuccessMsg}
import org.scalatest.wordspec.AnyWordSpecLike

class LiveControllerSpec extends AnyWordSpecLike {
  val command1 = new Command {
    override val name: String = "command1"

    override def execute(): AppOp[SuccessMsg] = IO.pure("execute command1").toAppOp
  }
  val command2 = new Command {
    override val name: String = "command2"

    override def execute(): AppOp[SuccessMsg] = IO.pure("execute command2").toAppOp
  }
  val commands = List(command1, command2)
  private val controller: LiveController = LiveController(commands)
  val emptyController = LiveController(Nil)

  "A getAllCommands" when {
    "commands is not empty" should {
      "return all commands" in {
        assertResult(commands)(controller.getAllCommands)
      }
    }
    "commands is empty" should {
      "return empty list" in {
        assert(emptyController.getAllCommands.isEmpty)
      }
    }
  }

  "A getCommandByNumber()" when {
    "command exists" should {
      "return command1" in {
        assertResult(Some(command1))(controller.getCommandByNumber(0))
      }
      "return command2" in {
        assertResult(Some(command2))(controller.getCommandByNumber(1))
      }
    }
    "command doesn't exist" should {
      "return None" in {
        assertResult(None)(controller.getCommandByNumber(5))
      }
    }
    "commands is empty" should {
      "return None" in {
        assertResult(None)(emptyController.getCommandByNumber(-1))
      }
    }
  }
}
