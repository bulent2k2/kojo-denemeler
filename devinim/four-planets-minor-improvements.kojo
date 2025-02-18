// http://ikojo.in/sf/i7piLDz/5
clear
val sampleEveryNsteps = 11 // Any counting number, 1 or more. sets how solid we trace the orbits
// Sun(red), comet 1(green), Earth(blue), comet 2(purple), Mars(orange)
val (pos, size, initSpeed) = (41.0, 3.0, 2.0)
val bodies = List(
    Body(1000, Point(0, 0), Vec(0, 0), Pic(6 * size, red)),
    Body(2.0, Point(pos, 2 * pos), Vec(initSpeed, -1.5 * initSpeed), Pic(2 * size, green)),
    Body(3.0, Point(4 * pos, 2 * pos), Vec(initSpeed / 2, -initSpeed), Pic(2 * size, blue)),
    Body(5.0, Point(-6 * pos, 2 * pos), Vec(-initSpeed / 3, initSpeed), Pic(3 * size, purple)),
    Body(6.0, Point(0, -7 * pos), Vec(-initSpeed, 0), Pic(3 * size, orange)),
)
case class Body(mass: Double, initPos: Point, vel: Vec, pic: Pic) {
    val p = trans(initPos.x, initPos.y) * pic.ct -> Picture.circle(pic.size)
    p.draw
}
case class Pic(size: Double, c: Color) { val ct = penColor(c) * fillColor(c) }
case class Vec(var x: Double, var y: Double) { // can represent position, velocity or force
    def add(v2: Vec) = { x += v2.x; y += v2.y; this }
    def scale(c: Double) = { x *= c; y *= c; this }
    def len2 = x * x + y * y
    def len3 = math.max(0.0001, length * len2) // prevent div by 0
    def length = math.sqrt(len2)
}
def dv(p1: Point, p2: Point, mass2: Double) = { // delta velocity: change in speed, horizontal and vertical
    val vec = Vec(p2.x - p1.x, p2.y - p1.y) // dv of body in p1 due to second body in p2 with mass2
    vec.scale(mass2 / vec.len3)
}
def step() = {
    var stepCount = 0; // count how many times the loop below ran so far
    if (stepCount % sampleEveryNsteps == 0)
        for (b <- bodies) {
            val orbitPic = trans(b.p.position.x, b.p.position.y) * b.pic.ct ->
                Picture.circle(2)
            orbitPic.draw
        }
    stepCount += 1
    for (b <- bodies) {
        for (other <- bodies if other != b)
            b.vel.add(dv(b.p.position, other.p.position, other.mass))
        b.p.setPosition(b.p.position.x + b.vel.x, b.p.position.y + b.vel.y)
    }
}
animate { step }
zoomXY(0.3, 0.3, pos, -pos)
invisible