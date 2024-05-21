
özellik İşleYöntemli[A[_]] {
    tanım map[B, C](f: B => C): A[C] = işle(f)
    tanım işle[B, C](işlev: B => C): A[C]
}

özellik DüzİşleYöntemli[A[_]] {
    tanım flatMap[B, C](f: B => A[C]): A[C] = düzİşle(f)
    tanım düzİşle[B, C](işlev: B => A[C]): A[C]
}

özellik İşleYöntemleri[B, A[_]] {
    tanım map[C](f:      B => C): A[C] = işle(f)
    tanım işle[C](işlev: B => C): A[C]

    tanım flatMap[C](f:     B => A[C]): A[C] = düzİşle(f)
    tanım düzİşle[C](işlev: B => A[C]): A[C]
}

// bu olmadı henüz...
özellik İşleYöntemleri2[B, A[_]] {
    tür Bu[C] = İşleYöntemleri2[C, A]
    tanım map[C](f:      B => C): Bu[C] = işle(f)
    tanım işle[C](işlev: B => C): Bu[C]

    tanım flatMap[C](f:     B => Bu[C]): Bu[C] = düzİşle(f)
    tanım düzİşle[C](işlev: B => Bu[C]): Bu[C]
}
