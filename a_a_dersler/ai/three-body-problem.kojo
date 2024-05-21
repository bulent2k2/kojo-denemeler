// Three body problem of gravity is chaotic!
val (pos, size, initSpeed, normalize) = (50, 5, 1.0, 100)
val (c1,c2,c3) = (penColor(red) * fillColor(red), penColor(blue) * fillColor(blue), penColor(green) * fillColor(green))
val (b1,b2,b3) = (c1 -> Picture.circle(size), c2 -> Picture.circle(size), c3 -> Picture.circle(size))
cleari()
draw(trans(pos+random(10),pos+random(10)) -> b1, trans(-pos+random(10),-pos+random(10)) -> b2, trans(-pos+random(10), pos+random(10)) -> b3)
var (dx1, dy1, dx2, dy2, dx3, dy3) =  (initSpeed, 0.0, -initSpeed, 0.0, 0.0, 0.0)
def angle(p1: Point, p2: Point): Double = math.atan(math.abs((p1.y - p2.y) / (p1.x - p2.x)))
def distance(p1: Point, p2: Point): Double = math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
def vert(diagonal: Double, angle: Double) = diagonal*math.sin(angle)
def horz(diagonal: Double, angle: Double) = diagonal*math.cos(angle)
def force(distance: Double) = normalize / math.pow(math.max(size*3, distance), 2)
def dv(p1: Point, p2: Point) = {
    val a = angle(p1, p2)
    val f = force(distance(p1, p2))
    val (fx, fy) = (horz(f,a), vert(f,a))
    (if (p1.x > p2.x) -fx else fx, 
     if (p1.y > p2.y) -fy else fy)
}
animate {
    val (p1, p2, p3) = (b1.position, b2.position, b3.position)
    for ( (p, c) <- List((p1, c1), (p2, c2), (p3, c3)) ) draw(trans(p.x, p.y) * c -> Picture.circle(0.1))
    for (p <- List(p2, p3)) {
        val (fx, fy) = dv(p1, p)
        dx1 += fx; dy1 += fy
    }
    for (p <- List(p3, p1)) {
        val (fx, fy) = dv(p2, p)
        dx2 += fx; dy2 += fy
    }
    for (p <- List(p1, p2)) {
        val (fx, fy) = dv(p3, p)
        dx3 += fx; dy3 += fy
    }
    b1.setPosition(p1.x + dx1, p1.y + dy1)
    b2.setPosition(p2.x + dx2, p2.y + dy2)
    b3.setPosition(p3.x + dx3, p3.y + dy3)
}