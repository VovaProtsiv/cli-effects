package dev.protsiv.serializer


import org.scalatest.wordspec.AnyWordSpecLike
import dev.protsiv.model.IP

class IpSerializerSpec extends AnyWordSpecLike {
  "A toIp()"  when {
    "valid json" should {
      "return the Right side" in {
        assert(Right(IP("255.255.11.135")) === IpSerializer.toIp("{\"ip\":\"255.255.11.135\"}"))
      }
    }
    "invalid json" should {
      "return the Left side" in {
        assert( IpSerializer.toIp("{\"ip\"\"255.255.11.135\"}").isLeft)
      }
    }
  }
}
