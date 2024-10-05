package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX,screenY;
	public int hasKey = 0; // indicates how many keys does player have currently
	
	public Player(GamePanel gp,KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		
		screenX = gp.screenWidth / 2 - (gp.tileSize/2); // these are final and the player screen position doesn't change
		screenY = gp.screenHeight / 2 - (gp.tileSize/2); // positioned at center
		
		solidArea = new Rectangle(8,16,32,32);
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		setDefaultvalues();
		getPlayerImage();
	}
	
	public void setDefaultvalues() {
		worldX = gp.tileSize * 23; // player position on the world map x,y
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
	}
	
	public void getPlayerImage() {
		try {
			
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		
		// This method is called 60 fps according to the frame rate of the game
		
		if (keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true || keyH.upPressed) {
			
			if (keyH.upPressed == true) {
				direction = "up";
				//worldY -= speed;
			}
			else if(keyH.downPressed == true) {
				direction = "down";
				//worldY += speed;
			}
			else if(keyH.leftPressed == true) {
				direction = "left";
				//worldX -= speed;
			}
			else if (keyH.rightPressed == true) {
				direction = "right";
				//worldX += speed;
			}
			// check tile collision
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			// check object collision
			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);
			
			//if collision is off player can move
			if(collisionOn == false) {
				switch(direction) {
				case "up":
					worldY -= speed;
					break;
				case "down":
					worldY += speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
				}
			}
			
			// so in every frame this is called and the spriteCounter increase by 1
			// player image changes every ten frame
			spriteCounter++;
			if(spriteCounter > 15) {
				if(spriteNum == 1) {spriteNum = 2;}
				else if(spriteNum == 2) {spriteNum = 1;}
				spriteCounter = 0;
			}
			
		}
		
		
	}
	
	public void pickUpObject(int index) {
		if(index != 999) {
			String objectName = gp.obj[index].name;
			//gp.obj[index] = null;
			switch(objectName) {
			case "key":
				gp.playSE(1);
				hasKey++;
				gp.obj[index] = null;
				//System.out.println("Key:"+hasKey);
				gp.ui.showMessage("You got a key!!!");
				break;
			case "door":
				if(hasKey >0) {
					gp.playSE(3);
					gp.obj[index] = null;
					hasKey--;
					//System.out.println("LeftOverKey:"+hasKey);
					gp.ui.showMessage("Door is opened now");
				}
				else {
					gp.ui.showMessage("You need a key");
				}
				break;
			case "boots":
				gp.playSE(2);
				speed+= 2;
				gp.obj[index] = null;
				gp.ui.showMessage("SpeedUP ++");
				break;
			case "chest":
				// if you get the chest the game stops and game over
				gp.ui.gameOver = true;
				gp.stopMusic();
				gp.playSE(4);
				
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		//g2.setColor(Color.white);
		//g2.fillRect(x, y,gp.tileSize,gp.tileSize);
		
		BufferedImage image = null;
		switch(direction) {
		case "up":
			if(spriteNum == 1) {image = up1;} // this draws both the images based on the spriteNum
			if(spriteNum == 2) {image = up2;}
			break;
		case "down":
			if(spriteNum == 1) {image = down1;}
			if(spriteNum == 2) {image = down2;}
			break;
		case "left":
			if(spriteNum == 1) {image = left1;}
			if(spriteNum == 2) {image = left2;}
			break;
		case "right":
			if(spriteNum == 1) {image = right1;}
			if(spriteNum == 2) {image = right2;}
			break;
		}
		
		g2.drawImage(image,screenX,screenY,gp.tileSize,gp.tileSize,null);
		
	}
}
