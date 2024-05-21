// two planets around a star
// http://ikojo.in/sf/XSDZnz6/8
cleari

val (pos, size, initSpeed, gFactor, sampleEveryN) =
  (40, 3, 2.0, 25, 10)

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
val (c1,c2,c3) = (penColor(red) * fillColor(red), penColor(blue) * fillColor(blue), penColor(green) * fillColor(green))
// sun(red) earth(blue) jupiter(green)
val os = Obj(100, Point(-pos, pos), Vec(0,0), Pic(4*size, c1))
val o1 = Obj(2, Point(0, 3*pos), Vec(initSpeed,-1.5*initSpeed), Pic(size, c2))
val o2 = Obj(4, Point(3*pos,3*pos), Vec(initSpeed/2, -initSpeed), Pic(2*size, c3))

def angle(p1: Point, p2: Point): Double = math.atan(math.abs((p1.y - p2.y) / (p1.x - p2.x)))
def distance(p1: Point, p2: Point): Double = math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
def vert(diagonal: Double, angle: Double) = diagonal*math.sin(angle)
def horz(diagonal: Double, angle: Double) = diagonal*math.cos(angle)
def force(distance: Double) = gFactor / math.pow(math.max(0.01, distance), 2)
def dv(p1: Point, p2: Point, mratio: Double) = {
    val a = angle(p1, p2)
    val f = mratio*force(distance(p1, p2))
    val (fx, fy) = (horz(f,a), vert(f,a))
    (if (p1.x > p2.x) -fx else fx, 
     if (p1.y > p2.y) -fy else fy)
}
zoomXY(0.7, 0.7, -3*pos, -pos)
var step = 0 // just to sample the orbits to trace them
animate { // this commands loops over its block 40 times every second or so
    val (p1, p2, p3) = (os.p.position, o1.p.position, o2.p.position)
    step += 1
    if (step%sampleEveryN == 1) for ((p,c) <- List((p1, c1), (p2, c2), (p3, c3))) 
      draw(trans(p.x, p.y) * c -> Picture.circle(2))
    //if (step > 130 * 2) stopAnimation
    for ((p, o) <- List((p2, o1), (p3, o2))) {
        val (fx, fy) = dv(p1, p, o.mass/os.mass)
        os.vel.x += fx; os.vel.y += fy
    }
    for ((p, o) <- List((p3, o2), (p1, os))) {
        val (fx, fy) = dv(p2, p, o.mass/o1.mass)
        o1.vel.x += fx; o1.vel.y += fy
    }
    for ((p, o) <- List((p1, os), (p2, o1))) {
        val (fx, fy) = dv(p3, p, o.mass/o2.mass)
        o2.vel.x += fx; o2.vel.y += fy
    }
    os.p.setPosition(p1.x + os.vel.x, p1.y + os.vel.y)
    o1.p.setPosition(p2.x + o1.vel.x, p2.y + o1.vel.y)
    o2.p.setPosition(p3.x + o2.vel.x, p3.y + o2.vel.y)
}