package main.functionality.monster;

import main.functionality.Player;
import main.functionality.Purchasable;

/**
 * The item class is used to represent items. These items implement purchasable {@link Purchasable} can therefore be bought
 * and sold. These items can be used on monsters {@link Monster} in order to increase their stats.
 * 
 */
public class Item implements Purchasable{
	/** The name of the item */
	private String name;
	/** The buy price of the item */
	private int buyPrice;
	/** The stat the item should buff */
	private String statToBuff;
	/** The amount which to buff the statToBuff */
	private int buffAmount;

	//constructors
	/**
	 * creates a potion item
	 */
	public Item() {
		this.name = "Potion";
		this.statToBuff = "CurrentHealth";
		this.buyPrice = 10;
		this.buffAmount =100;
	}
	/**
	 * creates an item with its attributes set to the given parameters
	 * @param name the name to give the item
	 * @param buyPrice the buyPrice to give the item
	 * @param statToBuff the monster stat that this item should buff
	 * @param buffAmount the amount which to buff the statToBuff given
	 */
	public Item(String name, int buyPrice, String statToBuff, int buffAmount) {
		this.name = name;
		this.buyPrice = buyPrice;
		this.statToBuff = statToBuff;
		this.buffAmount = buffAmount;
	}
	
	/**
	 * creates a copy of another item
	 * @param item Sets the item to copy
	 */
	public Item(Item item){
		this.name = item.name;
		this.buyPrice = item.buyPrice;
		this.statToBuff = item.statToBuff;
		this.buffAmount = item.buffAmount;
	}
	
	//methods
	/**
	 * check if this item is equal to another given item
	 * @param other the item to check equality with
	 * @return true if the two items are equal and false otherwise
	 */
	public boolean equals(Item other) {
		if(this.name.equals(other.name) &&
		   this.buyPrice== other.buyPrice &&
		   this.statToBuff.equals(other.statToBuff) &&
		   this.buffAmount == other.buffAmount) {
		   return true;}
		else {return false;}
	}
	
	/**
	 * sells this item if it is owned by the seller
	 * @param seller the player that is selling this item
	 */
	@Override
	public void sell(Player seller) {
		int sellPrice = (this.getSellPrice());
		seller.setGold(seller.getGold() + sellPrice); 
		if(seller.getItems().remove(this)) {
			seller.setGold(seller.getGold() + sellPrice);
			System.out.println("Your " + this +" was successfully sold for " + sellPrice +" gold");
		}
		else {
			System.out.println("You do not own this item");
		}
	}
	
	/**
	 * buys this item if the buyer can afford it and does not already have a full inventory
	 * @param buyer the player that is buying this item
	 */
	@Override
	public void buy(Player buyer) {
		if(buyer.getItems().size() >= 5){
			System.out.println("Your item inventory is full please sell an item first");
		}
		else if (buyPrice > buyer.getGold()) {
			System.out.println("The " +this + " Costs " + buyPrice + " gold. This is more than you can afford");
		}
		else {
			buyer.addItem(this);
			buyer.setGold(buyer.getGold() - buyPrice);
			System.out.println("The " +this +" was successfully bought for "+buyPrice+" gold and added to your inventory");
		}
	}

	
	/**
	 * uses the statToBuff field of the item to determine which stat to change for the monster
	 * and then applies the stat update
	 * @param monster to use the item on
	 */
	public void use(Monster monster) {
		String stat = statToBuff;
		if(stat.equals("Attack")){
			monster.setAttack(monster.getAttack() + buffAmount);
			System.out.println(monster+"'s attack increased thanks to the "+this);
		}
		//if the item is a healing item heal the monster
		else if(stat.equals("CurrentHealth")){
			monster.setCurrentHealth(monster.getCurrentHealth()+ buffAmount);
			//if the healing item heals the monster over its max health set its currentHealth to its max health
			if (monster.getCurrentHealth() > monster.getMaxHealth()) {
				monster.setCurrentHealth(monster.getMaxHealth());
				System.out.println(monster+" was successfully healed by the "+this);
			}
		}
		else if(stat.equals("MaxHealth")) {
			monster.setMaxHealth(monster.getMaxHealth() + buffAmount);
			System.out.println(monster+"'s max health increased thanks to the "+this);
		}
		else if(stat.equals("HealAmount")) {
			monster.setHealAmount(monster.getHealAmount() + buffAmount);
			System.out.println(monster+"'s heal amount increased thanks to the "+this);
		}
		else {
			System.out.println("Monsters do not have a stat called " + stat +" Please check item file for errors");
		}
	}
	
	//getters
	
	/**
	 * returns the buy price of the item
	 * @return the buy price of the item
	 */
	@Override
	public int getBuyPrice() {
		return buyPrice;
	}

	/**
	 * returns the sell price of the item
	 * @return the sell price of the item
	 */
	@Override
	public int getSellPrice() {
		return (buyPrice /2); //items sell for half their buy price
	}

	/**
	 * returns A string description of the item
	 * @return A string description of the item
	 */
	@Override
	public String getDescription() {
		String rString = String.format("%s: This item buffs %s by %d points.",name,statToBuff,buffAmount);
		return(rString);
	}
	
	/**
	 * returns the name of this item
	 * @return the name of the item
	 */
	@Override
	public String toString() {
		return(name);
	}

	/**
	 * returns the name of this item
	 * @return the name of the item
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * returns the monster stat to be buffed by this item
	 * @return the monster stat to be buffed by this item
	 */
	public String getStatToBuff() {
		return statToBuff;
	}
	/**
	 * returns the amount this item should buff its statToBuff
	 * @return the amount this item should buff its statToBuff
	 */
	public int getBuffAmount(){
		return(buffAmount);
	}

}
