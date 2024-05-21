// two planets around a star
// http://ikojo.in/sf/XSDZnz6/8
cleari

val sampleEveryN = 10

case class Vec(var x: Double, var y: Double) // can represent variable position, force, velocity, etc.
case class Pic(size: Double, ct: net.kogics.kojo.picture.ComposableTransformer)
case class Obj(mass: Double, initPos: Point, vel: Vec, pic: Pic) {
    val p = trans(initPos.x, initPos.y) * pic.ct -> Picture.circle(pic.size)
    draw(p)
}

val opacity = 150
val red = Color(255, 0, 0, opacity)
val green = Color(0, 255, 0, opacity)
val blue = Color(0, 110, 255, opacity)
// sun(red) earth(blue) jupiter(green)

val (pos, size, initSpeed, gFactor) = (40.0, 3.0, 2.0, 25.0)
val objs = List(
    Obj(100, Point(-pos, pos), Vec(0, 0), Pic(4 * size, penColor(red) * fillColor(red))),
    Obj(2.0, Point(0, 3 * pos), Vec(initSpeed, -1.5 * initSpeed), Pic(size, penColor(blue) * fillColor(blue))),
    Obj(4.0, Point(3 * pos, 3 * pos), Vec(initSpeed / 2, -initSpeed), Pic(2 * size, penColor(green) * fillColor(green)))
)

def dv(p1: Point, p2: Point, mratio: Double) = { // delta velocity, i.e., change in speed, horizontal and vertical
    val angle = math.atan(math.abs((p1.y - p2.y) / (p1.x - p2.x)))
    val distance = math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
    val dv = mratio * gFactor / math.pow(math.max(0.01, distance), 2)
    val dvx = dv * math.cos(angle) * (if (p1.x > p2.x) -1 else 1)
    val dvy = dv * math.sin(angle) * (if (p1.y > p2.y) -1 else 1)
    (dvx, dvy)
}
zoomXY(0.7, 0.7, -3 * pos, -pos)
var step = 0
animate {
    if (step % sampleEveryN == 0) for (o <- objs)
        draw(trans(o.p.position.x, o.p.position.y) * o.pic.ct -> Picture.circle(2))
    step += 1
    //if (step > 250) stopAnimation
    for (o <- objs) {
        for (other <- objs if other != o) {
            val (dvx, dvy) = dv(o.p.position, other.p.position, other.mass / o.mass)
            o.vel.x += dvx
            o.vel.y += dvy
        }
        o.p.setPosition(o.p.position.x + o.vel.x, o.p.position.y + o.vel.y)
    }
}