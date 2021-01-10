val ratio = (4, 3)
cleari
val numPerTurn = 120
val angle = 360.0/(numPerTurn * math.min(ratio._1, ratio._2))
val (rp, cc, rc) = (2, 100, 20)
val (x1, y1, x2, y2) = (-cc, cc, cc, -cc)
val (c1, c2, c3) = (blue, red, purple)
val p1 = trans(x1, y1) * fillColor(c1) * penColor(c1) -> PicShape.circle(rp)
val p2 = trans(x2, y2) * fillColor(c2) * penColor(c2) -> PicShape.circle(rp)
draw(p1)
draw(p2)
val p3 = trans(p2.position.x, p1.position.y) * fillColor(noColor) * penColor(noColor) -> PicShape.circle(rp)
draw(p3); p3.invisible()

p1.act { self =>
    self.rotateAboutPoint(ratio._1*angle, x1, 0)
    draw(trans(self.position.x, self.position.y) * penThickness(1.0) * penColor(c1) -> PicShape.circle(rp))
}
p2.act { self =>
    self.rotateAboutPoint(ratio._2*angle, 0, y2)
    draw(trans(self.position.x, self.position.y) * penThickness(1.0) * penColor(c2) -> PicShape.circle(rp))
}
p3.act { self =>
    self.setPosition(p2.position.x, p1.position.y)
    draw(trans(self.position.x, self.position.y) * penThickness(1.0) * penColor(c3) -> PicShape.circle(rp))
}
