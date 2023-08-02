package dev.protsiv

import cats.effect.{IO, Resource}
import org.http4s.client
import dev.protsiv.service.Client

trait FakeClient extends Client{
    val json = "{\"ip\":\"255.255.11.135\"}"

    override def call(msg: String, resource:Resource[IO,client.Client[IO]]): IO[String] = IO.pure(json)
}
