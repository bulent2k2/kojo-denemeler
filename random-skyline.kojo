// uncomment to get the same random sequence:
// setRandomSeed(0)
clear
val w = 20 // width of each rectangle (fixed)
val max_h = w * 12 // max height of each rectangle (randomized to be shorter than this)
def sb = setBackgroundV(Color(4, 0, 63), Color(163, 111, 91))
sb
invisible()
repeati(30) { n =>
    println("Skyline #" + n)
    setSpeed(superFast)
    home()   
    val pict = Picture {
        setPosition(-300, -200)
        repeat(30) {
            val h = random(w, max_h + 1)
            val color = Color(0, 12, 13, 0)
            setFillColor(black)
            setPenColor(Color(103, 103, 103))
            forward(h) // go up
            right()
            forward(w) // go right
            right()
            forward(h) // go down
            right()
            forward(w) // go left
            hop(-w)
            right // look up
        }
    }
    draw(pict)
    Thread.sleep(333)
    pict.erase()
}
