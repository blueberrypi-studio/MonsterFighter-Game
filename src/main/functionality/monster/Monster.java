package main.functionality.monster;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.functionality.Game;
import main.functionality.Player;
import main.functionality.Purchasable;
import main.functionality.monster.combat.BattlingMonster;


/**
 * The monster class represents monsters. These monsters can be bought and sold 
 * by a player. All the fields of a monster have getters and setters allowing them
 * to be easily altered allowing for them to be easily used by other classes such
 * as the Battle class
 *  
 */
public class Monster implements Purchasable{
	/** The name of the monster */
	private String name;
	/** The type of the monster */
	private String type ;
	/** The max health of the monster */
	private int maxHealth;
	/** The current health of the monster */
	private int currentHealth;
	/** The heal amount of the monster which dictates how much hit heals overnight */
	private int healAmount;
	/** The attacking power of the monster */
	private int attack;
	/** The rarity of the monster */
	private String rarity ;
	/** The moves the monster can use */
	private ArrayList<Move> moves;
	
	private Image image;
	
	/**
	 * creates a goblin monster
	 */
	public Monster() {
		this.name = "Goblin";
		this.type = "Normal";
		this.maxHealth = 150;
		this.currentHealth = 150;
		this.healAmount = 100;
		this.attack = 5;
		this.rarity = "Common";
		InputStream imageStream = getClass().getResourceAsStream("/main/resources/sprites/Goblin.png"); 
		//reads the image
		if(Game.getDoGUi()) {
			try {
				image = ImageIO.read(imageStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//constructors
	/**
	 * creates a new monster with the same stats as another monster but with a blank set of moves
	 * @param monster Sets the monster to copy
	 */
	public Monster(Monster monster) {
		this.name = monster.name;
		this.type = monster.type;
		this.maxHealth = monster.maxHealth;
		this.currentHealth = monster.maxHealth;
		this.healAmount = monster.healAmount;
		this.attack = monster.attack;
		this.rarity = monster.rarity;
		if (Game.getDoGUi()){
			try {
				this.image = monster.getImage();
			}catch(NullPointerException e) {
				System.err.println("The image for this monster does not exist");
			}
		}
	}
	
	/**
	 * creates a new monster with fields set by the given parameters
	 * @param name sets the monsters name
	 * @param type sets the monsters type
	 * @param maxHealth sets the monsters max health
	 * @param healAmount sets the monsters heal amount
	 * @param attack sets the monsters attack
	 * @param rarity sets the monsters rarity
	 */
	public Monster(String name, String type, int maxHealth, int healAmount, int attack, String rarity){
		this.name = name;
		this.type = type;
		this.maxHealth = maxHealth;
		this.currentHealth = maxHealth;
		this.healAmount = healAmount;
		this.attack = attack;
		this.rarity = rarity;
		InputStream imageStream = getClass().getResourceAsStream("/main/resources/sprites/"+name+".png");
		if (Game.getDoGUi()){
			try {
				image = ImageIO.read(imageStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(IllegalArgumentException e) {
				System.err.println("The image for this monster does not exist");
			}
		}
		
	}
	
	/**
	 * creates a new monster with the same stats as a battling monster
	 * @param battler Sets the battling monster to convert to a regular monster
	 */
	public Monster(BattlingMonster battler) {
		this.name = battler.getName();
		this.type = battler.getType();
		this.maxHealth = battler.getMaxHealth();
		this.currentHealth = battler.getMaxHealth();
		this.healAmount = battler.getHealAmount();
		this.attack = battler.getAttack();
		this.rarity = battler.getRarity();
		this.moves = battler.getMoves();
		this.currentHealth= battler.getCurrentHealth();
		if (Game.getDoGUi()){
			try {
				this.image = battler.getImage();
			}catch(NullPointerException e) {
				System.err.println("The image for this monster does not exist");
			}
		}

		
	}
	
	
	//methods
	/**
	 * returns the monsters name
	 * @return the monsters name
	 */
	public String toString() {
		return(name);
	}
	
	/**
	 * returns true if Monsters are equal
	 * @return true if Monsters are equal and false otherwise
	 * @param other the monster to check equality with
	 */
	public boolean equals(Monster other) {
		if(this.moves != null && other.moves !=null){			
			if (!this.moves.equals(other.moves)){return false;}
		}
		
		if (this.name.equals(other.getName()) &&
			this.type.equals(other.getType()) &&
			this.maxHealth == other.getMaxHealth() &&
			this.currentHealth == other.getCurrentHealth() &&
			this.healAmount == other.getHealAmount() &&
			this.attack == other.getAttack() &&
			this.rarity.equals(other.getRarity())){
			return true;
		}
		
		return false;
	}	
	
	/**
	 * sells this monster if it is owned by the seller
	 */
	public void sell(Player seller) {
		int sellPrice = (this.getSellPrice());
		seller.setGold(seller.getGold() + sellPrice); //monsters sell for half their purchase price
		seller.getTeam().remove(this);
		System.out.println("Your " + this +" was successfully sold for " + sellPrice +" gold");
	}
	
	
	/**
	 * buys this monster and adds it to the players team if the players team
	 *  is not already full and they can afford it
	 */
	@Override
	public void buy(Player buyer) {
		int cost = this.getBuyPrice();
		if(buyer.getTeam().size() >= 4){
			System.out.println("Your team is full please sell a monster first");
		}
		else if (cost > buyer.getGold()) {
			System.out.println(this + " Costs " + cost + " gold. This is more than you can afford");
		}
		else {
			buyer.addToTeam(this);
			buyer.setGold(buyer.getGold() - cost);
			System.out.println(this +" was successfully bought for "+this.getBuyPrice()+" gold and added to your team");
		}
	}
	
	
	//getters and setters 
	
	/**
	 * returns a description of the monster
	 * @return a description of the monster
	 */
	@Override
	public String getDescription() {
		if (moves != null) {
			String rString = String.format("Name: %s, Type:%s, Attack:%d, Max Health:%d, CurrentHealth:%d, HealAmount:%d, Rarity:%s, Moves: " +moves , name,type,attack,maxHealth,currentHealth,healAmount, rarity);
			return(rString);
		}
		String rString = String.format("Name: %s, Type:%s, Attack:%d, Max Health:%d, %d CurrentHealth:%d, Rarity:%s, Moves:",name,type,attack,maxHealth,currentHealth,healAmount, rarity);
		return(rString);
		
	}
	
	
	/**
	 * returns the price of the monster based on it's rarity
	 * @return price of the monster based on it's rarity
	 */
	@Override
	public int getBuyPrice() {
		if(this.getRarity().equals("Common")) {return(5);}
		if(this.getRarity().equals("Uncommon")) {return(10);}
		if(this.getRarity().equals("Rare")) {return(20);}
		if(this.getRarity().equals("Super-Rare")) {return(30);}
		else {return(50);} //value of a legendary monster
	}
	
	/**
	 * returns the sell price of this monster
	 * @return the sell price of the monster
	 */
	public int getSellPrice() {
		return (this.getBuyPrice() /2);//monsters sell for half their purchase price		
	}
	
	/**
	 * returns the name of this monster
	 * @return the name of the monster
	 */
	public String getName() {return name;}
	
	/**
	 * sets the name of the monster
	 * @param name the new name of the monster
	 */
	public void setName(String name) {this.name = name;}

	/**
	 * returns the type of this monster
	 * @return the type of the monster
	 */
	public String getType() {return type;}
	

	/**
	 * returns the max health of this monster
	 * @return the maxHealth of the monster
	 */
	public int getMaxHealth() {return maxHealth;}

	/**
	 * sets the max health of the monster
	 * @param maxHealth the new max health of the monster
	 */
	public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}

	/**
	 * returns the current health of this monster
	 * @return the current health of the monster
	 */
	public int getCurrentHealth() {return currentHealth;}
	
	/**
	 * sets the current health of the monster
	 * @param health the new current health of the monster
	 */
	public void setCurrentHealth(int health) {this.currentHealth = health;}
	
	/**
	 * returns the attack power of this monster
	 * @return the attack of this monster
	 */
	public int getAttack() {return attack;}

	/**
	 * sets the attack of the monster
	 * @param attack the new attack of the monster
	 */
	public void setAttack(int attack) {this.attack = attack;}

	/**
	 * returns the heal amount of this monster
	 * @return the heal amount of this monster
	 */
	public int getHealAmount() {return healAmount;}

	/**
	 * sets the heal amount of the monster
	 * @param healAmount the new heal amount of the monster
	 */
	public void setHealAmount(int healAmount) {this.healAmount = healAmount;}

	/**
	 * returns this monsters moves
	 * @return the moves of this monster
	 */
	public ArrayList<Move> getMoves() {return moves;}
	
	/**
	 * returns the rarity of this monster
	 * @return the rarity of this monster
	 */
	public String getRarity() {return rarity;}
	
	/**
	 * sets the moves of the monster
	 * @param moves the new moves of the monster
	 */
	public void setMoves(ArrayList<Move> moves) {this.moves = moves;}

	/**
	 * returns this monsters image
	 * @return the image of this monster
	 */
	public Image getImage() {
		return image;
	}
	

}
