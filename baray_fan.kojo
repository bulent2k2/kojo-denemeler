clear()
setSpeed(slow)
savePosHe()

def drawFan() = {
    println("drawing fan")
    repeat(4) {
        println(position)
        forward(30)
        right()
        forward(50)
        right()
        forward(40)
        right()
    }
}

def OppositeFan =
    repeat(4) {
        forward(30)
        right
        forward(40)
        right
        forward(50)
        right
    }

//showAxes
if (true) {
    //drawFan
    
    val p1 = Picture { drawFan }
    p1.setFillColor(blue)
    draw(p1)
    
    val p2 = fillColor(green) * flip * trans(100, 0) -> Picture { drawFan }
    draw(p2)
/*
    val l1 = trans(-140, 40) -> PicShape.line(180, 0)
    l1.setPenThickness(4)
    draw(l1)
*/
    val a1 = trans(-50, 40) -> PicShape.arc(90, 180)
    draw(a1)
}
if (false) {
    val p3 = PicShape.rect(100, 50)
    val p4 = trans(-100, 0) -> p3
    val p5 = PicShape.ellipse(100, 200)
    draw(p5)
}
invisible
/*
savePosHe()
right(180, 100)

hop(0)

setFillColor(green)

drawFan ; restorePosHe()
right()
setFillColor(blue)
OppositeFan
*/

