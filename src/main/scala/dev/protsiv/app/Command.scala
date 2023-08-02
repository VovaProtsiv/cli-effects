package dev.protsiv.app

import cats.Show
import cats.effect.{IO, Resource}
import dev.protsiv.app.Configuration.{AppOp, SuccessMsg, path, readEnv}
import dev.protsiv.app.Syntax.{EitherOps, IOOps}
import dev.protsiv.serializer.IpSerializer
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client

trait Command {
  val name: String

  def execute(): AppOp[SuccessMsg]

  def isExit: Boolean = false
}

object Command {
  implicit val showCommand: Show[Command] = Show.show(_.name)
}

object ExitCommand extends Command {
  override val name: String = "Exit app"

  override def isExit: Boolean = true

  override def execute(): AppOp[SuccessMsg] = IO.pure("Finished.").toAppOp
}

object GetIp extends Command {

  override val name: SuccessMsg = "Get IP"
  val resource: Resource[IO, client.Client[IO]] = BlazeClientBuilder[IO].resource
  override def execute(): AppOp[SuccessMsg] = {
    for {
      env <- readEnv
      _ <- env.console.printLine(s"Connecting to $path").toAppOp
      json <- env.client.call(path,resource).toAppOp
      address <- IpSerializer.toIp(json).toAppOp
    } yield s"Your IP: ${address.ip}"
  }
}