sil()
çıktıyıSil()
def fibo(n: Sayı): Sayı = 
    if (n < 2) 1 else fibo(n - 1) + fibo(n - 2)

hızıKur(hızlı /*orta*/ )
kalemRenginiKur(mavi)
kalemKalınlığınıKur(10)
yaklaş(0.20)
atla(800, 500)
yineleDizinli(16) { i =>
    val boy = fibo(i - 1)
    yaz(s"$boy ")
    kalemRenginiKur(rastgeleRenk)
    ileri(3 * boy)
    if (i > 6) kalemKalınlığınıKur(30)
    //yazı(boy)
    sağ(90)
}
kalemKalınlığınıKur(2)
geri(50)
yazıBoyunuKur(150)
kalemRenginiKur(kırmızı)
yazı("Durduk!")