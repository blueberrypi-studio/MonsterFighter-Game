package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.functionality.Game;
import main.functionality.monster.Item;
import main.functionality.monster.Monster;

class ItemTest {
	Item potion = new Item();
	Game game;

	@BeforeEach
	void setUp() {
		Game.setDoGUi(false);
		potion = new Item();
		game = new Game();
	}

	@Test
	void testUse() {
		Monster testMonster = new Monster();
		
		testMonster.setCurrentHealth(50);
		potion.use(testMonster);
		assertEquals(testMonster.getCurrentHealth(),150); //tests that the monster is healed by the potion
		
		//tests that the monster cannot be healed over its maximum health
		potion = new Item();
		potion.use(testMonster);
		assertEquals(testMonster.getCurrentHealth(), testMonster.getMaxHealth());
	
	}
	
	@Test
	void testGetDescription() {
		String printString = ("Potion: This item buffs CurrentHealth by 100 points.");
		System.out.println(printString);
		assertEquals(potion.getDescription(),printString );
	}
	
	//runs through all the items that are loaded into the game and tries to use them checkign that they are valid
	@Test
	void testAllItemsValidity() {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream errorOutput = new PrintStream(byteArrayOutputStream);
		PrintStream stdOutput= System.out;
		System.setOut(errorOutput);
		for(Item item :Game.getItems()) {
			Monster testMonster = new Monster();
			item.use(testMonster);
		System.setErr(stdOutput);
		String errorMessage = byteArrayOutputStream.toString();
		assertFalse(errorMessage.contains("Monsters do not have a stat called "));
		}
			
	}

}
