package main.gui.entity;

import java.util.Arrays;
import java.util.Random;

import main.gui.GamePanel;


public class NPC_OldMan extends Entity {

	public NPC_OldMan(GamePanel gp) {
		super(gp);

		direction = "down";
		speed = 1;

		getImage();
		setDialogue();

	}

	public void getImage() {

		up1 = setup("/npc/oldman_up_1");
		up2 = setup("/npc/oldman_up_2");
		down1 = setup("/npc/oldman_down_1");
		down2 = setup("/npc/oldman_down_2");
		left1 = setup("/npc/oldman_left_1");
		left2 = setup("/npc/oldman_left_2");
		right1 = setup("/npc/oldman_right_1");
		right2 = setup("/npc/oldman_right_2");

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
		dialogues[0] = "Hello " + gp.game.getPlayer().getName()
				+ "\nWould you like to Buy or Sell your Items and Monsters";

	}

	public void speak() {
		faceUserOnInteract();
		Arrays.fill(gp.ui.options, null);
		gp.ui.options[0] = "Buy";
		gp.ui.options[1] = "Sell Monsters";
		gp.ui.options[2] = "Sell Items";
		gp.dialogueMode = gp.shopMode;
		gp.gameState = gp.dialogueState;

		super.speak();

	}
}
