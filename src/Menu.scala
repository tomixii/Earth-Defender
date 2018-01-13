import processing.core._
import scala.collection.mutable.Buffer

class Menu(p: PApplet, delta: Double) {
  val buttonWidth = (1030*delta/3).toFloat  //isomman napin leveys
  val buttonHeight = (1030*delta/8).toFloat //isomman napin korkeus
  val smallButtonWidth = (1030*delta/8).toFloat //pienemmän napin leveys
  val smallButtonHeight = (1030*delta/8).toFloat//pienemmän napin korkeus
 
  var soundsOn = true //tietää onko sovellus mutella
  var gameIsOn = false//tietää onko peli käynnissä
  
  //luo tarvittaville kuville muuttujat
  var logoimg       = new PImage
  var playimg       = new PImage
  var helpimg       = new PImage
  var exitimg       = new PImage
  var resumeimg     = new PImage
  var restartimg    = new PImage
  var menuimg       = new PImage
  var menubuttonimg = new PImage
  var muteimg       = new PImage
  var unmuteimg     = new PImage
  var backimg       = new PImage
  var gameover      = new PImage
  var questionmark  = new PImage
  var healthimg     = new PImage
  var easyimg       = new PImage
  var litimg        = new PImage
  var hardimg       = new PImage
  
  
  var difficulty = 0 //pelin vaikeustaso
  var whichMenu = 0 //tietää mikä menuvalikko päällä. -1: ei menua vaan itse peli, 0: päävalikko, 1: helpruutu, 2: pelin loppumisruutu
  
  //esittelee graafisesti hienon valikon
  def show() = {
    if(whichMenu == 0){ //päävalikko
    	p.image(menuimg, p.width/2 - this.buttonWidth/2 - (100*delta).toFloat, (50*delta).toFloat,
    			this.buttonWidth + (200*delta).toFloat, 3 * this.buttonHeight + (500*delta).toFloat)//menun tausta
    	p.image(logoimg, p.width/2 - (250*delta).toFloat, (65*delta).toFloat,
    	        this.buttonWidth + (160*delta).toFloat, (150*delta).toFloat)//pelin logo
    	p.image(helpimg, p.width/2 - this.buttonWidth/2, (320*delta).toFloat + 2 * buttonHeight,
    	        this.buttonWidth, this.buttonHeight)//helpnappi
    	p.image(exitimg, p.width/2 - this.buttonWidth/2, (370*delta).toFloat + 3 * buttonHeight,
    	        this.buttonWidth, this.buttonHeight)//exitnappi
    	
    	if(soundsOn)//onko äänet päällä vai ei
    		p.image(unmuteimg, p.width/2 + (20*delta).toFloat, (220*delta).toFloat,this.smallButtonWidth, this.smallButtonHeight)
   		else
   			p.image(muteimg, p.width/2 + (20*delta).toFloat, (220*delta).toFloat,this.smallButtonWidth, this.smallButtonHeight)
    	
   	  if(gameIsOn){
    		p.image(restartimg, p.width/2 - this.smallButtonWidth - 20, (220*delta).toFloat ,
    		        this.smallButtonWidth, this.smallButtonHeight) //restartnappi   
    		p.image(resumeimg, p.width/2 - this.buttonWidth/2, (270*delta).toFloat + buttonHeight,
    		        this.buttonWidth, this.buttonHeight)    	  //resumenappi
    	}else{
    		p.image(playimg, p.width/2 - this.buttonWidth/2, (270*delta).toFloat + buttonHeight ,
    		        this.buttonWidth, this.buttonHeight) //playnappi
    		if(difficulty == 0)//vaikeustason muuttaminen(0: easy, 1: medium, 2: hard)
    		  p.image(easyimg, p.width/2 - this.smallButtonWidth - 20, (220*delta).toFloat, this.smallButtonWidth, this.smallButtonHeight)
    		else if(difficulty == 1)
    		  p.image(litimg, p.width/2 - this.smallButtonWidth - 20, (220*delta).toFloat ,this.smallButtonWidth, this.smallButtonHeight)
    		else
    		  p.image(hardimg, p.width/2 - this.smallButtonWidth - 20, (220*delta).toFloat ,this.smallButtonWidth, this.smallButtonHeight)
    	}
    }else if(whichMenu == 1){//helpruutu
    	p.textSize((24*delta).toFloat)
    	p.fill(255)
      p.image(menuimg, p.width/2 - this.buttonWidth/2 - (100*delta).toFloat, (50*delta).toFloat,
    	        this.buttonWidth + (200*delta).toFloat, 3 * this.buttonHeight + (500*delta).toFloat)//menun tausta
 			p.image(backimg, p.width/2 - this.buttonWidth/2 - (85*delta).toFloat, (65*delta).toFloat,
 			        this.smallButtonWidth * 0.7f, this.smallButtonHeight * 0.7f)//takaisinnuoli
 			//infotekstejä
 			p.text("Your mission is to protect the Earth with moon from asteroids. Press right arrow(->) to move clockwise and left arrow(<-) to move anti-clockwise.",         
 			        p.width/2 - this.buttonWidth/2 - (85*delta).toFloat, (60*delta).toFloat + this.smallButtonHeight,
 			        this.buttonWidth + (180*delta).toFloat, (150*delta).toFloat)
 			p.image(questionmark, p.width/2 - this.buttonWidth/2 - (85*delta).toFloat, 
 			        this.smallButtonHeight + (240*delta).toFloat, (40f*delta).toFloat, (40f*delta).toFloat)
 			p.text("Collect these to get better chance to get good power-ups. If they hit the Earth there's good chance you get a bad power-up.",
 			        p.width/2 - this.buttonWidth/2 - (40*delta).toFloat, this.smallButtonHeight + (240*delta).toFloat,
 			       this.buttonWidth + (140*delta).toFloat, (150f*delta).toFloat)
 			p.image(healthimg, p.width/2 - this.buttonWidth/2 - (85*delta).toFloat, 
 			        this.smallButtonHeight + (420*delta).toFloat, (40f*delta).toFloat, (40f*delta).toFloat)
 			p.text("Collect these to get +1 life. If they hit the Earth, nothing happens.",
 			       p.width/2 - this.buttonWidth/2 - (40*delta).toFloat, this.smallButtonHeight + (420*delta).toFloat,
 			       this.buttonWidth + (140f*delta).toFloat, (80f*delta).toFloat)
 			p.text("[M] = mute/unmute \n[P] = pause",
 			       p.width/2 - this.buttonWidth/2 - (40*delta).toFloat, this.smallButtonHeight + (510*delta).toFloat,
 			       (320f*delta).toFloat, (100f*delta).toFloat)	
 			p.textSize((60*delta).toInt)       
 			p.text("HAVE FUN!", (330f*delta).toFloat, (800f*delta).toFloat, (350f*delta).toFloat,(120f*delta).toFloat)
    }else if(whichMenu == 2){//pelinloppumisruutu, jossa on mahdollisuus menuun ja mahdollisuus aloittaa uusi peli
        p.image(gameover, p.width/2 - (450*delta).toFloat, p.height/2 - (150*delta).toFloat,
                (900*delta).toFloat, (300*delta).toFloat)
        p.image(restartimg, p.width/2 - buttonWidth - (20*delta).toFloat, p.height/2 - (20*delta).toFloat, this.buttonWidth, this.buttonHeight)
        p.image(menubuttonimg, p.width/2 + (20*delta).toFloat, p.height/2 - (20*delta).toFloat, this.buttonWidth, this.buttonHeight)
    } 
  }
}