package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
	public int worldX,worldY,speed;
	public BufferedImage up1,up2,down1,down2,left1,left2,right1,right2; // used to store image files, it acts as a buffer.
	public String direction;
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	public Rectangle solidArea; // used to create a invisible rectangle
	public int solidAreaDefaultX,solidAreaDefaultY;
	public boolean collisionOn = false;

}
