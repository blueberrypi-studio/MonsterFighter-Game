package main.functionality;

import java.util.Random;

import main.functionality.monster.Monster;
import main.functionality.monster.MonsterGenerator;
/**
 * The random event class is used to generate random events 
 * and cause them to happen to a player. This class can causes more than one
 * random event to occur.
 */
public class RandomEvent {
	
	/** The player to perform random events for/on */
	private Player player;
	/** The day the random events are possibly taking place on*/
	int day;
	/** The random number generator responsible for making the events random*/
	private Random rng;
	
	/**
	 * Creates a new random event
	 * @param player the player to create the random event for
	 * @param day the day at which the random event is generated
	 */
	public RandomEvent(Player player, int day) {
		this.player = player;
		this.day =day;
		rng = new Random();
	}

	/**
	 * Levels up a monster in the players team
	 */
	public void monsterLevelUp() {
		int monToLevelIndex = rng.nextInt(player.getTeam().size()); //gets the index of the monster to level randomly
		Monster monToLevel = player.getTeam().get(monToLevelIndex);
		monToLevel.setAttack(monToLevel.getAttack()+2);
		System.out.println("Your "+monToLevel+" gained 2 attack overnight");
	}
	/**
	 * Causes a monster in the players team to leave
	 */
	public void monsterLeave() {
		int monToLeaveIndex = rng.nextInt(player.getTeam().size()); //gets the index of the monster to leave randomly
		System.out.println(player.getTeam().get(monToLeaveIndex)+" Left your team overnight");
		player.getTeam().remove(monToLeaveIndex);
		
	}
	/**
	 * Causes a new monster to join the players team
	 */
	public void monsterJoin() {
		MonsterGenerator gen = new MonsterGenerator(day);
		player.addToTeam(gen.generateMonster());
		Monster newMonster = player.getTeam().get(player.getTeam().size() -1);
		System.out.println("A new monster called "+newMonster +" joined your team overnight");
	}
	
	/**
	 * Randomly decides what events should happen to this randomEvents player
	 * if an event is decided to happen its corresponding method is called
	 */
	public void decideEvents() {	
		int monsterLevelUpChance =  20;
		if(rng.nextInt(100)< monsterLevelUpChance) {
			monsterLevelUp();
		}
		
		int monsterLeaveChance = 1*player.getTodaysFaints();
		if(rng.nextInt(100)< monsterLeaveChance) {
			monsterLeave();
		}
		int monsterJoinChance =  (4 - player.getTeam().size()) * 4;
		if(rng.nextInt(100)<monsterJoinChance) {
			monsterJoin();
		}
	}
	
}
