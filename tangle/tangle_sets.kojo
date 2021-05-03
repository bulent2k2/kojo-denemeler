/* b2 karesine her basışımızda yeni bir nokta ekleriz ve onu düzlemsel kare grid çizgesinin
 * dört kenarından birindeki noktalara bağlarız. Karenin dört kenarı olduğu için, dört yeni
 * nokta ekleyebiliriz en çok.
 * Bu dört kenarı nasıl belirleriz. Bir üçlüyle:
 *    (a,b,c) a: ilk nokta, b: son noktadan bir sonraki, c: iki nokta arasındaki adım boyu
 * İlk KNS noktanın olduğu kenara üst kenar diyelim. Ondan başlayıp saat yönünde gidelim:
 *   üst(0)/sağ(1)/alt(2)/sol(3)
 *  2x2     c: +1/+2/-1/-2                        4x4        c: +1/+4/-1/-4
 *     0 1                                           0 1 2 3
 *     2 3   => sides: 0 1/1 3/3 2/2 0               4 5 6 7
 *  3x3      c: +1/+3/-1/-3                          8 9 a b
 *     0 1 2                                         c d e f   => sides: 0-3/3-f/f-c/c-0
 *     3 4 5
 *     6 7 8 => sides: 0 1 2/2 5 8/8 7 6/6 3 0
 */

// her küme bize yeni eklenen noktanın komşularını veriyor
def kümeler(d: Int) = {
    def iç4Kenar() = {  // ilk kare çizitin kenarları
        val r1 = Vector((0, 1), (d - 1, d), (d * d - 1, -1), (d * (d - 1), -d))
        val l0 = (for ((a, c) <- (for (i <- 0 to 3) yield r1(i))) yield (a, (a + c * d), c)).toList
        for ((a, b, c) <- l0) yield (Range(a, b, c).toList)
    }
    // dış kenarı iki listeyi fermuar gibi bir araya getirerek buluyoruz
    def dış(i1: List[Int], i2: List[Int]) = {
        val l3 = i2.zip(i1.tail ::: List(i1.head)).flatMap(p => List(p._1, p._2))
        val l4 = l3 ::: List(l3.head)
        for (i <- 0 to 7 by 2) yield (l4.drop(i).take(3))
    }
    val d2 = d * d
    val l1 = List(0, d - 1, d2 - 1, d2 - d)
    val l2 = List(d2, d2 + 1, d2 + 2, d2 + 3)
    val l3 = l2.map(_ + 4)
    val l4 = l3.map(_ + 4)
    val l5 = l4.map(_ + 4)
    List(iç4Kenar, dış(l1, l2), dış(l2, l3), dış(l3, l4), dış(l4, l5), dış(l5, l5.map(_ + 4))).flatMap(_.toList)
}
clearOutput
val d = 4
var i = d * d // ilk eklenen noktanın dizin nosu
kümeler(d).map(l => l.mkString("", " ", "")).foreach(s => { println(f"$s%11s -> n$i"); i += 1 })

/*
 * d=2
 *
 *       15
 *
 *    11  4  8
 *       0 1
 * 14  7     5  12
 *       2 3
 *    10  6  9
 *
 *       13
 *
 *      0 1 -> n4
 *      1 3 -> n5
 *      3 2 -> n6
 *      2 0 -> n7
 *    4 1 5 -> n8
 *    5 3 6 -> n9
 *    6 2 7 -> n10
 *    7 0 4 -> n11
 *    8 5 9 -> n12
 *   9 6 10 -> n13
 *  10 7 11 -> n14
 *   11 4 8 -> n15
 *  12 9 13 -> n16
 * 13 10 14 -> n17
 * 14 11 15 -> n18
 *  15 8 12 -> n19
 * 16 13 17 -> n20
 * 17 14 18 -> n21
 * 18 15 19 -> n22
 * 19 12 16 -> n23
 * 20 17 21 -> n24
 * 21 18 22 -> n25
 * 22 19 23 -> n26
 * 23 16 20 -> n27
 *
*/

/* d=3  (bu örnekle çıktı kümelerin formülü!)
 *       9
 *     0 1 2
 *  12 3 4 5 10
 *     6 7 8
 *      11
 *
 * yeni bir kare oldu. Köşeleri: 9,10,11,12
 * dört nokta daha: d2 = d*d
 *   9  2 10         d2   d-1  d2+1
 *   10 8 11         d2+1 d2-1 d2+2
 *   11 6 12         d2+2 d2-d d2+3
 *   12 0 9          d2+3 0    d2
 *
 * yeni noktalar: 9,10,11,12 (genel olarak d2, d2+1, d2+2, d2+3)
 * ara noktalar: 2,8,6,0     (genel olarak d-1, d2-1, d2-d, 0)
 *
 *  16   9   13
 *     0 1 2
 *  12 3 4 5 10
 *     6 7 8
 *  15  11   14
 *
 * Yeni kenarlar:
 *   13 10 14      d2+4 d2+1 d2+5
 *   14 11 15      d2+5 d2+2 d2+6
 *   15 12 16      d2+6 d2+3 d2+7
 *   16  9 13      d2+7 d2   d2+4
 *
 * Bir daha:
 *             20
 *         16   9   13
 *            0 1 2
 *      19 12 3 4 5 10 17
 *            6 7 8
 *         15  11   14
 *             18
 *
 *    0 1 2 -> n9
 *    2 5 8 -> n10
 *    8 7 6 -> n11
 *    6 3 0 -> n12
 *   9 2 10 -> n13
 *  10 8 11 -> n14
 *  11 6 12 -> n15
 *   12 0 9 -> n16
 * 13 10 14 -> n17
 * 14 11 15 -> n18
 * 15 12 16 -> n19
 *  16 9 13 -> n20
 * 17 14 18 -> n21
 * 18 15 19 -> n22
 * 19 16 20 -> n23
 * 20 13 17 -> n24
 * 21 18 22 -> n25
 * 22 19 23 -> n26
 * 23 20 24 -> n27
 * 24 17 21 -> n28
 * 25 22 26 -> n29
 * 26 23 27 -> n30
 * 27 24 28 -> n31
 * 28 21 25 -> n32
 *
 */

/* d=4
 *    23   16   20
 *      0 1 2 3
 *      4 5 6 7 17
 *   19 8 9 a b
 *      c d e f
 *   22    18   21
 *
 *     0 1 2 3 -> n16
 *   3 7 11 15 -> n17
 * 15 14 13 12 -> n18
 *    12 8 4 0 -> n19
 *     16 3 17 -> n20
 *    17 15 18 -> n21
 *    18 12 19 -> n22
 *     19 0 16 -> n23
 *    20 17 21 -> n24
 *    21 18 22 -> n25
 *    22 19 23 -> n26
 *    23 16 20 -> n27
 *    24 21 25 -> n28
 *    25 22 26 -> n29
 *    26 23 27 -> n30
 *    27 20 24 -> n31
 *    28 25 29 -> n32
 *    29 26 30 -> n33
 *    30 27 31 -> n34
 *    31 24 28 -> n35
 *    32 29 33 -> n36
 *    33 30 34 -> n37
 *    34 31 35 -> n38
 *    35 28 32 -> n39
 * 
 */
