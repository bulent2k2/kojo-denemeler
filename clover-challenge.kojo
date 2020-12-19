clear
val p = Picture {
    setSpeed(fast)
    left(270, 50)
    penUp
    home
    penDown
    right(270, 50)
    penUp
    home
    penDown
    hop(-100); right; right
    savePosHe()
    left(270, 50)
    restorePosHe()
    right(270, 50)
    invisible
}
draw(trans(0, 50) -> p)
invisible
