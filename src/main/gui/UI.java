package main.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import java.util.Arrays;

import main.functionality.monster.Item;
import main.functionality.monster.Monster;
import main.functionality.monster.Move;
import main.functionality.Game;
import main.functionality.Player;
import main.functionality.Shop;

/**
 * 
 * @author Haydn, Max User interface Class to display all menus and windows
 *
 */
public class UI {

	GamePanel gp;
	Font arial_40;
	Graphics2D g2;

	public Shop shop;

	public int commandNum = 0; // TODO javadoc for public variables
	public int monsterNum = 0;
	public int itemNum = 0;
	public int shopNum = 0;
	public int dialogueNum = 0;
	public int sellNum = 0;
	public int sellMonNum = 0;
	public int monsterUseNum = 0;

	public boolean messageOn = false;
	public String message = "";
	int messageCounter = 0;
	public String currentDialogue;
	public Player player;
	ArrayList<Monster> monsterList;
	ArrayList<Item> itemList;
	public Item itemSelected = null;

	public String options[] = new String[3];

	public String keyBinds[] = new String[5];

	/**
	 * UI constructor
	 * 
	 * @param gp GamePanel
	 */
	public UI(GamePanel gp) {
		this.gp = gp;

		arial_40 = new Font("Arial", Font.PLAIN, 40);
	}

	/**
	 * debug ui, not used in final game
	 * 
	 * @param text String to show on screen
	 */
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}

	/**
	 * method to toggle which UI screen to draw based on current gamestate
	 * 
	 * @param g2 Graphics 2D
	 */
	public void draw(Graphics2D g2) {

		this.g2 = g2;
		g2.setFont(arial_40);
		g2.setColor(Color.white);
		shop = gp.game.getShopToday();

		switch (gp.gameState) {

		case 0: // titleState
			drawHelpScreen();
			break;

		case 1: // playState
			drawHUD();
			break;

		case 2: // consolePause
			drawConsolePause();
			break;

		case 3: // shopState
			Arrays.fill(keyBinds, null); // Clear previous keyBind array
			// set current keyBind options
			keyBinds[0] = "W&S: Scroll";
			keyBinds[1] = "E: Buy";
			keyBinds[2] = "ESC: Back";
			drawKeybindHints(gp.tileSize, gp.tileSize * 2); // draw keyBind hints with current options
			drawShopScreen();
			break;

		case 4: // dialogueState
			if (gp.dialogueMode == gp.shopMode) {
				drawDialogueScreen(3);
			} else if (gp.dialogueMode == gp.sleepMode) {
				drawDialogueScreen(2);
			} else {
				drawDialogueScreen(2);
			}
			break;

		case 5: // menuState
			drawMenuScreen();

			Arrays.fill(keyBinds, null); // Clear previous keyBind array
			// set current keyBind options
			keyBinds[0] = "W&S: Scroll";
			keyBinds[1] = "E: Select";
			keyBinds[2] = "ESC: Back";

			drawKeybindHints(gp.tileSize, gp.tileSize * 2); // draw keyBind hints with current options
			break;
		case 6: // monsterMenuState
			drawMonsterMenuScreen(monsterNum); // Clear previous keyBind array
			Arrays.fill(keyBinds, null);
			// set current keyBind options
			keyBinds[0] = "W&S: Scroll";
			keyBinds[1] = "R: Rename";
			keyBinds[2] = "ESC: Back";
			keyBinds[3] = "T: Switch";

			drawKeybindHints(gp.tileSize, gp.tileSize * 2); // draw keyBind hints with current options
			break;
		case 7: // menuState
			drawInventoryScreen(itemNum);
			Arrays.fill(keyBinds, null); // Clear previous keyBind array
			// set current keyBind options
			keyBinds[0] = "W&S: Scroll";
			keyBinds[1] = "R: Use Item";
			keyBinds[2] = "ESC: Back";

			drawKeybindHints(gp.tileSize, gp.tileSize * 2); // draw keyBind hints with current options
			break;
		case 8:
			
			break;
		case 9:
			drawSellScreen(); // Draw Shop Sell Screen
			Arrays.fill(keyBinds, null); // Clear previous keyBind array
			// set current keyBind options
			keyBinds[0] = "W&S: Scroll";
			keyBinds[1] = "E: Sell";
			keyBinds[2] = "ESC: Back";
			drawKeybindHints(gp.tileSize, gp.tileSize * 2); // draw keyBind hints with current options
			break;
		case 10:
			drawMonsterSellScreen();
			Arrays.fill(keyBinds, null); // Clear previous keyBind array
			// set current keyBind options
			keyBinds[0] = "W&S: Scroll";
			keyBinds[1] = "E: Sell";
			keyBinds[2] = "ESC: Back";
			drawKeybindHints(gp.tileSize, gp.tileSize * 2); // draw keyBind hints with current options
			break;
		case 11:
			drawSleepScreen();
			break;
		case 12:
			gp.battleUI.draw(g2);
			break;
		case 13:
			useItem();
		}
	}

	public void useItem() {
		// TODO Auto-generated method stub
		
		drawMonsterMenuScreen(monsterUseNum);
	}

	private void drawHelpScreen() {
		// TODO Auto-generated method stub
		int x = gp.tileSize * 10;
		int y = gp.tileSize * 3;
		int width = gp.tileSize * 20;
		int height = gp.tileSize * 15;

		drawSubWindow(x, y, width, height);

		x += 20;
		y += 60;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
		g2.drawString("Welcome " + gp.game.getPlayer().getName(), x, y);
		y += 60;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
		g2.drawString("Battle players to earn Gold and points.", x, y);
		y += 70;
		g2.drawString("Use your gold to buy Monsters and Items", x, y);
		y += 50;
		g2.drawString("from the Old Man in the Shop.", x, y);
		y += 70;
		g2.drawString("Move your Player with the W,A,S,D Keys", x, y);
		y += 50;
		g2.drawString("Access the Menu or Pause with the ESC Key", x, y);
		y+= 70;
		g2.drawString("Interact with NPCs or select options with the E Key", x, y);
		y+= 50;
		g2.drawString("Interact with the bed in the house next to the shop",x,y);
		y+= 50;
		g2.drawString("to move to the next day",x,y);
		y+= 130;
		g2.drawString("Show this screen again by choosing Help in the menu", x, y);
		y += 50;
		g2.drawString("Press the ESC key to continue your Adventure", x, y);
		y += 50;

	}

	private void drawSleepScreen() {
		// TODO Auto-generated method stub
		gp.ui.chooseDialogueOption(2);
	}

	private void drawMonsterSellScreen() {
		// TODO Auto-generated method stub
		drawMonsterMenuScreen(sellMonNum);
	}

	private void drawSellScreen() {
		drawInventoryScreen(sellNum);
	}

	/**
	 * method to draw Shop screen
	 */
	private void drawShopScreen() {
		int windowX = 10 * gp.tileSize;
		int windowY = 2 * gp.tileSize;
		int width = 7 * gp.tileSize;
		int height = 15 * gp.tileSize;

		shop = gp.game.getShopToday();
		ArrayList<Monster> monstersOnSale = shop.getMonstersOnSale();
		ArrayList<Item> itemsOnSale = shop.getItemsOnSale();

		int textSpacing = 50;
		String text;
		int length;
		Monster monster = null;// = monstersOnSale.get(0);
		Item item = null;// = itemsOnSale.get(0);

		drawSubWindow(windowX, gp.tileSize * 11, gp.tileSize * 7, gp.tileSize * 2);
		g2.drawString("Gold: " + Integer.toString(gp.game.getPlayer().getGold()), windowX + 20, gp.tileSize * 11 + 60);
		int x = windowX + 15;
		int y = windowY + 60;

		drawSubWindow(windowX, windowY, width, height - gp.tileSize * 6); // shop list box

		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
		g2.drawString("Shop:", x, y);

		windowX += width;
		width = gp.tileSize * 13;
		drawSubWindow(windowX, windowY, width, height - gp.tileSize * 2); // shop info box

		y += textSpacing;
		int monListSize = monstersOnSale.size();
		int itemListSize = itemsOnSale.size();
		int shopListSize = monListSize + itemListSize;

		for (int i = 0; i < shopListSize; i++) {

			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
			if (i < monListSize) {
				monster = monstersOnSale.get(i);
				text = monster.getName();

			} else {
				item = itemsOnSale.get(i - monListSize);
				text = item.getName();

			}
			g2.drawString(text, x, y);
			length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

			if (shopNum == i) {
				if (i < monListSize) {
					drawMonsterInfo(monster);
					drawMonsterMoves(monster, windowX, windowY);

				} else {
					drawItemInfo(item);
				}
				g2.drawString("<", x + length + gp.tileSize / 2, y);
				g2.setColor(Color.WHITE);
			}
			y += textSpacing;
		}
		drawConsole();
	}

	/**
	 * Draws a console for the guiOutput from system.out.print to be displayed
	 */
	public void drawConsole() {
		int screenX = gp.tileSize;
		int screenY = gp.tileSize * 15;
		int x = screenX + 20;
		int y = screenY + 60;

		int width = gp.tileSize * 38;
		int height = gp.tileSize * 5;

		g2.setBackground(Color.GRAY);

		drawSubWindow(screenX, screenY, width, height);

		String[] consoleOutput = Game.guiOutput.toString().split("\n");// Since drawString ignores newlines (\n) it
																			// is done manually
		for (String line : consoleOutput) {
			g2.drawString(line, x, y);
			y += 40;
		}
	}

	public void drawConsolePause() {
		gp.ui.drawConsole();
		gp.gameState = gp.consolePauseState;
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
		itemList = player.getItems();
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
					drawItemInfo(itemList.get(i));
					g2.setColor(Color.WHITE);
				}
				y += textSpacing;
			}
		}
		drawConsole();

	}

	private void drawItemInfo(Item item) {
		// TODO Auto-generated method stub
		String name = "Name: " + item.getName();
		String statToBuff = "Buffs: " + item.getStatToBuff();
		String buffAmount = "Buff amount: " + item.getBuffAmount();
		String sellP ="Sell price: "+ item.getSellPrice();
		String buyP = "Price: "+item.getBuyPrice();
		int textSpacing = 50;

		int x = gp.tileSize * 17 + 20;
		int y = gp.tileSize * 2 + 60;

		g2.drawString(name, x, y);
		y += textSpacing;
		g2.drawString(statToBuff, x, y);
		y += textSpacing;
		g2.drawString(buffAmount, x, y);
		y += textSpacing;
		if( gp.gameState == gp.shopSellItemsState){
			g2.drawString(sellP, x, y);
		}
		if(gp.gameState==gp.shopBuyState) {
			g2.drawString(buyP, x, y);
		}

	}

	/**
	 * Draws MonsterMenuScreen when gameState = monsterMenuState
	 * 
	 */
	private void drawMonsterMenuScreen(int command) {
		// TODO Auto-generated method stub
		int windowX = 10 * gp.tileSize;
		int windowY = 2 * gp.tileSize;
		int width = 7 * gp.tileSize;
		int height = 13 * gp.tileSize;
		player = gp.game.getPlayer();
		monsterList = player.getTeam();
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
				drawMonsterMoves(monster, windowX, windowY); // TODO reenable
				g2.setColor(Color.WHITE);
			}
			y += textSpacing;
		}
		drawConsole();
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
		int healAmount = monster.getHealAmount();
		int buyPrice = monster.getBuyPrice();

		int x = gp.tileSize * 17 + 20;
		int y = gp.tileSize * 2 + 20;

		getRarityColour(rarity);

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

		y += 30;
		g2.drawString("Heal amount: " + healAmount, x, y);

		if (gp.gameState == gp.shopBuyState) {
			y += 30;
			g2.drawString("Price: " + buyPrice + " gold", x, y);
		}
		if (gp.gameState == gp.shopSellMonstersState) {
			y += 30;
			int sellP = monster.getSellPrice();
			g2.drawString("Sell price: " + sellP + " gold", x, y);
		}

	}

	/**
	 * sets color of background based on rarity
	 * 
	 * @param rarity Monster rarity
	 */
	private void getRarityColour(String rarity) {
		switch (rarity) {
		case "Common":
			g2.setColor(Color.GRAY);
			break;

		case "Uncommon":
			g2.setColor(Color.GREEN);
			break;

		case "Rare":
			g2.setColor(Color.BLUE);
			break;

		case "Super-Rare":
			// TODO add RGB value for purple
			g2.setColor(Color.magenta);
			break;

		case "Legendary":
			g2.setColor(Color.YELLOW);
			break;
		}
	}

	private void drawMenuScreen() {
		// TODO Auto-generated method stub
		int x = 32 * gp.tileSize;
		int y = 2 * gp.tileSize;
		int width = 7 * gp.tileSize;
		int height = 15 * gp.tileSize;
		Player player = gp.game.getPlayer();
		String text;
		int menuSpacing = 60;
		int length;

		drawSubWindow(x, y, width, height);

		String playerName = player.getName();
		int score = player.getScore();
		int money = player.getGold();
		int currentDay = gp.game.getDay();
		int totalDays = gp.game.getNumberOfDays();

		x += 20;
		y += 65;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
		g2.drawString(playerName, x, y);
		y += menuSpacing;
		g2.drawString("Day " + currentDay + "/" + totalDays, x, y);
		y += menuSpacing;
		g2.drawString("Score: " + score, x, y);
		y += menuSpacing;
		g2.drawString("Gold: " + money, x, y);

		// Menu options
		text = "Monsters";
		y += menuSpacing * 2;
		g2.drawString(text, x, y);
		length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		if (commandNum == 0) {
			g2.drawString("<", x + length + gp.tileSize / 2, y);
		}

		text = "Inventory";
		y += menuSpacing;
		g2.drawString(text, x, y);
		length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		if (commandNum == 1) {
			g2.drawString("<", x + length + gp.tileSize / 2, y);
		}

		text = "Help";
		y += menuSpacing;
		g2.drawString(text, x, y);
		length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		if (commandNum == 2) {
			g2.drawString("<", x + length + gp.tileSize / 2, y);
		}

		text = "Quit";
		y += menuSpacing;
		g2.drawString(text, x, y);
		length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		if (commandNum == 3) {
			g2.drawString("<", x + length + gp.tileSize / 2, y);
		}
	}

	private void drawHUD() {
		// TODO Auto-generated method stub
		int x = 1 * gp.tileSize;
		int y = 19 * gp.tileSize;
		int width = 5 * gp.tileSize;
		int height = 2 * gp.tileSize;

		drawSubWindow(x, y, width, height);

		int currentDay = gp.game.getDay();
		int maxDays = gp.game.getNumberOfDays();

		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
		g2.drawString("Day " + currentDay + "/" + maxDays, x + 20, y + 65);
	}

	public void drawTitleScreen() {
//		startPanel();
	}

	/**
	 * Draws the dialogue screen
	 * 
	 * @param optionAmount the amount of option that should be available in this
	 *                     dialogue screen
	 */
	private void drawDialogueScreen(int optionAmount) {
		// TODO Auto-generated method stub
		// WINDOW

		int x = gp.tileSize * 2;
		int y = gp.tileSize / 2;
		int width = gp.screenWidth - (gp.tileSize * 4);
		int height = gp.tileSize * 5;

		drawSubWindow(x, y, width, height);
		gp.ui.chooseDialogueOption(optionAmount);// sets which dialogue option is currently selected
		x += gp.tileSize;
		y += gp.tileSize;

		for (String line : currentDialogue.split("\n")) { // Since drawString ignores newlines (\n) it is done manually
			g2.drawString(line, x, y);
			y += 40;
		}
	}

	public void chooseDialogueOption(int optionAmount) {
		int x = gp.tileSize * 25;
		int y = gp.tileSize * 4;
		int height = gp.tileSize * optionAmount;
		int maxLength = 0;

		// draws the console for specified dialogue modes

		// pick longest string for width
		for (int i = 0; i < options.length; i++) {
			if (options[i] != null) {
				int length = (int) g2.getFontMetrics().getStringBounds(options[i], g2).getWidth();
				if (length > maxLength) {
					maxLength = length;
				}
			} else {
				break;
			}

		}
		drawSubWindow(x, y, maxLength + gp.tileSize, height);

		x += 10;
		y += gp.tileSize;

		for (int i = 0; i < options.length; i++) {
			if (options[i] != null) {
				g2.drawString(options[i], x, y);
			} else {
				break;
			}
			if (gp.ui.dialogueNum == i) {
				g2.drawString("<", x + maxLength + gp.tileSize / 2, y);
			}
			y += 40;
		}

	}

	public void drawKeybindHints(int x, int y) {
		int width = gp.tileSize * 5;
		int height = gp.tileSize;

		for (int i = 0; i < keyBinds.length; i++) { // loop through keyBinds Array to get height without including null
													// values
			if (keyBinds[i] != null) {
				height += 40;
			}
		}
		drawSubWindow(x, y, width, height);

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
		for (int i = 0; i < keyBinds.length; i++) { // loop through keyBinds Array to show Available options
			if (keyBinds[i] != null) {
				g2.drawString(keyBinds[i], x + 15, y + 50);
			}
			y += 40;

		}

	}

	/**
	 * Draws black box and white border for all UI windows
	 * 
	 * @param x      x value of top left point
	 * @param y      y value of top left point
	 * @param width  width of box (pixels)
	 * @param height height of box (pixels)
	 */
	public void drawSubWindow(int x, int y, int width, int height) {

		Color c = new Color(0, 0, 0, 180); // Set dialogue window color here (R, G, B, TRANSPARENCY)
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);

		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
	}

	/**
	 * Returns the int value of the middle of a string for rendering nicely
	 * 
	 * @param text
	 * @return the int value of the middle of a string for rendering nicely
	 */
	public int getXCentered(String text) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth / 2 - length / 2;
		return x;
	}

}
