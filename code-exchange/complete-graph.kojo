// For comments, see: complete-graph--verbose.kojo
val numberOfPoints = 11   // change and re-run
import Staging.{ sin, cos, line, reset, stroke }
def point(i: Int, n: Int, r: Int) = {
    val angle = 2 * math.Pi * i / n
    ((r * cos(angle)).toInt, (r * sin(angle)).toInt)
}
def pairs(n: Int) = for (i <- 0 until n; j <- 0 until n; if i < j) yield (i, j)
def draw(n: Int) {
    val radius = 250
    val points = for (i <- 0 until n) yield point(i, n, radius)
    val lines = for ((i, j) <- pairs(n)) yield (points(i), points(j))
    for (((x1, y1), (x2, y2)) <- lines) line(x1, y1, x2, y2)
}
reset()
setBackground(blue)
stroke(yellow)
draw(numberOfPoints)