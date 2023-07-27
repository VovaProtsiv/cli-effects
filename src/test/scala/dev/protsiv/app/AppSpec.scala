package dev.protsiv.app

import cats.effect.testing.scalatest.AsyncIOSpec
import dev.protsiv.{FakeClient, FakeConsole}
import dev.protsiv.app.App.{printOptions, readCommandNumber}
import dev.protsiv.app.CommonSample.{console, env, expectedPrintOptions, printOptionCommands}
import dev.protsiv.app.Configuration.Environment
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
        console.linesToRead =  List("exit")
        readCommandNumber().run(env).value.asserting(_ => console.linesWritten shouldBe List("Your option: "))
      }
    }

  }

  override protected def afterEach(): Unit = {
    console.linesWritten = Nil
    super.afterEach()
  }



}
