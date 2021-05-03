// http://ikojo.in/sf/Q9zvc92/4
// if we randomly select three points equidistance from a point,
// how often would that triangle contain the center?
import math.{cos, sin, round, sqrt}
type Point = (Double, Double) // point has coordinates (x, y)
type Points = List[Point] // the three corners of a triangle 
val Pi2 = 2.0 * math.Pi // 2*Pi radians is one full turn around the circle
val r = 100 // radius of the circle
def degree2xy(d: Int) = {
    val ra = Pi2 * d.toDouble / 360.0
    (r * cos(ra), r * sin(ra))
}
def centerInTriangle(ps: Points): Boolean = {
    assert(ps.size == 3, "Only 2D for now. Use three points!")
    val ls = (ps.zip(ps.last :: ps).map { case (p1, p2) => length(p1, p2) }).sorted.reverse
    val (h, s1, s2) = (ls(0), ls(1), ls(2)) // longest edge and the two shorter edges
    h * h < (s1 * s1 + s2 * s2)
}
def length(p1: Point, p2: Point) = {
    val ((x1, y1), (x2, y2)) = (p1, p2)
    val (d1, d2) = (x2 - x1, y2 - y1)
    mround(sqrt(d1 * d1 + d2 * d2))
}
def mround(d: Double): Double = round(d * 100) / 100.0
def points(n: Int = 3) = for (i <- (1 to n).toList) yield degree2xy(randomFrom(1 to 360))
def drawOneRandomTriangle() = {
    val ps = points()
    drawIt(ps, centerInTriangle(ps))
}
def computeProbability(numTrials: Int = 5000) = {
    val numFaveOutcome = (for (i <- 1 to numTrials if (centerInTriangle(points()))) yield (1)).size
    println("Probability(center is inside)=" + mround(numFaveOutcome / numTrials.toDouble))
}
def drawIt(ps: Points, fill: Boolean = false) = {
    cleari; val fc = if (fill) red else noColor
    draw(penColor(blue) * trans(0, 0) -> PicShape.circle(r)) // circle
    draw(fillColor(fc) * trans(0, 0) -> PicShape.circle(4)) // center is filled if it is inside the triangle
    ps.map(p => draw(fillColor(fc) * trans(p._1, p._2) -> PicShape.circle(4))) // the corners of the triangle
    ps.zip(ps.last :: ps).map {
      case (p1, p2) => { // the edges of the triangle
        val (x1, y1, x2, y2) = (p1._1, p1._2, p2._1, p2._2)
        draw(trans(x1, y1) -> PicShape.line(x2-x1, y2-y1))
      }
    }
}
// we could also select four random points: drawIt(points(4))
drawOneRandomTriangle()
computeProbability()
