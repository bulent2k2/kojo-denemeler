// #include io

val lines = input.fromFile("/Users/ben/.zshrc")
lines.size
lines.take(1)
lines.drop(19)

// relative to installation path:
//   Writing to /Applications/Kojo Learning Environment.app/fooBar.txt
// or for my local build:
//   /Users/ben/src/kojo/git/kojo/fooBar.txt
output.toFile(lines)("fooBar.txt")