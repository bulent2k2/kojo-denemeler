// http://www.kogics.net/public/kojolite/samples/quickref/picture-sample.kojo

def r = PicShape.rect(40, 60)
val car = fillColor(green) * penColor(gray) -> r
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

car.react { self =>
  self.translate(1, 0)
  self.scale(1.001)
  if (isKeyPressed(Kc.VK_P)) {
    self.rotate( 1)
  }
  if (self.collidesWith(walls)) {
    self.setFillColor(red)
    self.stopReactions()
  }
}

car.onMouseClick { (x, y) =>
  car.scale(0.9)
}

activateCanvas()
