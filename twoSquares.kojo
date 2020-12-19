

clear()
setSpeed(slow)
setPenThickness(20)
val green1 = ColorMaker.hsla(116, 1.00, 0.49, 0.50)
setPenColor(Color(0, 255, 0, 0))
setFillColor(green1)
// you can use cm as an abbreviation for ColorMaker setFillColor(cm.lightPink)
repeat(4) {
forward(100)
right(90)
}
left; hop(50)
left; hop(50)
left; left
val blue1 = ColorMaker.hsla(227, 1.00, 0.49, 0.50)
setPenColor(Color(252, 252, 255, 0))
setFillColor(blue1)
repeat(4) {
    forward(100)
    right(90)
}
