// en meşhur matematiksel fonksiyon, yani işlevlerden birini çizelim.
// Quadratic, yani ikinci dereceden polinom yani neydi Türkçesi? 
sil
yaklaşXY(0.9, 0.5, 60, 400)
tanım eğri(x: Kesir) = 0.01 * x * x - 0.5 * x - 10
gridiGöster(); eksenleriGöster()
dez aralık = 200
atla(-aralık,eğri(-aralık))
hızıKur(orta)
için(x <- -aralık+10 |-| aralık+100; eğer (x % 10 == 0)) noktayaGit(x, eğri(x))










