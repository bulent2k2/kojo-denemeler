// http://ikojo.in/sf/h7B906q/9

// two pairs of binary stars to have four color paintings....
val (pos, size, initSpeed, gravitationalConstant, sampleEveryN) = (50, 5, 1.0, 100, 4)
val brushSize = 6 // paints the orbits. Set to 0 to turn off
val opacity = 50
val red = Color(255, 0, 0, opacity)
val green = Color(0, 255, 0, opacity)
val blue = Color(0, 110, 255, opacity)
val orange = Color(255, 255, 0, opacity) // actually yellow
val black = Color(0, 0, 0, opacity) // actually gray
val (c1,c2,c3,c4) = (penColor(red) * fillColor(red), penColor(blue) * fillColor(blue), penColor(green) * fillColor(green), penColor(orange) * fillColor(orange))
val (b1,b2,b3,b4) = (c1 -> Picture.circle(size), c2 -> Picture.circle(size), c3 -> Picture.circle(size), c4 -> Picture.circle(size))
cleari()
draw(trans(pos+random(10),pos+random(10)) -> b1, trans(-pos+random(10),-pos+random(10)) -> b2, trans(-pos+random(10), pos+random(10)) -> b3, trans(pos+random(10), -pos+random(10))  -> b4)
var (dx1, dy1, dx2, dy2, dx3, dy3, dx4, dy4) =  (initSpeed, 0.0, -initSpeed, 0.0, 0.0, 0.0, 0.0, 0.0)
def angle(p1: Point, p2: Point): Double = math.atan(math.abs((p1.y - p2.y) / (p1.x - p2.x)))
def distance(p1: Point, p2: Point): Double = math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
def vert(diagonal: Double, angle: Double) = diagonal*math.sin(angle)
def horz(diagonal: Double, angle: Double) = diagonal*math.cos(angle)
def force(distance: Double) = gravitationalConstant / math.pow(math.max(size*3, distance), 2)
def dv(p1: Point, p2: Point) = {
    val a = angle(p1, p2)
    val f = force(distance(p1, p2))
    val (fx, fy) = (horz(f,a), vert(f,a))
    (if (p1.x > p2.x) -fx else fx, 
     if (p1.y > p2.y) -fy else fy)
}
var step = 1 // trace the orbits once every four or more iterations
animate {
    val (p1, p2, p3, p4) = (b1.position, b2.position, b3.position, b4.position)
    // comment out the next line to stop tracing the orbits:
    if (brushSize > 0 && step % sampleEveryN == 1) { for ( (p, c) <- List((p1, c1), (p2, c2), (p3, c3), (p4, c4)) ) draw(trans(p.x, p.y) * c -> Picture.circle(brushSize))}; step += 1
    for (p <- List(p2, p3, p4)) {
        val (fx, fy) = dv(p1, p)
        dx1 += fx; dy1 += fy
    }
    for (p <- List(p3, p4, p1)) {
        val (fx, fy) = dv(p2, p)
        dx2 += fx; dy2 += fy
    }
    for (p <- List(p4, p1, p2)) {
        val (fx, fy) = dv(p3, p)
        dx3 += fx; dy3 += fy
    }
    for (p <- List(p1, p2, p3)) {
        val (fx, fy) = dv(p4, p)
        dx4 += fx; dy4 += fy
    }
    b1.setPosition(p1.x + dx1, p1.y + dy1)
    b2.setPosition(p2.x + dx2, p2.y + dy2)
    b3.setPosition(p3.x + dx3, p3.y + dy3)
    b4.setPosition(p4.x + dx4, p4.y + dy4)
}
