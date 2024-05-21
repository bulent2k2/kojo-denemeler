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


// Missing definition
def drawFilledCircle(x: Double, y: Double, ballSize: Double, color: Color) = {
    val ball = trans(x, y) * fillColor(color) -> Picture.circle(ballSize)
    draw(ball)
    ball
}

// Ball position variables
var x = 0
var y = 0

// Ball movement speed variables
var dx = 4 // Change in x (speed in the horizontal direction)
var dy = 2 // Change in y (speed in the vertical direction)

// Ball size
val ballSize = 20

// Setup the canvas
cleari()
setBackground(yellow)

// Draw the ball at the initial position
val ball = drawFilledCircle(x, y, ballSize, blue)
  
// Animation loop
animate {
  // Update the ball's position
  x += dx
  y += dy
  ball.setPosition(x, y)
  
  // Check for collision with the left or right walls
  if (x <= canvasBounds.x + ballSize || x >= canvasBounds.getMaxX - ballSize) {
    dx = -dx // Reverse the horizontal direction
  }

  // Check for collision with the top or bottom walls
  if (y <= canvasBounds.y + ballSize || y >= canvasBounds.getMaxY - ballSize) {
    dy = -dy // Reverse the vertical direction
  }
}
