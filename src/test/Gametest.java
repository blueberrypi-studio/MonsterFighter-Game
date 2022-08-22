package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.functionality.Game;
import main.functionality.Player;
import main.functionality.monster.Monster;

class Gametest {
	
	Game game;
	Player testPlayer;
	String fakePlayerInput;

	@BeforeEach
	void setUp() throws Exception {
		game = new Game();
		Game.setDoGUi(false);
		testPlayer =  new Player("TESTMAN",new Monster());
		fakePlayerInput="";
	}

	
	@Test
	void TestSetUpGame() {
		//tests the game setup works for invalid inputs
		fakePlayerInput= "Jeff\n";
		//tries entering an invalid number aswell as an invalid index for each prompt
		//then correctly gives an answer
		for(int i=0; i<3;i++) {	
			fakePlayerInput += "k\n20\n3\n"; 
		}
		fakePlayerInput +="k\n20\n2\nTERMINATE\n";
		game.setPlayerInput(fakePlayerInput);
		game.setUpGame();
		assertEquals(game.getPlayer().getGold(),20);//asserts that the players gold has been correctly set for the difficulty 3(Hard)
		assertEquals(game.getPlayer().getTeam().size() ,1); //asserts that the play was initialised with a monster
		
	}
	
	@Test
	void testViewRenameTeam(){
		for(int i=0; i<2;i++) {	
			fakePlayerInput += "k\n20\n1\n"; 
		}
	}
	
	@Test
	void testSleep() {
		//asserts that sleep causes the players monsters to heal
		testPlayer.getTeam().get(0).setCurrentHealth(1);	
		game.setPlayer(testPlayer);
		game.setNumberOfDays(2);
		fakePlayerInput ="6\n";	
		game.setPlayerInput(fakePlayerInput);
		game.sleep();
		//asserts that sleep causes the players monsters to heal
		assertTrue(testPlayer.getTeam().get(0).getCurrentHealth() >1);
		//asserts that sleep ends the game and therefore does not increment the days 
		assertEquals(game.getDay(),2);

	}
	

	@Test
	void testSwapFrontMonster() {
		Monster secondMon = new Monster();
		secondMon.setName("Bob");
		testPlayer.addToTeam(secondMon);
		Monster firstMon = testPlayer.getTeam().get(0);
		//uses swapFrontMonster to swap the players two monsters and asserts that they have swapped indices
		game.setPlayer(testPlayer);
		game.swapFrontMonster(secondMon);
		assertEquals(testPlayer.getTeam().get(0),secondMon);
		assertEquals(testPlayer.getTeam().get(1),firstMon);
	}

}
