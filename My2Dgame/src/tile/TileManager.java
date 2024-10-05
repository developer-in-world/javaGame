package tile;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[] [];
	
	
	public TileManager(GamePanel gp){
		this.gp = gp;
		tile = new Tile[10];
		mapTileNum = new int [gp.maxWorldCol] [gp.maxWorldRow];
		getTileImage();
		loadMap("/maps/world01.txt");
	}

	public void getTileImage() {
		try {
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
			
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			tile[1].collision = true;
			
			tile[2] = new Tile();
			tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
			tile[2].collision = true;
			
			tile[3] = new Tile();
			tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));
			
			tile[4] = new Tile();
			tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
			tile[4].collision = true;
			
			tile[5] = new Tile();
			tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadMap(String filepath) {
		try {
			
			InputStream is = getClass().getResourceAsStream(filepath); // Importing the text
			BufferedReader br = new BufferedReader(new InputStreamReader(is)); // Reading the map content
			
			int col = 0;
			int row = 0;
			
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				String line = br.readLine(); // reading a line
				
				while(col < gp.maxWorldCol) {
					
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]); // string data is converted into the integer
					mapTileNum [col][row] = num;
					col++;
				}
				
				if(col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
				
			}
			br.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	public void draw(Graphics2D g2) {
//		
//		int worldCol = 0, worldRow = 0;
//		
//		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
//			int tileNum = mapTileNum[worldCol][worldRow];
//			
//			int worldX = worldCol * gp.tileSize; // worldX and Y is the position on the map
//			int worldY = worldRow * gp.tileSize;
//			int screenX = worldX - gp.player.worldX + gp.player.screenX; // used to draw the screen position of the tile drawings
//			int screenY = worldY - gp.player.worldY + gp.player.screenY;
//			
//			// by if statement we are only drawing the tiles around the player character only 
//			// reducing the overload of the processing power
//			
//			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
//		       worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
//		       worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
//		       worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
//			{
//				g2.drawImage(tile[tileNum].image, screenX, screenY,gp.tileSize,gp.tileSize,null);
//				
//			}
//			
//			
//			worldCol++;
//			
//			if(worldCol == gp.maxScreenCol) {
//				worldCol = 0;
//				worldRow++;
//			}
//			
//		}
//	}
	
	
	public void draw(Graphics2D g2) {
	    int worldCol = 0, worldRow = 0;
	    
	    // Calculate the starting and ending column and row numbers for the visible area
	    int startCol = Math.max(0, (gp.player.worldX - gp.player.screenX) / gp.tileSize);
	    int endCol = Math.min(49, (gp.player.worldX + gp.player.screenX + gp.tileSize) / gp.tileSize);
	    int startRow = Math.max(0, (gp.player.worldY - gp.player.screenY) / gp.tileSize);
	    int endRow = Math.min(49, (gp.player.worldY + gp.player.screenY + gp.tileSize) / gp.tileSize);
	    
	    for (int row = startRow; row <= endRow; row++) {
	        for (int col = startCol; col <= endCol; col++) {
	            int tileNum = mapTileNum[col][row];
	            
	            int worldX = col * gp.tileSize;
	            int worldY = row * gp.tileSize;
	            int screenX = worldX - gp.player.worldX + gp.player.screenX;
	            int screenY = worldY - gp.player.worldY + gp.player.screenY;
	            
	            // Culling: only draw tiles that are within the screen boundaries
	            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
	     		       worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
	     		       worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
	     		       worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
	                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
	            }
	        }
	    }
	}

	
}
