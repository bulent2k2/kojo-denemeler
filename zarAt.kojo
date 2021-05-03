def zarAt = (random(6) + 1, random(6) + 1)

println(zarAt)
repeati(0) { turn =>
    println("Turn: " + turn)
    println("Black to play: " + zarAt)
    println("White to play: " + zarAt)
}
