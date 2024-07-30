// http://ikojo.in/sf/FeTKZAs/6
val (pos, size, initSpeed, gFactor, sampleEveryN) = (40,5, 1.1, 200, 13)
val (c1,c2) = (penColor(red) * fillColor(red), penColor(blue) * fillColor(blue))
val (b1,b2) = (c1 -> Picture.circle(size), c2 -> Picture.circle(size))
cleari()
draw(trans(pos,pos) -> b1, trans(-pos,-pos) -> b2)
var (dx1, dy1, dx2, dy2) =  (initSpeed, 0.0, -initSpeed, 0.0)
def angle(p1: Point, p2: Point): Double = math.atan(math.abs((p1.y - p2.y) / (p1.x - p2.x)))
def distance(p1: Point, p2: Point): Double = math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
def vert(diagonal: Double, angle: Double) = diagonal*math.sin(angle)
def horz(diagonal: Double, angle: Double) = diagonal*math.cos(angle)
def force(distance: Double) = gFactor / math.pow(math.max(0.01, distance), 2)
var step = 0
animate {
    val (p1, p2) = (b1.position, b2.position)
    step += 1
    if (step%sampleEveryN == 1) for ((p,c) <- List((p1, c1), (p2, c2))) 
      draw(trans(p.x, p.y) * c -> Picture.circle(0.2))
    val (d, a) = (distance(p1, p2), angle(p1, p2))
    val f = force(d)
    val (fx, fy) = (horz(f,a), vert(f,a))
    if (p1.x > p2.x) { dx1 -= fx; dx2 += fx } else { dx1 += fx; dx2 -= fx }
    if (p1.y > p2.y) { dy1 -= fy; dy2 += fy } else { dy1 += fy; dy2 -= fy }
    b1.setPosition(p1.x + dx1, p1.y + dy1)
    b2.setPosition(p2.x + dx2, p2.y + dy2)
}
