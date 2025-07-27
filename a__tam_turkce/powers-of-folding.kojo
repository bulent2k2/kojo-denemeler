// The shocking result of folding a paper repeatedly...
//   https://onlinegdb.com/bYNzfW9qB/2
//   http://ikojo.in/sf/GIvSHYW/3

// we start with a paper that has a thickness of 0.1milimeters or 10^-4 meters 
// (100 paper stack would give be 1cm, or a 2/5th inch thick)
val k1: Double = 1.0e-4 
val k2: Double = 300 // a 100-floor skyscraper with 3 meters a floor
val k3 = 300000.0e3 // average distance to the moon is about 270,000 km
val k4 = k3 * 60 * 8.5 // distance to the Sun -- light gets to the moon in 1 second, and to the sun in 8.5 minutes
val k5 = k4 * 60 * 24 * 365 * 4 / 8.5 // nearest star is about 4 light years ahead
val k6 = k5 * 2.5e6 / 4 // nearest galaxy: 2.5 million light years
val k7 = k6 * 2e4 // edge of the known universe: 50 billion light years

val targets = Map(
    k2 -> "Top of a skyscraper",
    k3 -> "The Moon",
    k4 -> "The Sun",
    k5 -> "Proxima Centauri",
    k6 -> "Andromeda Galaxy",
    k7 -> "The Edge of our Universe"
)

def drawLine = println("=" * 40)
println("How many times do we need to fold a")
println(f"(very special) piece of paper ${1000 * k1}%1.1fmm")
println("thick to get as high as something")
println("up there? (And, the resulting side")
println("length of the folded paper would be)")
drawLine

var k = k1;
var side = 0.25 // length of the long edge of the "paper"
var numFolds = 0
for (target <- targets.keys.toList.sorted) {
    while (k < target) {
        numFolds += 1
        if (numFolds % 2 == 1) side /= 2
        k *= 2
    }
    println(f"${targets(target)} takes $numFolds folds (${side}%.2gm)")
}
drawLine
