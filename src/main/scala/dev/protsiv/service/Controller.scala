package dev.protsiv.service

import dev.protsiv.app.Command

import scala.util.Try


trait Controller {

  def getAllCommands: List[Command]

  def getCommandByNumber(number: Int): Option[Command]
}
case class LiveController(commands:List[Command]) extends Controller {

  override def getCommandByNumber(number: Int): Option[Command] =
    Try(commands(number)).toOption

  override def getAllCommands: List[Command] = commands

}


