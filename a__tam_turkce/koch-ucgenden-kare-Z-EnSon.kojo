// özyinelemeye güzel bir örnek
// koch üçgeninden karemsi bir altıgen

/*
    derinlik: özyineleme derinliği (order of recursion)
    uzunluk: en yüzeysel kenar uzunluğu
*/
dez (derinlik, uzunluk) = (4, 400)  
// anlamak için derinlik için 2 ya da 1 dene (hatta 0 da olur!)

tanım kochKenar(k: Kaplumbağa, düzey: Sayı, boy: Kesir): Birim = 
    eğer (düzey == 0) k.ileri(boy) // son düzeye geldik. durma vakti!
    yoksa {
        // her kenarı dört küçük kenarın bileşkesi olarak tanımlıyoruz!
        tanım küçüğüm = kochKenar(k, düzey-1, boy/3)
        tanım çizVeDön = { küçüğüm; k.sol(60) }
        çizVeDön // 1. kenar
        çizVeDön // 2. kenar
        k.sağ(180) // sağa 120 derece dönmeye denk olsun diye!
        çizVeDön // 3. kenar
        küçüğüm  // 4. kenar
    }

silVeSakla
dez ortalama = -uzunluk/3
dez k = yeniKaplumbağa(ortalama, ortalama * tuvalAlanı.eni / tuvalAlanı.boyu)
tanım kenar = kochKenar(k, derinlik, uzunluk)
k.hızıKur(orta)
dez renk = Renkler.altınbaşak
k.boyamaRenginiKur(renk)
k.kalemRenginiKur(renk)
tanım çizVeDön = { kenar; k.sağ(120) }
// artık çizmeye hazırız!
yinele(3) { çizVeDön }
// anlamadıysan, 2 ya da sadece 1 kere yinelemeyi dene
k.gizle