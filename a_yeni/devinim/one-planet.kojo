// http://ikojo.in/sf/h2BcRWz/10

val mratio = 60 // how massive the (red) star is relative to the (blue) planet
val initSpeedHorz = 2.0 // the planet moves right with this speed when the simulation starts
val initSpeedVert = -1.0 // the planet also moves down, but half as fast
val (pos, size, sampleEveryN) = (40, 3, 2)
val (c2,c1) = (penColor(red) * fillColor(red), penColor(blue) * fillColor(blue))
val (b1,b2) = (c1 -> Picture.circle(size), c2 -> Picture.circle(2*size))
cleari
draw(trans(pos,3*pos) -> b1, trans(0,pos) -> b2)
var (dx1, dy1, dx2, dy2) =  (initSpeedHorz, initSpeedVert, 0.0, 0.0)
def angle(p1: Point, p2: Point): Double = math.atan(math.abs((p1.y - p2.y) / (p1.x - p2.x)))
def distance(p1: Point, p2: Point): Double = math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
def vert(diagonal: Double, angle: Double) = diagonal*math.sin(angle)
def horz(diagonal: Double, angle: Double) = diagonal*math.cos(angle)
def force(distance: Double) = 25.0 / math.pow(math.max(0.01, distance), 2)
var step = 0
animate {
    val (p1, p2) = (b1.position, b2.position)
    if (step%sampleEveryN == 0) for ((p,c) <- List((p1, c1), (p2, c2))) 
      draw(trans(p.x, p.y) * c -> Picture.circle(0.2)); step += 1
    val (d, a) = (distance(p1, p2), angle(p1, p2))
    val f = force(d)
    val (fx, fy) = (horz(f,a), vert(f,a))
    if (p1.x > p2.x) { dx1 -= mratio * fx; dx2 += fx } else { dx1 += mratio * fx; dx2 -= fx }
    if (p1.y > p2.y) { dy1 -= mratio * fy; dy2 += fy } else { dy1 += mratio * fy; dy2 -= fy }
    b1.setPosition(p1.x + dx1, p1.y + dy1)
    b2.setPosition(p2.x + dx2, p2.y + dy2)
}

