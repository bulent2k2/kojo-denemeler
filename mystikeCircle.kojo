random(10, 50)


setBackground(white)
setSpeed(fast)
setPenColor(Color(128, 237, 21, 150))
repeat(20) {
savePosHe()
setPenThickness(random(1, 20)) 
setPenColor(cm.rgba(0, 30, 200, random(1, 255))) 
right(random(1, 360))
forward(random(5, 100))
restorePosHe()
}

 
setAnimationDelay(10) 
setPenColor(Color(240, 222, 12, 170))
val len = random(100, 200) 
repeat(2) {
    forward(len)
    right()
}

