import processing.core._
import java.awt.event.KeyEvent._
import java.awt.Toolkit
import scala.collection.mutable.Buffer
import scala.util.Random
import scala.math._
import java.io.File
import javax.sound.sampled._

object GUI extends PApplet{
  
  val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
  val delta = screenSize.getHeight()/1080 //tän avulla skaalataan joka näytölle sopivaksi
  val frameHeight = (1030*delta).toInt    //tähän tapaan
  val frameWidth = frameHeight
  
  var earthLives = 5
  
  var moon = new Moon(this, delta)   //luodaan kuu
  var earth = new Earth(this, delta) //luodaan maa
  var menu = new Menu(this, delta)   //luodaan menu-valikko
  var ball = new Ball(this, delta)   //luodaan pallo
  
  var projectiles = Buffer[Projectile]() // tänne varastoidaan kaikki tavara mitkä on tulossa maapalloa kohti
 
  var random = new Random       // Randomi satunnaista spawnausta varten
  var spawnradius = 700f*delta  // spawnaa asteroidit ja muut, jotka lähtevät kohti maapalloa 

  //luodaan osan tarvittavista kuvamuuttujista
  var backgroundimg = new PImage 
  var asteroidimg = new PImage
  var healthimg = new PImage
  var randomimg = new PImage
  
  var stepper: Double = 0.0 // menee luvut 0-4199 läpi ja spawnaa asioita sen mukaan mikä vaikeusaste on 
  var points = 0 
  var spawnRate : Double = 2.0 - (100.0/(100.0 + points)) //spawnaa asioita sitä nopeammin mitä enemmän on pisteitä
  var ballTimer = 0  // ajastin tietylle powerupille
  var moonTimer = 0  // ajastin tietylle powerupille

  var gameIsOver = false   // booleani joka tietää onko pelaaja hävinnyt
  var gameIsOn = false     // booleani joka tietää onko peli käynnissä
  var ballIsActive = false // booleani joka tietää onko pallopowerup käynnissä
  var bigMoon = false      // booleani joka tietää onko kuupowerup käynnissä
  /*
  //lataa ja luo tarvittavat äänitiedostot
  val impact = AudioSystem.getAudioInputStream(new File("explos.wav"))
  val clip = AudioSystem.getClip
  clip.open(impact)
  val swed = AudioSystem.getAudioInputStream(new File("swed2.wav"))
  val clip2  = AudioSystem.getClip
  clip2.open(swed)
  val health = AudioSystem.getAudioInputStream(new File("health.wav"))
  val clip3 = AudioSystem.getClip
  clip3.open(health)
  val hit = AudioSystem.getAudioInputStream(new File("hit.wav"))
  val clip4 = AudioSystem.getClip
  clip4.open(hit)
  val music = AudioSystem.getAudioInputStream(new File("miusik.wav"))
  val clip5 = AudioSystem.getClip
  clip5.open(music)
  clip5.loop(0)
  val switchAudio = AudioSystem.getAudioInputStream(new File("switch.wav"))
  val clip6 = AudioSystem.getClip
  clip6.open(switchAudio)
  */
  override def setup() = { 
  	size(frameWidth,frameHeight) //määrittää ikkunan koon
		frameRate(60)                //määrittää fps:n
		
		//lataa kaikki kuvat käyttöä varten
		backgroundimg      = loadImage("space.png")
		moon.img           = loadImage("moon.png")      
		earth.img          = loadImage("earth.png")
		ball.img           = loadImage("ball.png")
		asteroidimg        = loadImage("asteroid.png") 
    healthimg          = loadImage("health.png")
    randomimg          = loadImage("questionmark.png")
    menu.healthimg     = loadImage("health.png")
    menu.questionmark  = loadImage("questionmark.png")
    menu.menuimg       = loadImage("menu.png")
    menu.playimg       = loadImage("playbutton.png")
    menu.helpimg       = loadImage("helpbutton.png")
    menu.exitimg       = loadImage("exitbutton.png")
    menu.resumeimg     = loadImage("resumebutton.png")
    menu.restartimg    = loadImage("restartbutton.png")
    menu.menubuttonimg = loadImage("menubutton.png")
    menu.muteimg       = loadImage("mute.png")
    menu.unmuteimg     = loadImage("unmute.png")
    menu.backimg       = loadImage("backarrow.png")
    menu.gameover      = loadImage("game_over.png")
    menu.easyimg       = loadImage("ezbutton.png")
    menu.litimg        = loadImage("litbutton.png")
    menu.hardimg       = loadImage("hardbutton.png")
    menu.logoimg       = loadImage("logo.png")
  }
  // draw metodi piirtää itse pelin mitä pelataan tai valikon jos pelaaja on valikossa
 
	override def draw() = {
	  image(backgroundimg,0,0,frameHeight,frameWidth)//asettaa taustakuvaks avaruustaustan
	  if(menu.gameIsOn){ 
		  fill(255)
		  textSize((45*delta).toInt)
		  text(points, (10*delta).toInt, (40*delta).toInt) //näyttää pelaajalle pelaajan pisteet
		  text("Lives left: " + earthLives, (740*delta).toInt,(40*delta).toInt)	//näyttää pelaajalle jäljellä olevat elämät    
	  
      if(ballIsActive) {
        fill(255)
        var ballTime = ballTimer/60f
	    	text("BALL: " + f"$ballTime%1.1f", (10*delta).toInt, (90*delta).toInt) //näyttää pallopowerupin jäljellä olevan ajan
      }else
        ball.invisible()
	    if(bigMoon){
	      fill(255)
	      var moonTime = moonTimer/60f
	    	text("MOON: " + f"$moonTime%1.1f", (10*delta).toInt, (130*delta).toInt) //näyttää kuupowerupin jäljellä olevan ajan
	    	if(moonTimer == 0){ //palauttaa kuun normikokoiseksi powerupin jälkeen
  			  bigMoon = false
  			  moon.width = (45f*delta).toFloat
  			  moon.height = (45f*delta).toFloat
  			}
	    	moon.imgRadius = moon.width/2
	    }
		  if(moon.integer > 0){
			  fill(255, 0, 0)
			  text("SWICTH: OFF", (150*delta).toInt, (40*delta).toInt) //graafinen esitys pelaajalle siitä kummin päin nuolinäppäimet toimivat
		  }
  	  else{
  		  fill(0, 0, 255)
  		  text("SWICTH: ON", (150*delta).toInt, (40*delta).toInt) //graafinen esitys pelaajalle siitä kummin päin nuolinäppäimet toimivat
  	  }
	  }
	  if(menu.whichMenu == -1){ //jos menu ei oo päällä niin..
	    gameIsOver = earthLives <= 0
	    if(gameIsOver)		//jos pelaaja on hävinnyt niin..
	      gameOver 
		  if(stepper < 4199) stepper += spawnRate else stepper = 0 
      
		  if(this.stepper % (70 - menu.difficulty * 10) == 0 ) { // spawnaa enemmän asteroideja jos vaikeustaso on korkeampi
        spawn()
      }
      projectiles.foreach(_.update())    //päivittää jokaisen projectilen paikan
      projectiles = projectiles.filterNot(checkCollision) //poistaa tuhoutuneet projectilet projectilien päivityslistasta
      earth.display()               //näyttää maapallon ruudulla
      moon.update()                 // päivittää kuun paikan ja näyttää ruudulla
      if(ballIsActive){ //jos pallopowerup on käynnissä niin vähentää sen olemassaoloaikaa kunnes aika on nolla
        ball.update()   //päivittää pallon paikan
        ballTimer -= 1
        if(ballTimer == 0) {
          ballIsActive = false
        }
      }else{
        ball.invisible() //siirtää pallon pois ruudulta kun se ei ole käynnissä
	    }
      if(bigMoon){ //jos kuupowerup on käynnissä niin vähentää sen olemassaoloaikaa kunnes aika on nolla
	    	moon.width = (60f*delta).toFloat    //kasvattaa kuuta
  			moon.height = (60f*delta).toFloat
  			moonTimer -= 1
  			if(moonTimer == 0){
  			  bigMoon = false
  			  moon.width = (45f*delta).toFloat   //kutistaa kuun takaisin normaaliksi kun powerup loppuu
  			  moon.height = (45f*delta).toFloat
  			}
	    	moon.imgRadius = moon.width/2
	    }
      
	    //if(points == 420 && menu.soundsOn) clip2.start
	    
	  }else{                        //jos ollaan menussa niin..
		  projectiles.foreach(_.display) //ensin näytetään projectilet että ne jää sit
	    menu.show()                      //menun alle kun se näytetään
      fill(255)
	    if(menu.whichMenu == 2)        //näyttää pisteiden määrän pelaajalle jos pelaaja häviää
	    	text("YOU GOT " + points + " POINTS!", width/2 - 200, height/2 - 50)
	  }
	}
		
	def checkCollision(proj: Projectile): Boolean = {    //tarkistaa onko projectile törmännyt johonkin true jos on ja false jos ei
    
	  val distanceToMoon = sqrt(pow(proj.location.y - this.moon.y, 2) + pow(proj.location.x - this.moon.x, 2)).toFloat
    val distanceToCenter = sqrt(pow(proj.location.y - this.height/2, 2) + pow(proj.location.x - this.width/2, 2)).toFloat
    val distanceToBall = sqrt(pow(proj.location.y - this.ball.y, 2) + pow(proj.location.x - this.ball.x, 2)).toFloat
    
    if(distanceToMoon <= moon.imgRadius + proj.radius){ //törmäys kuuhun
      whatHappens(proj.hitsMoon)//sitten tutkitaan mitä tapahtuu törmäyksestä
      true
    }else if(distanceToCenter <= earth.radius + proj.radius){ //törmäys maahan 
      whatHappens(proj.hitsEarth)//sitten tutkitaan mitä tapahtuu törmäyksestä
      true
    }else if(distanceToBall <= ball.imgRadius + proj.radius){ //törmäys pallopoweruppiin
      if(ballIsActive){
        whatHappens(proj.hitsMoon)//sitten tutkitaan mitä tapahtuu törmäyksestä
        true
      }else
        false
    }else
    	false
	}
	/*	 Tutkitaan mitä tapahtuu törmäyksestä
	 *	 0: asteroid-kuu eli +10 pistettä
	 *	 1: asteroid-maa eli -1 elämä
	 *   2: health1-kuu eli +1 elämä
	 *   mysteeripallo-kuu niin 20% mahd. switchpoweruppiin, 40% pallopoweruppiin ja 40% kuupoweruppiin
	 * 	 mysteeripallo-maa niin 50% mahd. switchpoweruppiin, 25% palloon ja 25% kuuhun
	 * 	 3: pallopowerup
	 * 	 4: switchpowerup
	 * 	 5: kuupowerup
	 */
	def whatHappens(number: Int) : Unit = {
	  if(number == 0){
	    points += 10
	    if(menu.soundsOn) {
        //clip4.start
        //clip4.setFramePosition(0)
        earth.hit
      }
	  }
	  else if(number == 1){
	    earthLives -= 1
	    earth.stepper = 255 //efekti maapalloon osumisesta (kts. Earth-class)
	    /*
	    if(menu.soundsOn) {
	    	clip.setFramePosition(0)
        clip.start
	    }
	    */
	  }
	  else if(number == 2){
	    if(earthLives < 5) earthLives += 1
	    /*
	    if(menu.soundsOn) {
	    	clip3.setFramePosition(0)
        clip3.start
      }
      */
	  }else if(number == 3){
	    if(ballIsActive)
	      ballTimer += 60
	    else{
	    	ballIsActive = true
	    	ballTimer = 420
	    }
	  }else if(number == 4){
		  moon.integer = moon.integer * -1 //vaihtaa nuolinäppäimien toiminnalisuuden käänteiseksi
	    /*
		  if(menu.soundsOn){
	    	clip6.setFramePosition(0)
	    	clip6.start
	    }
	    */
	  }else if(number == 5){
	    if(bigMoon)
	      moonTimer += 60
	    else{
	    	bigMoon = true
	    	moonTimer = 420
	    }
	  }else if(number == 6)None //jos health osuu maahan niin ei tapahdu mitään
	}
	
  // Lisää projectiles bufferiin uuden projectilen, joka spawnaa satunnaiseen mestaan
  def spawn() : Unit = {
    val randomXY = random.nextInt(360).toRadians
    val x = (frameWidth/2 + Math.cos(randomXY) * spawnradius).toFloat //luo satunnaiset koordinaatit ulos ruudulta, josta 
    val y = (frameHeight/2 + Math.sin(randomXY) * spawnradius).toFloat//projectilet lähtevät kohti maata
    val rndm = random.nextInt(100)    
    if(rndm < 70) { //70% mahdollisuus spawnata asteroidi joka lisätään projectiles listaan
    	this.projectiles += new Asteroid(this,asteroidimg, this.delta, x, y, earth.width/2, ((4 + points/100)*delta).toInt + menu.difficulty) //RIP kpY ja kpX
    }else if(rndm < 85){ //15% mahdollisuus spawnata health joka lisätään projectiles listaan
    	this.projectiles += new Health_1(this, healthimg, this.delta, x, y, ((4 + points/100)*delta + menu.difficulty).toInt)
    }else    //15% mahdollisuus spawnata mysteeripallo joka lisätään projectiles listaan
    	this.projectiles += new Randomprojectile(this, randomimg, this.delta, x, y, ((4 + points/100)*delta).toInt + menu.difficulty)
  }
  // jos pelaaja on hävinny nii menu menee päälle ja näyttää pelaajalle pisteet
  def gameOver = {
    menu.whichMenu = 2
    menu.gameIsOn = false
  }
  
  //alustaa uuden pelin nollaamalla kaikki muuttujat alkuarvoihin ja poistamalla kaikki
  def resetGame = {
    points = 0
    earthLives = 3
    projectiles.clear()
    ballIsActive = false
    bigMoon = false
    ballTimer = 0
    moonTimer = 0
    moon.integer = 1
    earth.stepper = 0
    moon.height = (45f*delta).toFloat
    moon.width  = (45f*delta).toFloat
  }
  //vaihtaa vaikeustason easy-medium-hard
  def changeDifficulty = if(menu.difficulty == 2) menu.difficulty = 0 else menu.difficulty += 1
	
	//händlää näppäimistön painallukset
	override def keyPressed() = {
	  
	  if(keyCode == VK_RIGHT){
	    moon.rightIsPressed = true
	    moon.leftIsPressed = false
	    moon.growing = true
	  }
	  if(keyCode == VK_LEFT){
	    moon.leftIsPressed = true 
	    moon.rightIsPressed = false
	    moon.growing = true
	  }
	  if(key == 'p' || key == 'P'){
	    if(menu.whichMenu == 0) menu.whichMenu = -1 else if(menu.whichMenu == -1) menu.whichMenu = 0
	    menu.gameIsOn = true
	  }
	  if (key == 'm' || key == 'M') {
	    menu.soundsOn = !menu.soundsOn
      //if(menu.soundsOn) clip5.start else clip5.stop
    }
	}
	
	override def keyReleased() = {
	  
	  if(keyCode == VK_RIGHT){
	    moon.rightIsPressed = false 
	    moon.growing = false
	  }
	  if(keyCode == VK_LEFT){
	    moon.leftIsPressed = false 
	    moon.growing = false
	  }
	}
	/*
	 * händlää hiiren painallukset eli siis minkä valikon napin päällä ollaan ja painetaanko sitä
	 */
	
  override def mousePressed() = {
    if(mousePressed){
      if(menu.whichMenu == 0){   //päämenu      
    	  if(mouseX > width/2 - menu.smallButtonWidth - 20 && mouseX < width/2 - 20 &&
    			 mouseY > (220*delta).toFloat && mouseY < (220*delta).toFloat + menu.smallButtonHeight){//vasemmanpuoleinen pikkunappi
    		  if(menu.gameIsOn){
    	      resetGame //restart jos peli on käynnissä
    		    menu.whichMenu = -1
    		  }else{
    		    changeDifficulty // vaikeustason muutos jos peli ei ole käynnissä
    		  }
    	  }else if(mouseX > width/2 + 20 && mouseX < width/2 + menu.smallButtonWidth + 20 &&
    			       mouseY > (220*delta).toFloat && mouseY < (220*delta).toFloat + menu.smallButtonHeight){//oikeanpuoleinen pikkunappi
    		  menu.soundsOn = !menu.soundsOn    //mute/unmute
    		  //if(menu.soundsOn) clip5.start else clip5.stop
    	  }else if(mouseX > width/2 - menu.buttonWidth/2 && mouseX < width/2 - menu.buttonWidth/2 + menu.buttonWidth &&
    	      		 mouseY > (270*delta).toFloat + menu.buttonHeight && mouseY < (270*delta).toFloat + 2 * menu.buttonHeight){//ylin iso nappi
    		  if(gameIsOver)
    		    resetGame 
    		  menu.gameIsOn = true // jatka peliä jos peli käynnissä, uusi peli jos peli ei käynnissä
    		  menu.whichMenu = -1
    	  }else if(mouseX > width/2 - menu.buttonWidth/2 && mouseX < width/2 - menu.buttonWidth/2 + menu.buttonWidth &&
         			   mouseY > (320*delta).toFloat + 2 * menu.buttonHeight && mouseY < (320*delta).toFloat + 3 * menu.buttonHeight){//keskimmäinen iso nappi
    		  menu.whichMenu = 1 // avaa helpruudun
    	  }else if(mouseX > width/2 - menu.buttonWidth/2 && mouseX < width/2 - menu.buttonWidth/2 + menu.buttonWidth &&
         			   mouseY > (370*delta).toFloat + 3 * menu.buttonHeight && mouseY < (370*delta).toFloat + 4 * menu.buttonHeight){//alin iso nappi
    		  sys.exit //poistuu sovelluksesta
    	  }
      }else if(menu.whichMenu == 1){
        if(mouseX > width/2 - menu.buttonWidth/2 - (100*delta).toFloat + 15 && 
           mouseX < width/2 - menu.buttonWidth/2 - (100*delta).toFloat + 15 + menu.smallButtonWidth * 0.7f &&
    			 mouseY > (50*delta).toFloat + 15 && 
    			 mouseY < (50*delta).toFloat + 15 + menu.smallButtonHeight * 0.7f){ //helpruudussa takaisin nappi
          menu.whichMenu = 0 //palaa päävalikkoon
        }
      }else if(menu.whichMenu == 2){ //game over ruutu
    	  if(mouseX > this.width/2 - menu.buttonWidth - (20*delta).toFloat && mouseX < this.width/2 - (20*delta).toFloat &&
    			  mouseY > this.height/2 - (20*delta).toFloat && mouseY < this.height/2 - (20*delta).toFloat + menu.buttonHeight){ //vasemmanpuolinen nappi
    		  resetGame //aloittaa pelin uudelleen
    		  menu.gameIsOn = true
    		  menu.whichMenu = -1
    	  }else if(mouseX > this.width/2 + (20*delta).toFloat && mouseX < this.width/2 + menu.buttonWidth + (20*delta).toFloat &&
    			  mouseY > this.height/2 - (20*delta).toFloat && mouseY < this.height/2 - (20*delta).toFloat + menu.buttonHeight){ // oikeanpuolinen nappi
    		  projectiles.clear()
    		  menu.whichMenu = 0 //menee päävalikkoon
    	  }
      }
    }
  }
	//Alustaa pelin ikkunan
	def main(args: Array[String]) {
    val frame = new javax.swing.JFrame("Earth Defender McAwesome 2000")
    
    frame.getContentPane().add(this)
    frame.setSize(frameWidth,frameHeight)
    init
    frame.setVisible(true)
    frame.setLocationRelativeTo(null)
    frame.setResizable(false)
    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE)
  } 
}