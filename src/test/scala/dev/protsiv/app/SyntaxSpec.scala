package dev.protsiv.app

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import dev.protsiv.app.CommonSample.env
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers
import dev.protsiv.app.Syntax._
import dev.protsiv.app.Configuration.Error

class SyntaxSpec extends AsyncFreeSpec with AsyncIOSpec with Matchers {

  "IO toAppOP" - {
    "when IO not raise an ex" - {
      "should return Right side with a success msg" in {
        val successMsg = "foo"
        IO.pure(successMsg).toAppOp.run(env).value.asserting(_ shouldBe Right(successMsg))
      }
    }
    "when IO raise an ex" -{
      "should return Left side with an error msg" in {
        val errorMsg = "Something went wrong!"
        IO.raiseError[String](new RuntimeException(errorMsg))
          .toAppOp
          .run(env)
          .value
          .asserting(_ shouldBe Left(errorMsg))
      }
    }
  }
  "Either toAppOP" - {
    "when Either is Right" - {
      "should return Right side with a success msg" in {
        val successMsg = 123
        Right[Error,Int](successMsg).toAppOp.run(env).value.asserting(_ shouldBe Right(successMsg))
      }
    }
    "when Either is Left" - {
      "should return Left side with an error msg" in {
        val errorMsg = "Something went wrong!"
       Left[Error,Int](errorMsg)
          .toAppOp
          .run(env)
          .value
          .asserting(_ shouldBe Left(errorMsg))
      }
    }
  }
}
