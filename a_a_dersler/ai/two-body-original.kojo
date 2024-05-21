// The dance of twin stars in an eternal gravitational pull
val (size, initSpeed, normalize) = (5, 1.1, 100)
val (c1,c2) = (penColor(red) * fillColor(red), penColor(blue) * fillColor(blue))
val (b1,b2) = (c1 -> Picture.circle(size), c2 -> Picture.circle(size))
cleari()
draw(trans(20,20) -> b1, trans(-20,-20) -> b2)
var (dx1, dy1, dx2, dy2) =  (initSpeed, 0.0, -initSpeed, 0.0)
def angle(p1: Point, p2: Point): Double = math.atan(math.abs((p1.y - p2.y) / (p1.x - p2.x)))
def distance(p1: Point, p2: Point): Double = math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
def vert(diagonal: Double, angle: Double) = diagonal*math.sin(angle)
def horz(diagonal: Double, angle: Double) = diagonal*math.cos(angle)
def force(distance: Double) = normalize / math.pow(math.max(0.01, distance), 2)
def dv(p1: Point, p2: Point) = {
    val (d, a) = (distance(p1, p2), angle(p1, p2))
    val f = force(d)
    val (fx, fy) = (horz(f,a), vert(f,a))
    val (fx1, fx2) = if (p1.x > p2.x) (-fx, fx) else (fx, -fx)
    val (fy1, fy2) = if (p1.y > p2.y) (-fy, fy) else (fy, -fy)
    (fx1, fy1, fx2, fy2)
}
animate {
    val (p1, p2) = (b1.position, b2.position)
    val (fx1, fy1, fx2, fy2) = dv(p1, p2)
    dx1 += fx1; dy1 += fy1; dx2 += fx2; dy2 += fy2
    b1.setPosition(p1.x + dx1, p1.y + dy1)
    b2.setPosition(p2.x + dx2, p2.y + dy2)
}