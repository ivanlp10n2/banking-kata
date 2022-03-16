package ar.empanada

import ar.empanada.domain.{Account, AccountId, Money}
import cats.effect.{IO, Ref}
import cats.effect.kernel.Concurrent
import cats.kernel.Monoid
import cats.syntax.all._

trait Accounts[F[_]]{
  def deposit(id: AccountId, moneyToAdd: Money): IO[Option[Account]] = ???

  //  def deposit(id: Int, money: Money): F[Unit]
  def createNew: F[Option[Account]]
}

object Accounts{
  def make[F[_]: Concurrent](
    persistence: Ref[F, Map[AccountId, Money]]
  ): Accounts[F] =
    new Accounts[F] {
      override def createNew: F[Option[Account]] = {
        val newId = AccountId(1)
        persistence
          .updateAndGet(m => m + (newId -> Monoid[Money].empty))
          .map(m => m.collectFirst{
              case (id, money) if id == newId => Account(id, money)
            }
          )
      }
    }
}
