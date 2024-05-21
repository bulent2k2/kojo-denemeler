sil
dez uzunluk = 12 // daha irili ve ufaklı sayılar dene
kalemKalınlığınıKur(5*uzunluk)
hızıKur(çokHızlı)
artalanıKur(rastgeleRenk) // beyaz ya da siyah dene
dez (sol, alt) = (tuvalAlanı.x.sayıya, tuvalAlanı.y.sayıya)
yinele(300) {  // try more or less brush strokes
    kalemRenginiKur(Renk(rastgele(180), rastgele(180), rastgele(180), rastgele(100) + 100))
    konumuKur(rastgele(-2*sol) + sol, rastgele(-2*alt) + alt)
    ileri(uzunluk)
}
// kaplumbağa görünmesin istersen:
// gizle