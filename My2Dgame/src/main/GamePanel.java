package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
		// SCREEN SETTINGS
		final int originalTileSize = 16; // 16x16 tile
		final int scale = 3;
		
		final public int tileSize = originalTileSize * scale; // Final display size 48x48
		public final int maxScreenCol = 16;
		public final int maxScreenRow = 12;
		public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
		public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
		
		// World parameters
		
		public final int maxWorldCol = 50;
		public final int maxWorldRow = 50;
		//public final int worldWidth = tileSize * maxWorldCol;
		//public final int worldHeight = tileSize * maxWorldRow;
		
		// Initialization
		
		Thread gameThread;
		public CollisionChecker cChecker = new CollisionChecker(this);
		KeyHandler keyH = new KeyHandler();
		Sound music = new Sound();
		Sound se = new Sound();
		public Player player = new Player(this,keyH);
		TileManager tileM = new TileManager(this);
		public SuperObject obj[] = new SuperObject[10]; // we can display ten objects at the same time in game
		public AssetSetter aSetter = new AssetSetter(this);
		public UI ui = new UI(this);
		
		// Game FPS
		int fps = 60;
		
		public GamePanel() {
			this.setPreferredSize(new Dimension(screenWidth,screenHeight));
			this.setBackground(Color.black);
			this.setDoubleBuffered(true);
			this.addKeyListener(keyH);
			this.setFocusable(true); // to receive key input
			
			
	}
		
		public void setupGame() {
			aSetter.setObject();
			playMusic(0);
		}
		
		public void startGameThread() {
			gameThread = new Thread(this);
			gameThread.start();
		}

		@Override
		public void run() {
			// delta gameloop for 60 fps
			
			double drawInterval = 1000000000 / fps;
			double delta = 0;
			long lastTime = System.nanoTime();
			long currentTime;
			long timer = 0;
			int drawCount = 0;
			
			while (gameThread != null) {
				
				currentTime = System.nanoTime();
				
				delta += (currentTime - lastTime) / drawInterval;
				timer += (currentTime - lastTime);
				lastTime = currentTime;
				
				if (delta >= 1) {
					update();
					repaint();
					delta--;
					drawCount++;
				}
				
				if(timer >= 1000000000) {
					//System.out.println("FPS :"+ drawCount);
					drawCount = 0;
					timer = 0;
				}
				
				
			}
			
		}
		
		public void update() {
			player.update();
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			// drawing the tiles first
			tileM.draw(g2);
			
			// drawing the objects second 
			
			for(int i = 0;i<obj.length;i++) {
				// we are checking the slot is not empty, to avoid null pointer error
				
				if(obj[i] != null) {
					obj[i].draw(g2, this);
				}
			}
			
			// drawing the player character third
			player.draw(g2);
			
			//UI
			ui.draw(g2);
			
			g2.dispose(); // to free up memory
			
			
		}
		
		public void playMusic(int i) {
			music.setFile(i);
			music.play();
			music.loop();
		}
		
		public void stopMusic() {
			music.stop();
		}
		
		public void playSE(int i) {
			se.setFile(i);
			se.play();
		}

}