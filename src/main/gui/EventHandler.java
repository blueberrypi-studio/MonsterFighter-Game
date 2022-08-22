package main.gui;

import java.awt.Rectangle;
import java.util.Arrays;

public class EventHandler {

	GamePanel gp;
	Rectangle eventRect;
	int eventRectDefaultX, eventRectDefaultY;

	public EventHandler(GamePanel gp) {
		this.gp = gp;

		eventRect = new Rectangle();
		eventRect.x = 23;
		eventRect.y = 23;
		eventRect.width = 2;
		eventRect.height = 2;
		eventRectDefaultX = eventRect.x;
		eventRectDefaultY = eventRect.y;

	}

	public void checkEvent() {
		// player sleep

		if (hit(18, 34, "any")) {
			sleep(gp.dialogueState);
		}

	}

	public boolean hit(int eventCol, int eventRow, String requiredDirection) {

		boolean hit = false;

		gp.user.solidArea.x = gp.user.worldX + gp.user.solidArea.x;
		gp.user.solidArea.y = gp.user.worldY + gp.user.solidArea.y;
		eventRect.x = eventCol * gp.tileSize + eventRect.x;
		eventRect.y = eventRow * gp.tileSize + eventRect.y;

		if (gp.user.solidArea.intersects(eventRect)) {
			if (gp.user.direction.contentEquals(requiredDirection) || requiredDirection.contentEquals("any")) {
				hit = true;
			}
		}

		// reset hit-boxes (x, y)
		gp.user.solidArea.x = gp.user.solidAreaDefaultX;
		gp.user.solidArea.y = gp.user.solidAreaDefaultY;
		eventRect.x = eventRectDefaultX;
		eventRect.y = eventRectDefaultY;

		return hit;
	}

	private void sleep(int gameState) {
		// TODO Auto-generated method stub
		if (gp.KeyH.enterPressed) {
			Arrays.fill(gp.ui.options, null);
			gp.ui.options[0] = "Yes";
			gp.ui.options[1] = "no";
			
			gp.ui.currentDialogue = "Would you like to sleep and go to the next day";
			gp.dialogueMode = gp.sleepMode;
			gp.gameState = gp.dialogueState;
		}
		gp.KeyH.enterPressed = false;

	}
}
