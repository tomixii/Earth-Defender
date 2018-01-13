import processing.core._

class Earth(p: PApplet, delta: Double) {
  
  var img = new PImage
  val height: Float = (100f*delta).toFloat //maapallon leveys ja korkeus
  val width: Float = (100f*delta).toFloat 
  var x: Float = p.width/2 - width/2 //pistää maapallon kekselle ruutua
	var y: Float = p.height/2 - height/2 
	val radius = width/2
  var hit = false //booleani joka tietää onko maahan osuttu
  var stepper = 0 //auttaa maahan osumisen animaation toteutuksessa

  //näyttää maapallon keskellä ruutua
  def display() = { 
	  x = p.width/2 - width/2
	  y = p.height/2 - height/2
	  p.image(img,x, y, this.width, this.height)
	  hit = stepper != 0 
	  if(hit){ //lyhyt animaatio jos asteroidi osuu maahan
	    p.fill(255, 255, 255, math.max(stepper,0))
	    p.noStroke()
		  p.ellipse(x + this.width/2, y + this.height/2 ,this.width,this.height)
		  stepper -= 5
	  }
  } 
}