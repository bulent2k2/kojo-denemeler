// There are 1786 common English words in this file:
// yükle words

// here are all the triangle words
val tAllWords = List("A", "ABILITY", "ABOVE", "ACCOMPANY", "ACHIEVEMENT", "AGENCY", "AGREE", "AIR", "ALREADY", "AN", "ANCIENT", "APPARENT", "APPOINT", "APPROACH", "ASSUME", "AT", "ATMOSPHERE", "BAG", "BAND", "BANK", "BAR", "BEAT", "BELONG", "BENEATH", "BONE", "BOTH", "BRIDGE", "BUILDING", "BURN", "CALL", "CAPACITY", "CAREFUL", "CASE", "CHILD", "CIVIL", "CLOSELY", "COME", "CONFIDENCE", "CONFIRM", "CONSERVATIVE", "CONSTRUCTION", "CONTENT", "COULD", "CURRENTLY", "DECISION", "DEFINITION", "DEMOCRATIC", "DEPUTY", "DESPITE", "DISTINCTION", "EAST", "EDGE", "EDUCATIONAL", "EFFECT", "EQUIPMENT", "EVENT", "FACE", "FAIL", "FAMILY", "FEEL", "FIELD", "FIGURE", "FLOOR", "FREEDOM", "FUND", "FUTURE", "GENTLEMAN", "GREY", "GROWTH", "HAIR", "HAPPY", "HAVE", "HERE", "HIS", "IF", "INCIDENT", "INCREASED", "INCREASINGLY", "INDIVIDUAL", "INSTRUMENT", "INTEND", "INTENTION", "IS", "LAW", "LEADER", "LEAVE", "LENGTH", "LESS", "LITTLE", "LOVELY", "MAN", "MATCH", "MERELY", "MILK", "MISTAKE", "MOVE", "MUCH", "NEED", "NOTICE", "OBJECT", "OBJECTIVE", "OF", "OIL", "ONLY", "OTHER", "OURSELVES", "PART", "PASS", "PATH", "PERFORM", "PRISON", "PRIVATE", "PROBABLY", "PROCEDURE", "QUALITY", "QUESTION", "RANGE", "READ", "REAL", "RELIEF", "REMOVE", "REPRESENT", "REQUEST", "RESPOND", "RIDE", "SAMPLE", "SAY", "SEAT", "SECURITY", "SINGLE", "SKY", "SOIL", "SOLICITOR", "SONG", "SOUTHERN", "SPIRIT", "START", "SUGGESTION", "TALL", "TAX", "THEORY", "THREATEN", "THROUGHOUT", "TITLE", "TOOTH", "TOTALLY", "TRAVEL", "TYPE", "UNABLE", "UNDERSTAND", "UPON", "USE", "VARIOUS", "VARY", "VIDEO", "WAGE", "WARM", "WATCH", "WE", "WHILST", "WIDELY", "WOMAN")

/*
println(words.size)
println(words(0), words(words.size - 1))
// longest: ADMINISTRATION
val max = words.maxBy(_.size).size
println(max) // 14 letters => 14 * 26 = 364 => we don't need triangle numbers beyond 400
*/

// the first 30 triangle numbers suffice:
val tri = (1 to 30).map(n => n*(n+1)/2).toSet
def istri(n: Int) = tri.contains(n)

assert(List(1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78, 91, 105, 120, 136, 153, 171, 190, 210, 231, 253, 276, 300, 325, 351, 378, 406, 435, 465
).forall(istri), "unit test")
assert(List(2,4,5,11,400,500).forall(!istri(_)),"unit test2")

val a = 'A'.toInt
def value(word: String): Int = word.map(c => c.toInt - a + 1).sum

val tWords = List("A", "BOOK", "XYZ", "CXYZ", "ABILITY", "ABOVE", "ADMINISTRATION")
println(tWords.filter(w => istri(value(w))).size)