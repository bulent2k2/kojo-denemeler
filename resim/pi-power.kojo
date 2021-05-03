// e^i*pi = -1
// see def exp below where x = i*pi

val p = math.Pi

val s1 = 1.41421
val e1 = 2.71828   // (1 + 1/n)^n  and n -> Infinity   ((n+1)/n)^n
val p1 = 3.14159

def exp(x: Double, m: Int = 100) = math.pow(1 + x/(1.0 * m), m)  // goes to e^x as m goes to Infinity
println(exp(3), exp(10), exp(20))

def ne(x: Double) = math.pow((x-1)/x, x)  // 1 / e :-)
println(ne(9.0e15))
println(1/e1)
def pow(x: Double, y: Double) = math.pow(x, y)
val p2 = pow(p, p)
val p3x = pow(p2, p)
val p3 = pow(p, p2)
val p4x = pow(p3, p)
val p4 = pow(p, p3)
//> def pow(x: Double, y: Double): Double
//> val res2: Double = 3.141592653589793

// irrational to an irrational power can give integer
val s = math.sqrt(2)
pow(pow(s, s), s)

// transcendental to a transcendantal power can give an integer, too
val e = math.E
val ln2 = math.log(2)
pow(e, ln2)

def pow2(base: BigDecimal, e: BigDecimal) = {
    BigDecimal(math.pow(base, e))
}
def pow3(base: BigDecimal, e1: BigDecimal, e2: BigDecimal) =     if (false) 
  pow2(pow2(base, e1), e2) else  pow2(base, pow2(e1, e2))
//> def pow3(base: BigDecimal, e1: BigDecimal, e2: BigDecimal): scala.math.BigDecimal

pow2(4, pow2(3, 2))
pow2(pow2(4, 3), 2)

pow3(2,3,4)
pow2(2,pow2(3,4))
pow2(pow2(2,3),4)

pow3(2,2,2)
pow2(4, 2)
pow2(2, 4)

pow3(3,3,3)
val pi2 = BigDecimal(22/7.0)
val p3b = pow3(pi2, pi2, pi2)
pow2(p3b, pi2)
math.pow(pi2, p3b)
// pow2(pi2, p3b)