import math.{ cos, sin, round, sqrt }
case class Point(x: Double, y: Double)
type Points = List[Point]

val r = 200

// This is what we need to improve. First, we correct a minor thing we overlooked. What if we get the same angle for two points??
// So, we fix that using the 'distinct' method. But then we also need to make sure we get enough points for our polygon!
// To solve that problem, we use twice as many angles as needed.
// Next, we sort the angles! That takes care of the crossed edges (:-)
def randomPoints(n: Int = 3, testing: Boolean = false): Points = {
    val angles = for (i <- (1 to 2 * n).toList) yield randomFrom(1 to 360)
    val anglesSorted = angles.distinct.take(n).sorted
    if (anglesSorted.size != n) {
        throw new Exception("Tough luck! Try again.")
    }
    if (testing) println(anglesSorted.mkString("Angles: ", ", ", "."))
    anglesSorted.map { angle =>
        val angleInRadians = angle.toDouble * math.Pi / 180.0
        Point(r * cos(angleInRadians), r * sin(angleInRadians))
    }
}

def centerInsideTriangle(ps: Points): Boolean = {
    def length(p1: Point, p2: Point) = {
        def square(z: Double) = z * z
        sqrt(square(p2.x - p1.x) + square(p2.y - p1.y))
    }
    assert(ps.size == 3, "Only 2D for now. Use three points!")
    val ls = (ps.zip(ps.last :: ps).map { case (p1, p2) => length(p1, p2) }).sorted.reverse
    val (h, s1, s2) = (ls(0), ls(1), ls(2))
    h * h < (s1 * s1 + s2 * s2)
}

def computeProbability(numTrials: Int = 3000) = {
    val inside = for (i <- 1 to numTrials if (centerInsideTriangle(randomPoints()))) yield (1)
    println(f"Empirically, we got: Probability(center is inside triangle)=${inside.size / numTrials.toDouble}%.2f")
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
drawIt(randomPoints(4, true))
computeProbability()
// Now, can you update the computation to work for quadrangles?
// For a bonus challenge, try to do it for any random polygon!
