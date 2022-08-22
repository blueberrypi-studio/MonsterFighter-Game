package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.functionality.Game;
import main.functionality.Player;
import main.functionality.RandomEvent;
import main.functionality.monster.Monster;

class RandomEventTest {
	
	Player player;
	RandomEvent event;
	Game game = new Game();
	
	@BeforeEach
	void setUp() {
		Game.setDoGUi(false);
		player = new Player("Jeff", new Monster());
		event = new RandomEvent(player ,2);
		
		
	}

	@Test
	void testMonsterLevelUp() {
		int oldAttack = player.getTeam().get(0).getAttack();
		event.monsterLevelUp();
		int newAttack = player.getTeam().get(0).getAttack();
		assertEquals(oldAttack,newAttack-2);
	}

	@Test
	void testMonsterLeave() {
		event.monsterLeave();
		assertEquals(player.getTeam().size(),0);
	}

	@Test
	void testMonsterJoin() {
		event.monsterJoin();
		assertEquals(player.getTeam().size(),2);
	}

}
