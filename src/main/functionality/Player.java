package main.functionality;

import java.util.ArrayList;

import main.functionality.monster.Item;
import main.functionality.monster.Monster;

/**
 * The player class is used to represent the user. THe player class stores the important 
 * information about the state of the user so it can be passed to other classes in a clean
 * and easy manner where the information is easy to obtain and update.
 */
public class Player {
	
	/** The name of this player */
	private String name;
	/** The gold of this player */
	private int gold = 0;
	/** The team of monsters belonging to this player */
	private ArrayList<Monster> team = new ArrayList<Monster>();
	/** The items belonging to this player */
	private ArrayList<Item> items = new ArrayList<Item>();
	/** The amount of times this players monsters have fainted today */
	private int todaysFaints = 0;
	/** The score of this player */
	private int score = 0;
	/** The amount of battles this player has left for the day */
	private int battlesLeft; //battles left for the player to do today
	
	/**
	 * Creates a player with one monster in their team
	 * @param name the name to set the players name to
	 * @param starterMonster the monster to be in the players team
	 */
	public Player(String name ,Monster starterMonster) {
		this.setName(name);
		addToTeam(starterMonster);
	}
	
	/**
	 * constructor used for cloning a player
	 * @param player the player to clone
	 */
	public Player(Player player) {
		this.name = (player.name);
		this.gold = (player.gold);
		this.team = player.team;
		this.items = player.items;
		this.todaysFaints = player.todaysFaints;
		this.score = player.score;
		this.battlesLeft =player.battlesLeft;
	}

	
	/**
	 * Adds a monster to the players team
	 * @param monToAdd the monster to add to the players team
	 * @return A string that says the monToAdd was added to the players team
	 */
	public String addToTeam(Monster monToAdd) {
		team.add(monToAdd);
		return("A" + monToAdd.getName() + " was added to your team");
		}
	
	/**
	 * Adds an item to the players inventory
	 * @param itemToAdd the item to add to the players inventory
	 * @return A string that says the itemToAdd was added to the players inventory
	 */
	public String addItem(Item itemToAdd) {
		items.add(itemToAdd);
		return("A" + itemToAdd.getName() + " was added to your inventory");
	}

	/**
	 * Checks if two players are equal
	 * @param other the player to check equality with
	 * @return a boolean value which is true if players are equal and false otherwise
	 */
	public boolean equals(Player other) {
		//checks if teams are equal
		if ((this.team == null && other.team != null)||(this.team !=null && other.team ==null)) {
			return false;
		}
			
		if (this.team !=null && other.team !=null) {
			if(!this.team.equals(other.team)) {
				return(false);
			}
		}
		//checks if items are equal
		if ((this.items == null && other.items != null)||(this.items !=null && other.items ==null)) {
			return false;
		}
		
		if (this.items !=null ||other.items!=null) {
			if(!this.items.equals(other.items)) {
				return(false);
			}
		}
			
		if (this.name.equals(other.name) &&
				this.gold == other.gold &&
				this.score== other.score &&
				this.battlesLeft == other.battlesLeft &&
				this.todaysFaints== other.getTodaysFaints()){
				return true;}
		return(false);
	}
	/**
	 * returns the name of this player
	 * @return the name of this player
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets this players name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * returns this players gold
	 * @return the players gold
	 */
	public int getGold() {
		return gold;
	}

	/**
	 * sets this players gold to a new value
	 * @param gold the gold to set
	 */
	public void setGold(int gold) {
		this.gold = gold;
	}
	
	/**
	 * returns this players team
	 * @return the players team
	 */
	public ArrayList<Monster> getTeam(){
		return(team);
	}

	/**
	 * returns this players faint count for today
	 * @return this players faint count for today
	 */
	public int getTodaysFaints() {
		return todaysFaints;
	}

	/**
	 * adds one to this players todaysFaints
	 */
	public void addFaint() {
		this.todaysFaints += 1;
	}

	/**
	 * returns this players items
	 * @return this players items
	 */
	public ArrayList<Item> getItems() {
		return items;
	}
	
	/**
	 * sets the team to the updated team
	 * @param updatedTeam the new team for this player
	 */
	public void setTeam(ArrayList<Monster> updatedTeam) {
		this.team = updatedTeam;
	}

	/**
	 * returns this players score
	 * @return this players score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * sets this players score the given value
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}


	/**
	 * returns the amount of battles this player has left for today
	 * @return the amount of battles this player has left for today
	 */
	public int getBattlesLeft() {
		return battlesLeft;
	}


	/**
	 * sets the amount of battles left for this player today
	 * @param battlesLeft the battlesLeft to set
	 */
	public void setBattlesLeft(int battlesLeft) {
		this.battlesLeft = battlesLeft;
	}
	
	/**
	 * resets this players faint count for the day
	 */
	public void resetTodaysFaintCount() {
		this.todaysFaints =0;
	}

	
}
