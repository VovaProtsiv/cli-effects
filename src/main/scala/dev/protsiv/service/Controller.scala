package dev.protsiv.service

import dev.protsiv.app.Command


trait Controller {

  def getAllCommands: List[Command]

  def getCommandByNumber(number: Int): Option[Command]
}


