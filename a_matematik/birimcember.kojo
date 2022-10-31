val (slow, normal, fast, faster) = (1, 2, 3, 4)
val ratio = (slow, 1) // change this and re-run
cleari
val numPerTurn = 120
val angle = 360.0 / (numPerTurn * math.min(ratio._1, ratio._2))
val (rp, cc, rc) = (2.0, 80.0, 20.0)
val (x1, y1, x2, y2) = (-cc, cc, cc, -cc)
val (c1, c2, c3, c4, c5) = (blue, red, purple, green, red)
val p1 = trans(x1, y1) * fillColor(c1) * penColor(c1) -> Picture.circle(rp)
val p2 = trans(x2, y2) * fillColor(c2) * penColor(c2) -> Picture.circle(rp)
val p3 = trans(p2.position.y, p1.position.x) * fillColor(noColor) * penColor(noColor) -> Picture.circle(rp)
val sin = trans(0, p1.position.y) * fillColor(noColor) * penColor(noColor) -> Picture.circle(rp)
val cos = trans(0, 2 * cc) * fillColor(noColor) * penColor(noColor) -> Picture.circle(rp)
val drawTwoCircleRatio = false
draw(p1)
draw(p2); if (!drawTwoCircleRatio) p2.invisible()
draw(p3); p3.invisible()
draw(sin); sin.invisible()
draw(cos); cos.invisible()
val axisColor = black.fadeOut(0.5)
draw(trans(-2 * cc, -cc) * penColor(axisColor) -> Picture.line(rp, 4 * cc)) // v axis
draw(trans(0, -cc) * penColor(axisColor) -> Picture.line(rp, 4 * cc)) // v axis
draw(trans(-4 * cc, cc) * penColor(axisColor) -> Picture.line(17 * cc, rp)) // h axis
for (i <- 0 to 8) {
    draw(trans(120 * i, cc) * penColor(c4) -> Picture.circle(rp)) // zeros of sin(t)
    draw(trans(60 + 120 * i, cc) * penColor(c2) -> Picture.circle(rp)) // zeros of cos(t)
}
// arrow pointing from the origin (-2 * cc, cc) to the rotating ball
// origin == the center of the circular motion
draw(trans(-2 * cc, cc) * penColor(axisColor) -> Picture.circle(rp))
var arrow = trans(-2 * cc, cc) * penColor(black) -> Picture.line(cc, 0)
draw(arrow)
def updateArrow(p: Point) {
    arrow.erase()
    arrow = trans(-2 * cc, cc) * penColor(black) -> Picture.line(p.x + 2 * cc, p.y - cc)
    draw(arrow)
}
var t = 0.0
toggleFullScreenCanvas()
zoomXY(1, 1, 360, cc)
val başlamaAnı = buSaniye
animate {
    t += 2
    val tBar = t * ratio._1
    // blue ball rotating around the center in uniform speed
    p1.rotateAboutPoint(ratio._1 * angle, x1, 0)
    if (tBar > 4 * 240) {
        p1.erase()
        stopAnimations()
        println(s"Dalga ${yuvarla(buSaniye - başlamaAnı)} saniye sürdü")
    }
    updateArrow(p1.position)
    draw(trans(p1.position.x, p1.position.y) * penThickness(1.0) * penColor(c1) -> Picture.circle(rp))
    // its projection on the vertical axis == sine of the angle
    draw(trans(p1.position.x, cc) * penColor(c4.fadeOut(0.7)) -> Picture.line(0, p1.position.y - 80))
    // its projection on the horizontal axis == cosine of the angle
    draw(trans(-2 * cc, p1.position.y) * penColor(c2.fadeOut(0.7)) -> Picture.line(p1.position.x + 160, 0))
    if (drawTwoCircleRatio) {
        p2.rotateAboutPoint(ratio._2 * angle, 0, y2)
        draw(trans(p2.position.x, p2.position.y) * penThickness(1.0) * penColor(c2) -> Picture.circle(rp))
        p3.setPosition(p2.position.y, p1.position.x)
        draw(trans(p3.position.x, p3.position.y) * penThickness(1.0) * penColor(c3) -> Picture.circle(rp))
    }
    sin.setPosition(tBar, p1.position.y)
    draw(trans(sin.position.x, sin.position.y) * penThickness(1.0) * penColor(c4) -> Picture.circle(rp))
    cos.setPosition(tBar, 4 * cc + p1.position.x - cc)
    draw(trans(cos.position.x, cos.position.y) * penThickness(1.0) * penColor(c2) -> Picture.circle(rp))
}

