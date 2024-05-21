// See: ./pool-table*

// Ball size
val ballSize = 20

// Ball position variables
var x = 0.0
var y = -canvasBounds.y - 2*ballSize

// Ball movement speed variables
var dx = 2.0 // Change in x (speed in the horizontal direction)
var dy = 0.0 // Initial vertical speed is 0

// Gravity and damping
val gravity = 0.5 // Acceleration due to gravity
val damping = 0.95 // Energy loss on bounce

// Setup the canvas
cleari()
setBackground(yellow)

// Missing definition
def drawFilledCircle(x: Double, y: Double, ballSize: Double, color: Color) = {
    val ball = trans(x, y) * fillColor(color) -> Picture.circle(ballSize)
    draw(ball)
    ball
}

val ball = drawFilledCircle(x, y, ballSize, blue)

// Animation loop
animate {
  // Update the ball's position
  x += dx
  y += dy
  ball.setPosition(x, y)
  
  // Apply gravity
  dy -= gravity

  // Check for collision with the left or right walls
  if (x <= canvasBounds.x + ballSize || x >= canvasBounds.getMaxX - ballSize) {
    dx = -dx // Reverse the horizontal direction
  }

  // Check for collision with the ground
  if (y <= canvasBounds.y + ballSize) {
    y = canvasBounds.y + ballSize // Place the ball on the ground
    dy = -dy * damping // Reverse the vertical direction and apply damping
  }

  // Optionally, check for collision with the ceiling
  if (y >= canvasBounds.getMaxY - ballSize) {
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


