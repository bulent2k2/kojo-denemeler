// ~/src/kojo/git/kojo/src/main/scala/net/kogics/kojo/lite/i18n/trInit.scala
// where are they? Doesn't compile from trInit.scala:
/* 
[info] Compiling 172 Scala sources and 22 Java sources to /Users/ben/Desktop/src/kojo/git/kojo/target/scala-2.13/classes...
[error] /Users/ben/Desktop/src/kojo/git/kojo/src/main/scala/net/kogics/kojo/lite/i18n/trInit.scala:216: not found: value epochTime
[error]   def devirAn = epochTime
[error]                 ^
[error] /Users/ben/Desktop/src/kojo/git/kojo/src/main/scala/net/kogics/kojo/lite/i18n/trInit.scala:217: not found: value epochTimeMillis
[error]   def devirAnHassas = epochTimeMillis
[error]                       ^
[error] two errors found
[error] (compile:compileIncremental) Compilation failed
[error] Total time: 15 s, completed Dec 9, 2020 11:32:04 AM
 */

// epochTime - The difference, measured in seconds, between the current time and midnight, January 1, 1970 UTC.
// epochTimeMillis - The difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
def devirBuSaniye: Kesir = epochTime
def devirBuAn: Uzun = epochTimeMillis

// from e2.kojo. see the faster method..
type Num = İriSayı
def fib1(n: Int): Num = if (n < 2) n else fib1(n - 1) + fib1(n - 2)

val s1 = devirBuSaniye
val a1 = devirBuAn
// CAREFUL! fib1 is slow!!! 33 is ok (0.5 sec). 50 is too long...
// repeated runs go down to 200ms
// 37 -> 1.3s
// 38 2s
// 39 3s
// 40 5s
// 41 8s
// 42 13s
// 43 22s
// 44 35s
// 45 1 minute!
çıktıyıSil
val n = 45
satıryaz(s"Başlıyoruz. $n! hesabı ne kadar zaman alacak? ...")
val qed = (for(i <- 1 to n) yield(fib1(i))).filter(_ % 2 == 0).sum
val s2 = devirBuSaniye
val a2 = devirBuAn
satıryaz(a2-a1)
satıryaz(s2-s1)
