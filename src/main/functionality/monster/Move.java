package main.functionality.monster;


/**
 * The Move class represents moves that can be used by BattlingMonsters in
 * order to deal damage to other battlingMonsters
 */
public class Move {
	/** The name of the move */
	private String name;
	/** The base damage dealt by the move */
	private int damage ;
	/** The type of the move */
	private String type ; 
	/** The rarity of the move */
	private String rarity ; 
	
	/**
	 * Creates a new move using the given parameters
	 * @param name sets the name of the move
	 * @param damage sets the base damage dealt by the move
	 * @param type sets the type of the move
	 * @param rarity sets the rarity of the move
	 */
	public Move(String name, int damage, String type, String rarity){
		this.name = name;
		this.damage = damage;
		this.type = type;
		this.rarity = rarity;
	}
	
	/**
	 * creates a tackle move
	 */
	public Move() {
		this.name =  "Tackle";
		this.damage = 50;
		this.type = "Normal";
		this.rarity = "Common";		
	}
	
	/**
	 * returns a string representation of this move
	 * @return A string representation of the move
	 */
	public String toString() {
		return(name+":Damage="+damage);
	}
	
	/**
	 * returns a string containing this moves details
	 * @return A string containing the moves details
	 */
	public String getDetails() {
		return(String.format("%s: [Damage:%d Type:%s Rarity:%s]",name,damage,type,rarity));	
	}
	/**
	 * returns true if this move is equal to the given move
	 * @return A boolean value which is true when two moves are equal and false otherwise
	 * @param other the move to check equality with
	 */
	public boolean equals(Move other) {
		//checks if two moves are equal
		if (this.name.equals(other.getName()) &&
			    this.damage == other.getDamage() &&
			    this.type.equals(other.getType()) &&
			    this.rarity.equals(other.getRarity())) {
			    return true;
			}
		return false;
		
		
	}
	
	//getters
	/**
	 * returns the name of this move
	 * @return the name of the move
	 */
	public String getName() {
		return(name);
	}
	/**
	 * returns the base damage of this move
	 * @return the base damage of the move
	 */
	public int getDamage() {
		return damage;
	}
	/**
	 * returns the type of this move
	 * @return the type of the move
	 */
	public String getType() {
		return type;
	}
	/**
	 * the rarity of this move
	 * @return the rarity of the move
	 */
	public String getRarity() {
		return rarity;
		
	}
}
