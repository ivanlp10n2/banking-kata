package ar.empanada

import ar.empanada.domain.{AccountId, Money}
import cats.effect.kernel.Async
import cats.effect.{IO, IOApp, Ref}
import cats.syntax.all._

object Main extends IOApp.Simple {

  def run: IO[Unit] = program[IO]()

  def program[F[_]: Async](): F[Unit]={
    val emptyAccounts:Map[AccountId, Money] = Map.empty
    Ref.of(emptyAccounts).flatMap(ref =>{
      Accounts.make[F](ref).createNew
    })
      .void
  }
}
