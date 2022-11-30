// #yükle io/io

def swap(line: String, fromTo: (String, String)) = line.replace(fromTo._1, fromTo._2)
def swapKeywords(lines: List[String]) = for (line <- lines) yield swap(
    swap(
        swap(
            swap(
                swap(
                    swap(
                        swap(
                            swap(
                                swap(
                                    swap(
                                        swap(
                                            swap(
                                                swap(
                                                    swap(
                                                        swap(
                                                            swap(
                                                                swap(
                                                                    swap(
                                                                        swap(
                                                                            line,
                                                                            "val " -> "dez "),
                                                                        "var " -> "den "),
                                                                    "def " -> "tanım "),
                                                                "if " -> "eğer "),
                                                            "else " -> "yoksa "),
                                                        "for " -> "için "),
                                                    "yield " -> "ver "),
                                                "while " -> "yineleDoğruKaldıkça "),
                                            "do " -> "yap "),
                                        "class " -> "sınıf "),
                                    "case " -> "durum "),
                                "extends " -> "yayar "),
                            "new " -> "yeni "),
                        "match " -> "eşle "),
                    "true " -> "doğru "),
                "false " -> "yanlış "),
            "override " -> "baskın "),
        "type" -> "tür"),
    "import" -> "getir")

/*
 val lines = input.fromFile("/Users/ben/tmp/tmp.kojo")
 output.toFile(swapKeywords(lines))("/Users/ben/tmp/tmp2.kojo")
 */
