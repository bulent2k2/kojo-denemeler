val fuzzers: Seq[Int => Option[String]] =
    ((x: Int) => if (x % 3 == 0) Some("Fizz") else None) ::
        ((x: Int) => if (x % 5 == 0) Some("Buzz") else None) ::
        Nil

def fizzbuzz(number: Int): String =
    fuzzers.map(_(number)).foldLeft[Option[String]](None) {
        case (None, z)          => z
        case (z, None)          => z
        case (Some(z), Some(x)) => Some(z + x)
    }.getOrElse(s"$number")

(1 to 100).map(fizzbuzz).foreach(println)
