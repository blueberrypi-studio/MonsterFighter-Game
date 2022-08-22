package main.gui;

import main.gui.entity.Entity;

/**
 * The collision checker class checks collisions between player and walls,
 * players and entities, and entities and players
 * 
 * @author RyiSnow code implemented from tutorial
 */
public class CollisionChecker {

	GamePanel gp;

	/**
	 * Collision checker constructor
	 * 
	 * @param gp GamePanel
	 */
	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}

	/**
	 * Checks entity collision with tiles
	 * 
	 * @param entity Either NPC or Player
	 */
	public void checkTile(Entity entity) {

		int entityLeftWorldX = entity.worldX + entity.solidArea.x;
		int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
		int entityTopWorldY = entity.worldY + entity.solidArea.y;
		int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

		int entityLeftCol = entityLeftWorldX / gp.tileSize;
		int entityRightCol = entityRightWorldX / gp.tileSize;
		int entityTopRow = entityTopWorldY / gp.tileSize;
		int entityBottomRow = entityBottomWorldY / gp.tileSize;

		int tileNum1, tileNum2;
		try {
			switch (entity.direction) {
			case "up": // check collision up
				entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
				tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
				tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];

				if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
					entity.collisionOn = true;
				}
				break;
			case "down": // check collision down
				entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
				tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
				tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

				if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
					entity.collisionOn = true;
				}
				break;
			case "left": // check collision left
				entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
				tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
				tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

				if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
					entity.collisionOn = true;
				}
				break;
			case "right": // check collision right
				entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
				tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
				tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

				if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
					entity.collisionOn = true;
				}
				break;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			entity.collisionOn = true; // Fixes game crash if entity leaves map
									   // Entity leaving map is not possible now
		}
	}

	/**
	 * Check Collision between two entities
	 * 
	 * @param entity NPC
	 * @param target NPC or Player
	 * @return
	 */
	public int checkEntity(Entity entity, Entity[] target) {

		int index = 999;

		for (int i = 0; i < target.length; i++) {
			if (target[i] != null) {

				// Get Entity's Solid Area position
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;

				// Get the targets solid area position
				target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
				target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

				switch (entity.direction) {
				case "up":
					entity.solidArea.y -= entity.speed;
					if (entity.solidArea.intersects(target[i].solidArea)) {
						entity.collisionOn = true;
						index = i;
						break;
					}

				case "down":
					entity.solidArea.y += entity.speed;
					if (entity.solidArea.intersects(target[i].solidArea)) {
						entity.collisionOn = true;
						index = i;
						break;
					}

				case "left":
					entity.solidArea.x -= entity.speed;
					if (entity.solidArea.intersects(target[i].solidArea)) {
						entity.collisionOn = true;
						index = i;
						break;
					}

				case "right":
					entity.solidArea.x += entity.speed;
					if (entity.solidArea.intersects(target[i].solidArea)) {
						entity.collisionOn = true;
						index = i;
						break;
					}
				}
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				target[i].solidArea.x = target[i].solidAreaDefaultX;
				target[i].solidArea.y = target[i].solidAreaDefaultY;

			}
		}
		return index;

	}

	/**
	 * Check players collision with other entities
	 * 
	 * @param entity player
	 */
	public void checkPlayer(Entity entity) {
		// Get Entity's Solid Area position
		entity.solidArea.x = entity.worldX + entity.solidArea.x;
		entity.solidArea.y = entity.worldY + entity.solidArea.y;

		// Get the targets solid area position
		gp.user.solidArea.x = gp.user.worldX + gp.user.solidArea.x;
		gp.user.solidArea.y = gp.user.worldY + gp.user.solidArea.y;

		// check collisions
		switch (entity.direction) {
		case "up":
			entity.solidArea.y -= entity.speed;
			if (entity.solidArea.intersects(gp.user.solidArea)) {
				entity.collisionOn = true;
				break;
			}

		case "down":
			entity.solidArea.y += entity.speed;
			if (entity.solidArea.intersects(gp.user.solidArea)) {
				entity.collisionOn = true;
				break;
			}

		case "left":
			entity.solidArea.x -= entity.speed;
			if (entity.solidArea.intersects(gp.user.solidArea)) {
				entity.collisionOn = true;
				break;
			}

		case "right":
			entity.solidArea.x += entity.speed;
			if (entity.solidArea.intersects(gp.user.solidArea)) {
				entity.collisionOn = true;
				break;
			}
		}
		// reset values
		entity.solidArea.x = entity.solidAreaDefaultX;
		entity.solidArea.y = entity.solidAreaDefaultY;
		gp.user.solidArea.x = gp.user.solidAreaDefaultX;
		gp.user.solidArea.y = gp.user.solidAreaDefaultY;
	}
}
