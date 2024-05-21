// 1980li yıllarda televizyondaki Monty Hall adlı yarışma programında oynanan oyun
// Kapalı üç kapıdan birinin arkasında araba yani değerli bir ödül var
def car = randomFrom(1 to 3) // there is a sports car behind one of the three doors  
def pick = randomFrom(1 to 3) // the guest picks a door
val doors = Set(1, 2, 3) // there are goats behind the other two doors
def monty(c: Int, p: Int): Set[Int] = doors - c - p // Monty will open one of the remaining doors with a goat
val (c, p) = (car, pick)
val m = monty(c, p)
val msg = if (m.size == 2) s"${m.mkString(". ya da ")}" else m.head.toString
println(s"Rastgele bir örnek:\n  Araba $c. kapının arkasında olsun")
println(s"  Seçtiğimiz kapı $p. kapı olsun\n  Sunucunun açtığı kapı $msg. kapı olacak")
println(s"  Seçtiğimiz kapıda kalırsak ${if (c == p) "kazanırız" else "kaybederiz"}")
println(s"  Öbür kapalı kapıya geçersek ${if (c == p) "kaybederiz" else "kazanırız"}")
def stay: Boolean = car == pick // will the guest win if she stays with her original choice?
def switch: Boolean = monty(car, pick).size == 1 // will the guest win if he switches to the other door?
def runTrials(numTrials: Int = 3000) = {
    val numFaveOutcome1 = (for (i <- 1 to numTrials if stay) yield (1)).size
    val numFaveOutcome2 = (for (i <- 1 to numTrials if switch) yield (1)).size
    println(s"  İlk seçtiğimiz kapının arkasında kalınca $numFaveOutcome1 kere kazandık")
    println(s"  Öbür kapalı kapıya geçi $numFaveOutcome2 kere kazandık")
}
println("Önce hep kalarak, sonra da hep geçerek 3000'er kere oynayalım:")
runTrials()