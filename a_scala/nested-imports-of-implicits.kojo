// inherit implicit defs using traits
trait Foo {
  implicit class IntWithSquare(x: Int) {
    def square = x*x
  }
}
object Bar extends Foo {
    println(4.square)
}
import Bar._
println(5.square)