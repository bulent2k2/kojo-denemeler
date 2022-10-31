// importing regular definitions and types thru two levels
object Foo {
  def someBigFunc(): Int = {
    42
  }
  type Word = String
}
object Bar {
    import Foo._
    val someBigFunc = Foo.someBigFunc _
    type Word = Foo.Word
}
import Bar._
someBigFunc()
val w: Word = "word"