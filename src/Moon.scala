
import processing.core._

class Moon(p: PApplet, delta: Double) {
  
  var height = (45f*delta).toFloat
  var width = (45f*delta).toFloat
  var img = new PImage
  var angle = 360f //mistä kuu aloittaa 
  var imgRadius = width/2 //kuun kuvan säde
  val radius = 60f*delta.toFloat//kiertoradan säde
  var rightIsPressed = false//painetaanko oikeaa
  var leftIsPressed = false// vai vasenta
  var x: Float = (p.width/4 - width/4 + Math.cos(angle.toRadians)*(radius)).toFloat
  var y: Float = (p.height/4 - height/4 + Math.sin(angle.toRadians)*(radius)).toFloat
  var nvk: Double = 0 //kerroin, joka määrittää kuun kiihtyvyyden
  var growing = false //jos nuolinäppäin pohjassa nii kuu kiihtyy tiettyyn pisteeseen asti
  var integer = 1     //vaihtopowerupin kerroin, joka määrittää kumpaan suuntaa nuolinäppäimet liikuttaa kuuta
  
  //päivittää kuun sijainnin joko myötä- tai vastapäivään
  def update() = {
    if(growing)
      nvk += 100
    else
      nvk = 0
	  if(rightIsPressed){
      angle += integer * ((8 + (3 - 300/(100.0 + nvk)))*delta).toFloat //päivittää kuun kulman ympyräradalla
    }
  
    if(leftIsPressed){
	    angle -= integer * ((8 + (3 - 300/(100.0 + nvk)))*delta).toFloat //päivittää kuun kulman ympyräradalla
    }
    
    x = (p.width/2 + Math.cos(angle.toRadians)*(radius)*2).toFloat //päivittää kuun koordinaatit ympyräradalla
    y = (p.height/2 + Math.sin(angle.toRadians)*(radius)*2).toFloat
		p.image(img, x - width/2, y - width/2 , width, height)	//piirtää kuun 
  }
}