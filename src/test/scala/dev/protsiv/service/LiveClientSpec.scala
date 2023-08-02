package dev.protsiv.service

import cats.effect.testing.scalatest.AsyncIOSpec
import cats.effect.{IO, Resource}
import fs2.Stream
import org.http4s.{Entity, Response, client}
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers

class LiveClientSpec extends AsyncFreeSpec with AsyncIOSpec with Matchers {
  val path = "org.example.com"
  val body = "Test response"
  val errorMsg = "The client failed"
  val resource: Resource[IO, client.Client[IO]] = Resource.eval {
    IO {
      client.Client.apply[IO] { _ =>
        Resource.eval(IO(Response[IO](entity = Entity(Stream.emits(body.getBytes("UTF-8"))))))
      }
    }
  }

  val resourceFail: Resource[IO, client.Client[IO]] = Resource.eval {
    IO {
      client.Client.apply[IO] { _ =>
        Resource.eval(IO.raiseError(new RuntimeException(errorMsg)))
      }
    }
  }

  "LiveClient call" - {
    "should return response body without any changes" in {
      new LiveClient {}.call(path, resource).asserting(_ shouldBe body)
    }
    "should return IO with error if the client fails" in {
      new LiveClient {}.call(path, resourceFail).assertThrowsWithMessage[RuntimeException](errorMsg)
    }
  }
}
