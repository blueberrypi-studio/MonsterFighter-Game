package main.functionality;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import main.functionality.monster.Item;
import main.functionality.monster.Monster;
import main.functionality.monster.MonsterGenerator;


/**
 * The shop class is used to buy and sell purchasable objects to the player
 * these include monsters and items.
 */
public class Shop {
	/** The monsters on sale in this shop*/
	private ArrayList<Monster> monstersOnSale = new ArrayList<Monster>();
	/** The items on sale in this shop */
	private ArrayList<Item> itemsOnSale = new ArrayList<Item>();
	/** The day this shop was created on*/
	private int day;
	/** The player using this shop */
	private Player player;
	/** The scanner which takes input from the player through the command line */
	private Scanner playerInput = new Scanner(System.in);
	
	/**
	 * Creates a new show and generates Monsters and items to sell
	 * @param day the day the shop is being generated on
	 */
	public Shop(int day) {
		this.day = day;
		generateMonsters();
		generateItems();
	}
	
	/**
	 * Sell a purchasable that is owned by the player
	 * @param objectToSell the purchasable object to sell to the shop
	 */
	public void sell(Purchasable objectToSell) {
		objectToSell.sell(player);		
	}

	/**
	 * Generates the monsters for this shop to sell
	 */
	private void generateMonsters() {
		 MonsterGenerator generator = new MonsterGenerator(day);
		 for(int i=0; i<4; i++) {
			//generate a monster using the monsterGenerator and add it to the monsters on sale	
			 Monster newMonster = generator.generateMonster();
			 monstersOnSale.add(newMonster);
			}	 
	}
	
	/**
	 * Generates the items for this shop to sell
	 */
	private void generateItems(){
		for(int i =0; i<3;i++) {
			Random rng = new Random();
			int itemToSellIndex = rng.nextInt(Game.getItems().size());
			Item itemToAdd = new Item(Game.getItems().get(itemToSellIndex));//generates a new random item that exists in the item list
			itemsOnSale.add(itemToAdd);
		}
	}
	
	/**
	 * Buy a purchasable from the shop
	 * @param objectToBuy the purchasable object the player wants to buy
	 */
	public void buy(Purchasable objectToBuy) {
		Player playerBeforeBuy = new Player(player);
		objectToBuy.buy(player);
		if (!player.equals(playerBeforeBuy)) {
			monstersOnSale.remove(objectToBuy);
			itemsOnSale.remove(objectToBuy);
		}
	}
	
	/**
	 * sets this shops player and then prompts the user for input
	 * through the command line allowing them to use all the functions of the shop
	 * @param player the player entering the shop
	 */
	public void enterShop(Player player) {
		this.player = player;
		int optionIndex =100;
		while(optionIndex<1 || optionIndex>4) {
			System.out.println("Please select the number that corresponds to what you want to do");
			System.out.println("1: Buy monsters\n2: Buy items\n3: Sell monsters\n4: Sell items");
			System.out.println("Type Back to return to exit the shop and return to the options menu");
			String playerAction = playerInput.nextLine();
			if(playerAction.equals("Back")){
				break;
			}
			else {
				try {
					optionIndex = Integer.parseInt(playerAction);
				}	
				catch(NumberFormatException e) {
					System.out.println("That is not a valid number");
				}
			}	
		}
		if (optionIndex == 1) {
			buyMonsters();
		}
		else if(optionIndex == 2){
			buyItems();
		}
		else if(optionIndex == 3){
			sellMonsters();
		}
		else if(optionIndex == 4){
			sellItems();
		}
		else {
			System.out.println("");
		}
	}
	
	/**
	 * Prompts the user to choose monsters to buy if they are available
	 */
	public void buyMonsters() {
		if(monstersOnSale.size() ==0) {
			System.out.println("There are no more monsters to buy today");
			enterShop(player);
		}
		else {
			String playerAction;
			Boolean validOption = false;
			int optionIndex = 100;
			while(validOption == false) {
				System.out.println("Monsters on sale:");
				for(int i=0; i<monstersOnSale.size(); i++){
					System.out.println((i+1)+": "+monstersOnSale.get(i).getDescription());
					System.out.println("Price: "+monstersOnSale.get(i).getBuyPrice()+" gold");
				}
				System.out.println("Please select a number corresponding to the monster you want to buy or type Back to return to other shop options");
				playerAction = playerInput.nextLine();
				if (playerAction.equals("Back")){
					enterShop(player);
					return;
				}
				else {
					try {
						optionIndex = Integer.parseInt(playerAction);
						if(optionIndex >monstersOnSale.size() || optionIndex <=0) {
							System.out.println("That is not a valid monster to buy");
						}
						else {validOption = true;}
					}
					catch(NumberFormatException e) {
						System.out.println("That is not a valid number");
					}
				}
			}
			buy(monstersOnSale.get(optionIndex-1));
			buyMonsters();
		}
	}
	/**
	 * Prompts the user to choose items to buy if they are available
	 */
	public void buyItems() {
		if(itemsOnSale.size() ==0) {
			System.out.println("There are no more items to buy today");
			enterShop(player);
		}
		else {
			String playerAction;
			Boolean validOption = false;
			int optionIndex = 100;
			while(validOption == false) {
				System.out.println("Items on sale:");
				for(int i=0; i<itemsOnSale.size(); i++){
					System.out.println((i+1)+" "+itemsOnSale.get(i)+": "+itemsOnSale.get(i).getDescription());
					System.out.println("Price: "+itemsOnSale.get(i).getBuyPrice()+" gold");
				}
				System.out.println("Please select a number corresponding to the item you want to buy or type Back to return to other shop options");
				playerAction = playerInput.nextLine();
				if (playerAction.equals("Back")){
					enterShop(player);
					return;
				}
				else {
					try {
						optionIndex = Integer.parseInt(playerAction);
						if(optionIndex >itemsOnSale.size() || optionIndex <=0) {
							System.out.println("That is not a valid item to buy");
						}
						else {validOption = true;}
					}
					catch(NumberFormatException e) {
						System.out.println("That is not a valid number");
					}
				}
			}
			buy(itemsOnSale.get(optionIndex-1));
			buyItems();
		}
	}
	/**
	 * Prompts the user to choose monsters to sell if they own any
	 */
	public void sellMonsters() {
		if (player.getTeam().size() ==0) {;
			System.out.println("You have no monsters to sell");
			enterShop(player);
		}
		else {
			Boolean validOption = false;
			int optionIndex = 100;
			String playerAction;
			while(validOption == false) {
				System.out.println("Your monsters to sell");
				for(int i=0; i<player.getTeam().size(); i++){
					System.out.println((i+1)+": "+ player.getTeam().get(i).getDescription());
					System.out.println("Sells for "+player.getTeam().get(i).getSellPrice()+" gold");
				}
				System.out.println("Please select a number corresponding to the monster you want to sell or type Back to return to other shop options");
				playerAction = playerInput.nextLine();
				if (playerAction.equals("Back")){
					enterShop(player);
					return;
				}
				else {
					try {
						optionIndex = Integer.parseInt(playerAction);
						if(optionIndex >player.getTeam().size() || optionIndex <=0) {
							System.out.println("That is not a valid monster to sell");
						}
						else {validOption = true;}
					}
					catch(NumberFormatException e) {
						System.out.println("That is not a valid number");
					}
				}
			}
			sell(player.getTeam().get(optionIndex-1));
			sellMonsters();
		}
	}
	/**
	 * Prompts the user to choose items to sell if they own any
	 */
	public void sellItems() {
		if (player.getItems().size() ==0) {;
			System.out.println("You have no items to sell");
			enterShop(player);
		}
		else {
			Boolean validOption = false;
			int optionIndex = 100;
			String playerAction;
			while(validOption == false) {
				System.out.println("Your items to sell");
				for(int i=0; i<player.getItems().size(); i++){
					System.out.println((i+1)+": "+ player.getItems().get(i).getDescription());
					System.out.println("Sells for "+player.getItems().get(i).getSellPrice()+" gold");
				}
				System.out.println("Please select a number corresponding to the item you want to sell or type Back to return to other shop options");
				playerAction = playerInput.nextLine();
				if (playerAction.equals("Back")){
					enterShop(player);
					return;
				}
				else {
					try {
						optionIndex = Integer.parseInt(playerAction);
						if(optionIndex >player.getItems().size() || optionIndex <=0) {
							System.out.println("That is not a valid item to sell");
						}
						else {validOption = true;}
					}
					catch(NumberFormatException e) {
						System.out.println("That is not a valid number");
					}
				}
			}
			sell(player.getItems().get(optionIndex-1));
			sellItems();
		}
	}
	
	
	/**
	 * returns the monsters on sale
	 * @return the monsters on sale
	 */
	public ArrayList<Monster> getMonstersOnSale(){return(monstersOnSale);}

	/**
	 * returns the items on sale 
	 * @return the items on sale
	 */
	public ArrayList<Item> getItemsOnSale() {return itemsOnSale;}
	
	/**
	 * sets the player using this shop
	 * @param player the new player using this shop
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	


}
