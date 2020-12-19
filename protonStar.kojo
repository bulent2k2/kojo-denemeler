//cleari
setSpeed(fast)
if (false) repeat(4) {
    forward(100)
    right()
}
if (false) repeat(50) {
    forward(100)
    left(45)
    back(100)
    right(45)
}
clear()

def protonStar = {
    setAnimationDelay(50)
    setPenColor(white)
    var i = 0
    repeat(15) {
        i+=1
        println(i)
        setFillColor(Color(10, 209, 202, 35)) // click on the word blue to interactively change fill color
        repeat(4) {
            forward(200)
            right(500) // click on the number 90 to interactively change turn angle }
            right(360 / 15)
        }
    }
}
protonStar
invisible
