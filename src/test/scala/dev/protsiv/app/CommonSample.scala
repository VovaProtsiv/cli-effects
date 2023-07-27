package dev.protsiv.app

import cats.effect.IO
import dev.protsiv.{FakeClient, FakeConsole}
import dev.protsiv.app.Configuration.{AppOp, Environment, SuccessMsg}
import dev.protsiv.app.Syntax.IOOps
import dev.protsiv.service.LiveController

object CommonSample {
  val console: FakeConsole = new FakeConsole {
    override var linesToRead: List[String] = List.empty
  }
  val env = Environment(console,
    LiveController(List.empty),
    new FakeClient {}
  )

  val printOptionCommands = List(TestCommand1, TestCommand2)
  val expectedPrintOptions =List(
    s"Please select an option: \n"+
      s"\n(0) ${TestCommand1.name}" +
      s"\n(1) ${TestCommand2.name}"+
      "\n"
      )



  object TestCommand1 extends Command {
    override val name: String = "test command 1"

    override def execute(): AppOp[SuccessMsg] = IO.pure("executing test command 1").toAppOp
  }

  object TestCommand2 extends Command {
    override val name: String = "test command 2"

    override def execute(): AppOp[SuccessMsg] = IO.raiseError(new RuntimeException("Boom")).toAppOp
  }
}

