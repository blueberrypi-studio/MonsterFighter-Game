package main.functionality;

/**
 * This interface is used represent objects that can be purchased. For an object to be purchasable it 
 * must have a description and be able to be bought and sold. In order for objects to be be bought and sold
 * they must also must have a buy price and a sell price.
 */

public interface Purchasable {
	
	/**
	 * If the seller owns this object sell it and update the seller by removing this object from their possession and 
	 * increasing their gold by the sell price of his object.
	 * @param seller The player who is selling the object
	 */
	public void sell(Player seller);
	
	/**
	 * Update the seller by adding this object to their possession and decreasing their gold by the buy price of his object,
	 * but only if they can afford it 
	 * @param buyer The player who is buying the object
	 */
	public void buy(Player buyer);
	
	/**
	 * returns the buy price of the Purchasable object
	 * @return the buy price of the Purchasable object
	 */
	public int getBuyPrice();
	
	/**
	 * returns the sell price of the Purchasable object
	 * @return the sell price of the Purchasable object
	 */
	public int getSellPrice();
	
	/**
	 * returns a description of the Purchasable object
	 * @return a description of the Purchasable object
	 */
	public String getDescription();
	
	
	
	
}
