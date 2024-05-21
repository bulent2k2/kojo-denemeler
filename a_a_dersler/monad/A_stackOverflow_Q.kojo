// Many ways to represent an optional output

//1- type Output[T] = Option[T]

//2- 
case class MyOption[T](v: Option[T]) {
    def map[B](f: T => B): MyOption[B] = ???
    def flatMap[B](f: T => MyOption[B]): MyOption[B] = ???
}
//type Output[T] = MyOption[T]

//3-
class Maps[T, C[_]] {
    def map[B](f: T => B): C[B] = ???
    def flatMap[B](f: T => C[B]): C[B] = ???
}
case class MyOpt2[T](v: Option[T]) extends Maps[T, MyOpt2]
//type Output[T] = MyOpt2[T]

//4-
trait Maps2[A, C[_, _]] {
    def map[B](f: A => B): C[B, _] = ???
    def flatMap[B](f: A => C[B, _]): C[B, _] = ???
}
case class OptionInsideX[A, X[_]](value: X[Option[A]]) extends Maps2[A, OptionInsideX]

import scala.concurrent.Future
type Output[T] = OptionInsideX[T, Future]
    
class Test {
    case class Foo(v: Int)
    case class Bar(v: Int)

    def f2b(f: Foo): Output[Bar] = ???
    def b2v(b: Bar): Output[Int] = ???

    val f = Foo(42)
    for( b <- f2b(f);
         v <- b2v(b) ) yield v
}