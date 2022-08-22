package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.functionality.Game;
import main.functionality.Player;
import main.functionality.Shop;
import main.functionality.monster.Item;
import main.functionality.monster.Monster;

class ShopTest {

	Game game = new Game();
	Player player = new Player("Jeff", new Monster());
	Shop shop = game.getShopToday();
	
	@BeforeEach
	public void setUp() {
		Game.setDoGUi(false);
		player = new Player("Jeff", new Monster());
	}
	
	@Test
	void testGenerateMonsters() {
		shop = new Shop(15);
		shop.setPlayer(player);
		ArrayList<Monster> monstersForSale = shop.getMonstersOnSale();
		//checks to ensure there are only 4 monsters on sale
		assertEquals(4, monstersForSale.size());
		Monster mToCheck = monstersForSale.get(0);
		//checks to ensure each monster in the shop is a unique object 
		for(int i =1; i < monstersForSale.size(); i++) {
			assertFalse(mToCheck == monstersForSale.get(i));
		}
	}
	
	@Test
	void testGenerateItems(){
		
	}
	@Test
	void testBuy() {
		//monster tests
		
		//test buying a monster
		player.setGold(100);
		shop = new Shop(15);
		shop.setPlayer(player);
		int goldBeforePurchase = player.getGold();
		int teamSize = player.getTeam().size();
		shop.buy(shop.getMonstersOnSale().get(0));
		assertEquals(player.getTeam().size(),teamSize+1);//asserts that a monster was added to the team
		assertTrue(goldBeforePurchase > player.getGold()); // checks that gold was spent to buy the monster
		
		//tests trying to buy a monster with no gold
		player.setGold(0);
		shop.buy(shop.getMonstersOnSale().get(1));
		assertEquals(player.getGold(), 0); // checks that gold was not changed from unsuccessful purchase
		assertEquals(player.getTeam().size(), teamSize+1); //checks no new monsters were added to the team
		
		//Item tests
		
		//test buying an item in the normal case
		player.setGold(100);
		int itemsBeforePurchase = player.getItems().size();
		goldBeforePurchase = player.getGold();
		shop.buy(shop.getItemsOnSale().get(0));	
		assertEquals(player.getItems().size(),itemsBeforePurchase+1);//checks that the player has a new item
		assertTrue(goldBeforePurchase > player.getGold()); // checks that gold was spent to buy the item
		
		//tests trying to buy an item with no gold
		player.setGold(0);
		shop.buy(shop.getItemsOnSale().get(1));
		assertEquals(player.getGold(), 0); // checks that gold was not changed from unsuccessful purchase
		assertEquals(player.getItems().size(), teamSize); //checks no new items were added to the players inventory
		
		//tests try to buy an item when your inventory is full
		
}
	
	@Test
	void testSell() {
		//Monster tests
		
		player.setGold(0);
		shop = new Shop(15);	
		shop.setPlayer(player);
		
		//test selling a monster
		int goldBeforeSale = player.getGold();
		player.addToTeam(new Monster());
		assertEquals(player.getTeam().size(),2); //checks a monster was actually added
		shop.sell(player.getTeam().get(0));
		assertEquals(player.getTeam().size(),1); //checks that a monster was removed upon its sale
		assertTrue(goldBeforeSale < player.getGold()); // checks that gold was gained form the sale
		
		//tests selling an item
		goldBeforeSale = player.getGold();
		player.addItem(new Item());
		assertEquals(player.getItems().size(),1);//checks an item was actually added
		shop.sell(player.getItems().get(0));
		assertEquals(player.getItems().size(),0);//checks that the item was removed upon its sale
		assertTrue(goldBeforeSale < player.getGold()); // checks that gold was gained form the sale
	}

}
