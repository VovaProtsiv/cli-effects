package dev.protsiv.service


import cats.effect.{IO, Resource}
import org.http4s.{Method, Request, Uri, client}

trait Client {
  def call(msg: String, resource: Resource[IO, client.Client[IO]]): IO[String]
}

trait LiveClient extends Client {


  override def call(path: String, resource: Resource[IO, client.Client[IO]]): IO[String] = {
    resource
      .use {
        client => {
          val request: Request[IO] = Request(
            method = Method.GET,
            uri = Uri.unsafeFromString(path)
          )
          client.expect[String](request)
        }
      }
  }
}
