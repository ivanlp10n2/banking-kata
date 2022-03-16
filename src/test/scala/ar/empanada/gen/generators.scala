package ar.empanada.gen

import ar.empanada.domain.{Account, AccountId, Money}
import org.scalacheck.Gen

object generators {

  val generatedId: Gen[AccountId] =
    Gen
      .choose(0, 100)
      .map(AccountId(_))

  val positiveMoney: Gen[Money] =
    Gen
      .choose(1, 100000)
      .map(Money(_))

    val positiveAccountBalance: Gen[Account] =
      for{
        id <- Gen.choose(0, 100)
        money <- positiveMoney
      } yield Account(AccountId(id), money)
}
