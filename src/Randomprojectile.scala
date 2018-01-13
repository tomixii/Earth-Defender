import processing.core._
import scala.util.Random

class Randomprojectile(p: PApplet, img: PImage, delta: Double, x_0: Float, y_0: Float, speed: Int) extends Projectile(p, delta, x_0, y_0, speed) {
  
	def hitsMoon(): Int = {
		val rndm = Random.nextInt(100)
		if(rndm <= 20) 4 else if(rndm <= 60) 3 else 5 //valitsee mikä powerup tulee mysteripallosta
	}
  
	def hitsEarth(): Int = {
	  val rndm = Random.nextInt(100)
	  if(rndm <= 50) 4 else if(rndm <= 75) 3 else 5 //valitsee mikä powerup tulee mysteripallosta
	}
  

  def display = { //näyttää päivittämättä mysteeripallon kun menu päällä
    p.image(img, this.location.x - this.width / 2, this.location.y - this.height / 2, this.width, this.height)
  }

  def update() = {
    velocity.normalize() // "normalisoidaan", eli luodaan yksikkövektori
    velocity.mult(speed) // kasvatetaan nopeusvektoria niin paljon ku tarvii
    location.sub(velocity)
    p.image(img, this.location.x - this.width / 2, this.location.y - this.height / 2, this.width, this.height) //päivittää mysteeripallon paikan
  }

  
}