// türkçe klavye yoksa buradan aşırabilirsin
val harfler = """
ç
ğ
ı
ö
ş
ü
"""
harfler.map(_.toUpper) // "Ç Ğ I Ö Ş Ü" // büyük i işleri karıştırdı
val h2 = 
"""
Ç
Ğ
İ
Ö
Ş
Ü
"""
