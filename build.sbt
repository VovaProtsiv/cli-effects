ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "cli-effects"
  )

val catsEffectVersion = "3.5.1"
val http4sVersion = "1.0.0-M38"
val logbackVersion = "1.4.8"
val sprayJsonVersion = "1.3.6"
val scalatestVersion = "3.2.15"
val munitTestVersion = "1.0.7"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  // https://graphik-team.github.io/graal/doc/slf4j
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "io.spray" %% "spray-json" % sprayJsonVersion,
  // test dependencies
  "org.scalatest" %% "scalatest" % scalatestVersion % Test,
"org.typelevel" %% "cats-effect-testing-scalatest" % "1.5.0" % Test
)

Test / parallelExecution := false
