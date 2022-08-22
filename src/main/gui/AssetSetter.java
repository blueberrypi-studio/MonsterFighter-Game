package main.gui;

import main.functionality.monster.combat.Battle;
import main.gui.entity.BattleNPC;
import main.gui.entity.NPC_OldMan;

/**
 * Class to set NPC values and positions
 * 
 * @author Haydn
 *
 */
public class AssetSetter {

	GamePanel gp;

	/**
	 * Constructor for the assetSetter class
	 * 
	 * @param gp GamePanel values and methods
	 */
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}

	public void setObject() {
		// not implemented in this game, but can be used in expansion
	}

	/**
	 * Initialize the merchant NPC
	 */
	public void setMerchantNPC() {
		// Merchant NPC
		gp.npc[0] = new NPC_OldMan(gp);
		gp.npc[0].worldX = gp.tileSize * 29;
		gp.npc[0].worldY = gp.tileSize * 36;
	}

	/**
	 * Loads the 3 battle NPCs at pre set positions Individial stats can be set here
	 * Such as doBattle and doAI
	 */
	public void setNPC() {

		// battle NPCs

		gp.npc[1] = new BattleNPC(gp, 1);
		gp.npc[1].worldX = gp.tileSize * 10;
		gp.npc[1].worldY = gp.tileSize * 6;
		gp.npc[1].doAI = true; // DEBUG disable AI
		gp.npc[1].doBattle = true;

		gp.npc[2] = new BattleNPC(gp, 2);

		gp.npc[2].worldX = gp.tileSize * 38;
		gp.npc[2].worldY = gp.tileSize * 9;
		gp.npc[2].doAI = true; // DEBUG disable AI
		gp.npc[2].doBattle = true;

		gp.npc[3] = new BattleNPC(gp, 3);

		gp.npc[3].worldX = gp.tileSize * 23;
		gp.npc[3].worldY = gp.tileSize * 7;
		gp.npc[3].doAI = true; // DEBUG disable AI
	}
}