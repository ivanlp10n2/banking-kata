package ar.empanada

import ar.empanada.domain.{AccountId, Money}
import cats.Monoid
import cats.effect.testing.specs2.CatsEffect
import cats.effect.{IO, Ref}
import org.specs2.execute.Result
import org.specs2.mutable.Specification

class BankingSpec extends Specification with CatsEffect {
  "create account >> get account " should {
    " return a new account with zero balance" in {
      val map: IO[Ref[IO, Map[AccountId, Money]]] = IO.ref(Map())
      map.flatMap(ref => {
        Accounts.make[IO](ref).createNew
          .flatMap(id => ref
            .get
            .map(m => m.get(id))
            .map(money => money
              .fold[Result](
                failure("failed to create account")
              )(
                m => m must_== Monoid[Money].empty)
            )
          )
      })
    }
  }
  "deposit money" should {
    "add up money to its account balance" in {
      pending
      //      import MonoidSyntax._
      //      val account = new Account()
      //      val money = MoneyGen.positiveBalance.sample.getOrEmpty
      //      val expected = account.balance |+| money
      //
      //      account.deposit(money).balance === expected
    }
  }

}
