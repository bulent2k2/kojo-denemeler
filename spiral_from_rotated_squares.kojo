clear()
setSpeed(fast)
repeatFor(10 to 400 by 3) { n =>
    // n is the current element of the sequence that you are going through
    forward(n)
    right(91)
}