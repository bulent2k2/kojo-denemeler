val t0 = Staging.time

// takes about 7 seconds, due to memory allocation for
// 100 million integers (32-bit == 4 bytes => 0.4Gig, but also the container (IndexedSeq) ..
// for(x <- 1 to 10000; y <- 1 to 10000) yield(x * y)

// without yield, it is much faster (~0.4 seconds)
for (x <- 1 to 10000; y <- 1 to 10000) (x * y)

val test = for (x <- 1 to 10; y <- 1 to 10) yield (x * y)

// Thread.sleep(3000)
val t1 = Staging.time
clearOutput()
println(s"Took ${round(t1 - t0, 2)} seconds")