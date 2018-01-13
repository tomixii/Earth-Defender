import processing.core._

class Ball(p: PApplet, delta: Double) {
  
  val height = (30f * delta).toFloat
  val width = (30f * delta).toFloat
  var img = new PImage
  var angle = 270f
  val imgRadius = width / 2 //kuvan säde
  val radius = 180f * delta.toFloat //kiertoradan säde
  var x: Float = (p.width / 4 - width / 4 + Math.cos(angle.toRadians) * (radius )).toFloat
  var y: Float = (p.height / 4 - height / 4 + Math.sin(angle.toRadians) * (radius )).toFloat
  
  def invisible() = { // muuttaa pallon näkymättömäksi siirtämällä sen ulos ikkunasta
    p.image(img, -500, -500, width, height)
  }

  //päivittää pallon sijainnin myötäpäivään ympyräradalla
  def update() = {
    angle += 5f
    x = (p.width / 2 + Math.cos(angle.toRadians) * (radius) * 2).toFloat
    y = (p.height / 2 + Math.sin(angle.toRadians) * (radius) * 2).toFloat
    p.image(img, x - width / 2, y - width / 2, width, height)
  }
}