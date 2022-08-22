package main.gui;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import main.functionality.Game;
import main.functionality.monster.combat.Battle;
import main.gui.entity.Entity;
import main.gui.entity.User;
import main.gui.tile.TileManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable {

	// ================Debug Mode================

	public boolean debugMode = false;

	// ==========================================

	// =============Screen Settings==============

	int FPS = 60; // game FPS

	final int originalTileSize = 16; // Number of pixels in tile
	final int scale = 3;

	public final int tileSize = originalTileSize * scale; // tile size 48 * 48 Pixels
	// Ratio 4 * 3
	public final int maxScreenCol = 40; // editable parameters
	public final int maxScreenRow = 22; // editable parameters

	public final int screenWidth = tileSize * maxScreenCol;
	public final int screenHeight = tileSize * maxScreenRow;

	// =========================================

	// ==============WORLD SETTINGS=============

	public final int maxWorldCol = 50; // editable parameters
	public final int maxWorldRow = 50; // editable parameters
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldheight = tileSize * maxWorldRow;

	// =========================================

	// ===============GAME STATE================

	public Game game;

	public int gameState;
	public final int helpState = 0;
	public final int playState = 1;
	public final int consolePauseState = 2;
	public final int shopBuyState = 3;
	public final int dialogueState = 4;
	public final int menuState = 5;
	public final int monsterMenuState = 6;
	public final int inventoryState = 7;
	public final int endState = 8;
	public final int shopSellItemsState = 9;
	public final int shopSellMonstersState = 10;
	public final int sleepState = 11;
	public final int battleState = 12;
	public final int itemUseState = 13;

	// =========================================

	// ========== DIALOGUE STATES===============
	public int dialogueMode;
	public final int sleepMode = 0;
	public final int shopMode = 1;
	public final int battleMode = 2;
	public final int textMode = 3;

	// ==========================================

	TileManager tileM = new TileManager(this);
	public KeyHandler KeyH = new KeyHandler(this);
	AssetSetter aSetter = new AssetSetter(this);
	public EventHandler eventHandler = new EventHandler(this);

	Thread gameThread;
	public CollisionChecker collisionChecker = new CollisionChecker(this);
	public UI ui = new UI(this);

	public User user = new User(this, KeyH);
	public Entity npc[] = new Entity[10];
	public BattleUI battleUI;
	long currentTime;

	public void startGameThread() {

		gameThread = new Thread(this);
		gameThread.start();

	}

	/**
	 * Constructor for the GamePanel
	 * 
	 * @param game receives game data from CLI application
	 */
	public GamePanel(Game game) {
		this.game = game;
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); // Improve Rendering Performance
		this.addKeyListener(KeyH);
		this.setFocusable(true);
		setupGame();
		startGameThread();
	}

	/**
	 * Sets default values and initializes NPCs
	 */
	public void setupGame() {

		gameState = helpState; // Default gameState
		// sets up merchant NPC
		aSetter.setMerchantNPC();
		// sets up battle NPCs
		game.generateBattles();
		ArrayList<Battle> battleList = game.getTodaysBattles();
		for (int i = 1; i <= battleList.size(); i++) {
			aSetter.setNPC();

		}
	}

	/**
	 * Runs the gameLoop at 60FPS
	 */
	@Override
	public void run() {

		double drawInterval = 1000000000 / FPS;
		double nextDrawTime = System.nanoTime() + drawInterval;

//==============================Game Loop====================================

		while (gameThread != null) {

			// UPDATE: Update information (Character POS etc)
			update();

			// DRAW: draw screen using updated information
			repaint(); // (paintComponent)

			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				remainingTime = remainingTime / 1000000;
				if (remainingTime < 0) {
					remainingTime = 0;
				}
				Thread.sleep((long) remainingTime);
				nextDrawTime += drawInterval;

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//============================================================================	

	/**
	 * generate new NPCs and battles reset shop on player sleep
	 */
	public void playerSleep() {
		game.generateBattles();
		ArrayList<Battle> battleList = game.getTodaysBattles();
		for (int i = 1; i <= battleList.size(); i++) {
			aSetter.setNPC();

		}
		game.sleep();
	}

	/**
	 * method to update the player does not update player and NPCs outside of
	 * playState
	 */
	public void update() {
		switch (gameState) {
		case 1:
			user.update(); // PLAYER

			// NPC
			for (int i = 0; i < npc.length; i++) {
				if (npc[i] != null) {
					npc[i].update();
				}
			}
			break;
		}
	}

	/**
	 * Draws game in gamePanel
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		if (gameState == endState) {
			ui.draw(g2);
		} else {

			tileM.draw(g2);

			// NPC
			for (int i = 0; i < npc.length; i++) {
				if (npc[i] != null) {
					npc[i].draw(g2);
				}
			}
			user.draw(g2);
			ui.draw(g2); // draw UI

			if (KeyH.debugPressed == true) { // DEBUG TIMER FINISH
				g2.setColor(Color.white);

				g2.setFont(new Font("Arial", Font.PLAIN, 40));
				int x = 10;
				int y = 400;
				int lineHeight = 40;

				g2.drawString("WorldX: " + user.worldX, x, y);
				y += lineHeight;
				g2.drawString("WorldY: " + user.worldY, x, y);
				y += lineHeight;
				g2.drawString("Column: " + (user.worldX + user.solidArea.x) / tileSize, x, y);
				y += lineHeight;
				g2.drawString("Row: " + (user.worldY + user.solidArea.y) / tileSize, x, y);
				y += lineHeight;

			}
			g2.dispose(); // Save Memory
		}
	}
}
