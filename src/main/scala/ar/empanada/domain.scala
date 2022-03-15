package ar.empanada

import cats.Monoid

object domain {

  final case class Money(value: Int) extends AnyVal
  object Money{
    implicit val monoid: Monoid[Money] = new Monoid[Money] {
      override def empty: Money = Money(0)

      override def combine(x: Money, y: Money): Money =
        Money(x.value + y.value)
    }
  }

  final case class AccountId(id: Int) extends AnyVal

  final case class Account(id: AccountId, money: Money)
}
