// http://ikojo.in/sf/XSDZnz6/15

// two planets around a star

val sampleEveryN = 10

case class Vec(var x: Double, var y: Double) // can represent position, velocity or force
case class Pic(size: Double, ct: kojo.ComposableTransformer)
case class Obj(mass: Double, initPos: Point, vel: Vec, pic: Pic) {
    val p = trans(initPos.x, initPos.y) * pic.ct -> Picture.circle(pic.size)
    draw(p)
}

val opacity = 150
def ct(c: Color) = penColor(c) * fillColor(c)
val red = ct(Color(255, 0, 0, opacity))
val green = ct(Color(0, 204, 51, opacity))
val blue = ct(Color(0, 51, 204, opacity))

// star(red), planet 1(green), planet 2(blue)
val (pos, size, initSpeed, gFactor) = (40.0, 3.0, 2.0, 1.0)
val objs = List(
    Obj(1000, Point(0, 0), Vec(0, 0), Pic(6 * size, red)),
    Obj(2.0, Point(pos, 2 * pos), Vec(initSpeed, -1.5 * initSpeed), Pic(size, green)),
    Obj(3.0, Point(4 * pos, 2 * pos), Vec(initSpeed / 2, -initSpeed), Pic(2 * size, blue))
)

def dv(p1: Point, p2: Point, mass2: Double) = { // delta velocity, i.e., change in speed, horizontal and vertical
    val angle = math.atan(math.abs((p1.y - p2.y) / (p1.x - p2.x)))
    val distance = math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
    val dv = mass2 * gFactor / math.pow(math.max(0.01, distance), 2)
    val dvx = dv * math.cos(angle) * (if (p1.x > p2.x) -1 else 1)
    val dvy = dv * math.sin(angle) * (if (p1.y > p2.y) -1 else 1)
    (dvx, dvy)
}
zoomXY(0.6, 0.6, pos,-pos); invisible
var step = 0
animate {
    if (step % sampleEveryN == 0) for (o <- objs)
        draw(trans(o.p.position.x, o.p.position.y) * o.pic.ct -> Picture.circle(2))
    step += 1
    //if (step > 250) stopAnimation
    for (o <- objs) {
        for (other <- objs if other != o) {
            val (dvx, dvy) = dv(o.p.position, other.p.position, other.mass)
            o.vel.x += dvx
            o.vel.y += dvy
        }
        o.p.setPosition(o.p.position.x + o.vel.x, o.p.position.y + o.vel.y)
    }
}
