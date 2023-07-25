package dev.protsiv.app

object Configuration {
  val path:String = sys.env.getOrElse("CLI_EFFECTS_API_PATH","Path not found")
}
