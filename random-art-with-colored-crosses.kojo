clear()
setSpeed(superFast)
val pf = (225, 150) // playing field
val w = 20 // width of each rectangle (fixed)
val max_h = w * 6 // max height of each rectangle (randomized to be shorter than this)
val (cmin, cmax) = (0, 256)
val color = Color(32, 207, 42, 147)
repeat(100) {
    setPosition(random(-pf._1, pf._1), random(-pf._2, pf._2))
    repeat(20) {
        val h = random(w, max_h + 1)
        val (red, green, blue, opacity) = (
            random(cmin, cmax),
            random(cmin, cmax),
            random(cmin, cmax),
            100)
        val color = Color(red, green, blue, opacity)
        setFillColor(color)
        setPenColor(color)
        forward(h)
        right()
        forward(w)
        right()
        forward(h)
        right()
        forward(w)
        hop(-w)
        left(); left()
    }
}
invisible
