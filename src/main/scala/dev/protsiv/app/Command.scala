package dev.protsiv.app

import cats.effect.IO

trait Command {
  val name: String

  def execute(): IO[String]

  def isExit: Boolean = false
}
