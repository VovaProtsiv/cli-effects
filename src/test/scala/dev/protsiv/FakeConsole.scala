package dev.protsiv

import cats.effect.IO
import dev.protsiv.service.Console

trait FakeConsole extends Console {
  var linesToRead: List[String]
  var linesWritten: List[String] = Nil

    override def readLine(msg: String): IO[String] = {
      IO {
        val hd = linesToRead.head
        linesWritten = linesWritten :+ msg
        linesToRead = linesToRead.tail
        hd

      }
    }

    override def printLine(line: String, level: Console.Level = Console.Info): IO[Unit] =
      IO {
        linesWritten = linesWritten :+ line
      }

}