tanım be(x: Any) = x
// == <=> should
dez x = 1
x == be(1)
den y = 2
y *= 2
y == be(4)
dez deneme: İkil = eğer (y < x) { yanlış } yoksa { doğru }
deneme == be(doğru)
tanım t1(söz: Yazı): Sayı = söz eşle {
    durum "merhaba" => 1
    durum "dünya" => 2
    durum _ => 3
}
t1("merhaba") == be(1)
t1("dünya") == be(2)
t1("foo bar") == be(3)