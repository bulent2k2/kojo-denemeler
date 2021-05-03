// See the clean version: complete-graph.kojo

/* The task is to draw all lines between evenly spaced points on a circle.
 *     From: http://codex.kogics.net/codesketch?id=28
 * It is also known as a complete graph with n points (also called nodes or vertices)
 *     See:  https://www.jasondavies.com/complete-graphs/
 * Can you figure out how many lines (also called edges of the graph)
 * are there for n points?
 */

val numberOfPoints = 11 // change and re-run

/*
 * Borrowed from:
 *  http://weblogs.java.net/blog/cayhorstmann/archive/2010/11/12/geometry-problem-scala-0
 * with very small tweaks to make the SPDE code run on Kojo
 *
 * Note: java.net is no more. The web archive has the old page at:
 *     https://web.archive.org/web/20101205213845/http://weblogs.java.net/blog/cayhorstmann/archive/2010/11/12/geometry-problem-scala-0
 *
 * The original solution in C# (imperative) is at:
 *     https://through-the-interface.typepad.com/through_the_interface/2010/11/solving-a-fun-little-geometry-problem-in-c-and-f.html
 */

/* Divide the circle into a number of arcs of the same angle.
 * Imagine that we draw a point wherever two arcs touch.
 * If we have 5 arcs, we will have 5 points.
 * Now, draw a line to connect every point to every other.
 * Each such line is a diameter of the circle.
 */

import Staging._
import math.Pi

// the coordinate of n points on a circle of radius r, indexed by i = 0, 1, ..., n-1
def point(i: Int, n: Int, r: Int) = {
    val angle = 2 * math.Pi * i / n
    ((r * cos(angle)).toInt, (r * sin(angle)).toInt)
}

/* Get a unique pairing of all points so we can draw all the line
 * E.g., 6 * 5 / 2 = 15 pairs for 6 points
 * Or,   7 * 6 / 2 = 21 pairs for 7 points
 */
def pairs(n: Int) = for (i <- 0 until n; j <- 0 until n; if i < j) yield (i, j)

def draw(n: Int) {
    val radius = 250 // our circle is centered around (0,0) with this radius.
    println(s"Drawing the complete graph with $n points (also called nodes or vertices).")
    val points = for (i <- 0 until n) yield point(i, n, radius)
    println(s"We have ${points.size} points. They are: ${points.mkString(",")}")
    println(s"We draw ${pairs(n).size} lines, one for each pair: ${pairs(n).take(n + n / 2).mkString(",")}...")
    val lines = for ((i, j) <- pairs(n)) yield (points(i), points(j))
    for (((x1, y1), (x2, y2)) <- lines) {
        //println(s"Drawing from $x1,$y1 to $x2,$y2")
        line(x1, y1, x2, y2)
    }
}

/* screenSize(size, size)
 * stroke(yellow)
 * background(blue)
 */
reset()
setBackground(blue)
stroke(yellow)
draw(numberOfPoints)
