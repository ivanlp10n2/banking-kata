package ar.empanada

import ar.empanada.domain.{AccountId, Money}
import cats.Monoid
import cats.effect.testing.specs2.CatsEffect
import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Ref}
import cats.syntax.all._
import org.scalacheck.Prop.forAll
import org.specs2.ScalaCheck
import org.specs2.execute.Result
import org.specs2.matcher.Matchers
import org.specs2.mutable.SpecificationLike

class BankingSpec extends SpecificationLike with ScalaCheck with Matchers with CatsEffect {

  "create account >> get account" should {
    "return a new account with zero balance " in {
      val map: IO[Ref[IO, Map[AccountId, Money]]] = IO.ref(Map())
      map.flatMap(ref => {
        Accounts.make[IO](ref).createNew
          .map(acc => acc
            .fold[Result](
              failure("failed to create account")
            )(
              acc => acc.balance must_== Monoid[Money].empty)
          )
      })
    }

    "deposit money" should {
      "add up money to its account balance" in {
        import gen.generators._
        val map: IO[Ref[IO, Map[AccountId, Money]]] = IO.ref(Map())
        forAll(positiveAccountBalance, positiveMoney) { (account, moneyToAdd) =>
          val expected = account.balance |+| moneyToAdd
          map.flatMap(ref =>
            ref.update(m => m + (account.id -> account.balance)) *>
              Accounts.make[IO](ref).deposit(account.id, moneyToAdd)
                .map(acc => acc
                  .fold[Result](
                    failure("failed to deposit in account")
                  )(
                    acc => acc.balance must_== expected)
                )
          ).unsafeRunSync()
        }
      }
    }

  }
}
