sil
val length = 32 // try bigger or smaller values
setPenThickness(length)
setSpeed(superFast)
setBackground(randomColor) // try black or white
// or try a different background:
// setBackground(Color(180, 180, 0))
val (left, bottom) = (canvasBounds.x.toInt, canvasBounds.y.toInt)
val (right, top) = (-left, -bottom)
repeat(300) {  // try more or less brush strokes
    setPenColor(Color(random(180), random(180), random(180), random(100) + 100))
    setPosition(random(right-left) + left, random(top-bottom) + bottom)
    forward(length)
}
// uncomment to hide the little turtle:
// invisible