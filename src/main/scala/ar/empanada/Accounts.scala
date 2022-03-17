package ar.empanada

import ar.empanada.domain.{Account, AccountId, Money}
import cats.effect.{Ref, Sync}
import cats.kernel.Monoid
import cats.syntax.all._

trait Accounts[F[_]] {
  def deposit(id: AccountId, moneyToAdd: Money): F[Option[Account]]

  def createNew: F[Option[Account]]
}

object Accounts {
  def make[F[_] : Sync](bankAccounts: Ref[F, Map[AccountId, Money]]): Accounts[F] =
    new Accounts[F] {
      override def createNew: F[Option[Account]] = {
        val newId = AccountId(1)
        bankAccounts
          .updateAndGet(m => m + (newId -> Monoid[Money].empty))
          .map(m => m.collectFirst {
            case (id, money) if id == newId => Account(id, money)
          })
      }

      override def deposit(id: AccountId, moneyToAdd: Money): F[Option[Account]] = {
          bankAccounts.modify(accounts => {
            val maybeAccount = accounts
              .collectFirst { case (savedId, money) if savedId == id => Account(savedId, money) }
              .map(acc => acc.copy(balance = acc.balance |+| moneyToAdd))

            val updatedAccounts = accounts  ++ maybeAccount.map(a => a.id -> a.balance)

            (updatedAccounts, maybeAccount)
          })
      }
    }
}
