package ar.empanada.gen

import ar.empanada.domain.Money
import org.scalacheck.Gen

object generators {

  val positiveMoney: Gen[Money] =
    Gen
      .choose(1, 100000)
      .map(Money(_))
}
