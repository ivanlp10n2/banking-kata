package ar.empanada

import cats.Monoid

object MonoidSyntax {
  implicit class MonoidOption[A: Monoid](op: Option[A]){
   def getOrEmpty: A = op.getOrElse(Monoid[A].empty)
  }
}
