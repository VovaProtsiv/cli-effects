package dev.protsiv.app

import cats.Show
import cats.effect.IO

trait Command {
  val name: String

  def execute(): IO[String]

  def isExit: Boolean = false
}
object Command {
  implicit val showCommand: Show[Command] = Show.show(_.name)
}