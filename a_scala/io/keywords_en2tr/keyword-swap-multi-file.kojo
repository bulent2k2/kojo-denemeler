// #yükle keyword-swap

// continued in do2.kojo ... do10.kojo
/* don't do: 
  "arduino-prog.kojo",
  "composing-music.kojo",
  "scala-tutorial.kojo",
  */

 /* problems: 
 1- else at the end of a line
 2- words in turkish like hedef (has def in it) and duvar (has var in it)
 3- import used in instruction palette, mandelbrot, rangoli
 4- object, this, lazy, private in mandelbrot and more
 5- type in snowflake
 6- return, trait and object in tic-tac-toe
 */
for (
    file <- List(
        "addition-game.kojo",
        "angle-experiment.kojo",
        "angles.kojo",
        "car-ride.kojo",
        "circles.kojo",
        "collidium.kojo",
        "crazy-square.kojo",
        "dragon-curve.kojo",
        "estimating-pi-mc.kojo",
        "eye-effects.kojo",
        "eye.kojo",
        "face-multi.kojo",
        "fern.kojo",
        "ferris-wheel.kojo",
        "fib-tree.kojo",
        "flappy-ball.kojo",
        "genart-cubic-disarray.kojo",
        "genart-delaunay.kojo",
        "genart-hypnotic-squares.kojo",
        "genart-joy-division.kojo",
        "genart-mondrian.kojo",
        "genart-tiled-lines.kojo",
        "genart-tri-mesh.kojo",
        "hunted.kojo",
        "image-collage.kojo",
        "image-right-split.kojo",
        "instruction-palette.kojo",
        "kojo-documentation.kojo",
        "l-systems.kojo",
        "lamp-animation.kojo",
        "lamp-animation2.kojo",
        "lighted-star.kojo",
        "lunar-lander.kojo",
        "mandelbrot.kojo",
        "memory-cards.kojo",
        "multiplication-game.kojo",
        "penta-pattern.kojo",
        "physics-fma.kojo",
        "physics-uvats.kojo",
        "pong.kojo",
        "prime-factors.kojo",
        "primes.kojo",
        "radiance.kojo",
        "random-dots.kojo",
        "rangoli.kojo",
        "read-vector-bargraph.kojo",
        "read-vector-mean.kojo",
        "shapes-cols.kojo",
        "sierpinski-tri.kojo",
        "snowflake.kojo",
        "some-notes.kojo",
        "spiral-effects.kojo",
        "spiral-hexagon-tiles.kojo",
        "spiral-square-tiles.kojo",
        "spiral.kojo",
        "sprite-animation.kojo",
        "square-pattern.kojo",
        "square.kojo",
        "subtraction-game.kojo",
        "synchronized-squares.kojo",
        "synchronized-squares2.kojo",
        "tan-theta.kojo",
        "tangram-skier.kojo",
        "tic-tac-toe.kojo",
        "tree0.kojo",
        "tree1.kojo",
        "tree2.kojo",
        "tune1.kojo",
        "tune2.kojo",
        "turtle-controller.kojo",
        "two-turtle-interaction.kojo",
        "two-turtle-interaction2.kojo",
        "unit-circle.kojo",
        "widgets-canvas.kojo")
) {
    val frompath = s"/Users/ben/src/kojo/git/kojo/src/main/resources/samples/tr/$file"
    val topath = s"/Users/ben/tmp/kojo/swap/$file"
    val lines = input.fromFile(frompath)
    output.toFile(swapKeywords(lines))(topath)
}