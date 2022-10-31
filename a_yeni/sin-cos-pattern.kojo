// https://codex.kogics.net/codesketch?sketchId=1711
cleari()
originBottomLeft()
val cb = canvasBounds
val bg = cm.radialGradient(
    cb.width / 2, cb.height / 2, cm.gold.fadeOut(0.2), 
    cb.width/2, cm.white, false)
setBackground(bg)
setRefreshRate(100)

class Sketch {
    var t = 0.0
    def setup(surface: CanvasDraw) {
        import surface._
        stroke(cm.black.fadeOut(0.8))
        strokeWeight(1)
        //        fill(white)
        translate(cwidth / 2, cheight / 2)
    }

    def drawLoop(surface: CanvasDraw) {
        import surface._
        ellipse(math.cos(t / 10) * 200, math.sin(t / 50) * 200, 100, 100)
        t += 1
    }
}

val sketch = new Sketch
val pic = Picture.fromSketch(sketch, 1)
draw(pic)