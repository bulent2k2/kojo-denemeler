// PE 38 Pandigital Multiples
// http://ikojo.in/sf/f1GIpvb/1

// 1 to 9 pandigital numbers
val all = for (i <- 1 to 9) yield i.toByte
def ispd(n: Int) = digits(n).sorted == all
// pd multiples 
def pdm(n: Int, m: Int = 2) = 
  for (b <- 2 to m) {
     val c = d2n((1 to b).flatMap(x => digits(x * n)).toList)
     if (ispd(c)/* && c > 912123123*/) println(n, b, c)
  }

println(9327*2) // this gives us the answer
for (n <- 1 to 10) pdm(n, 10)
pdm(496132,5) // overflow! 987513246 is bogus
for (n <- 11 to 999) pdm(n, 3)
for (n <- 1000 to 9999) pdm(n, 2)

type Digits = List[Byte]
def digits(n: Int): Digits =
  if (n < 10) List(n.toByte)
  else ((n%10).toByte::digits(n/10).reverse).reverse
def d2n(ds: Digits): Int = ds.reverse.zip(powers(ds.size)).map{case (d, p) => d*p}.sum
def powers(n: Int) = for(p <- 0 until n) yield(math.pow(10, p).toInt)

assert(
  List(true, true, true, false, false, false)
    ==
  List(ispd(123456789), ispd(987654321), ispd(135798642),
       ispd(111), ispd(1223456789), ispd(234567890)
  )
)
