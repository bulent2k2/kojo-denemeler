// #include writeToFile

// new io code in: ~/src/kojo/a_scala/io/io.kojo

// Now in: ~/src/kojo/git/kojo/installer/examples/anagram/load-word-list.kojo
/* word lists are also there:
 ~/src/kojo/git/kojo/installer/examples/anagram/load-word-list.kojo
 ~/src/kojo/git/kojo/installer/examples/anagram/words.txt
 ~/src/kojo/git/kojo/installer/examples/anagram/tr/sozcuk-dizinini-yukle.kojo
 ~/src/kojo/git/kojo/installer/examples/anagram/tr/sozcukler.txt
 */

/* see:
   ~/Desktop/COPYA/src/scala/scala-master/forcomp/src/main/scala/common/package.scala
   ~/Desktop/COPYA/src/scala/scala-master/forcomp/src/main/scala/forcomp/package.scala
*/
def sözcükDizininiYükle: List[String] = {
  // val path = "/Users/ben/src/kojo/a_yeni/sozluk_a.txt"
  // val path = "/Users/ben/src/git/Turkce-Kelime-Listesi/turkce_kelime_listesi.txt"
  val path = "/Users/ben/src/git/Turkce-Kelime-Listesi/turkce_sozcuk_dizini.txt"
  val l = _doIt_(path)
  l
  /* 
  val l2 = l.filter(word => word.size > 1 && !word.contains(" "))  // filter out single letters and multiple words like "köy yeri"
  val l3 = l2.filter(_.charAt(0).isLower).distinct // filter out Kr and other element symbols
  puts.writeToFile(l3)("/Users/ben/src/git/Turkce-Kelime-Listesi/test2")
  l3
   */
}
def loadDictionary: List[String] = {
    val path = "/Users/ben/Desktop/COPYA/src/scala/scala-master/forcomp/src/main/resources/forcomp/linuxwords.txt"
    _doIt_(path)
}
def _doIt_(path: String) = {
    val dictFile = new java.io.File(path)
    if (dictFile.exists) {
        val istream = new java.io.FileInputStream(dictFile)
        try {
          val s = scala.io.Source.fromInputStream(istream)
          s.getLines.toList // return value here
        }
        catch {
            case e: Exception =>
                println("Could not load word list" + e)
                throw e
        }
        finally {
            istream.close
        }
    }
    else {
        throw new Exception("Could not open dictFile at " + path)
    }
}

def testLoadDictionary() = {
    val dict = loadDictionary
    println(s"There are ${dict.length} words in the dictionary.")
    println(s"First ten: ${dict take 10 mkString(", ")} and last ten: ${dict takeRight 10 mkString(", ")}")
}
//testLoadDictionary()
