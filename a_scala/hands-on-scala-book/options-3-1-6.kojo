// Chapter 3: Basic Scala Page 6/15
def hello(title: String, firstName: String, lastNameOpt: Option[String]) = {
    lastNameOpt match {
        case Some(lastName) => println(s"Hello $title. $lastName")
        case None => println(s"Hello $firstName")
    }
}

hello("Mr", "Baray", None)
hello("Dr", "Bulent", Some("Basaran"))
