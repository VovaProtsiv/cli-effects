package dev.protsiv.serializer

import dev.protsiv.app.Configuration.Error
import dev.protsiv.model.IP
import spray.json.{DefaultJsonProtocol, RootJsonFormat, enrichString}

import scala.util.Try

object IpSerializer extends DefaultJsonProtocol {
  implicit val ipFormat: RootJsonFormat[IP] = jsonFormat1(IP)

  def toIp(json: String): Either[Error, IP] = {
    Try(json.parseJson.convertTo[IP])
      .toEither
      .left
      .map(_.getMessage)
  }
}
