package dev.protsiv.app

import cats.data.{EitherT, ReaderT}
import cats.effect.IO
import cats.implicits.toBifunctorOps
import dev.protsiv.app.Configuration.Configuration.{AppOp, Error, ErrorOr}
import dev.protsiv.app.Configuration.Environment

object Syntax {
  implicit class IOOps[A](fa: IO[A]) {

    def toAppOp: AppOp[A] = {
      val attemptIO: IO[Either[Error, A]] =
        fa.attempt.map(_.leftMap(_.getMessage))
      val errorOr: ErrorOr[A] = EitherT(attemptIO)
      ReaderT.liftF(errorOr)
    }
  }

  implicit class AppOps[A](fa: AppOp[A]) {

    def runApp(env: Environment): IO[Either[Error, A]] =
      fa.run(env).value

  }

}
