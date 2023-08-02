package dev.protsiv.app


import cats.effect.{IO, Resource}
import cats.effect.testing.scalatest.AsyncIOSpec
import dev.protsiv.app.CommonSample.{console, env}
import dev.protsiv.service.Client
import org.http4s.{Entity, Response, client}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers

class CommandSpec extends AsyncFreeSpec with AsyncIOSpec with Matchers with BeforeAndAfterEach {
  val resource: Resource[IO, client.Client[IO]] = Resource.eval(IO {
    client.Client.apply[IO] { _ =>
      Resource.eval(IO(Response[IO](entity = Entity.empty)))
    }
  })
  val errorMsg = "Something went wrong!"
  val clientEr = new Client {
    override def call(msg: String,resource: Resource[IO, client.Client[IO]]): IO[String] = IO.raiseError(new RuntimeException(errorMsg))
  }
  val envWithClientError = env.copy(client = clientEr)
  "Exit command" - {
    "should return Right side" in {
      ExitCommand.execute().run(env).value.asserting(_ shouldBe Right("Finished."))
    }
    "isExit should be true" in {
      assert(ExitCommand.isExit)
    }
  }
  "GetIP command" - {
    "when client send a valid response" - {
      "should return Right side" in {
        GetIp.execute().run(env).value.asserting(_ shouldBe Right("Your IP: 255.255.11.135"))
      }
      "should write to the console" in {
        GetIp.
          execute().
          run(env).
          value.
          asserting(_ => console.linesWritten shouldBe List(s"Connecting to ${Configuration.path}"))
      }
    }
    "when client raise an ex" - {
      "should return a Left side" in {
        GetIp.execute().run(envWithClientError).value.asserting(_ shouldBe Left(errorMsg))
      }
    }
    "when IpConverter raise an ex" - {
      "should return a Left side" in {
        val cl = new Client {
          override def call(msg: String, resource: Resource[IO, client.Client[IO]]): IO[String] = IO.pure("Unexpected response")
        }
        val environment = env.copy(client = cl)
        GetIp.execute().run(environment).value.asserting(e => e.isLeft shouldBe true)
      }
    }
    "isExit should be false" in {
      assert(!GetIp.isExit)
    }
  }

  override protected def afterEach(): Unit = {
    console.linesWritten = Nil
    super.afterEach()
  }
}
