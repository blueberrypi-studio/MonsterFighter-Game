package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.functionality.Game;
import main.functionality.Player;
import main.functionality.monster.Item;
import main.functionality.monster.Monster;
import main.functionality.monster.MonsterGenerator;
import main.functionality.monster.Move;
import main.functionality.monster.combat.Battle;


class BattleTest {
	private Battle battleToTest;
	InputStream sysInBackup =System.in;
	Game game = new Game();
	
	
	@BeforeEach
	public void setUp() {
		Game.setDoGUi(false);
	}
	//function available for manual testing of the battle class
	/*
	@Test
	public void manualTest() {
		battleToTest = autoGenerateBattle();
		battleToTest.start();
	}
	*/
	
	/**
	 * helper function for the battle tests generates a custom battle
	 * @param playerMonsters the amount of monsters the test needs the player to have
	 * @param enemyMonsters the amount of monsters the test need the enemy to have
	 * @param playerWeak determines if the player should have strong monsters or weak monster true = weak monsters
	 */
	private Battle generateCustomBattle(int playerMonsters , int enemyMonsters, boolean playerWeak) {
		Monster starter;
		ArrayList<Move> monsterMoves = new ArrayList<Move>();
		//creates an extremely powerful monster 
		if (playerWeak == false){
			starter = new Monster("Unstoppable-Phoenix","Fire", 300,200,40,"Legendary");
			Move moveToadd= new Move("Delete",999,"Fire","Common");
			for(int i=0; i<4;i++) {
				monsterMoves.add(moveToadd);
			}
			starter.setMoves(monsterMoves);	
		}
		//creates an extremely weak monster 
		else {
			starter = new Monster("Unstoppable-Phoenix","Fire", 1,200,5,"Legendary");
			Move moveToadd= new Move("Delete",1,"Fire","Common");
			for(int i=0; i<4;i++) {
				monsterMoves.add(moveToadd);
			}
			starter.setMoves(monsterMoves);
			
		}
		
		//initialise the player with playerMonsters copies of the above created monster along with a steak item
		Player player = new Player("Jeff", starter);
		player.setGold(0);
		player.addItem(new Item("Steak",10,"Attack", 2));
		for(int i=0; i <playerMonsters-1; i++) {
			player.addToTeam(starter);
		}

		
		//creates a weak monster
		ArrayList<Move> enemyMoves = new ArrayList<Move>();
		for(int i=0; i<4;i++) {
			enemyMoves.add(new Move());
		}
		Monster enemyMonster = new Monster();
		enemyMonster.setMoves(enemyMoves);
		
		//Initialise the enemy with enemyMonsters copies the weak monster
		Player enemyPlayer = new Player("Bob", enemyMonster);
		for(int i=0; i <enemyMonsters-1; i++) {
			enemyPlayer.addToTeam(enemyMonster);
		}
		//create a new battle with the player and the enemyPlayer
		battleToTest = new Battle(player, enemyPlayer, 2, 1);
		return(battleToTest);
	}
	
	/**
	 * helper function for the battle tests that Auto-generates a battle
	 * with random monsters
	 */
	public Battle autoGenerateBattle() {
		MonsterGenerator gen = new MonsterGenerator(15);
		Monster starter = gen.generateMonster();
		Player player = new Player("Jeff", starter);
		battleToTest = new Battle(player, 2,1, "Jeff");
		return(battleToTest);
	}
	
	
	@Test
	public void testBattle() {
		battleToTest = autoGenerateBattle();
		InputStream sysInBackup =System.in;
		System.setIn(new ByteArrayInputStream("TERMINATE\n".getBytes()));
		battleToTest.start();
		System.setIn(sysInBackup);
		//tests GenerateBattlers
		assertFalse(battleToTest.getEnemies().size() < 2);
		assertFalse(battleToTest.getEnemies().size() > 4);
		
		//tests that start sets the battlers to the ones in the first list position
		assertEquals(battleToTest.getEnemyMonster(), battleToTest.getEnemies().get(0));
		assertEquals(battleToTest.getPlayerMonster(), battleToTest.getPlayerBattlers().get(0));
	}
	
	
	@Test
	public void testPlayerVictory() {
		InputStream sysInBackup =System.in;
		System.setIn(new ByteArrayInputStream("Attack\n1\n".getBytes()));
		battleToTest = generateCustomBattle(1,1,false);
		Player player = battleToTest.start();
		System.setIn(sysInBackup);
		assertTrue(player.getGold() >0); // checks the player gains gold for winning the battle
		assertTrue(player.getScore() >0); //checks the player gets score for winning the battle
		//tests using healing item on players monster after it is attacked
	}
	
	@Test
	public void testSwapOut() {
		InputStream sysInBackup =System.in;
	    System.setIn(new ByteArrayInputStream("Swap battler\n1\nTERMINATE\n".getBytes()));
	    battleToTest = generateCustomBattle(2,2,false);
		battleToTest.start();
		System.setIn(sysInBackup);
		//test player swap
		assertEquals(battleToTest.getPlayerMonster(), battleToTest.getPlayerBattlers().get(1));	
	}
	
	@Test
	public void testUseItem() {
		InputStream sysInBackup =System.in;
		System.setIn(new ByteArrayInputStream("Use item\nSteak\nTERMINATE\n".getBytes()));
		battleToTest = generateCustomBattle(4,4,false);
		battleToTest.start();
		System.setIn(sysInBackup);
		//tests that the player monsters attack increases upon the use of the steak item
		assertEquals(battleToTest.getPlayerMonster().getAttack(), 42);
		 //checks that the enemy monster attacked the players monster after the item was used
		assertTrue(battleToTest.getPlayerMonster().getCurrentHealth() <battleToTest.getPlayerMonster().getMaxHealth());
	}
	@Test
	public void testDefeat() {
		InputStream sysInBackup =System.in;
		System.setIn(new ByteArrayInputStream("Attack\n1\n".getBytes()));
		battleToTest = generateCustomBattle(1,4,true);
		Player player = battleToTest.start();
		System.setIn(sysInBackup);
		assertEquals(player.getGold(),0); // checks the player gains no gold for losing the battle
		assertEquals(player.getScore() ,0); //checks the player gets no score for losing the battle
		assertEquals(player.getTodaysFaints(),1); //ensures the players faint count was updated accordingly
		assertEquals(player.getTeam().get(0).getCurrentHealth(), 0); //ensures the players monster is set to zero health
	}
	
	

	
	
	
}
