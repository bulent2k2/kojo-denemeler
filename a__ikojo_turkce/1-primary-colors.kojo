// Opacity has a range from 0 meaning fully transparent (no color) to 255 meaning fully opaque
val opacity = 150
val red = Color(255, 0, 0, opacity)
val green = Color(0, 255, 0, opacity)
val blue = Color(0, 110, 255, opacity)
val yellow = Color(255, 255, 0, opacity)
val black = Color(0, 0, 0, opacity)
cleari
val radius = 100
val pen = penColor(noColor)
def circle(c: Color) = fillColor(c) * pen -> Picture.circle(radius)
draw(
  circle(red),
  trans(radius,0) -> circle(yellow),
  trans(radius/2, radius) -> circle(blue),
  trans(-2*radius, 0) * fillColor(black) * penColor(cm.lightGrey) * penThickness(5)
    -> Picture.circle(radius/2),
  trans(-2*radius, radius) * fillColor(white) * penColor(cm.darkGrey) * penThickness(5)
    -> Picture.circle(radius/2)
)