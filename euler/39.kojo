// PE 39 Integer Right Triangles
// http://ikojo.in/sf/KgdVIzg/3

// right triangle with integral side
val m: HashMap[Int, Int] = HashMap.empty
for (a <- 1 to 300) 
  for (b <- a to 400) 
    for (c <- b to 500) {
      val p = a + b + c
      if (p <= 1000 && a*a + b*b == c*c) {
        if(m.contains(p)) m(p) += 1 
        else m(p) = 1
        if(p == 840) println(a,b,c)
      }
    }
println(m.maxBy{ case (k, v) => v })
