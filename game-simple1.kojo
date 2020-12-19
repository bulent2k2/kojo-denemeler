// http://www.kogics.net/public/kojolite/samples/quickref/picture-sample.kojo
//
// a simple game
// you need to keep the rectangle within the canvas
// the rectangle moves and grows in size
// its speed goes up as its size increases
// you can rotate it by pressing the 'P' key
// you can make it smaller by clicking on it

def rect = PicShape.rect(40, 60)
val car = fillColor(green) * penColor(gray) -> rect
val cb = canvasBounds
val walls = GPics(
  trans(-cb.width / 2, -cb.height / 2) -> PicShape.vline(cb.height),
  trans(cb.width / 2, -cb.height / 2) -> PicShape.vline(cb.height),
  trans(-cb.width / 2, -cb.height / 2) -> PicShape.hline(cb.width),
  trans(-cb.width / 2, cb.height / 2) -> PicShape.hline(cb.width)
)

cleari()
draw(car)
draw(walls)

var state = 0
car.react { self =>
  self.translate(1, 0)
  self.scale(1.001)
  if (isKeyPressed(Kc.VK_P)) {
    if (state != 1) println("Turning right")
    self.rotate(-1)
    state = 1
  } else if (isKeyPressed(Kc.VK_O)) { 
    if (state != 2) println("Turning left")
    self.rotate( 1)
    state = 2
  } else if (isKeyPressed(Kc.VK_Z)) {
    if (state != 3) { println("Waiting..."); Console.out.flush() } // flush didn't work!
    // Problem: java.lang.IllegalMonitorStateException
    // wait(10)
    Thread.sleep(1000) // 2 seconds
    if (state != 3) { println("Done waiting. Let's go again.") }
    state = 3
  } else {
    if (state > 0) {
      println("Going straight")
      state = -1
    }
  }
  if (self.collidesWith(walls)) {
    self.setFillColor(red)
    self.stopReactions()
    println("Crashed! :-( Try again :-) ")
  }
}

car.onMouseClick { (x, y) =>
  car.scale(0.9)
}

activateCanvas()
println("Drive safely. Good luck!")


