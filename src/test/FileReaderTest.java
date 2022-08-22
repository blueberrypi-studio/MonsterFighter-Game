package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.functionality.FileReader;
import main.functionality.Game;
import main.functionality.monster.Monster;
import main.functionality.monster.Move;


class FileReaderTest {

	
	String testFilePath = "/test/resources/";
	
	@BeforeEach
	public void setUp() {
		Game.setDoGUi(false);
	}
	
	@Test
	public void testReadMoveList() {
		//tests reading a valid move file and makes sure that the FileReader formats it correctly into an ArrayList
		try {
			InputStream file = getClass().getResourceAsStream(testFilePath +"ValidMoveList.csv");
			ArrayList<Move> moveList = FileReader.readMoveList(file);
			
			String[] moveNames = {"Earthquake", "Fireball", "Tackle"};
			int[] moveDmgs = {70,60,50};
			String[] moveTypes = {"Earth","Fire","Normal"};
			String[] moveRaritys = {"Rare","Uncommon","Common"};
			
			for (int i=0; i<moveList.size(); i++) {
				assertEquals(moveList.get(i).getName(),moveNames[i]);
				assertEquals(moveList.get(i).getDamage(),moveDmgs[i]);
				assertEquals(moveList.get(i).getType(),moveTypes[i]);
				assertEquals(moveList.get(i).getRarity(),moveRaritys[i]);
			}
		}
		catch (NullPointerException e) {			
			System.out.println("Could not find specified file");
			e.printStackTrace();
		}
	}

	@Test
	public void testReadMonsterList() {
		ArrayList<Monster> monsterList;
		//tests reading a valid monster file and makes sure that the FileReader formats it correctly into an an ArrayList
		try {
			InputStream file = getClass().getResourceAsStream(testFilePath +"ValidMonsterList.csv");
			monsterList = FileReader.readMonsterList(file);
			Monster testMonster = new Monster("Joe","Fire",10,5,5,"Common");
			assertEquals(monsterList.get(0).equals(testMonster), true);
		} catch (NullPointerException e) {
			System.out.println("Could not find specified file");
			e.printStackTrace();
		}
	} 

}
