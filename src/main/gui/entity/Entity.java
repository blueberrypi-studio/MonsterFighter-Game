package main.gui.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.functionality.Game;
import main.functionality.monster.combat.Battle;
import main.gui.GamePanel;
import main.gui.UtilityTools;

public class Entity {
	// Entity Super Class

	GamePanel gp;
	Game game;

	public int worldX, worldY;
	public int speed;
	public boolean doAI = true;
	public boolean doBattle = false;
	public Battle battle = null;

	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public String direction;
	public int solidAreaDefaultX, solidAreaDefaultY;

	public int spriteCounter = 0;
	public int spriteNum = 1;

	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

	public boolean collisionOn = false;

	public int actionLockCounter;

	String dialogues[] = new String[20];

	int dialogueIndex = 0;

	public Entity(GamePanel gp) {
		this.gp = gp;
		game = gp.game;
	}

	public BufferedImage setup(String imagePath) {

		UtilityTools uTool = new UtilityTools();
		BufferedImage image = null;

		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = uTool.ScaleImage(image, gp.tileSize, gp.tileSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;

	}

	public void draw(Graphics2D g2) {

		BufferedImage image = null;

		int screenX = worldX - gp.user.worldX + gp.user.screenX;
		int screenY = worldY - gp.user.worldY + gp.user.screenY;

		// =============IMPROVED RENDERING EFFICIENCY===================
		if (worldX + gp.tileSize > gp.user.worldX - gp.user.screenX
				&& worldX - gp.tileSize < gp.user.worldX + gp.user.screenX
				&& worldY + gp.tileSize > gp.user.worldY - gp.user.screenY
				&& worldY - gp.tileSize < gp.user.worldY + gp.user.screenY) {

			switch (direction) {
			case "up":
				if (spriteNum == 1) {
					image = up1;
				}
				if (spriteNum == 2) {
					image = up2;
				}
				break;

			case "down":
				if (spriteNum == 1) {
					image = down1;
				}
				if (spriteNum == 2) {
					image = down2;
				}
				break;

			case "left":
				if (spriteNum == 1) {
					image = left1;
				}
				if (spriteNum == 2) {
					image = left2;
				}
				break;

			case "right":
				if (spriteNum == 1) {
					image = right1;
				}
				if (spriteNum == 2) {
					image = right2;
				}
				break;

			}
			g2.drawImage(image, screenX, screenY, null);
		}
		// =============================================================
	}

	public void faceUserOnInteract() {
		// Switch NPC direction to face Character
		switch (gp.user.direction) {
		case "up":
			direction = "down";
			break;
		case "down":
			direction = "up";
			break;
		case "left":
			direction = "right";
			break;
		case "right":
			direction = "left";
			break;
		}
	}

	public void speak() {
		if (dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;

		faceUserOnInteract();

	}

	public void setAction() {
	}

	public void update() {

		setAction();

		collisionOn = false;
		gp.collisionChecker.checkTile(this);
		gp.collisionChecker.checkPlayer(this);

		// IF COLLISION IS FALSE, NPC CAN MOVE
		if (collisionOn == false) {
			if (doAI) {
				switch (direction) {
				case "up":
					worldY -= speed;
					break;
				case "down":
					worldY += speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
				}
			}
		}
		// ANIMATION CODE
		spriteCounter++;

		if (spriteCounter > 12) {
			if (spriteNum == 1) {
				spriteNum = 2;
			} else if (spriteNum == 2) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}

	}

}
