package dev.protsiv.service

import cats.effect.IO

import scala.io.{AnsiColor, StdIn}


trait Console {
  import Console._
  def readLine(msg: String): IO[String]

  def printLine(line: String, level: Level = Info): IO[Unit]
}

trait LiveConsole extends Console  {
  import Console._

  override def readLine(msg: String): IO[String] =
    IO(StdIn.readLine(msg))

  override def printLine(line: String, level: Level = Info): IO[Unit] =
    IO(println(level.getColor + line))

}

object Console {
  sealed trait Level {
    def getColor: String
  }

  case object Info extends Level {
    override def getColor: String = AnsiColor.BLUE
  }

  case object Error extends Level {
    override def getColor: String = AnsiColor.RED
  }

  case object Success extends Level {
    override def getColor: String = AnsiColor.GREEN
  }
}
