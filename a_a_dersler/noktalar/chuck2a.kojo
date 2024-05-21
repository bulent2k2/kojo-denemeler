clear
zoomXY(0.2, 0.2, 0, 400)
setSpeed(medium)
repeat(100) {
  setPenColor(randomColor)
  forward(10)
  dot(random(10, 200))
}

/* Get to it slowly by starting with
   dot(100)
then
   forward(50)
then
   dot(100)
   forward(50)
   dot(100)
run it a few times, then
   clear
   dot(100)
   forward(50)
   dot(100)
then add
   setPenColor(green)
then add
   setPenColor(randomColor)
   forward(50)
   dot(100)
to get:
 */
/*
clear
dot(100)
forward(50)
setPenColor(green)
dot(100)
setPenColor(randomColor)
forward(50)
dot(100)
 */
/* Then, remove lines 2-5 and add repeat:
clear
repeat (3) {
  setPenColor(randomColor)
  forward(50)
  dot(100)
}
 */
