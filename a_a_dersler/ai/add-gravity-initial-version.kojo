// First read: ./pool-table*

// Ball position variables
var x = canvasWidth / 2
var y = canvasHeight / 2

// Ball movement speed variables
var dx = 2 // Change in x (speed in the horizontal direction)
var dy = 0 // Initial vertical speed is 0

// Ball size
val ballSize = 20

// Gravity and damping
val gravity = 0.1 // Acceleration due to gravity
val damping = 0.8 // Energy loss on bounce

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
  
  // Apply gravity
  dy += gravity

  // Check for collision with the left or right walls
  if (x <= ballSize || x >= canvasWidth - ballSize) {
    dx = -dx // Reverse the horizontal direction
  }

  // Check for collision with the ground
  if (y >= canvasHeight - ballSize) {
    y = canvasHeight - ballSize // Place the ball on the ground
    dy = -dy * damping // Reverse the vertical direction and apply damping
  }

  // Optionally, check for collision with the ceiling
  if (y <= ballSize) {
    dy = -dy * damping // Apply damping when hitting the ceiling
  }
}

/* ChatGPT:
In this version, dy starts at 0, and gravity is added to dy on each animation 
cycle to simulate acceleration due to gravity. When the ball hits the ground 
(canvasHeight - ballSize), the vertical speed dy is negated and multiplied by
the damping factor to simulate the energy loss from the bounce. The damping 
factor is less than 1, causing the ball to bounce lower each time.

This program will create a more realistic bouncing ball effect by simulating
gravity and damping, making each bounce less high than the previous one.
*/


