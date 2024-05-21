// Monty Hall's show: Let's Make a Deal!
def car = randomFrom(1 to 3) // there is a sports car behind one of the three doors  
def pick = randomFrom(1 to 3) // the guest picks a door
val doors = Set(1, 2, 3) // there are goats behind the other two doors
def monty(c: Int, p: Int): Set[Int] = doors - c - p // Monty will open one of the remaining doors with a goat
val (c, p) = (car, pick)
val m = monty(c, p)
val msg = if (m.size == 2) s"door ${m.mkString(" or ")}" else m.head.toString
println(s"Sample run:\n  The car is behind door $c.\n  You picked door $p.\n  Monty opens $msg.") 
def stay: Boolean = car == pick // will the guest win if she stays with her original choice?
def switch: Boolean = monty(car, pick).size == 1 // will the guest win if he switches to the other door?
def runTrials(numTrials: Int = 3000) = {
    val numFaveOutcome1 = (for (i <- 1 to numTrials if stay) yield (1)).size
    val numFaveOutcome2 = (for (i <- 1 to numTrials if switch) yield (1)).size
    def mround(d: Double): Double = math.round(d * 100) / 100.0
    println("  Probability(win if stay)=" + mround(numFaveOutcome1 / numTrials.toDouble))
    println("  Probability(win if switch)=" + mround(numFaveOutcome2 / numTrials.toDouble))
}
println("Try 3000 times:")
runTrials()