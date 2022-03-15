package ar.empanada

import ar.empanada.domain.{AccountId, Money}
import cats.effect.Ref
import cats.effect.kernel.Concurrent
import cats.kernel.Monoid
import cats.syntax.all._

trait Accounts[F[_]]{
//  def deposit(id: Int, money: Money): F[Unit]
  def createNew: F[AccountId]
}

object Accounts{
  def make[F[_]: Concurrent](
    persistence: Ref[F, Map[AccountId, Money]]
  ): Accounts[F] =
    new Accounts[F] {
      override def createNew: F[AccountId] = {
        val newId = AccountId(1)
        persistence
          .updateAndGet(m => m + (newId -> Monoid[Money].empty))
          .as(newId)
      }
    }
}
