package main.functionality.monster.combat;

import main.functionality.Game;
import main.functionality.monster.Monster;
import main.functionality.monster.Move;

/**
 * The BattlingMonster class is designed to extend the functionality of the {@link Monster} class
 * by giving it methods that can be used for battle along with a prefix that details
 * whether it is an enemy or a player monster
 */
public class BattlingMonster extends Monster{
	/** a prefix that details whether this is an enemy or player monster */
	private String prefix; 

	
	
	/**
	 * creates a new battling monster from a regular monster
	 * @param monsterToBattle the monster that is being used to Battle
	 * @param playerOwned determines the monsterPrefix for the monster,  true ="Your " , false="The enemy "
	 */
	public BattlingMonster(Monster monsterToBattle, Boolean playerOwned) {
		super(monsterToBattle);
		this.setCurrentHealth(monsterToBattle.getCurrentHealth());
		this.setMoves(monsterToBattle.getMoves());
		if (playerOwned == true){
			prefix = ("Your ");
		}
		else {prefix = "The enemy ";}
	}
	/**
	 *Causes this battling monster to perform an attack on another battling monster
	 * @param move the move being used to perform the attack
	 * @param target the monster the attack is being performed on
	 */
	public void UseAttack(Move move, BattlingMonster target) {
		//calculates total damage to be done
		String moveType = move.getType();
		String targetType = target.getType();
		Float typeAttackModifier = Game.getCombatTypeRelationships().get(moveType).get(targetType);
		double monsterAttackModifer = (this.getAttack()*0.1);
		int totalDmg = (int) (move.getDamage()*typeAttackModifier*monsterAttackModifer);
		
		//inflicts the damage and prints the result
		String moveEffectiveness = getMoveEffectiveNess(typeAttackModifier);
		System.out.println(prefix + this +" used " + move.getName() + moveEffectiveness);
		target.setCurrentHealth(target.getCurrentHealth() -totalDmg);
		target.prefix = target.prefix.substring(0,1).toLowerCase() + target.prefix.substring(1); //uncapitalises the prefix
		System.out.println(totalDmg+" damage " +"was inflicted to "+target.prefix +target);
		target.prefix = target.prefix.substring(0,1).toUpperCase() + target.prefix.substring(1); //capitalises the prefix
	}
	
	
	/**
	 * @return a string description of the BattlingMonster
	 */
	@Override
	public String getDescription() {
		String rString = String.format("Type:%s, Attack:%d, Max Health:%d, Current Health:%d, Moves:%s",getType(),getAttack(),getMaxHealth(),getCurrentHealth(),getMoves());
		return(rString);
	}
	
	/**
	 * @param modifier that determines the effectiveness of the move used
	 * @return a string detailing the effectiveness of the move used
	 */
	private String getMoveEffectiveNess(float typeAttackModifier) {
		if (typeAttackModifier == 2) {
			return("\nIt's super effective"); 
		}
		else if(typeAttackModifier ==0.5) {
			return("\nIt's not very effective");	
		}
		else {
			return("");
		}
	}
	
	

}
