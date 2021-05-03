def p = Picture {
  repeat (4) {
    forward(50)
    right()
  }
}

clear()
invisible()
val pic = axes * fillColor(blue) -> p
draw(pic)
