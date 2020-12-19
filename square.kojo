// create a new command named square1 that 
// always draws squares of size 100
def square1() {
    repeat(4) {
        forward(100)
        right(90)
    }
}

def square2(size: Int) {
    repeat(4) {
        forward(size)
        right(90)
    }
}

clear()
// now use the newly defined commands
def triSquare() = {
    square1()
    square2(50)
    square2(25)
}

setSpeed(fast)
repeat(4) {
    triSquare()
    left()
}
