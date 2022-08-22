package main.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import main.functionality.Player;
import main.functionality.monster.Item;
import main.functionality.monster.Monster;
import main.functionality.monster.Move;
import main.functionality.monster.combat.Battle;
import main.functionality.monster.combat.BattlingMonster;
import main.gui.entity.BattleNPC;

public class BattleUI {
	GamePanel gp;
	Font arial_40;
	Graphics2D g2;
	String options[] = new String[3];

	public int battleNum = 0;

	public int combatMode = 0;
	public final int optionMode = 0;
	public final int fightMode = 1;

	public final int moveMode = 2;
	public int moveModeNum = 0;
	public int selectedMove;

	public final int switchMode = 3;
	public int monsterSwapNum = 0;
	public int selectedMonster = 0;

	public final int itemMode = 4;
	public int monsterUseNum = 0;
	public int itemUseNum = 0;

	public final int loseMode = 5;
	public final int winMode = 6;
	public int monsterResultNum = 0;
	/** BattleMode for using an item **/
	public final int itemUseMode = 7;

	public Item itemSelected = null;

	public Battle battle;
	Player player;
	BattleNPC opponent;

	public Monster currentMonsterPlayer;
	public Monster currentMonsterOpponent;

	ArrayList<Move> moveList;

	public BattleUI(GamePanel gp, BattleNPC opponent) {
		this.gp = gp;
		arial_40 = new Font("Arial", Font.PLAIN, 40);
		this.player = gp.game.getPlayer();
		this.opponent = opponent;
		battle = opponent.battle;
		battle.start();

	}

	public void draw(Graphics2D g2) {

		this.g2 = g2;
		g2.setFont(arial_40);
		g2.setColor(Color.white);
		g2.setBackground(Color.LIGHT_GRAY);

		if (battle.getPlayerMonster().getCurrentHealth() <= 0 && battle.getPlayerBattlers().size() != 0) { // Check if
																											// players
																											// Monster
																											// has
																											// fainted
			combatMode = switchMode; // Opens switch mode
		}
		if (battle.getPlayerBattlers().size() == 0) { // Checks to see if player has lost (All monsters fainted)
			combatMode = loseMode;

		}

		if (battle.getEnemies().size() == 0) { // Checks to see if player has lost (All monsters fainted)
			combatMode = winMode;

		}

		switch (combatMode) { // Switch statement to tell renderer what to do at different states of battle
		case 0:
			drawMonsterImages();
			drawMenu();
			drawBattleInfo();
			drawConsole();
			
			break;
		case 1:
			drawMonsterImages();
			runBattle();
			drawBattleInfo();
			drawConsole();
			break;
		case 2:
			drawMonsterImages();
			drawBattleInfo();
			drawMoves();
			drawConsole();

			break;
		case 3:
			drawMonsterImages();
			drawConsole();
			drawMonsters(monsterSwapNum, battle.getPlayerBattlers());

			break;
		case 4:
			drawMonsterImages();
			drawItems();
			drawConsole();
			break;

		case 5: // loseState
			drawMonsterStatusScreen(monsterResultNum, player.getTeam());
			gp.ui.drawConsole();
			break;
		case 6: // winState
			drawMonsterStatusScreen(monsterResultNum, player.getTeam());
			gp.ui.drawConsole();
			this.opponent.battle = null;
			break;
		case 7:
			drawMonsterMenuScreen(monsterUseNum, battle.getPlayerBattlers());
			break;
		}
	}

	/**
	 * Draws the players Item menu
	 * 
	 * @param items
	 */
	private void drawItems() {
		// TODO Auto-generated method stub
		drawInventoryScreen(itemUseNum);
	}

	/**
	 * Method to draw players inventory
	 */
	private void drawInventoryScreen(int menuSelector) {
		int windowX = 10 * gp.tileSize;
		int windowY = 2 * gp.tileSize;
		int width = 7 * gp.tileSize;
		int height = 13 * gp.tileSize;
		player = gp.game.getPlayer();
		ArrayList<Item> itemList = player.getItems();
		int textSpacing = 50;
		String text;
		int length;

		int x = windowX + 15;
		int y = windowY + 60;

		drawSubWindow(windowX, windowY, width, height);

		windowX += width;
		width = 11 * gp.tileSize;
		drawSubWindow(windowX, windowY, width, height);

		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
		g2.drawString("Items", x, y);

		y += textSpacing;
		if (itemList.size() != 0) { // check if player has no items
			for (int i = 0; i < itemList.size(); i++) {
				g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
				text = itemList.get(i).getName();
				g2.drawString(text, x, y);
				length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
				if (menuSelector == i) {
					g2.drawString("<", x + length + gp.tileSize / 2, y);
					drawInventoryInfo(itemList.get(i));
					g2.setColor(Color.WHITE);
				}
				y += textSpacing;
			}
		}
		drawConsole();

	}

	private void drawInventoryInfo(Item item) {
		// TODO Auto-generated method stub
		String name = "Name: " + item.getName();
		String statToBuff = "Buffs: " + item.getStatToBuff();
		String buffAmount = "Buff amount: " + item.getBuffAmount();
		int textSpacing = 50;

		int x = gp.tileSize * 17 + 20;
		int y = gp.tileSize * 2 + 60;

		g2.drawString(name, x, y);
		y += textSpacing;
		g2.drawString(statToBuff, x, y);
		y += textSpacing;
		g2.drawString(buffAmount, x, y);
		y += textSpacing;

	}

	/**
	 * Draws Player and opponent info
	 */
	private void drawBattleInfo() {
		drawPlayerMonsterInfo(battle.getPlayerMonster());
		drawOpponentMonsterInfo(battle.getEnemyMonster());
	}

	/**
	 * Draws opponent monster info
	 * 
	 * @param opponentMonster opponents current monster
	 */
	private void drawOpponentMonsterInfo(BattlingMonster opponentMonster) {
		// TODO Auto-generated method stub
		int screenX = gp.tileSize * 22;
		int screenY = gp.tileSize;
		int width = gp.tileSize * 10;
		int height = gp.tileSize * 5;
		int textSpacing = 50;

		int x = screenX + 15;
		int y = screenY + 50;

		drawSubWindow(screenX, screenY, width, height);
		
		g2.drawString(opponentMonster.getName(), x, y);
		y += textSpacing;
		g2.drawString(
				String.format("Health: %s/%s", opponentMonster.getCurrentHealth(), opponentMonster.getMaxHealth()), x,
				y);
		y += textSpacing;
		g2.drawString(String.format("Attack: %s", opponentMonster.getAttack()), x, y);
		y += textSpacing;
		g2.drawString(String.format("Type: %s", opponentMonster.getType()), x, y);
		x += gp.tileSize * 6;
		g2.drawString(opponent.name, x, y);
	}

	/**
	 * Draws player monster info
	 * 
	 * @param playerMonster players current monster
	 */
	private void drawPlayerMonsterInfo(BattlingMonster playerMonster) {
		// TODO Auto-generated method stub
		int screenX = gp.tileSize * 8;
		int screenY = gp.tileSize;
		int width = gp.tileSize * 10;
		int height = gp.tileSize * 5;
		int textSpacing = 50;

		int x = screenX + 15;
		int y = screenY + 50;

		drawSubWindow(screenX, screenY, width, height);

		g2.drawString(playerMonster.getName(), x, y);
		y += textSpacing;
		g2.drawString(String.format("Health: %s/%s", playerMonster.getCurrentHealth(), playerMonster.getMaxHealth()), x,
				y);
		y += textSpacing;
		g2.drawString(String.format("Attack: %s", playerMonster.getAttack()), x, y);
		y += textSpacing;
		g2.drawString(String.format("Type: %s", playerMonster.getType()), x, y);

	}

	/**
	 * Runs the battle using players chosen move
	 */
	private void runBattle() {
		// TODO Auto-generated method stub
		Move move = battle.getPlayerMonster().getMoves().get(selectedMove); // get current move
		battle.playerAttack(move); // use player move, method calls enemy move
		combatMode = optionMode; // return to optionMode
	}

	/**
	 * Draw both players monsters in battle mode
	 */
	private void drawMonsterImages() {
		// TODO Auto-generated method stub
		Monster playerMonster = battle.getPlayerMonster();
		Monster opponentMonster = battle.getEnemyMonster();

		drawSubWindow(gp.tileSize * 1, gp.tileSize * 1, gp.tileSize * 7, gp.tileSize * 10);
		g2.drawImage(playerMonster.getImage(), gp.tileSize * 1, gp.tileSize * 3, null);

		drawSubWindow(gp.tileSize * 32, gp.tileSize * 1, gp.tileSize * 7, gp.tileSize * 10);
		g2.drawImage(opponentMonster.getImage(), gp.tileSize * 32, gp.tileSize * 3, null);
	}

	/**
	 * Draws the monster menu
	 * 
	 * @param command   currently selected monster
	 * @param arrayList players monsterList
	 */
	private void drawMonsters(int command, ArrayList<BattlingMonster> arrayList) {

		drawMonsterMenuScreen(command, arrayList);
	}

	/**
	 * Displays available move to use in battle
	 */
	private void drawMoves() {
		// Set top left corner
		int windowX = gp.tileSize * 8;
		int windowY = gp.tileSize * 6;

		ArrayList<Move> moveList = battle.getPlayerMonster().getMoves(); // get move list
		drawSubWindow(windowX, windowY, gp.tileSize * 20, gp.tileSize * 5); // draw background window
		windowY += gp.tileSize;
		windowX += 20;
		g2.drawString("Moves: (Name,Type,Damage,Rarity)", windowX, windowY);
		int x = windowX;
		int y = windowY + gp.tileSize;

		for (int i = 0; i < moveList.size(); i++) { // draw list of moves
			Move move = moveList.get(i);
			String moveName = move.getName();
			String moveRarity = move.getRarity();
			String moveType = move.getType();
			int moveDamage = move.getDamage();
			String text = String.format("%s, %s, %s, %s", moveName, moveType, moveDamage, moveRarity);
			g2.drawString(text, x, y);
			int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth(); // get length of string
			if (moveModeNum == i) {
				g2.drawString("<", x + length + gp.tileSize / 2, y);

			}
			y += 40;
		}
	}

	/**
	 * Draws option menu for Battles
	 */
	private void drawMenu() {
		// set top left corner
		int x = gp.tileSize * 27;
		int y = gp.tileSize * 7;
		int width = gp.tileSize * 5;
		int height = gp.tileSize * 4;

		drawSubWindow(x, y, width, height);
		options[0] = "Fight";
		options[1] = "Monsters";
		options[2] = "Items";
		x += 20;
		y += 60;
		for (int i = 0; i < options.length; i++) {
			g2.drawString(options[i], x, y);

			if (battleNum == i) {
				int length = (int) g2.getFontMetrics().getStringBounds(options[i], g2).getWidth();
				g2.drawString("<", x + length + gp.tileSize / 2, y);
			}
			y += 50;
		}
	}

	/*
	 * Draws console, output is redirected and displayed using System.out (only in
	 * gui) System.err prints to console
	 */
	public void drawConsole() {
		// set top left corner
		int screenX = gp.tileSize;
		int screenY = gp.tileSize * 11;
		int x = screenX + 20;
		int y = screenY + 60;

		int width = gp.tileSize * 38;
		int height = gp.tileSize * 9;

		g2.setBackground(Color.GRAY);

		drawSubWindow(screenX, screenY, width, height); // draw background window

		String[] consoleOutput = gp.game.guiOutput.toString().split("\n");// Since drawString ignores newlines (\n) it
																			// is done manually
		for (String line : consoleOutput) {
			g2.drawString(line, x, y);
			y += 40;
		}
	}

	/**
	 * Draws the monster menu screen in battles
	 * 
	 * @param command     currently selected monster
	 * @param monsterList players monster list
	 */
	private void drawMonsterMenuScreen(int command, ArrayList<BattlingMonster> monsterList) {
		// Set top left corner
		int windowX = 10 * gp.tileSize;
		int windowY = 2 * gp.tileSize;
		int width = 7 * gp.tileSize;
		int height = 13 * gp.tileSize;
		player = gp.game.getPlayer();

		int textSpacing = 50;
		String text;
		int length;

		int x = windowX + 15;
		int y = windowY + 60;

		drawSubWindow(windowX, windowY, width, height / 2);

		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
		g2.drawString("Monsters:", x, y);

		windowX += width;
		width = gp.tileSize * 13;
		drawSubWindow(windowX, windowY, width, height);

		y += textSpacing;

		for (int i = 0; i < monsterList.size(); i++) {
			Monster monster = monsterList.get(i);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
			text = monster.getName();
			g2.drawString(text, x, y);
			length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			if (command == i) {
				g2.drawString("<", x + length + gp.tileSize / 2, y);
				drawMonsterInfo(monster);
				drawMonsterMoves(monster, windowX, windowY);
			}
			y += textSpacing;
		}
	}

	private void drawMonsterStatusScreen(int command, ArrayList<Monster> monsterList) {
		// TODO Auto-generated method stub
		int windowX = 10 * gp.tileSize;
		int windowY = 2 * gp.tileSize;
		int width = 7 * gp.tileSize;
		int height = 13 * gp.tileSize;
		player = gp.game.getPlayer();

		int textSpacing = 50;
		String text;
		int length;
		
		drawSubWindow(windowX, 0, width+ gp.tileSize*13, 2*gp.tileSize); //draws box to hold status message
		text ="Your team's after battle status";
		int x = gp.tileSize*15;
		int y = 5*gp.tileSize/4;
		g2.drawString(text, x,y);
		
		
		x = windowX + 15;
		y = windowY + 60;

		drawSubWindow(windowX, windowY, width, height / 2);

		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
		g2.drawString("Monsters:", x, y);

		windowX += width;
		width = gp.tileSize * 13;
		drawSubWindow(windowX, windowY, width, height);

		y += textSpacing;

		for (int i = 0; i < monsterList.size(); i++) {
			Monster monster = monsterList.get(i);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
			text = monster.getName();
			g2.drawString(text, x, y);
			length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			if (command == i) {
				g2.drawString("<", x + length + gp.tileSize / 2, y);
				drawMonsterInfo(monster);
				drawMonsterMoves(monster, windowX, windowY);
			}
			y += textSpacing;
		}
	}

	private void drawMonsterMoves(Monster monster, int windowX, int windowY) {
		// TODO Auto-generated method stub
		ArrayList<Move> moveList = monster.getMoves();

		windowY += gp.tileSize * 8;
		drawSubWindow(windowX, windowY, gp.tileSize * 13, gp.tileSize * 5);
		windowY += gp.tileSize;
		windowX += 20;
		g2.drawString("Moves: (Name,Type,Damage,Rarity)", windowX, windowY);
		int x = windowX;
		int y = windowY + gp.tileSize;

		for (int j = 0; j < moveList.size(); j++) {
			Move move = moveList.get(j);
			String moveName = move.getName();
			String moveRarity = move.getRarity();
			String moveType = move.getType();
			int moveDamage = move.getDamage();
			g2.drawString(String.format("%s, %s, %s, %s", moveName, moveType, moveDamage, moveRarity), x, y);
			y += 40;
		}

	}

	/**
	 * sub method of drawMonsterMenu to draw right half of menu
	 * 
	 * @param i index of monsterList to be drawn
	 */
	public void drawMonsterInfo(Monster monster) {
		String name = monster.getName();
		String type = monster.getType();
		String rarity = monster.getRarity();
		int currentHealth = monster.getCurrentHealth();
		int maxHealth = monster.getMaxHealth();
		int attack = monster.getAttack();

		int x = gp.tileSize * 17 + 20;
		int y = gp.tileSize * 2 + 20;

//		getRarityColour(rarity);

		// converts imageStream into an image
		g2.drawImage(monster.getImage(), x, y, 320, 320, null, gp);

		g2.setColor(Color.white);
		x += 320 + 10;

		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));

		y += 30;
		g2.drawString(name, x, y);

		y += 30;
		g2.drawString("Rarity: " + rarity, x, y);

		y += 30;
		g2.drawString("Type: " + type, x, y);

		y += 30;
		g2.drawString("Health: " + currentHealth + "/" + maxHealth, x, y);

		y += 30;
		g2.drawString("Attack: " + attack, x, y);

	}

	public void drawSubWindow(int x, int y, int width, int height) {

		Color c = new Color(0, 0, 0, 180); // Set dialogue window color here (R, G, B, TRANSPARENCY)
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);

		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
	}
}
