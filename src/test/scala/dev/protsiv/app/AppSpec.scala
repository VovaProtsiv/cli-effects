package dev.protsiv.app

import cats.effect.testing.scalatest.AsyncIOSpec
import dev.protsiv.app.App.{executeCommand, printOptions, readCommandNumber}
import dev.protsiv.app.CommonSample._
import dev.protsiv.service.LiveController
import org.scalatest.BeforeAndAfterEach
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers

class AppSpec extends AsyncFreeSpec with AsyncIOSpec with Matchers with BeforeAndAfterEach {
  "printOptions()" - {
    "should write to the console" in {
      val environment = env.copy(controller = LiveController(printOptionCommands))
      printOptions.
        run(environment).
        value.
        asserting(_ => console.linesWritten shouldBe expectedPrintOptions)
    }
  }
  "readCommandNumber()" - {
    "when input is valid int" - {
      "should return Right with a number" in {
        console.linesToRead = List("3")
        readCommandNumber().run(env).value.asserting(_ shouldBe Right(3))
      }
      "should write to console" in {
        console.linesToRead = List("3")
        readCommandNumber().run(env).value.asserting(_ => console.linesWritten shouldBe List("Your option: "))
      }
    }
    "when input isn't valid int" - {
      "should Left with an error msg" in {
        console.linesToRead = List("exit")
        readCommandNumber().run(env).value.asserting(_ shouldBe Left("Invalid option selected"))
      }
      "should write to console" in {
        console.linesToRead = List("exit")
        readCommandNumber().run(env).value.asserting(_ => console.linesWritten shouldBe List("Your option: "))
      }
    }

  }


  "executeCommand()" - {
    "when successfully flow" - {
      "should write all commands to the console" in {
        executeCommand.
          run(executeCommandEnvSuccessFlow).
          value.
          asserting(_ => executeCommandConsole.linesWritten shouldBe expectedExecuteCommandSuccessFlow)
      }
      "should return Right with a success msg" in {
        executeCommandConsole.linesToRead = List("0")
        executeCommand.
          run(executeCommandEnvSuccessFlow).
          value.
          asserting(_ shouldBe Right(false))
      }
    }
    "when unsuccessful flow" - {
      "if commandNumber return Left side" - {
        "should write to the console" in {
          executeCommandConsole.linesToRead = List("exit")
          executeCommand.
            run(executeCommandEnvSuccessFlow).
            value.
            asserting(_ => executeCommandConsole.linesWritten shouldBe expectedPrintOptions ++ List("Your option: "))
        }
        "should return Left side" in {
          executeCommandConsole.linesToRead = List("exit")
          executeCommand.
            run(executeCommandEnvSuccessFlow).
            value
            .asserting(_ shouldBe Left("Invalid option selected"))
        }
      }
      "if command not found " - {
        "should return Left side with error msg" in {
          executeCommandConsole.linesToRead = List("12")
          executeCommand.
            run(executeCommandEnvSuccessFlow).
            value
            .asserting(_ shouldBe Left("Command not found"))
        }
      }
      "if command raise an error" - {
        "should return Left side with error msg" in {
          executeCommandConsole.linesToRead = List("1")
          executeCommand.
            run(executeCommandEnvSuccessFlow).
            value
            .asserting(_ shouldBe Left("Boom"))
        }
      }
    }
  }
  "run()" - {
    "when successfully flow" - {
      "should return Right(())" in {
        runConsole.linesToRead = List("0", "2")
        App.run().
          run(runEnv).
          value.
          asserting(_ shouldBe Right(()))
      }
      "should write to the console" in {
        runConsole.linesToRead = List("0", "2")
        App.run().
          run(runEnv).
          value.
          asserting(_ => runConsole.linesWritten shouldBe expectedRunSuccessFlow)
      }
    }
    "when unsuccessful flow" - {
      "if command raise un error" - {
        "should should return Right(())" in {
          runConsole.linesToRead =List("0","1","2")
          App.run().
            run(runEnv).
            value.
            asserting(_  shouldBe Right(()))
        }
        "should write to the console error msg " in {
          runConsole.linesToRead = List("0", "1", "2")
          App.run().
            run(runEnv).
            value.
            asserting(_=>runConsole.linesWritten.contains("\nBoom\n") shouldBe true)
        }
      }
    }
  }

  override protected def afterEach(): Unit = {
    console.linesWritten = Nil
    executeCommandConsole.linesWritten = Nil
    runConsole.linesWritten = Nil
    super.afterEach()
  }


}
