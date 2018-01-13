import processing.core._

abstract class Projectile(p: PApplet, delta: Double, x_0: Float, y_0: Float, speed: Int) {
  
  //x_0, y_0 on koordinaatit spawnauspisteessä
  
  val height = (40f*delta).toFloat  //kohti tulevan asian korkeus ja leveys
  val width = (40f*delta).toFloat
  val radius = width/2 //asian säde
  
  val w = (1030 * delta).toInt //ikkunan leveys ja korkeus
  val h = (1030 * delta).toInt
  
  var location = new PVector(x_0, y_0) //vektori origosta spawnauspisteeseen, pysyy vakiona
  var velocity = new PVector(x_0, y_0) //vektori origosta spawnauspisteeseen, jota lähetään muuttaan
  var center = new PVector(w/2, h/2)   //vektori origosta keskipisteeseen
  
  velocity.sub(center) // alkupisteen vektori miinus(eli sub) keskipisteen vektori,saadaan vektori alkupisteestä keskipisteesen
  
  def update()
  
  def display 
  
  def hitsMoon(): Int
  
  def hitsEarth(): Int
}