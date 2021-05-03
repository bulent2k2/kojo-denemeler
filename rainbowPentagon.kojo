
clear()
setSpeed(superFast)
setPenColor(darkGray)
repeat(18) {
    setFillColor(randomColor.fadeOut(0.5))
    repeat(5) {
        forward(150)
        right(360 / 5)
    }
    right(20)
    invisible
}
