// triangular, pentagonal and hexagonal numbers
def t(n: Long): Long = n * (n + 1) / 2
def p(n: Long): Long = n * (3 * n - 1) / 2
def h(n: Long): Long = n * (2 * n - 1)
clearOutput()
println("lets go!")
assert(t(285) - p(165) == 0)
assert(p(165) - h(143) == 0)

// roots = (-b +|- sqrt(b*b - 4ac)) / 2a
def t2n(t: Long) = math.round((-1 + math.sqrt(8 * t + 1)) / 2).toLong
/*  n*n + n - 2t = 0 => a = 1, b = 1, c = -2t
    n = (-1 + sqrt(1 + 8t)) / 2                      */
def ist(x: Long) = t(t2n(x)) == x
def h2n(h: Long) = math.round((1 + math.sqrt(8 * h + 1)) / 4).toLong
/*  2n*n - n - h = 0 => a = 2, b = -1, c = -h
    n = (1 + sqrt(1 + 8h)) / 4                       */
def ish(x: Long) = h(h2n(x)) == x
/*  3 * n * n - n - 2p = 0 => a = 3, b = -1 c = -2p
    n = (1 + sqrt(1 + 24p))/6                        */
def p2n(p: Long) = math.round((1 + math.sqrt(24 * p + 1)) / 6).toLong
def isp(x: Long) = p(p2n(x)) == x

def isph(x: Long) = isp(x) && ish(x)
val mmm = Int.MaxValue
val max = Long.MaxValue // 9-223372-036854-775807 e19
println(mmm, max) // 2147483647, 9223372036854775807
val m = 30000
for (n <- 1 to m if isp(h(n))) println(n, h(n)) // if h, then t
/* (1,1)
   (143,40755)
   (27693,1533776805) */
val answer = 1533776805
assert(ist(answer) && isph(answer))
println("done!")