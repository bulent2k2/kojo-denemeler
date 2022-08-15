import math.{ cos, sin, round, sqrt }
case class Point(x: Double, y: Double)
type Points = List[Point]
case class Triangle(p1: Point, p2: Point, p3: Point)
def t2ps(t: Triangle): Points = List(t.p1, t.p2, t.p3)
type Triangles = List[Triangle]

val r = 200

def randomPoints(n: Int = 3, testing: Boolean = false): Points = {
    val angles = for (i <- (1 to 2 * n).toList) yield randomFrom(1 to 360)
    val anglesSorted = angles.distinct.take(n).sorted
    if (anglesSorted.size != n) { // Can you tell why we need to check for this?
        throw new Exception("Tough luck! Try again.")
    }
    if (testing) println(anglesSorted.mkString("Angles: ", ", ", "."))
    anglesSorted.map { angle =>
        val angleInRadians = angle.toDouble * math.Pi / 180.0
        Point(r * cos(angleInRadians), r * sin(angleInRadians))
    }
}

// This is where we address the bonus! The trick is to reuse the method that checks when a triangle includes the center.
// We posit that given a polygon that includes the center, there must be at least one triangle slice of the polygon
// (meaning we can pick three of the corners of the polygon) that includes the center. Can you see why that's the case?
// To be certain, can you prove (or disprove) this?
def isCenterInside(ps: Points): Boolean = {
    def isCenterInsideTriangle(t: Triangle): Boolean = {
        def length(p1: Point, p2: Point) = {
            def square(z: Double) = z * z
            sqrt(square(p2.x - p1.x) + square(p2.y - p1.y))
        }
        val ps = t2ps(t)
        val ls = (ps.zip(ps.last :: ps).map { case (p1, p2) => length(p1, p2) }).sorted.reverse
        val (h, s1, s2) = (ls(0), ls(1), ls(2))
        h * h < (s1 * s1 + s2 * s2)
    }
    def allTriangles(l: Points): Triangles = {
        val ts = for (e1 <- l; e2 <- l; e3 <- l if e1 != e2 && e2 != e3 && e3 != e1) yield Set(e1, e2, e3)
        ts.toSet.map { s: Set[Point] => val t = s.toList; Triangle(t(0), t(1), t(2)) }.toList
    }
    allTriangles(ps).exists{ t => isCenterInsideTriangle(t) }
}

def computeProbability(numCorners: Int = 3, numTrials: Int = 3000) = {
    val inside = for (i <- 1 to numTrials if (isCenterInside(randomPoints(numCorners)))) yield (1)
    println(f"Empirically, we got: Probability(center is inside polygon)=${inside.size / numTrials.toDouble}%.2f")
}

def drawIt(ps: Points, lineColor: Color = red, fill: Boolean = false) = {
    val penC = penColor(lineColor)
    val fillC = fillColor(if (fill) red else noColor)
    draw(penColor(blue) -> Picture.circle(r))
    draw(fillC -> Picture.circle(4))
    ps.map(p => draw(fillC * penC * trans(p.x, p.y) -> Picture.circle(4)))
    ps.zip(ps.last :: ps).map {
        case (p1, p2) => {
            draw(trans(p1.x, p1.y) * penC -> Picture.line(p2.x - p1.x, p2.y - p1.y))
        }
    }
}

cleari
// try 2, 3, 4, 5, 6, 7, 8, 9 and 10 :-)
val numCorners = 4
val polygon = randomPoints(numCorners, true)
drawIt(polygon, red, isCenterInside(polygon))
if (numCorners > 5) { timeit { computeProbability(numCorners) } }
else computeProbability(numCorners)

