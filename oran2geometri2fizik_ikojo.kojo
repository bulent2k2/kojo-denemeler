// http://ikojo.in/sf/dRpGP6z/1
// Inspired by Roger Antonsen - Math is the hidden secret to understanding the world TED Talk
// Try other ratios like 2/1, 3/2, etc..
val ratio = (4, 3) // change this and re-run
cleari
val numPerTurn = 120
val angle = 360.0/(numPerTurn * math.min(ratio._1, ratio._2))
val (rp, cc, rc) = (2, 80, 20)
val (x1, y1, x2, y2) = (-cc, cc, cc, -cc)
val (c1, c2, c3) = (blue, red, purple)
val p1 = trans(x1, y1) * fillColor(c1) * penColor(c1) -> PicShape.circle(rp)
val p2 = trans(x2, y2) * fillColor(c2) * penColor(c2) -> PicShape.circle(rp)
val p3 = trans(p2.position.x, p1.position.y) * fillColor(noColor) * penColor(noColor) -> PicShape.circle(rp)
draw(p1)
draw(p2)
draw(p3); p3.invisible()

animate {
    p1.rotateAboutPoint(ratio._1*angle, x1, 0)
    draw(trans(p1.position.x, p1.position.y) * penThickness(1.0) * penColor(c1) -> PicShape.circle(rp))
    p2.rotateAboutPoint(ratio._2*angle, 0, y2)
    draw(trans(p2.position.x, p2.position.y) * penThickness(1.0) * penColor(c2) -> PicShape.circle(rp))
    p3.setPosition(p2.position.x, p1.position.y)
    draw(trans(p3.position.x, p3.position.y) * penThickness(1.0) * penColor(c3) -> PicShape.circle(rp))
}
