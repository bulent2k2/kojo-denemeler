// if we randomly select three points equidistance from a central point,
// how often would the triangle we stumble upon contain the center?
import Staging._
import Staging.{ clear, circle, point, setPenColor, setFillColor }
type Point = (Double, Double)
type Points = List[Point]
val Pi2 = 2.0 * math.Pi // 2*Pi radians is one full turn
val r = 200 // radius of the circle
def degree2xy(d: Int) = {
    val ra = Pi2 * d.toDouble / 360.0
    (r * cos(ra), r * sin(ra))
}
def centerInTriangle(ps: Points): Boolean = {
    assert(ps.size == 3, "Only 2D for now. Use three points!")
    val ls = ((ps.zip(ps.last :: ps)).map { case (p1, p2) => length(p1, p2) }).sorted.reverse
    val (h, s1, s2) = (ls(0), ls(1), ls(2)) // longest edge and the two shorter edges
    h * h < (s1 * s1 + s2 * s2)
}
def length(p1: Point, p2: Point) = {
    val ((x1, y1), (x2, y2)) = (p1, p2)
    val (d1, d2) = (x2 - x1, y2 - y1)
    round(sqrt(d1 * d1 + d2 * d2), 2)
}
def points(n: Int = 3) = for (i <- (1 to n).toList) yield degree2xy(randomFrom(1 to 360))
def drawOneRandomTriangle() = {
    val ps = points()
    drawIt(ps, centerInTriangle(ps))
}
def computeProbability(numTrials: Int = 5000) = {
    val numFaveOutcome = (for (i <- 1 to numTrials if (centerInTriangle(points()))) yield (1)).size
    println("Probability(center is inside the triangle)=" + round(numFaveOutcome / numTrials.toDouble, 2))
}
def drawIt(ps: Points, fill: Boolean = false) = {
    clear; setPenColor(blue)
    circle(0, 0, r)
    setFillColor(if (fill) red else noColor)
    setPenColor(red)
    circle(0, 0, 4)
    ps.map(p => circle(p._1, p._2, 4))
    ps.zip(ps.last :: ps).map {
        case (p1, p2) => line(p1._1, p1._2, p2._1, p2._2)
    }
}
// we could also select four randome points: drawIt(points(4))
drawOneRandomTriangle()
computeProbability()