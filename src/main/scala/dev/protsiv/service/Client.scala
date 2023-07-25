package dev.protsiv.service


import cats.effect.IO
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.{Method, Request, Uri}

trait Client {
  def call(msg: String): IO[String]
}

trait LiveClient extends Client {

  override def call(path: String): IO[String] = {

    BlazeClientBuilder[IO].resource
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
