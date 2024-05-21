// two planets around a star
// http://ikojo.in/sf/XSDZnz6/8

case class Vec(var x: Double, var y: Double) // can represent position, force, velocity, etc.
case class Obj(mass: Double, initPos: Point, vel: Vec)
val (mratio, pos, size, initSpeed, gFactor, sampleEveryN) = (
  50, 40, 3, 2.0, 25, 5)
val opacity = 200
val red = Color(255, 0, 0, opacity)
val green = Color(0, 255, 0, opacity)
val blue = Color(0, 110, 255, opacity)
val orange = Color(255, 100, 0, opacity)
val black = Color(0, 0, 0, opacity) // actually gray

val (c1,c2,c3,c4,c5) = (penColor(red) * fillColor(red), penColor(blue) * fillColor(blue), penColor(green) * fillColor(green), penColor(orange) * fillColor(orange), penColor(black) * fillColor(black))
val (b1,b2,b3,b4,b5) = (c1 -> Picture.circle(4*size), c2 -> Picture.circle(size), c3 -> Picture.circle(2*size), c4 -> Picture.circle(size), c5 -> Picture.circle(size))

cleari
val state: List[(Picture, Obj)] = List(
  (b1, Obj(100, Point(-pos, pos), Vec(0,0))), 
  (b2, Obj(1, Point(0, 3*pos), Vec(initSpeed,-1.5*initSpeed))),
  (b3, Obj(10, Point(4*pos,4*pos), Vec(-initSpeed/3, -initSpeed/3)))
  )
state.map(s => s match {
  case(pic, obj) => draw(trans(obj.initPos.x, obj.initPos.y) -> pic)
})
val os = state(0)._2 // red sun
val o1 = state(1)._2 // blue earth
val o2 = state(2)._2 // green jupiter
def angle(p1: Point, p2: Point): Double = math.atan(math.abs((p1.y - p2.y) / (p1.x - p2.x)))
def distance(p1: Point, p2: Point): Double = math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
def vert(diagonal: Double, angle: Double) = diagonal*math.sin(angle)
def horz(diagonal: Double, angle: Double) = diagonal*math.cos(angle)
def force(distance: Double) = gFactor / math.pow(math.max(0.01, distance), 2)
//def force2(distance: Double, mass: Double) = mass * force(distance)

var step = 0 // just to sample the orbits to trace them
animate { // this commands loops over its block 40 times every second or so
    val (p1, p2, p3) = (b1.position, b2.position, b3.position)
    step += 1
    if (step%sampleEveryN == 1) for ((p,c) <- List((p1, c1), (p2, c2))) 
      draw(trans(p.x, p.y) * c -> Picture.circle(0.2))
    if (step > 130 * 2) stopAnimation
    val (d, a) = (distance(p1, p2), angle(p1, p2))
    val f = force(d)
    val (fx, fy) = (horz(f,a), vert(f,a))
    if (p1.x > p2.x) { os.vel.x -= fx; o1.vel.x += mratio*fx } else { os.vel.x += fx; o1.vel.x -= mratio*fx }
    if (p1.y > p2.y) { os.vel.y -= fy; o1.vel.y += mratio*fy } else { os.vel.y += fy; o1.vel.y -= mratio*fy }
    b1.setPosition(p1.x + os.vel.x, p1.y + os.vel.y)
    b2.setPosition(p2.x + o1.vel.x, p2.y + o1.vel.y)
    o2.vel.x *= 0.99
    b3.setPosition(p3.x + o2.vel.x, p3.y + o2.vel.y)
}
