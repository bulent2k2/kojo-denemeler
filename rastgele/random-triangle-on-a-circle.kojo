// if we pick three points on a circle at random,
// how often would the resulting triangle contain
// the center of the circle?

// First, define a new type: Point. It has x and y coordinates:
case class Point(x: Double, y: Double)
type Points = List[Point] // this is a type alias for convenience
// Now, another type to represent triangles:
case class Triangle(p1: Point, p2: Point, p3: Point) {
    def points: Points = List(p1, p2, p3)
}
// We need only one circle. So, no need for a new type. A single object would do.
object Circle {
    val radius = 200 // Does the probability depend on this?
}

def randomTriangle = Triangle(randomPoint, randomPoint, randomPoint)
def randomPoint = randomPoints(numPoints = 1)(0)
// how to get random points on a circle? Hint: use a random angle!
def randomPoints(numPoints: Int = 3): Points = {
    // given an angle, compute the point
    def angle2Point(degree: Int) = {
        val radians = degree.toDouble * math.Pi / 180.0
        Point(Circle.radius * math.cos(radians), Circle.radius * math.sin(radians))
    }
    { for (i <- 1 to numPoints) yield angle2Point(randomFrom(1 to 360)) }.toList
}

def computeProbability(numTrials: Int = 100000) = {
    val numInside = (for (i <- 1 to numTrials if (isCenterInside(randomTriangle))) yield (1)).size
    println(f"Only $numInside out of $numTrials triangles contain the center. The ratio of the two is ~${numInside / numTrials.toDouble}%.2f.")
}

// given a triangle on the circle, does it contain the center of the circle?
// How to do this?
// Hint: One of the edges is the longest when the triangle is a right (pythagorean) triangle.
// In that case, the long edge (the hypothenus) goes over the center!
def isCenterInside(triangle: Triangle): Boolean = {
    def length(p1: Point, p2: Point) = {
        def square(z: Double) = z * z
        math.sqrt(square(p2.x - p1.x) + square(p2.y - p1.y))
    }
    val ps = triangle.points
    val ls = (ps.zip(ps.last :: ps).map { case (p1, p2) => length(p1, p2) }).sorted.reverse
    val (h, s1, s2) = (ls(0), ls(1), ls(2)) // longest edge and the two shorter edges
    h * h < (s1 * s1 + s2 * s2)
}

def drawOneTriangle = {
    val triangle = 1 match { // cases 2 and 3 are only for testing. Try them!
        case 1 => randomTriangle
        case 2 => { val p = randomPoint; Triangle(p, p, p) }
        case 3 => { val p = randomPoint; Triangle(p, p, randomPoint) }
    }
    drawThePicture(triangle.points, red, isCenterInside(triangle))
}

def drawThePicture(ps: Points, lineColor: Color = red, fill: Boolean = false) = {
    val penC = penColor(lineColor)
    val fillC = fillColor(if (fill) red else noColor)
    draw(penColor(blue) -> Picture.circle(Circle.radius))
    draw(fillC -> Picture.circle(4)) // The center of the circle. Filled if it is inside the triangle
    ps.map(p => draw(fillC * penC * trans(p.x, p.y) -> Picture.circle(4))) // the corners of the triangle
    ps.zip(ps.last :: ps).map { // the three edges of the triangle
        case (p1, p2) => {
            draw(trans(p1.x, p1.y) * penC -> Picture.line(p2.x - p1.x, p2.y - p1.y))
        }
    }
}

cleari
drawOneTriangle
computeProbability()
// We could also draw four or more points and connect them with lines:
//   drawThePicture(randomPoints(5), green)
// Note: sometimes the lines cross.
// Challenge: Can you improve the code so that we always get a proper polygon?

/*
   So, now you know that the odds of a triangle to contain the center of the circle is roughly 0.25 or one fourth.
   But, can you prove that it is exactly one fourth?
   In other words, only one random triangle in four contains the center on average.
   Why is that the case?
 */
