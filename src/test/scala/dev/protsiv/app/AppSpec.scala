package dev.protsiv.app

import cats.effect.testing.scalatest.AsyncIOSpec
import dev.protsiv.app.App.printOptions
import dev.protsiv.app.CommonSample.{console, env, expectedPrintOptions, printOptionCommands}
import dev.protsiv.service.LiveController
import org.scalatest.BeforeAndAfterEach
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers

class AppSpec extends AsyncFreeSpec with AsyncIOSpec with Matchers with BeforeAndAfterEach{
"printOptions()" -{
  "should write to the console" in {
    val environment = env.copy(controller = LiveController(printOptionCommands))
    printOptions.
      run(environment).
      value.
      asserting(_=> console.linesWritten shouldBe expectedPrintOptions)
  }
}

  override protected def afterEach(): Unit = {
    console.linesWritten = Nil
    super.afterEach()
  }
}
