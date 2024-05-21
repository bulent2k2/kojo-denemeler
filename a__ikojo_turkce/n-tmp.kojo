// Let's ask the Turtle to draw a curve for us.
// We want to plot the graph of y = a*x^2 + b*x +c
def poly(x:Double) = 0.01 * x * x - 0.5 * x - 10
// Remove the constant (-10) at the end above and rerun

// gridOn() // these two are available only in the desktop app
// axesOn()
zoomXY(0.5, 0.3, 60, 400)
setSpeed(medium)
val range=200
setPosition(-range, poly(-range))
for(x <- -range+10 to range+100; if (x % 10 == 0))
  lineTo(x, poly(x))
draw(fillColor(black) -> Picture.circle(5))
draw(scale(2.5) * trans(0, 30) -> Picture.text("origin"))