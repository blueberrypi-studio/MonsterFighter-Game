package main.functionality.monster;

import java.util.ArrayList;
import java.util.Random;

import main.functionality.Game;

/**
 * The MonsterGenerator class is used to randomly generate monsters based on a day
 * that represents the day of the current game. Later days have a higher chance of generating rarer monsters
 * these chances are determined by the monsterProbs field of the {@link Game} class. This class also randomly generates 
 * a set of moves for each monster it generates based on the rarity of the monster it is generating the moves for.
 * The probabilities that determine the chance of a rarity of move being generated are determined by the
 * moveProbs field of the {@link Game} class.
 */
public class MonsterGenerator {
	/** The day this monster generator was initialised for*/
	private int day;
	
	/**
	 * constructs a new monster generator 
	 * @param day the day this monster generator is being initialised for
	 */
	
	public MonsterGenerator(int day) {
		this.day = day;
	}

	/**
	 * Generates a random monster
	 * @return a randomly Generated Monster based on the day of this monsterGenerator
	 */
	public Monster generateMonster() {
		//System.out.println("Day: " + day);
		ArrayList<Float> monsterProbsToday = Game.getMonsterProbs().get(day); //gets monster probabilities based on the current day
		Random rng = new Random();
		float randomFloat = rng.nextFloat(100);
		int listIndex =-1;
		while (randomFloat > 0){
			listIndex += 1;
			randomFloat = randomFloat - monsterProbsToday.get(listIndex);
		}
		String monsterRarity = Game.getRarities().get(listIndex); //gets a rarity based on the list index
		ArrayList<Monster> monsterList = Game.getMonsters();
		
		ArrayList<Monster> monstersCorrectRarity = new ArrayList<Monster>();
		for(Monster monster: monsterList) {
			if(monster.getRarity().equals(monsterRarity)){
				monstersCorrectRarity.add(monster);
			}
		}
		//generates a random int that specifies an index of the monsterCorrectRarity list so a monster can be selected
		int randomInt = rng.nextInt(monstersCorrectRarity.size());
		Monster monsterToAdd = monstersCorrectRarity.get(randomInt);
		monsterToAdd = new Monster(monsterToAdd);
		ArrayList<Move> Moves = generateMoves(monsterRarity, monsterToAdd.getType());
		monsterToAdd.setMoves(Moves);
		return(monsterToAdd);
	}
	
	/**
	 * Generates a random set of moves
	 * @param rarity the rarity of the monster to generate moves for
	 * @param type the type of the monster to generate moves for
	 * @return a randomly generated set of moves based on monster rarity that are valid for a given type of monster
	 */
	private ArrayList<Move> generateMoves(String rarity , String type){
		ArrayList<Float> moveProbsForRarity = Game.getMoveProbs().get(rarity); //get the move probabilities based on the monsters rarity
		ArrayList<Move>moveList = Game.getMoves(); //gets the full move list
				
		ArrayList<Move> ValidMoveList = new ArrayList<Move>();//creates a new ArrayList to hold moves of valid type
		//generate valid move list
		for(Move move: moveList) {
			if(move.getType().equals(type) || move.getType().equals("Normal")){
				ValidMoveList.add(move);
			}
		}
		
		ArrayList<Move> movesToAdd = new ArrayList<Move>(); // creates the arrayList which will hold the moves to be added to a monster
		
		int movesCreated= 0;
		Random rng = new Random();
		while(movesCreated < 4) {
			Float randomFloat = rng.nextFloat(100);
			int listIndex =-1;
			while (randomFloat > 0){
				listIndex += 1;
				randomFloat = randomFloat - moveProbsForRarity.get(listIndex);
			}
			//System.out.println(listIndex);
			String moveRarity = Game.getRarities().get(listIndex); //gets a rarity for the move to be generated based on the list index
			
			ArrayList<Move> rarityMoves = new ArrayList<Move>(); //creates a new ArrayList to hold moves of a specific rarity that are valid
			
			//adds all moves that are valid and the correct rarity to the rarityMoves list
			for(Move move: ValidMoveList) {
				if(move.getRarity().equals(moveRarity)){
					rarityMoves.add(move);
				}
			}
			
			//generates a random int that specifies an index of the rarityMoves list so a move can be selected
			int randomInt = rng.nextInt(rarityMoves.size());
			movesToAdd.add(rarityMoves.get(randomInt));
			movesCreated +=1;
			}
		return(movesToAdd);
	
	
	}
	
}
