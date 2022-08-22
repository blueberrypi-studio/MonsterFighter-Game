package main.functionality;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import main.functionality.monster.Item;
import main.functionality.monster.Monster;
import main.functionality.monster.Move;
import main.functionality.monster.combat.Battle;
import main.gui.GameWindow;
import main.gui.StartMenu;

/**
 * The game class is the main class for the monster fighter game. It is created
 * on program launch and responsible for holding important information that
 * should exist for the game from start to finish. The game is responsible for
 * controlling all window actions.
 */
public class Game {
	private static boolean doGUI = true; // toggle cli
	/** The name of the user who is playing this game */
	private Player player;
	/** The shop for today in this game */
	private Shop shopToday;
	/** The maximum number of days this game will run for */
	private int numberOfDays = 0;
	/** The current day of this game */
	private int day = 1;
	/** The difficulty setting of this game: easy =1 , normal =2 , hard =3 */
	private int difficulty = 0;
	// private StartMenu startMenu;
	// private GameWindow gameWindow;
	// private ShopWindow shopWindow;
	/** The list of battles for the player to participate in today */
	private ArrayList<Battle> todaysBattles = new ArrayList<Battle>();
	/** The scanner responsible for reading player input from the command line */
	private Scanner playerInput = new Scanner(System.in);
	/**
	 * The ByteArrayOutput stream responsible for holding the text outputted by
	 * System.out.print
	 */
	public static ByteArrayOutputStream guiOutput;

	// variables used for mass data storage
	/** The list of moves that can be chosen when moves are randomly generated */
	private static ArrayList<Move> moves = new ArrayList<Move>();
	/**
	 * The list of monsters that can be chosen when monsters are randomly generated
	 */
	private static ArrayList<Monster> monsters = new ArrayList<Monster>();
	/**
	 * A table that contains probabilities that determine how likely a certain
	 * monster rarity is to be generated based on the day
	 */
	private static ConcurrentHashMap<Integer, ArrayList<Float>> monsterProbs = new ConcurrentHashMap<Integer, ArrayList<Float>>();
	/**
	 * A table that contains probabilities that determine how likely a certain move
	 * rarity is to be generated based on the rarity of the monster it is being
	 * generated for
	 */
	private static ConcurrentHashMap<String, ArrayList<Float>> moveProbs = new ConcurrentHashMap<String, ArrayList<Float>>();
	/**
	 * A list containing the allowed types for monsters and moves in the game to
	 * generate with
	 */
	private static ArrayList<String> types = new ArrayList<String>(
			Arrays.asList("Ice", "Fire", "Water", "Dark", "Light", "Earth", "Thunder", "Normal"));
	/** A list containing all the different rarity's allowed in the game */
	private static ArrayList<String> rarityList = new ArrayList<String>(
			Arrays.asList("Common", "Uncommon", "Rare", "Super-Rare", "Legendary"));
	/**
	 * A list containing all of the items in the game that can be randomly generated
	 */
	private static ArrayList<Item> items = new ArrayList<Item>();
	/**
	 * A table that contains the combat relationship data for move type damage
	 * depending on a targets type check the{@link FileReader#readTypeCombatTable()}
	 * method for more detail on how this works
	 */
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, Float>> combatTypeRelationships = new ConcurrentHashMap<String, ConcurrentHashMap<String, Float>>();
	/**
	 * A list containing all of the NPC names in the game that can be randomly
	 * generated
	 */
	private static ArrayList<String> npcNames = new ArrayList<String>(
			Arrays.asList("Bob", "Jeff", "Eric", "Matt", "John", "Jackson", "Alex"));

	/**
	 * creates a new game and loads the essential files
	 **/
	public Game() {
		LoadFiles();
	}

	/**
	 * the main method. Runs the game
	 * 
	 * @param args The command line arguments.
	 **/
	public static void main(String[] args) {
		Game game = new Game();
		if (!doGUI) {
			game.setUpGame();
		} else {
			game.guiStart();
		}

	}

	public void guiStart() {
		// welcomes the player to the game with a new shop and 3 battles to fight
		guiOutput = new ByteArrayOutputStream();
		PrintStream nOutStream = new PrintStream(guiOutput);
		System.setOut(nOutStream);
		launchStartMenu();
	}

	/**
	 * launches the Start menu
	 */
	public void launchStartMenu() {
		StartMenu startMenu = new StartMenu(this);
	}

	/**
	 * closes the start menu
	 */
	public void closeStartMenu(StartMenu startMenu) {
		startMenu.closeWindow();
		launchGameWindow();
	}

	/**
	 * launches the game window
	 */
	public void launchGameWindow() {
		GameWindow gameWindow = new GameWindow(this);
	}

	/**
	 * closes the game window
	 */
	public void closeGameWindow(GameWindow gameWindow) {
		gameWindow.closeWindow();
	}

	/**
	 * A Command line method that sets up the game by prompting the player to choose a name, difficulty, game
	 * length and starter monster. This method should only be ran in the command line version of the application
	 * in the GUI version of the application the start menu class replaces this methods use.
	 */
	public void setUpGame() {
		// sets the players name
		System.out.println("Please enter your name:");
		String playerName = playerInput.nextLine();

		// sets the difficulty setting
		while (difficulty < 1 || difficulty > 3) {
			System.out.println("Please select the number that represents the difficulty you want to play on");
			String[] playerDiffOptions = { "Easy", "Medium", "Hard" };
			for (int i = 0; i < 3; i++) {
				System.out.println((i + 1) + ": " + playerDiffOptions[i]);
			}
			try {
				setDifficulty(Integer.parseInt(playerInput.nextLine()));
			} catch (NumberFormatException e) {
				System.out.println("That is not a difficulty option number");
			}
		}
		// sets the game length
		while (numberOfDays < 3 || numberOfDays > 15) {
			System.out.println("Please enter the amount of days you want the game to go for.");
			System.out.println("Game length can range from 3 to 15 days");
			try {
				numberOfDays = Integer.parseInt(playerInput.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("That is not a difficulty option number");
			}
		}
		// sets the starter monster
		Monster starter;
		ArrayList<Monster> starterList = generateStarters();
		int starterToChoose = 0;
		while (starterToChoose > 3 || starterToChoose < 1) {
			System.out.println("Please select the number that represents the starter monster you want to choose");
			System.out.println("1: " + starterList.get(0).getDescription());
			System.out.println("2: " + starterList.get(1).getDescription());
			System.out.println("3: " + starterList.get(2).getDescription());
			try {
				starterToChoose = Integer.parseInt(playerInput.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("That is not a difficulty option number");
			}
		}
		starter = starterList.get(starterToChoose - 1);

		// renames the starter monster if the user wants to
		String answer = "";
		while (!(answer.equals("1") || answer.equals("2"))) {
			System.out.println("Would you like to name your starter?");
			System.out.println("Please input the number that corresponds to the action you want");
			System.out.println("1: Yes\n2: No");
			answer = playerInput.nextLine();
		}
		if (answer.equals("1")) {
			renameMonster(starter);
		}
		// creates a player object to represent the user and sets its gold based on the
		// difficulty selected
		player = new Player(playerName, starter);
		player.setGold(60 / difficulty);

		// welcomes the player to the game with a new shop and 3 battles to fight
		refreshShop();
		player.setBattlesLeft(3);
		generateBattles();
		System.out.println("Welcome to day " + day + " " + player.getName() + "\n");
		presentOptions();

	}

	/**
	 * generates the list of available starter monsters
	 * 
	 * @return a list of available starter monsters
	 */

	public ArrayList<Monster> generateStarters() {
		ArrayList<Monster> starters = new ArrayList<Monster>();
		Monster starter;

		// sets up starter 1 and adds it to starters
		starter = new Monster("Arctic-Beast", "Ice", 200, 100, 15, "Uncommon");
		ArrayList<Move> monsterMoves = new ArrayList<Move>();
		for (int i = 0; i < 2; i++) {
			monsterMoves.add(new Move("Tackle", 50, "Normal", "Common"));
			monsterMoves.add(new Move("Freeze", 50, "Ice", "Common"));
		}
		starter.setMoves(monsterMoves);
		starters.add(starter);

		// sets up starter 2 and add its to starters
		starter = new Monster("Gilled-Beast", "Water", 200, 100, 15, "Uncommon");
		monsterMoves = new ArrayList<Move>();
		for (int i = 0; i < 2; i++) {
			monsterMoves.add(new Move("Tackle", 50, "Normal", "Common"));
			monsterMoves.add(new Move("Steady-Flow", 50, "Water", "Common"));
		}
		starter.setMoves(monsterMoves);
		starters.add(starter);

		// sets up starter 3 and its to starters
		starter = new Monster("Flaming-Beast", "Fire", 200, 100, 15, "Uncommon");
		monsterMoves = new ArrayList<Move>();
		for (int i = 0; i < 2; i++) {
			monsterMoves.add(new Move("Tackle", 50, "Normal", "Common"));
			monsterMoves.add(new Move("Melt", 50, "Fire", "Common"));
		}
		starter.setMoves(monsterMoves);
		starters.add(starter);

		return (starters);
	}

	/**
	 * Loads the files needed for the game to run
	 */
	public void LoadFiles() {
		String resFilepath = "/main/resources/";
		try {
			InputStream file = getClass().getResourceAsStream(resFilepath + "MoveList.csv");
			moves = FileReader.readMoveList(file);

			file = getClass().getResourceAsStream(resFilepath + "MonsterList.csv");
			monsters = FileReader.readMonsterList(file);

			file = getClass().getResourceAsStream(resFilepath + "MonsterProbs.csv");
			monsterProbs = FileReader.readMonsterProbs(file);

			file = getClass().getResourceAsStream(resFilepath + "MoveProbs.csv");
			moveProbs = FileReader.readMoveProbs(file);

			file = getClass().getResourceAsStream(resFilepath + "ItemList.csv");
			items = FileReader.readItemList(file);

			file = getClass().getResourceAsStream(resFilepath + "TypeCombatTable.csv");
			combatTypeRelationships = FileReader.readTypeCombatTable(file);
		} catch (NullPointerException e) {
			System.out.println("Could not find specified file");
			e.printStackTrace();
		}
	}

	/**
	 * Refreshes the shop with new items and monsters based on the day
	 */

	public void refreshShop() {
		shopToday = new Shop(day);
		shopToday.setPlayer(player);
	}

	/**
	 * Command line method that renames a monster to a string the player inputs
	 * @param monToName the monster to rename
	 */
	public void renameMonster(Monster monToName) {
		System.out.println("Please input what you want to rename your monster:");
		String newName = playerInput.nextLine();
		monToName.setName(newName);
	}

	/**
	 * Moves the game to the next day
	 */
	public void sleep() {
		if (day >= numberOfDays) {
			endGame();
		} else {
			day += 1;
			// heals the players monsters overnight
			for (Monster mon : player.getTeam()) {
				int healthBefore = mon.getCurrentHealth();
				mon.setCurrentHealth(healthBefore + mon.getHealAmount());
				if (mon.getCurrentHealth() > mon.getMaxHealth()) {
					mon.setCurrentHealth(mon.getMaxHealth());
				}
			}
			// rolls random events
			RandomEvent possibleEvent = new RandomEvent(player, day);
			possibleEvent.decideEvents();
			// subtracts score based on the amount of faints for the day and then resets the
			// players faint count
			int scoreToLose = (player.getTodaysFaints() * 50);
			System.out.println("You lost " + scoreToLose + " score points due to your monsters fainting "
					+ player.getTodaysFaints() + " times");
			player.setScore(player.getScore() - scoreToLose);
			player.resetTodaysFaintCount();
			// welcomes the player to the new day and presents their option
			System.out.println("Welcome to day " + day + " " + player.getName() + "\n");
			refreshShop();
			player.setBattlesLeft(3);
			generateBattles();
			if (!doGUI) {
				presentOptions();
			}

		}

	}

	/**
	 * generates a list of battles available to the player
	 */
	public void generateBattles() {
		todaysBattles = new ArrayList<Battle>();
		Random rng = new Random();
		for (int i = 0; i < 3; i++) { // 3 battles per day
			String npcName = npcNames.get(rng.nextInt(npcNames.size()));
			Battle battleToAdd = new Battle(player, day, difficulty, npcName);
			todaysBattles.add(battleToAdd);
		}
	}

	/**
	 * Command Line application method that presents all of the options that the player can perform.
	 */
	private void presentOptions() {
		System.out.println("Hi " + player.getName() + " what would you like to do");
		System.out.println("1: Visit shop");
		System.out.println("2: View todays battles");
		System.out.println("3: View/Rename/Reorder your team");
		System.out.println("4: View game stats");
		System.out.println("5: View your inventory");
		System.out.println("6: Go to next day");
		int optionAmount = 6;
		String playerAction = "";
		Boolean validOption = false;
		int optionIndex = 100;
		while (validOption == false) {
			System.out.println("Please select a number corresponding to the action you would like to do");
			playerAction = playerInput.nextLine();
			if (playerAction.equals("TERMINATE")) { // causes the game to terminate
				playerInput.close();
				return;
			}
			try {
				optionIndex = Integer.parseInt(playerAction);
				if (optionIndex > optionAmount || optionIndex <= 0) {
					System.out.println("That is not a valid option");
				} else {
					validOption = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("That is not a valid number");
				presentOptions();
			}
		}
		if (optionIndex == 1) {
			viewShop();
		} else if (optionIndex == 2) {
			viewTodaysBattles();

		} else if (optionIndex == 3) {
			viewRenameTeam();
		} else if (optionIndex == 4) {
			// view the stats of the player and the game
			while (!playerAction.equals("Back")) {
				System.out.println(player.getName() + "'s Game stats:");
				System.out.println("Gold: " + player.getGold());
				System.out.println("Score: " + player.getScore());
				System.out.println("Current day: " + day);
				System.out.println("Days remaining: " + (numberOfDays - day));
				System.out.println("Type Back to return to the options menu");
				playerAction = playerInput.nextLine();
			}
			presentOptions();
		} else if (optionIndex == 5) {
			// view the players inventory
			viewInventory();
		} else if (optionIndex == 6) {
			// moves to the next day
			sleep();
		}
	}

	/**
	 * Command line method that presents the player with their inventory and gives
	 * them the option to use their items
	 */
	public void viewInventory() {
		// view the players inventory
		String playerAction = "";
		boolean validAction = false;
		if (player.getItems().size() == 0) {
			System.out.println("You have no items in your inventory");
			presentOptions();
		}
		while (!validAction) {
			System.out.println(player.getName() + "'s Inventory:");
			for (int i = 0; i < player.getItems().size(); i++) {
				System.out.println((i + 1) + ": " + player.getItems().get(i).getDescription());// prints a description
																								// of the item linking
																								// to a number
			}
			System.out.println("\nType Back to return to the options menu");
			System.out.println("Or type in a number corresponding to the item you want to use");

			playerAction = playerInput.nextLine();
			if (playerAction.equals("Back")) {
				validAction = true;
			} else {
				try {
					Item itemToUse = player.getItems().get(Integer.parseInt(playerAction) - 1);
					useItem(itemToUse);
				} catch (NumberFormatException e) {
					System.out.println("That is not a valid number");
					viewInventory();
				} catch (IndexOutOfBoundsException e) {
					System.out.println("That item does not exist");
					viewInventory();
				}
			}

		}
		presentOptions();
	}

	/**
	 * presents the player with their monsters and asks which one they would like to
	 * use itemToUse on
	 * 
	 * @param itemToUse the item the player wants to use on a monster
	 */
	public void useItem(Item itemToUse) {
		System.out.println("You selected " + itemToUse.getDescription());
		System.out.println("Please enter the number that corresponds to the monster to use the item on:");
		int teamSize = player.getTeam().size();
		for (int i = 0; i < teamSize; i++) {
			System.out.println((i + 1) + ": " + player.getTeam().get(i).getDescription());
		}
		String playerAction = playerInput.nextLine();
		int monIndex = Integer.parseInt(playerAction) - 1;
		try {
			itemToUse.use(player.getTeam().get(monIndex));
			player.getItems().remove(itemToUse);
		} catch (NumberFormatException e) {
			System.out.println("That is not a valid number");
			useItem(itemToUse);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("That is not a valid monster");
			useItem(itemToUse);
		}
	}

	/**
	 * presents the player with the battles they can partake in today and give them
	 * the option to start a battle
	 */
	public void viewTodaysBattles() {
		if (player.getTeam().size() == 0) {
			System.out.println("You currently have no monsters and therefore cannot battle");
			System.out.println("Please buy a monster first");
			presentOptions();
		}
		if (player.getBattlesLeft() == 0) {
			System.out.println("You can no longer battle today as all your monsters fainted last battle");
			presentOptions();
		}
		System.out.println(
				"Please select a number that corresponding to the battle to partake in or type Back to go return to the options menu");
		for (int i = 0; i < todaysBattles.size(); i++) {
			System.out.println((i + 1) + ": " + "Battle against " + todaysBattles.get(i).getEnemyPName());
		}
		String playerAction = playerInput.nextLine();
		if (playerAction.equals("Back")) {
			presentOptions();
		} else {
			try {
				Battle battleToStart = todaysBattles.get(Integer.parseInt(playerAction) - 1);
				player = battleToStart.start();
				this.todaysBattles.remove(battleToStart);
				presentOptions();
			} catch (NumberFormatException e) {
				System.out.println("That is not a valid number");
				viewTodaysBattles();
			} catch (IndexOutOfBoundsException e) {
				System.out.println("That battle does not exist");
				viewTodaysBattles();
			}
		}
	}

	/**
	 * presents the player with their team and allows the player to rename their
	 * monsters as well as change what monster comes out first when a battle starts
	 */
	public void viewRenameTeam() {
		boolean validOption = false;
		String playerAction = "";
		while (!validOption) {
			// prints the players team
			System.out.println(player.getName() + "'s team:");
			int teamSize = player.getTeam().size();
			for (int i = 0; i < teamSize; i++) {
				System.out.println((i + 1) + ": " + player.getTeam().get(i).getDescription());
			}
			System.out.println("\nType Back to return to the options menu");
			System.out.println("Type Reorder if you want to change what monster comes out first when a battle starts");
			System.out.println(
					"Or enter the number corresponding to the monster you want to rename if you want to rename monsters");
			playerAction = playerInput.nextLine();
			if (playerAction.equals("Back")) {
				validOption = true;
			} else if (playerAction.equals("Reorder")) {
				reorderMonsters();
				validOption = true;
			} else {
				// try to rename a monster using the input given by the player
				try {
					renameMonster(player.getTeam().get(Integer.parseInt(playerAction) - 1));
					validOption = true;
				} catch (NumberFormatException e) {
					System.out.println("That is not a valid number");
					viewRenameTeam();
				} catch (IndexOutOfBoundsException e) {
					System.out.println("That monster does not exist");
					viewRenameTeam();
				}
			}
		}
		presentOptions();
	}

	/**
	 * presents the player with their team and asks them which monster they want to
	 * come out first at the start of a battle
	 */
	public void reorderMonsters() {
		boolean validOption = false;
		String playerAction = "";
		while (!validOption) {
			System.out.println(player.getName() + "'s team:");
			int teamSize = player.getTeam().size();
			for (int i = 0; i < teamSize; i++) {
				System.out.println((i + 1) + ": " + player.getTeam().get(i).getDescription());
			}
			System.out.println(
					"Please enter the number corresponding to the monster you want to come out first at the start of a battle");
			playerAction = playerInput.nextLine();
			try {
				int newFirstMonIndex = Integer.parseInt(playerAction) - 1;
				Monster newFirstMonster = player.getTeam().get(newFirstMonIndex);
				swapFrontMonster(newFirstMonster);
				validOption = true;
			} catch (NumberFormatException e) {
				System.out.println("That is not a valid number");
				reorderMonsters();
			} catch (IndexOutOfBoundsException e) {
				System.out.println("That monster does not exist");
				reorderMonsters();
			}

		}
	}

	/**
	 * swaps the front monster in the players team with another given monster
	 * 
	 * @param monster The monster who will become the new front monster
	 */
	public void swapFrontMonster(Monster monster) {
		Monster oldFirstMonster = player.getTeam().get(0);
		int newFirstMonIndex = player.getTeam().indexOf(monster);
		player.getTeam().set(0, monster);
		player.getTeam().set(newFirstMonIndex, oldFirstMonster);
	}

	/**
	 * Causes the player to enter the shop bringing up the {@link Shop} classes own
	 * prompt for user input. After exiting the shop check if the game is over
	 */
	public void viewShop() {
		shopToday.enterShop(player);
		if (player.getTeam().size() == 0) {
			checkIfGameOver();
		}
		presentOptions();
	}

	/**
	 * checks to see if the player has zero monsters and can't afford anymore in
	 * which case the game would be over
	 */
	public void checkIfGameOver() {
		int lowestCostForMon;
		if (day <= 10) {
			lowestCostForMon = 5; // 5 is the lowest cost possible for a monster at day<10(common rarity);
		} else if (day <= 12) {
			lowestCostForMon = 10; // 10 is the lowest cost possible for a monster at days 11 and 12(uncommon
									// rarity);
		} else {
			lowestCostForMon = 20; // 15 is the lowest cost possible for a monster at days 13 to 15 (Rare rarity);
		}
		int itemGold = 0; // total gold to be gained from the player selling their items
		for (Item item : player.getItems()) {
			itemGold += item.getSellPrice();
		}
		if (lowestCostForMon > player.getGold() + itemGold) {
			endGame();
		}
	}

	/**
	 * Ends the game
	 */
	public void endGame() {
		System.out.println("GAME OVER");
		System.out.println("You selected a game duration of " + numberOfDays + " days and you played for " + day + " days");
		int score = calculateFinalScore();
		System.out.println("Congratulations you achieved a final score of " + score + " points");
		if(Game.doGUI) {
			
		}
		else {			
			try {
				playerInput.close();
			} catch (java.lang.IllegalStateException e) {
				;
			}
		}
		

	}

	/**
	 * calculate the players final score for the game each bit of gold they have
	 * left over is worth 10 gold
	 * 
	 * @return the players final score
	 */
	public int calculateFinalScore() {
		int score = player.getScore();
		int gold = player.getGold();
		score += gold * 10;
		player.setScore(score);
		return (player.getScore());
	}

	// Getters and setters

	/**
	 * returns the max number of days this game will run for
	 * 
	 * @return the number of days this game will run for
	 */
	public int getNumberOfDays() {
		return numberOfDays;
	}

	/**
	 * sets the number of days this game will go for
	 * 
	 * @param NumberOfDays the number of days to set
	 */
	public void setNumberOfDays(int NumberOfDays) {
		this.numberOfDays = NumberOfDays;
	}

	/**
	 * returns the difficulty of this game
	 * 
	 * @return difficulty of this game
	 */
	public int getDifficulty() {
		return difficulty;
	}

	/**
	 * sets the difficulty of this game
	 * 
	 * @param difficulty sets the game difficulty
	 */
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * returns the day of this game
	 * 
	 * @return the day of this game
	 */
	public int getDay() {
		return day;
	}

	/**
	 * sets the day of this game
	 * 
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	// getters for data storage variables
	/**
	 * returns The list of moves that can be chosen when moves are randomly
	 * generated
	 * 
	 * @return The list of moves that can be chosen when moves are randomly
	 *         generated
	 */
	public static ArrayList<Move> getMoves() {
		return (Game.moves);
	}

	/**
	 * returns The list of monsters that can be chosen when monsters are randomly
	 * generated
	 * 
	 * @return The list of monsters that can be chosen when monsters are randomly
	 *         generated
	 */
	public static ArrayList<Monster> getMonsters() {
		return (Game.monsters);
	}

	/**
	 * returns A table that contains probabilities that determine how likely a
	 * certain monster rarity is to be generated based on the day
	 * 
	 * @return A table that contains probabilities that determine how likely a
	 *         certain monster rarity is to be generated based on the day
	 */
	public static ConcurrentHashMap<Integer, ArrayList<Float>> getMonsterProbs() {
		return (Game.monsterProbs);
	}

	/**
	 * returns A table that contains probabilities that determine how likely a
	 * certain move rarity is to be generated based on the rarity of the monster it
	 * is being generated for
	 * 
	 * @return A table that contains probabilities that determine how likely a
	 *         certain move rarity is to be generated based on the rarity of the
	 *         monster it is being generated for
	 */
	public static ConcurrentHashMap<String, ArrayList<Float>> getMoveProbs() {
		return (Game.moveProbs);
	}

	/**
	 * returns A list containing the allowed types for monsters and moves in the
	 * game to generate with
	 * 
	 * @return A list containing the allowed types for monsters and moves in the
	 *         game to generate with
	 */
	public static ArrayList<String> getTypes() {
		return (Game.types);
	}

	/**
	 * returns A list containing all the different rarity's allowed in the game
	 * 
	 * @return A list containing all the different rarity's allowed in the game
	 */
	public static ArrayList<String> getRarities() {
		return (Game.rarityList);
	}

	/**
	 * returns A list containing all of the items in the game that can be randomly
	 * generated
	 * 
	 * @return A list containing all of the items in the game that can be randomly
	 *         generated
	 */
	public static ArrayList<Item> getItems() {
		return (Game.items);
	}

	/**
	 * returns the combatTypeRelationships for this game
	 * 
	 * @return the combatTypeRelationships for this game
	 */
	public static ConcurrentHashMap<String, ConcurrentHashMap<String, Float>> getCombatTypeRelationships() {
		return combatTypeRelationships;
	}

	/**
	 * returns the player of this game
	 * 
	 * @return the player of this game
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * sets the player of this game to a new player
	 * 
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * returns the shop for today
	 * 
	 * @return the shopToday
	 */
	public Shop getShopToday() {
		return shopToday;
	}

	/**
	 * sets the player input to be drawn from a string instead of System.in
	 * 
	 * @param playerInputString the string to replace System.in
	 */
	public void setPlayerInput(String playerInputString) {
		playerInput = new Scanner(playerInputString);
	}

	/**
	 * gets the players battles for the day
	 * 
	 * @return todaysBattles list of battles the play has in the current day
	 */
	public ArrayList<Battle> getTodaysBattles() {
		return todaysBattles;
	}

	/**
	 * sets whether or not the GUI is enabled
	 * 
	 * @param tOrF a boolean which is true turns the GUI on
	 */
	public static void setDoGUi(boolean tOrF) {
		doGUI = tOrF;
	}

	/**
	 * returns true if GUI is enabled
	 * 
	 * @return true if the GUI is enabled
	 */
	public static boolean getDoGUi() {
		return (doGUI);
	}

}
