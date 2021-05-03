type F = (Int) => Int //> type F
def f(in: F) = in(3) //> def f(in: F): Int
f(x => x*x) //> val res10: Int = 9
type F2 = (Int, F) => Int//> type F2 //> type F2
def f2(n: Int, in: F2): Int = in(n, x => x*x*x) //> def f2(n: Int, in: F2): Int
f2(3, (x, f2) => x+1) //> val res11: Int = 4
