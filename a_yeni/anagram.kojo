// #include load-dictionary
// Now in: ~/src/kojo/git/kojo/installer/examples/anagram/anagram.kojo.installed
// Anagrams in 42 lines! -- first class of funprog with scala, for comprehensions
// From: ~/Desktop/COPYA/src/scala/course/forcomp/src/main/scala/forcomp/Anagrams.scala
type Word = String
type Sentence = List[Word]
type Occurrences = List[(Char, Int)]
val dictionary: List[Word] = loadDictionary
def wordOccurrences(w: Word): Occurrences = w.groupBy(_.toLower).map { case (k, v) => (k, v.size) }.toList.sorted
def sentenceOccurrences(s: Sentence): Occurrences = wordOccurrences(s.mkString)
lazy val dictionaryByOccurrences: Map[Occurrences, List[Word]] = dictionary.groupBy(wordOccurrences(_)) withDefaultValue Nil
def wordAnagrams(word: Word): List[Word] = dictionaryByOccurrences(wordOccurrences(word))
def combinations(occurrences: Occurrences): List[Occurrences] = {
    def singleCase(occ: (Char, Int)): List[Occurrences] = List() :: (occ match {
        case (char, count) => (for (n <- 1 to count) yield List(char -> n)).toList
    })
    occurrences match {
        case Nil         => List(List())
        case head :: Nil => singleCase(head)
        case head :: tail => for {
            elem <- singleCase(head)
            subset <- combinations(tail)
        } yield elem ++ subset
    }
}
def subtract(x: Occurrences, y: Occurrences): Occurrences =
    y.foldLeft(x.toMap) { (map, pair) =>
        pair match { // pair is an element of the list (y) that is being folded over
            case (char, count) => {
                val now = map.apply(char)
                if (now == count) map - char else map updated (char, now - count)
            }
        }
    }.toList.sorted
def sentenceAnagrams(sentence: Sentence): List[Sentence] = {
    def os2ana(os: Occurrences): List[Sentence] =
        os match {
            case Nil => List(Nil)
            case _ => for {
                comb <- combinations(os)
                word <- dictionaryByOccurrences(comb)
                sentence <- os2ana(subtract(os, comb))
            } yield word :: sentence
        }
    os2ana(sentenceOccurrences(sentence))
}

clearOutput()
for (sentence <- List(List("hello", "dolly"))) {
  val ana = timeit { sentenceAnagrams(sentence) }
  println(sentence.mkString(
    "Sentence: ", " ", s"\nhas ${ana.length} anagrams: ${ana.map(_.mkString(" ")).mkString(", ")}"
  ))
}
