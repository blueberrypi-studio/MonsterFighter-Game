package main.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import main.functionality.monster.Item;
import main.functionality.monster.Monster;
import main.gui.entity.BattleNPC;

public class KeyHandler implements KeyListener {

	public boolean upPressed, downPressed, leftPressed, rightPressed, debugPressed, enterPressed, menuPressed;
	GamePanel gp;

	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		gp.game.guiOutput.reset();

		// =================== PlayState =====================

		if (gp.gameState == gp.playState) {

			// Movement Keys
			if (code == KeyEvent.VK_W) {
				upPressed = true;
			}
			if (code == KeyEvent.VK_S) {
				downPressed = true;
			}
			if (code == KeyEvent.VK_A) {
				leftPressed = true;
			}
			if (code == KeyEvent.VK_D) {
				rightPressed = true;
			}

			// dialogue keys
			if (code == KeyEvent.VK_E) {
				enterPressed = true;
			}

			// Debug keys
			if (code == KeyEvent.VK_P) {
				if (debugPressed) {
					debugPressed = false;
				} else if (!debugPressed) {
					debugPressed = true;
				}
			}

			// Map live reload for DEV purposes
			if (code == KeyEvent.VK_O) {
				gp.tileM.loadMap("/maps/world01.txt");
			}

//			//Pause key
//			if(code==KeyEvent.VK_TAB) {gp.gameState = gp.pauseState;}

			// Menu key
			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.menuState;
			}
		}

		// =====================================================

		// =================== helpState ======================

		else if (gp.gameState == gp.helpState) {
			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.playState;
			}
		}

		// =====================================================

		// =================== consolePauseState ======================

		else if (gp.gameState == gp.consolePauseState) {
			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.playState;
			} else if (code == KeyEvent.VK_E) {
				gp.gameState = gp.playState;
			}
		}

		// =====================================================

		// =================== shopSellItemsState ======================

		else if (gp.gameState == gp.shopSellItemsState) {

			ArrayList<Item> itemList = gp.game.getPlayer().getItems();

			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.playState;
			} // Return to playState

			if (code == KeyEvent.VK_W) {
				gp.ui.sellNum--;
				if (gp.ui.sellNum < 0) {
					gp.ui.sellNum = gp.ui.player.getItems().size() - 1;
				}
			}
			if (code == KeyEvent.VK_S) {
				gp.ui.sellNum++;
				if (gp.ui.sellNum > gp.ui.player.getItems().size() - 1) {
					gp.ui.sellNum = 0;
				}
			}

			if (code == KeyEvent.VK_E) {
				Item item;

				for (int i = 0; i < itemList.size(); i++) {
					if (gp.ui.sellNum == i) {
						item = itemList.get(i);
						gp.ui.shop.sell(item);
					}
				}
			}
		}

		// =====================================================

		// =================== shopSellMonstersState ======================

		else if (gp.gameState == gp.shopSellMonstersState) {

			ArrayList<Monster> monsterList = gp.game.getPlayer().getTeam();

			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.playState;
			} // Return to playState

			if (code == KeyEvent.VK_W) {
				gp.ui.sellMonNum--;
				if (gp.ui.sellMonNum < 0) {
					gp.ui.sellMonNum = gp.ui.player.getTeam().size() - 1;
				}
			}
			if (code == KeyEvent.VK_S) {
				gp.ui.sellMonNum++;
				if (gp.ui.sellMonNum > gp.ui.player.getTeam().size() - 1) {
					gp.ui.sellMonNum = 0;
				}
			}

			if (code == KeyEvent.VK_E) {
//				System.out.println("Pressed E from shop");
				Monster monster;

				for (int i = 0; i < monsterList.size(); i++) {
					if (gp.ui.sellMonNum == i) {
						monster = monsterList.get(i);
						gp.ui.shop.sell(monster);
					}
					if (monsterList.size() == 0) {
						gp.game.checkIfGameOver();
					}
				}
			}
		}

		// =====================================================

		// =================== shopBuyState =======================

		else if (gp.gameState == gp.shopBuyState) {
			int monsterListSize = gp.ui.shop.getMonstersOnSale().size();
			int itemListSize = gp.ui.shop.getItemsOnSale().size();
			int shopSize = monsterListSize + itemListSize;
			int itemIndex = 0;
			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.playState;
			}

			if (code == KeyEvent.VK_W) {
				gp.ui.shopNum--;
				if (gp.ui.shopNum < 0) {
					gp.ui.shopNum = shopSize - 1;
				}
			}
			if (code == KeyEvent.VK_S) {
				gp.ui.shopNum++;
				if (gp.ui.shopNum > shopSize - 1) {
					gp.ui.shopNum = 0;
				}
			}

			if (code == KeyEvent.VK_E) {
				Monster monster;
				Item item;

				itemIndex = getItemListIndex(monsterListSize, gp.ui.shopNum);
				switch (gp.ui.shopNum) {
				case 0:
					if (monsterListSize >= 1) {
						monster = gp.ui.shop.getMonstersOnSale().get(0);
						gp.ui.shop.buy(monster);
					} else if (itemListSize >= 1) {
						item = gp.ui.shop.getItemsOnSale().get(itemIndex);
						gp.ui.shop.buy(item);
					}
					break;
				case 1:
					if (monsterListSize >= 2) {
						monster = gp.ui.shop.getMonstersOnSale().get(1);
						gp.ui.shop.buy(monster);
					} else if (itemListSize >= 1) {
						item = gp.ui.shop.getItemsOnSale().get(itemIndex);
						gp.ui.shop.buy(item);
					}
					break;
				case 2:
					if (monsterListSize >= 3) {
						monster = gp.ui.shop.getMonstersOnSale().get(2);
						gp.ui.shop.buy(monster);
					} else if (itemListSize >= 1) {
						item = gp.ui.shop.getItemsOnSale().get(itemIndex);
						gp.ui.shop.buy(item);
					}
					break;
				case 3:
					if (monsterListSize >= 4) {
						monster = gp.ui.shop.getMonstersOnSale().get(3);
						gp.ui.shop.buy(monster);
					} else if (itemListSize >= 1) {
						item = gp.ui.shop.getItemsOnSale().get(itemIndex);
						gp.ui.shop.buy(item);
					}
					break;
				case 4:
					item = gp.ui.shop.getItemsOnSale().get(itemIndex);
					gp.ui.shop.buy(item);
					break;
				case 5:
					item = gp.ui.shop.getItemsOnSale().get(itemIndex);
					gp.ui.shop.buy(item);
					break;
				case 6:
					item = gp.ui.shop.getItemsOnSale().get(itemIndex);
					gp.ui.shop.buy(item);
					break;
				}
			}
		}

		// =====================================================

		// =================== DialogueState ===================

		else if (gp.gameState == gp.dialogueState) {
			if (code == KeyEvent.VK_E) {
				if (gp.dialogueMode == gp.shopMode) {
					if (gp.ui.dialogueNum == 0) {
						gp.gameState = gp.shopBuyState;
					}
					if (gp.ui.dialogueNum == 1) {
						gp.gameState = gp.shopSellMonstersState;
					}
					if (gp.ui.dialogueNum == 2) {
						gp.gameState = gp.shopSellItemsState;
					}

				} else if (gp.dialogueMode == gp.sleepMode) {
					if (gp.ui.dialogueNum == 0) {
						gp.ui.drawConsolePause();
						gp.playerSleep();
					}
					if (gp.ui.dialogueNum == 1) {
						gp.gameState = gp.playState;
					}
				} else if (gp.dialogueMode == gp.textMode) {
					if (gp.ui.dialogueNum == 0 || gp.ui.dialogueNum == 1) {
						gp.gameState = gp.playState;
					}
				} else if (gp.dialogueMode == gp.battleMode) {
					if (gp.ui.dialogueNum == 0) {
						gp.gameState = gp.battleState;
					} else if (gp.ui.dialogueNum == 1) {
						gp.gameState = gp.playState;
					}
				}
			}

			else if (code == KeyEvent.VK_W) {
				gp.ui.dialogueNum--;
				if (gp.ui.dialogueNum < 0) {
					gp.ui.dialogueNum = gp.ui.options.length - 1;
				}
			} else if (code == KeyEvent.VK_S) {
				gp.ui.dialogueNum++;
				if (gp.ui.dialogueNum > gp.ui.options.length - 1) {
					gp.ui.dialogueNum = 0;
				}
			}
		}

		// =====================================================

		// =================== battleState ===================
		else if (gp.gameState == gp.battleState) { // Check if game in battleState

			if (gp.battleUI.combatMode == gp.battleUI.optionMode) { // Check if battle in optionMode
				if (code == KeyEvent.VK_W) { // Scroll function of menu
					gp.battleUI.battleNum--;
					if (gp.battleUI.battleNum < 0) {
						gp.battleUI.battleNum = gp.battleUI.options.length - 1;
					}
				} else if (code == KeyEvent.VK_S) { // Scroll function of menu
					gp.battleUI.battleNum++;
					if (gp.battleUI.battleNum > gp.battleUI.options.length - 1) {
						gp.battleUI.battleNum = 0;
					}
				} else if (code == KeyEvent.VK_E) { // Select button of current menu				
						if (gp.battleUI.battleNum == 0) {
							gp.battleUI.combatMode = gp.battleUI.moveMode; // switch to move selection menu
						}
						if (gp.battleUI.battleNum == 1) {
							gp.battleUI.combatMode = gp.battleUI.switchMode; // switch to monster swap menu
						}
						if (gp.battleUI.battleNum == 2) {
							if (gp.game.getPlayer().getItems().size() == 0) {
								System.out.println("You have no items to use.");
							} else {
							gp.battleUI.combatMode = gp.battleUI.itemMode; // switch to item use menu
						}
					}
				}
			} else if (gp.battleUI.combatMode == gp.battleUI.moveMode) { // Check if battle in optionMode
				if (code == KeyEvent.VK_ESCAPE) {
					gp.battleUI.combatMode = gp.battleUI.optionMode;
				} else if (code == KeyEvent.VK_W) { // Scroll function of menu
					gp.battleUI.moveModeNum--;
					if (gp.battleUI.moveModeNum < 0) {
						gp.battleUI.moveModeNum = 3;
					}
				} else if (code == KeyEvent.VK_S) { // Scroll function of menu
					gp.battleUI.moveModeNum++;
					if (gp.battleUI.moveModeNum > 3) {
						gp.battleUI.moveModeNum = 0;
					}
				} else if (code == KeyEvent.VK_E) {
					for (int i = 0; i < 4; i++) { // run battle using chosen user move
						if (gp.battleUI.moveModeNum == i) {
							gp.battleUI.selectedMove = i;
							gp.battleUI.combatMode = gp.battleUI.fightMode;
						}
					}
				}

			} else if (gp.battleUI.combatMode == gp.battleUI.switchMode) { // Check if battle in optionMode
				if (code == KeyEvent.VK_ESCAPE) {
					gp.battleUI.combatMode = gp.battleUI.optionMode;
				}
				if (code == KeyEvent.VK_W) {
					gp.battleUI.monsterSwapNum--;
					if (gp.battleUI.monsterSwapNum < 0) {
						gp.battleUI.monsterSwapNum = gp.battleUI.battle.getPlayerBattlers().size() - 1;
					}
				} else if (code == KeyEvent.VK_S) {
					gp.battleUI.monsterSwapNum++;
					if (gp.battleUI.monsterSwapNum > gp.battleUI.battle.getPlayerBattlers().size() - 1) {
						gp.battleUI.monsterSwapNum = 0;
					}
				} else if (code == KeyEvent.VK_E) {
					for (int i = 0; i < 4; i++) {
						if (gp.battleUI.monsterSwapNum == i) {
							gp.battleUI.battle.swapOut(gp.battleUI.battle.getPlayerBattlers().get(i), true);
							gp.battleUI.combatMode = gp.battleUI.optionMode;

						}
					}
				}

			} else if (gp.battleUI.combatMode == gp.battleUI.loseMode // Check if battle in win OR loseMode
					|| gp.battleUI.combatMode == gp.battleUI.winMode) {
				if (code == KeyEvent.VK_ESCAPE) {
					gp.gameState = gp.playState;
				}
				if (code == KeyEvent.VK_W) {
					gp.battleUI.monsterResultNum--;
					if (gp.battleUI.monsterResultNum < 0) {
						gp.battleUI.monsterResultNum = gp.battleUI.player.getTeam().size() - 1;
					}
				} else if (code == KeyEvent.VK_S) {
					gp.battleUI.monsterResultNum++;
					if (gp.battleUI.monsterResultNum > gp.battleUI.player.getTeam().size() - 1) {
						gp.battleUI.monsterResultNum = 0;
					}

				}
			} else if (gp.battleUI.combatMode == gp.battleUI.itemMode) {
				if (code == KeyEvent.VK_ESCAPE) {
					gp.battleUI.combatMode = gp.battleUI.optionMode;
				}
				if (code == KeyEvent.VK_W) {
					gp.battleUI.itemUseNum--;
					if (gp.battleUI.itemUseNum < 0) {
						gp.battleUI.itemUseNum = gp.battleUI.player.getItems().size() - 1;
					}
				} else if (code == KeyEvent.VK_S) {
					gp.battleUI.itemUseNum++;
					if (gp.battleUI.itemUseNum > gp.battleUI.player.getItems().size() - 1) {
						gp.battleUI.itemUseNum = 0;
					}
				} else if (code == KeyEvent.VK_E) {
					for (int i = 0; i < 4; i++) {
						if (gp.battleUI.itemUseNum == i) {
							gp.battleUI.itemSelected = gp.battleUI.player.getItems().get(i);
							gp.battleUI.combatMode = gp.battleUI.itemUseMode;
						}
					}
				}
			} else if (gp.battleUI.combatMode == gp.battleUI.itemUseMode) {

				if (code == KeyEvent.VK_ESCAPE) {
					gp.battleUI.combatMode = gp.battleUI.itemMode; // Return to itemState
				} else if (code == KeyEvent.VK_W) {
					gp.battleUI.monsterUseNum--;
					if (gp.battleUI.monsterUseNum < 0) {
						gp.battleUI.monsterUseNum = gp.battleUI.battle.getPlayerBattlers().size() - 1;
					}
				} else if (code == KeyEvent.VK_S) {
					gp.battleUI.monsterUseNum++;
					if (gp.battleUI.monsterUseNum > gp.battleUI.battle.getPlayerBattlers().size() - 1) {
						gp.battleUI.monsterUseNum = 0;
					}
				}
				if (code == KeyEvent.VK_E) {
					for (int i = 0; i < gp.battleUI.battle.getPlayerBattlers().size(); i++) {
						if (gp.battleUI.monsterUseNum == i) {
							gp.battleUI.itemSelected.use(gp.battleUI.battle.getPlayerBattlers().get(i));
							gp.game.getPlayer().getItems()
									.remove(gp.battleUI.player.getItems().indexOf(gp.battleUI.itemSelected));
							gp.battleUI.combatMode = gp.battleUI.optionMode;
							gp.battleUI.itemSelected = null;
						}

					}
				}
			}
		}

		// =====================================================

		// =================== menuState =======================

		else if (gp.gameState == gp.menuState) { // Check if game in menuState
			if (code == KeyEvent.VK_ESCAPE) { // if ESC pressed
				gp.gameState = gp.playState; // return to game
			}

			if (code == KeyEvent.VK_W) { // if W pressed
				gp.ui.commandNum--; // scroll function of menu
				if (gp.ui.commandNum < 0) { // reset if scroll number exceeds menu bounds
					gp.ui.commandNum = 3;
				}
			}
			if (code == KeyEvent.VK_S) { // if W pressed
				gp.ui.commandNum++; // scroll function of menu
				if (gp.ui.commandNum > 3) { // reset if scroll number exceeds menu bounds
					gp.ui.commandNum = 0;
				}
			}

			if (code == KeyEvent.VK_E) { // if E pressed
				switch (gp.ui.commandNum) { // Switch State based on user input
				case 0:
					gp.gameState = gp.monsterMenuState; // Open monster Menu
					break;
				case 1:
					gp.gameState = gp.inventoryState; // Open inventory
					break;
				case 2:
					gp.gameState = gp.helpState; // Open help screen
					break;
				case 3:
					System.exit(0); // TODO Add check if user meant this input // exit the game
					break;
				}
			}
		}

		// =====================================================

		// =================== mosterMenuState =================

		else if (gp.gameState == gp.monsterMenuState) { // check if game in monsterMenuState
			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.menuState; // Return to menuState
			}

			if (code == KeyEvent.VK_W) { // Scroll function of menu
				gp.ui.monsterNum--;
				if (gp.ui.monsterNum < 0) { // reset if scroll number exceeds menu bounds
					gp.ui.monsterNum = gp.ui.player.getTeam().size() - 1;
				}
			}
			if (code == KeyEvent.VK_S) { // Scroll function of menu
				gp.ui.monsterNum++;
				if (gp.ui.monsterNum > gp.ui.player.getTeam().size() - 1) { // reset if scroll number exceeds menu
																			// bounds
					gp.ui.monsterNum = 0;
				}
			}
			if (code == KeyEvent.VK_R) { // rename monster
				Monster monToRename = gp.ui.player.getTeam().get(gp.ui.monsterNum);
				String newMonName = JOptionPane.showInputDialog("Please enter a new name for your " + monToRename+" that is 15 or less characters"); // open rename option pane
				if (newMonName == null){
					; //do nothing
				}
				else {
					while (newMonName.length() >15 ) {
						newMonName = JOptionPane.showInputDialog("Please enter a new name for your " + monToRename+" that is 15 or less characters"); // open rename option pane
					}
					if (newMonName.length() !=0) {
						monToRename.setName(newMonName); // set new Monster name
					}
				}
			}
			if (code == KeyEvent.VK_T) { // switch first monster
				Monster newFrontMon = gp.ui.player.getTeam().get(gp.ui.monsterNum);
				gp.game.swapFrontMonster(newFrontMon); // bring selected monster to front of team (used first in battle)
			}

		}

		// =====================================================

		// ================ itemUseState ========================
		else if (gp.gameState == gp.itemUseState) { // check if game in itemUseState
			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.inventoryState; // Return to menuState
			}

			if (code == KeyEvent.VK_W) { // Scroll function of menu
				gp.ui.monsterUseNum--;
				if (gp.ui.monsterUseNum < 0) { // reset if scroll number exceeds menu bounds
					gp.ui.monsterUseNum = gp.ui.player.getTeam().size() - 1;
				}
			}
			if (code == KeyEvent.VK_S) { // Scroll function of menu
				gp.ui.monsterUseNum++;
				if (gp.ui.monsterUseNum > gp.ui.player.getTeam().size() - 1) { // reset if scroll number exceeds menu
																				// bounds
					gp.ui.monsterUseNum = 0;
				}
			}
			if (code == KeyEvent.VK_E) {
				for (int i = 0; i < gp.game.getPlayer().getTeam().size(); i++) {
					if (gp.ui.monsterUseNum == i) {
						gp.ui.itemSelected.use(gp.game.getPlayer().getTeam().get(i));

						gp.game.getPlayer().getItems().remove(gp.ui.player.getItems().indexOf(gp.ui.itemSelected));
						gp.gameState = gp.inventoryState;
						gp.ui.itemSelected = null;
					}
				}
			}
		}

		// =================== inventoryState =================

		else if (gp.gameState == gp.inventoryState) {
			if (code == KeyEvent.VK_ESCAPE) {
				gp.gameState = gp.menuState;
			} // Return to menuState

			if (code == KeyEvent.VK_W) {
				gp.ui.itemNum--;
				if (gp.ui.itemNum < 0) {
					gp.ui.itemNum = gp.ui.player.getItems().size() - 1;
				}
			}
			if (code == KeyEvent.VK_S) {
				gp.ui.itemNum++;
				if (gp.ui.itemNum > gp.ui.player.getItems().size() - 1) {
					gp.ui.itemNum = 0;
				}
			}
			if (code == KeyEvent.VK_E) {
				for (int i = 0; i < gp.game.getPlayer().getItems().size(); i++) {
					if (gp.ui.itemNum == i) {
						gp.ui.itemSelected = gp.game.getPlayer().getItems().get(i);
						gp.gameState = gp.itemUseState;
					}
				}
			}
		}
	}

	// =====================================================

	/**
	 * helper method to buy correct items and monsters
	 * 
	 * @param monsterListSize size of monster list
	 * @param shopNum         selected shop options
	 * @return index of itemList to buy
	 */
	private int getItemListIndex(int monsterListSize, int shopNum) {
		// Reset menu options relative to list indexes based on list sizes
		switch (shopNum) {
		case 0:
			if (monsterListSize == 0) {
				return 0;
			}
			break;
		case 1:
			if (monsterListSize == 0) {
				return 1;
			} else if (monsterListSize == 1) {
				return 0;
			}
			break;
		case 2:
			if (monsterListSize == 0) {
				return 2;
			} else if (monsterListSize == 1) {
				return 1;
			} else if (monsterListSize == 2) {
				return 0;
			}
			break;

		case 3:
			if (monsterListSize == 1) {
				return 2;
			} else if (monsterListSize == 2) {
				return 1;
			} else if (monsterListSize == 3) {
				return 0;
			}
			break;

		case 4:
			if (monsterListSize == 2) {
				return 2;
			} else if (monsterListSize == 3) {
				return 1;
			} else if (monsterListSize == 4) {
				return 0;
			}
			break;

		case 5:
			if (monsterListSize == 3) {
				return 2;
			} else if (monsterListSize == 4) {
				return 1;
			} else {
				return 0;
			}
		case 6:
			return 2;
		}
		return 0;
	}

	/**
	 * KeyRealese method to reset key States on key release
	 */
	@Override
	public void keyReleased(KeyEvent e) {

		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W) {
			upPressed = false;
		}

		if (code == KeyEvent.VK_S) {
			downPressed = false;
		}

		if (code == KeyEvent.VK_A) {
			leftPressed = false;
		}

		if (code == KeyEvent.VK_D) {
			rightPressed = false;
		}

		if (code == KeyEvent.VK_E) {
			enterPressed = false;
		}

	}

}
