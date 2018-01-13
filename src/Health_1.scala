import processing.core._

class Health_1(p: PApplet, img: PImage, delta: Double, x_0: Float, y_0: Float, speed: Int) extends Projectile(p, delta, x_0, y_0, speed){
  
  def hitsMoon(): Int = 2 //palauttaa luvun joka kertoo mitä tapahtuu
  
  def hitsEarth(): Int = 6 //palauttaa luvun joka kertoo mitä tapahtuu
  
  def display = { //näyttää päivittämättä asteroidin kun menu päällä
    p.image(img, this.location.x - this.width/2, this.location.y - this.height/2, this.width, this.height)
  }
  
  def update() = {
    velocity.normalize() // "normalisoidaan", eli luodaan yksikkövektori
    velocity.mult(speed)     // kasvatetaan nopeusvektoria niin paljon ku tarvii
    location.sub(velocity)
    p.image(img, this.location.x - this.width/2, this.location.y - this.height/2, this.width, this.height) //päivittää healthin paikan
  }
}