package dev.protsiv.app

import cats.data.{EitherT, ReaderT}
import cats.effect.IO
import cats.implicits.toBifunctorOps
import dev.protsiv.app.Configuration.{AppOp, Environment, Error, ErrorOr}

object Syntax {
  implicit class IOOps[A](fa: IO[A]) {

    def toAppOp: AppOp[A] = {
      val attemptIO: IO[Either[Error, A]] =
        fa.attempt.map(_.leftMap(_.getMessage))
      val errorOr: ErrorOr[A] = EitherT(attemptIO)
      ReaderT.liftF(errorOr)
    }
  }

  implicit class EitherOps[A](fa: Either[Error,A]) {
    def toAppOp: AppOp[A] = {
      ReaderT.liftF(EitherT(IO.pure(fa)))
    }
  }

  implicit class AppOps[A](fa: AppOp[A]) {

    def runApp(env: Environment): IO[Either[Error, A]] =
      fa.run(env).value

  }

}
