package main.functionality.monster.combat;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import main.functionality.Game;
import main.functionality.Player;
import main.functionality.monster.Item;
import main.functionality.monster.Monster;
import main.functionality.monster.MonsterGenerator;
import main.functionality.monster.Move;

/**
 * The Battle class is used to represent a battle in the monster fighter game.
 * The main way to use this class is creating a battle object and then using
 * the {@link #start() start} method to start the battle.
 */
public class Battle {
	/** The list of the enemy battling monsters. */
	private ArrayList<BattlingMonster> enemies = new ArrayList<BattlingMonster>();
	/** The list of the players battling monsters. */
	private ArrayList<BattlingMonster> playerBattlers = new ArrayList<BattlingMonster>();
	/** The list of updated monsters for the players team to be set to after the battle . */
	private ArrayList<Monster> updatedTeam = new ArrayList<Monster>();
	/** The users player that is participating in the battle*/
	private Player player;
	/** The day the battle is taking place on*/
	private int day = 5;
	/** The players currently battling monster */
	private BattlingMonster playerMonster;
	/** The enemies currently battling monster */
	private BattlingMonster enemyMonster;
	/** The games difficulty setting */
	private int difficulty;
	/** The scanner responsible for reading player input during the battle */
	private Scanner playerInput = new Scanner(System.in);
	/** The enemy players name */
	private String enemyPName= "";
	
	
	
	/**
	 * Constructor to use when enemies are auto generated
	 * @param player the player to participate in the battle
	 * @param day sets the day the battle is taking place on
	 * @param difficulty sets the difficulty of the battle
	 * @param npcName sets the enempyPlayers name
	 */
	public Battle(Player player, int day, int difficulty, String npcName) {
		this.player = player;
		this.day = day;
		this.difficulty = difficulty;
		enemyPName =npcName;
		if(!Game.getDoGUi()) { //only generates battlers if not using GUI
			generateBattlers();
		}
	}
	
	
	/**
	 * Constructor used for passing in a player with enemies rather than auto generating them
	 * @param user the users player to participate in the battle
	 * @param enemy used to set the enemies monsters
	 * @param difficulty sets the difficulty of the battle
	 * @param day sets the day the battle is taking place on
	 */
	public Battle(Player user, Player enemy, int difficulty, int day) {
		this.player = user;
		this.difficulty = difficulty;
		this.day = day;
		enemyPName = enemy.getName();
		if (enemy.getTeam().size() == 0) {
			enemies.add((BattlingMonster)new Monster());
		}
		//loads enemy's team
		enemy.getTeam().forEach(M -> enemies.add(new BattlingMonster(M,false)));
	}
	
	
	
	/**
	 * Generates the battlers Being used in this battle
	 */
	public void generateBattlers() {		
		//generates enemies using monster generator
		MonsterGenerator generator = new MonsterGenerator(day);
		int enemyNum = 3+ day/8 ; //number of enemies to generate (maximum of 4 at day 15)
		for(int i =0; i<enemyNum ; i++) {
			Monster newMonster = new Monster();
			newMonster = generator.generateMonster();
			enemies.add(new BattlingMonster(newMonster,false));
		}
	}
	
	/**
	 * Starts the battle 
	 * @return the updated player resulting from the battle
	 */
	public Player start() {
		//generates a team of playerBattlers from the players team
		//ensures only non fainted monsters are sent to battle
		for (Monster Mon:player.getTeam()) {
			if (Mon.getCurrentHealth() != 0) {
				 playerBattlers.add(new BattlingMonster(Mon, true));
			}
		}
		if(Game.getDoGUi()) {generateBattlers();}; //generates battlers if gui is active
		
		playerMonster = playerBattlers.get(0);//create a new battling monster from the first monster in the players team
		enemyMonster = enemies.get(0); //create a new battling monster from the first monster in the enemies list
		printStartMessage();
		if (!Game.getDoGUi()) {
			playerInput = new Scanner(System.in);
			playerAction();
		}
		return(player);
	}
	public void printStartMessage() {
		System.out.println("New battle agianst "+enemyPName+" has begun");
		System.out.println("Your "+playerMonster +" was summoned");
		System.out.println(playerMonster.getDescription());
		
		System.out.println("\nAn enemy "+enemyMonster +" appears to fight");
		System.out.println(enemyMonster.getDescription());
		
		System.out.println("\nPlayer battlers: "+playerBattlers);
		System.out.println("Player items: "+player.getItems());
	}

	
	/**
	 * Swaps out a BattlingMonster with another Battling Monster
	 * @param newMon the monster to swap out with
	 * @param pMonster determines whether the monster being swaps belongs to the player or the enemy
	 * true if the monster is player owned
	 * @return updatedPMonster the updated version of the players Monster that is being swapped out
	 */
	public void swapOut(BattlingMonster newMon, Boolean pMonster) {
		//player swap
		if (pMonster == true) {
			int monsterIndex = playerBattlers.indexOf(newMon); //gets the index of the monster to come out to battle
			if (monsterIndex == -1) {
				System.out.println("the monster you are trying to swap to does not exist in the players team");
				return;
			}
			else if(newMon == playerMonster) {
				System.out.println("That is your currently battling monster");
				return;
			}
			if(playerMonster.getCurrentHealth() ==0) {
				playerMonster = newMon; //the new player monster in battle is now newMon
				System.out.println("Your "+newMon + " Comes out to battle");
				return;
			}
			playerMonster = newMon; //the new player monster in battle is now newMon
			System.out.println("Your "+newMon + " Comes out to battle");
			enemyAttack();
			return;
		}
		//enemySwap (only occurs upon an enemy monster fainting)
		else {
			int monsterIndex = enemies.indexOf(newMon);
			if (monsterIndex == -1) {
				System.out.println("the monster you are trying to swap to does not exist in the enemy team");
				return;
			}
			enemyMonster = newMon; 
			System.out.println("\nA hostile "+newMon + " Comes out to battle");
		}				
	}
	
	/**
	 * Asks the player to perform a valid battle action and causes that action to happen
	 */
	private void playerAction() {
		System.out.println("\nPlease type in the action you want to use: Swap battler, Attack, Use item");
		String playerAction = playerInput.nextLine();
		if (playerAction.equals("Attack")) {
			attackAction();
		}
		else if (playerAction.equals("Swap battler")) {
			swapBattlerAction();
		}
		else if (playerAction.equals("Use item")) {
			useItemAction();
		}
		else if (playerAction.equals("TERMINATE")) {
			playerInput.close();
		}
		else {
			System.out.println("That is not a valid action to perform");
			playerAction();
		}
	}
	
	
	/**
	 * Causes the attack action to occur using a move inputed by the player
	 */
	private void attackAction() {
		boolean validAction = false;
		//while move to use is invalid
		String playerAction;
		int moveIndex=0;
		while(!validAction) {
			//prints all moves and their details
			System.out.println("Available moves for your "+playerMonster);
			for(int i=0; i<playerMonster.getMoves().size();i++) {
				System.out.println((i+1)+" "+playerMonster.getMoves().get(i).getDetails());
			}
			//asks the user to input a move
			System.out.println("Please choose a number that corresponds to the move you want to use");
			playerAction = playerInput.nextLine();
			try{
				moveIndex = Integer.parseInt(playerAction)-1;
				if(moveIndex>=0 &&moveIndex<playerMonster.getMoves().size()) {
					validAction= true;
				}
			}
			catch(NumberFormatException e) {
				System.out.println("That is not a valid number");
			}
					
		}
		playerAttack(playerMonster.getMoves().get(moveIndex));
	}
	
	/**
	 * Causes the swap battler action to happen.
	 */
	@SuppressWarnings("unchecked")
	private void swapBattlerAction() {
		if (playerBattlers.size()==1) {
			System.out.println("You do not have any other battlers to swap to");
			playerAction();
		}
		else {
			ArrayList<BattlingMonster> availableBattlers = new ArrayList<BattlingMonster>();
			availableBattlers = (ArrayList<BattlingMonster>) playerBattlers.clone();
			availableBattlers.remove(playerMonster);
			System.out.println("Monsters to swap to:");
			for(int i=0; i<availableBattlers.size(); i++) {
				System.out.println((i+1)+": "+availableBattlers.get(i)+": ["+availableBattlers.get(i).getDescription()+" ]");
			}
			String playerAction;
			int monsterIndex = 100;
			while(monsterIndex >availableBattlers.size()-1 || monsterIndex <0) {
				try {
					System.out.print("Please input the number of the monster you want to swap to:\n");
					playerAction = playerInput.nextLine();
					monsterIndex = Integer.parseInt(playerAction)-1;
				}
				catch(NumberFormatException e) {
					System.out.println("That is not a valid number");
				}
			}
			swapOut(availableBattlers.get(monsterIndex),true);
		}
	}
	
	/**
	 * Causes the use item action to happen.
	 */
	private void useItemAction() {
		if (player.getItems().size() ==0) {
			System.out.println("You don't have any items to use");
			playerAction();
		}
		int itemIndex = -1;
		String playerAction;
		while(itemIndex ==-1) {
			//prints the players items and their descriptions
			System.out.println("Items available to use:");
			for(Item item:player.getItems()) {
				System.out.println(item.getDescription());
			}
			System.out.println("Please input an item to use");
			playerAction = playerInput.nextLine();
			itemIndex = getItemIndex(playerAction);
		}
		player.getItems().get(itemIndex).use(playerMonster);
		player.getItems().remove(player.getItems().get(itemIndex));
		enemyAttack();
	}
	
	
	/**
	 *causes the players current battling monster to use an attack on the enemy monster
	 * @param moveToUse the move for the players monster to use
	 */
	public void playerAttack(Move moveToUse) {
		playerMonster.UseAttack(moveToUse, enemyMonster);
		//checks if the enemies monster has fainted
		if (enemyMonster.getCurrentHealth() <= 0) {
			System.out.println("The enemy "+enemyMonster +" fainted");
			enemies.remove(enemyMonster);
			if (enemies.size() == 0) {
				endBattle(true);
			}
			else {
				swapOut(enemies.get(0), false);
				if(!Game.getDoGUi()) {
					playerAction();
				}
			}
		}
		else {
			enemyAttack();
		}
	}
	
	

	/**
	 * causes the enemy to attack the players monster
	 */
	private void displayStatus() {
		System.out.println("\nYour currently battling monster is "+playerMonster);
		System.out.println("Current stats: " + playerMonster.getDescription());
		System.out.println("\nThe current enemy monster is "+ enemyMonster);
		System.out.println("Current stats: " + enemyMonster.getDescription());
	}
	
	/**
	 * causes the enemy to attack the players monster
	 */
	public void enemyAttack() {
		System.out.println(" ");
		Random rng = new Random();
		//chooses a random move for the enemyMonsterTouse
		int moveIndexToUse = rng.nextInt(4);
		Move moveToUse = enemyMonster.getMoves().get(moveIndexToUse);
		enemyMonster.UseAttack(moveToUse, playerMonster);
		//checks if the players monster has fainted
		if (playerMonster.getCurrentHealth() <= 0) {
			playerMonsterFaint();
		}
		else {
			if (!Game.getDoGUi()) {
				displayStatus();
				playerAction();
			}
		}
	}
	
	/**
	 * causes a players monster to faint
	 */
	@SuppressWarnings("unchecked")
	public void playerMonsterFaint() {
		player.addFaint();
		playerMonster.setCurrentHealth(0);
		System.out.println("Your "+ playerMonster+" fainted");
		updatedTeam.add(new Monster(playerMonster));
		playerBattlers.remove(playerMonster);	
		if(playerBattlers.size() ==0){
			endBattle(false);
		}
		else {
			ArrayList<BattlingMonster> availableBattlers = new ArrayList<BattlingMonster>();
			availableBattlers = (ArrayList<BattlingMonster>) playerBattlers.clone();
			availableBattlers.remove(playerMonster);
			if(!Game.getDoGUi()) {
				System.out.println("Monsters to swap to:");
				for(int i=0; i<availableBattlers.size(); i++) {
					System.out.println((i+1)+": "+availableBattlers.get(i)+": ["+availableBattlers.get(i).getDescription()+" ]");
				}
				String playerAction;
				int monsterIndex = 100;
				while(monsterIndex >availableBattlers.size()-1 || monsterIndex <0) {
					try {
						System.out.print("Please input the number of the monster you want to swap to:\n");
						playerAction = playerInput.nextLine();
						monsterIndex = Integer.parseInt(playerAction)-1;
					}
					catch(NumberFormatException e) {
						System.out.println("That is not a valid number");
					}
				}
				swapOut(availableBattlers.get(monsterIndex),true);
				displayStatus();
				playerAction();
			}
			else {
				System.out.println("Please select a monster to swap to");
			}
		}
	}
	
	/**
	 * @param itemToUse the name of the item the player wants to use 
	 * @return the index of the item the player wants to use if the item is invalid -1 is returned
	 */
	private int getItemIndex(String itemToUse) {
		ArrayList<String> itemNames = new ArrayList<String>();
		player.getItems().forEach(I-> itemNames.add(I.getName()));
		if(itemNames.contains(itemToUse)){
			return(itemNames.indexOf(itemToUse));
		}
		else {
			System.out.println("Your do not have the item "+itemToUse);
			return(-1);
		}	
	}
	
	/**
	 * ends the battle
	 * @param win determines whether or not the player won the battle
	 */
	public void endBattle(boolean win) {
		//updates the players team with changes from the battle 
		
		//ensures that the players monsters that were fainted before the battle aren't deleted
		for(Monster M: player.getTeam()) {
			if(M.getCurrentHealth()==0) {
				updatedTeam.add(M);
			}
		}
		if (Game.getDoGUi()) {
			Game.guiOutput.reset();
		}
		//updates the players team with the after battle status
		playerBattlers.forEach(M -> updatedTeam.add(new Monster(M)));
		player.setTeam(updatedTeam);
		
		//if the player won the battle
		if (win == true) {
			System.out.println("All of the enemies monsters have fainted");
			System.out.println("Good job " + player.getName() +" you won the battle!");
			//updates the players gold
			int goldToGain = day*(15/difficulty);
			System.out.println("You gained "+goldToGain+" gold");
			player.setGold(player.getGold() + day*(15/difficulty));
			
			//updates the players score
			int scoreToGain = (day*100*difficulty);
			player.setScore(player.getScore() + scoreToGain );
			System.out.println("You gained "+scoreToGain + " points from this battle");
		}
		//if the player lost the battle
		else {
			System.out.println("All your monsters have fainted");
			System.out.println("How unfortunate " + player.getName() +" you lost the battle");
			System.out.println("You can no longer battle today");
			player.setBattlesLeft(0);
		}	
		//prints the teams status
		if(!Game.getDoGUi()) {
			System.out.println("\nYour teams new status:");
			player.getTeam().forEach(M -> System.out.println(M.getDescription()));
			System.out.println("");
		}
	}

	
	//getters and setters
	
	/**
	 * returns the players currently battling monster
	 * @return the players currently battling monster
	 */
	public BattlingMonster getPlayerMonster() {
		return playerMonster;
	}

	/**
	 * returns the enemies currently battling monster
	 * @return the enemies currently battling monster
	 */
	public BattlingMonster getEnemyMonster() {
		return enemyMonster;
	}
	
	/**
	 * returns a list containing the enemies in the battle
	 * @return a list containing the enemies in the battle
	 */
	public ArrayList<BattlingMonster> getEnemies() {
		return enemies;
	}
	
	/**
	 * returns a list containing the players battling monsters
	 * @return the players battling monsters
	 */
	public ArrayList<BattlingMonster> getPlayerBattlers() {
		return playerBattlers;
	}


	/**
	 * returns the enemy players name
	 * @return the enemy players name
	 */
	public String getEnemyPName() {
		return enemyPName;
	}

	
	
	

}
