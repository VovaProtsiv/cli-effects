package dev.protsiv

import cats.effect.IO
import dev.protsiv.service.Client

trait FakeClient extends Client{
    val json = "{\"ip\":\"255.255.11.135\"}"

    override def call(msg: String): IO[String] = IO.pure(json)
}
