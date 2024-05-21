def tree(distance: Double) {
    if (distance > 4) {
        setPenThickness(distance/3)
        setPenColor(Color(distance.toInt, math.abs(255-distance*3).toInt, 125))
        forward(distance)
        right(25)
        tree(distance*0.8-2)
        left(45)
        tree(distance-10)
        right(20)
        forward(-distance)
    }
}
cleari()
setSpeed(superFast) // or fast
hop(-200)
zoomXY(0.5,0.6,-50,0)
tree(95)