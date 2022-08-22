package main.gui.entity;

import java.util.Arrays;
import java.util.Random;

import main.functionality.Player;
import main.functionality.monster.combat.Battle;
import main.gui.BattleUI;
import main.gui.GamePanel;

public class BattleNPC extends Entity {

	public Battle battle;
	public BattleUI battleUI;

	public Player player = gp.game.getPlayer();
	public String name;

	public BattleNPC(GamePanel gp, int i) {
		super(gp);
		battle = gp.game.getTodaysBattles().get(i - 1);
		direction = "down";
		speed = 1;

		getImage();
		setDialogue();
	}

	public void getImage() {

		up1 = setup("/npc/battler_up_1");
		up2 = setup("/npc/battler_up_2");
		down1 = setup("/npc/battler_down_1");
		down2 = setup("/npc/battler_down_2");
		left1 = setup("/npc/battler_left_1");
		left2 = setup("/npc/battler_left_2");
		right1 = setup("/npc/battler_right_1");
		right2 = setup("/npc/battler_right_2");

	}

	public void setAction() {

		actionLockCounter++;
		if (actionLockCounter == 120) {

			Random random = new Random();
			int i = random.nextInt(100) + 1; // pick a number from 1 to 100

			if (i <= 25) {
				direction = "up";
			}
			if (i > 25 && i <= 50) {
				direction = "down";
			}
			if (i > 50 && i <= 75) {
				direction = "left";
			}
			if (i > 75 && i <= 100) {
				direction = "right";
			}
			actionLockCounter = 0;

		}

	}

	public void setDialogue() {
		dialogues[0] = "Greetings " + gp.game.getPlayer().getName() + "\n" + "You dare challenge me to a Duel?";
	}

	public void speak() {
		faceUserOnInteract();
		if (battle == null) {
			dialogues[0] = "I Don't want to battle anymore!";
			dialogues[1] = "You're too good for me";
			dialogues[2] = "LEAVE ME ALONE!";
			gp.dialogueMode = gp.textMode;
			gp.gameState = gp.dialogueState;
			Arrays.fill(gp.ui.options, null); // RESET options array to clear extra options
			gp.ui.options[0] = "Ok";
			gp.ui.options[1] = "Bye";
			super.speak();
			return;

		} else if (gp.game.getPlayer().getTeam().size() == 0) {
			dialogues[0] = "You have no monsters to challenge me with.\n" + "Please purchase more from the shop.";
			Arrays.fill(gp.ui.options, null); // RESET options array to clear extra options
			gp.ui.options[0] = "Ok";
			gp.ui.options[1] = "Bye";
			gp.dialogueMode = gp.textMode;
			gp.gameState = gp.dialogueState;
			super.speak();
			return;

		} else if (gp.game.getPlayer().getBattlesLeft() == 0) {
			dialogues[0] = "All your Monsters Fainted Today\nGo get some rest and try again tomorrow!";
			Arrays.fill(gp.ui.options, null); // RESET options array to clear extra options
			gp.ui.options[0] = "Ok";
			gp.ui.options[1] = "Bye";
			gp.dialogueMode = gp.textMode;
			gp.gameState = gp.dialogueState;
			super.speak();
			return;
			
		} else {
			gp.battleUI = new BattleUI(gp, this);
			name = battle.getEnemyPName();
			Arrays.fill(gp.ui.options, null); // RESET options array to clear extra options
			gp.ui.options[0] = "Accept";
			gp.ui.options[1] = "Decline";
			gp.battleUI.combatMode = gp.battleUI.optionMode;
			gp.dialogueMode = gp.battleMode;
			gp.gameState = gp.dialogueState;
			setDialogue();
			super.speak();
		}
	}
}
