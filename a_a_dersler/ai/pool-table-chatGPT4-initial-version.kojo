// Bouncing Ball Kojo Simulation
// https://chat.openai.com/share/f4bc04e7-a1b6-4f42-a30c-d5b99aaebc8a

// Ben: Write a kojo program that simulates a bouncing ball

// ChatGPT: Creating a Kojo program to simulate a bouncing ball
//   involves using basic programming concepts such as loops, 
//   conditionals, and variables to control the animation. 
//   Here's a basic example of how you could write such a program in Kojo. This program assumes you have a basic understanding of how Kojo's canvas and coordinate system work.

//   The program makes a ball bounce within the Kojo window, 
//   reversing its direction when it hits the edges of the window.
//   We'll use variables to keep track of the ball's position and
//   direction of movement.

// Ball position variables
var x = canvasWidth / 2
var y = canvasHeight / 2

// Ball movement speed variables
var dx = 2 // Change in x (speed in the horizontal direction)
var dy = 2 // Change in y (speed in the vertical direction)

// Ball size
val ballSize = 20

// Setup the canvas
cleari()
setBackground(white)

// Animation loop
animate {
  // Clear the previous frame
  cleari()
  
  // Draw the ball at the new position
  drawFilledCircle(x, y, ballSize, blue)

  // Update the ball's position
  x += dx
  y += dy

  // Check for collision with the left or right walls
  if (x <= ballSize || x >= canvasWidth - ballSize) {
    dx = -dx // Reverse the horizontal direction
  }

  // Check for collision with the top or bottom walls
  if (y <= ballSize || y >= canvasHeight - ballSize) {
    dy = -dy // Reverse the vertical direction
  }
}

/* ChatGPT:
This program defines initial values for the ball's position and speed. 
It uses an animate block to continuously move the ball by updating its 
position based on the speed variables (dx and dy). When the ball collides 
with the edges of the window (canvasWidth and canvasHeight define the 
window boundaries), the program reverses the ball's direction by negating 
dx or dy.

The drawFilledCircle function is used to draw the ball on the canvas at
its current position, and cleari() is called at the beginning of each
animation cycle to clear the canvas before the next position of the ball
is drawn. This creates the animation effect.

You can adjust the dx, dy, and ballSize variables to change the speed and
size of the ball.
*/

// Ben: Now, add gravity

/* ChatGPT: 
To simulate gravity in the bouncing ball program, we'll modify the vertical
movement to include a gravity effect. The ball will accelerate downwards as
it falls, simulating gravity. We'll also introduce a damping factor to
simulate the loss of energy when the ball bounces off the ground, making
each subsequent bounce a little lower than the previous one.

Here's how you can modify the program to include gravity and damping:
<See kojo file named add-gravity-initial-version.kojo>
*/
