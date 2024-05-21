// http://ikojo.in/sf/WGhudzO/6

// three planets around a star
val sampleEveryN = 42

case class Body(mass: Double, initPos: Point, vel: Vec, pic: Pic) {
  val p = trans(initPos.x, initPos.y) * pic.ct -> Picture.circle(pic.size)
  draw(p)
}
case class Vec(var x: Double, var y: Double) { // can represent position, velocity or force
  def add(v2: Vec) = {
    x += v2.x
    y += v2.y
  }
}
case class Pic(size: Double, c: Color) {
  val ct = penColor(c) * fillColor(c)
}

// star(red), planet 1(green), planet 2(blue)
val (pos, size, initSpeed, gFactor) = (40.0, 3.0, 2.0, 1.0)
val bodies = List(
  Body(1000, Point(0, 0), Vec(0, 0), Pic(6 * size, red)),
  Body(2.0, Point(pos, 2 * pos), Vec(initSpeed, -1.5 * initSpeed), Pic(size, green)),
  Body(3.0, Point(4 * pos, 2 * pos), Vec(initSpeed / 2, -initSpeed), Pic(2 * size, blue)),
  Body(5.0, Point(-6 * pos, 2 * pos), Vec(-initSpeed / 3, initSpeed), Pic(3 * size, purple))
)
def dv(p1: Point, p2: Point, mass2: Double) = { // delta velocity, i.e., change in speed, horizontal and vertical
  import math.{atan, abs, sqrt, pow, cos, sin}
  val distance = sqrt(pow(p1.x - p2.x, 2) + pow(p1.y - p2.y, 2))
  val dv = mass2 * gFactor / pow(math.max(0.01, distance), 2)
  val angle = atan(abs((p1.y - p2.y) / (p1.x - p2.x)))
  Vec(
    dv * cos(angle) * (if (p1.x > p2.x) -1 else 1), 
    dv * sin(angle) * (if (p1.y > p2.y) -1 else 1)
  )
}
zoomXY(0.3, 0.3, pos,-pos); invisible
var step = 0
animate {
  if (step % sampleEveryN == 0) for (b <- bodies)
    draw(trans(b.p.position.x, b.p.position.y) * b.pic.ct -> Picture.circle(2))
  step += 1
  //if (step > 400) stopAnimation
  for (b <- bodies) {
    for (other <- bodies if other != b)
      b.vel.add(dv(b.p.position, other.p.position, other.mass))
    b.p.setPosition(b.p.position.x + b.vel.x, b.p.position.y + b.vel.y)
  }
}
