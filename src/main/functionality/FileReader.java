package main.functionality;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import main.functionality.monster.Item;
import main.functionality.monster.Monster;
import main.functionality.monster.Move;


/**
 * The FileReader class provides the ability to load essential 
 * files need for the monster fighter game to run.
 */
public class FileReader {
	
	/**
	 * Reads the move list file 
	 * @param moveFile the file to read the move list from
	 * @return the move list file in the form of an arrayList of moves
	 * @throws NullPointerException if the move list file can't be found this exception is thrown
	 */
	public static ArrayList<Move> readMoveList(InputStream moveFile) throws NullPointerException{
		ArrayList<String> typeList = Game.getTypes();
		ArrayList<String> rarityList = Game.getRarities();
		ArrayList<Move> moveList = new ArrayList<Move>();
		try {
		    Scanner myReader = new Scanner(moveFile);
		    myReader.useDelimiter(",|\\s*\n");
		    myReader.nextLine(); // skips field names
		    while(myReader.hasNext()) {
		    	//read in all Move fields in sequential order
		    	String name = myReader.next();
		    	int damage = myReader.nextInt(); 
		    	String type = myReader.next();
		    	String rarity = myReader.next();
		    	
		    	
		    	if(typeList.contains(type) && rarityList.contains(rarity) ){
		    		//create the move
			    	 Move moveToAdd = new Move(name, damage, type, rarity);
			    	 moveList.add(moveToAdd);
		    	}
		    	
		    	else {
		    		if(!typeList.contains(type)){
			    		System.out.println(type +" is not a valid move type and therefore the move was not added");
			    	}
			    	
			    	if(!rarityList.contains(rarity))  {
			    		System.out.println(rarity +" is not a valid rarity and therefore the move was not added");
			    	}
		    	}
		     }
		     myReader.close();
		}
		//catch exceptions	
		catch(InputMismatchException e) {
			System.out.println("Incorrect field type found in move list file");
			e.printStackTrace();
		}
		
		return(moveList);
	}
	
	/**
	 * Reads the monster list file
	 * @param monsterFile the file to read the monster list from
	 * @return the monster list file in the form of an arrayList of monsters
	 * @throws NullPointerException if the monster list file can't be found the inputStream is null and this exception is thrown
	 */
	public static ArrayList<Monster> readMonsterList(InputStream monsterFile) throws NullPointerException {
		ArrayList<Monster> monsterList = new ArrayList<Monster>();
		ArrayList<String> typeList = Game.getTypes();
		ArrayList<String> rarityList = Game.getRarities();
		try {
		      Scanner myReader = new Scanner(monsterFile);
		      myReader.useDelimiter(",|\\s*\n");
	    	  myReader.nextLine(); //skips field names
		      while(myReader.hasNext()) {
		    	  //read in all monster fields in sequential order
		    	  String name = myReader.next();
		    	  String type = myReader.next();
		    	  int maxHealth = myReader.nextInt();
		    	  int healAmount = myReader.nextInt();
		    	  int attack = myReader.nextInt();
		    	  String rarity = myReader.next();
		    	  if(typeList.contains(type) && rarityList.contains(rarity) ){
		    		  //creates the monster with the variables defined above
			    	  Monster monsterToAdd = new Monster(name,type,maxHealth,healAmount,attack,rarity);
			    	  monsterList.add(monsterToAdd);
			    	}
			    	
			    	else {
			    		if(!typeList.contains(type)){
				    		System.out.println(type +" is not a valid monster type and therefore the monster was not added");
				    	}
				    	
				    	if(!rarityList.contains(rarity))  {
				    		System.out.println(rarity +" is not a valid rarity and therefore the monster was not added");
				    	}
			    	}
		      }
		      myReader.close();
		}
		//catch exceptions
		catch(InputMismatchException e) {
			System.out.println("Incorrect field type found in monster list file");
			e.printStackTrace();
		}
		return(monsterList);
	}
	
	/**
	 * Reads the monster probabilities file 
	 * @param monProbFile the file to read the monster probabilities from
	 * @return  the monster probabilities file in the form of a ConcurrentHashMap that maps an Integer day
	 * to an arrayList of floats where the floats in the arrayList represent a monster probability for each rarity.
	 * @throws NullPointerException if the monsterProbs file can't be found this exception is thrown
	 */
	public static ConcurrentHashMap<Integer, ArrayList<Float>> readMonsterProbs(InputStream monProbFile) throws NullPointerException {
		int maxDays = 15; // maximum amount of days the game can have
		ConcurrentHashMap<Integer, ArrayList<Float>> monsterProbs = new ConcurrentHashMap<Integer, ArrayList<Float>>(maxDays);
		try{
		    Scanner myReader = new Scanner(monProbFile);
		    myReader.useDelimiter(",|\\s*\n");
		    myReader.nextLine(); // skips field names
		    ArrayList<Float> percentList = new ArrayList<Float>(); //list used for temporarily storing percent values
		    while(myReader.hasNext()) {
		    	percentList = new ArrayList<Float>(); //list used for temporarily storing percent values
		    	int dayNumber = myReader.nextInt();
		    	for (int i=0; i<5; i++) {
		    		float prob = myReader.nextFloat();
		    		percentList.add(Float.valueOf(prob)); 		
		    	}
		    	monsterProbs.put(dayNumber, percentList);
		    }
		    myReader.close();
		}
		catch(InputMismatchException e) {
			System.out.println("Incorrect field type found in monsterProbs file");
			e.printStackTrace();
		}
		return(monsterProbs);
	}	
	
	/**
	 * Reads the move probabilities file 
	 * @param moveProbFile the file to read the move probabilities from
	 * @return the move probabilities file in the form of a ConcurrentHashMap that maps a String that
	 * represents a move rarity to an arrayList of floats where the floats in the arrayList represent a move probability
	 * for each different rarity of monster.
	 * @throws NullPointerException if the moveProbs file can't be found this exception is thrown
	 */
	public static ConcurrentHashMap<String, ArrayList<Float>> readMoveProbs(InputStream moveProbFile) throws NullPointerException {
		ConcurrentHashMap<String, ArrayList<Float>> moveProbs = new ConcurrentHashMap<String, ArrayList<Float>>();
		try{
		    Scanner myReader = new Scanner(moveProbFile);
		    myReader.useDelimiter(",|\\s*\n");
		    myReader.nextLine(); // skips field names
		    ArrayList<Float> percentList = new ArrayList<Float>(); //list used for temporarily storing percent values
		    while(myReader.hasNext()) {
		    	percentList = new ArrayList<Float>();
		    	String monsterRarity = myReader.next();
		    	for (int i=0; i<5; i++) {
		    		float prob = myReader.nextFloat();
		    		percentList.add(Float.valueOf(prob)); 		
		    	}
		    	moveProbs.put(monsterRarity, percentList);
		    }
		    myReader.close();
		}
		catch(InputMismatchException e) {
			System.out.println("Incorrect field type found in moveProbs file");
			e.printStackTrace();
		}
		return(moveProbs);
	}
	/**
	 * Reads the item List file
	 * @param itemFile the file to read the item list from
	 * @throws NullPointerException if the item list file can't be found this exception is thrown
	 * @return the item list file in the form of an arrayList of items
	 */
	public static ArrayList<Item> readItemList(InputStream itemFile) throws NullPointerException{
		ArrayList<Item> itemList = new ArrayList<Item>();
		try {
		    Scanner myReader = new Scanner(itemFile);
		    myReader.useDelimiter(",|\\s*\n");
		    myReader.nextLine(); // skips field names
		    while(myReader.hasNext()) {
		    	String name = myReader.next();
		    	String statToBuff= myReader.next(); 
		    	int buffAmount = myReader.nextInt();
		    	int price = myReader.nextInt();
		    	Item itemToAdd = new Item(name,price,statToBuff,buffAmount);
		    	itemList.add(itemToAdd); 
		    }
		    myReader.close();
		}
		//catch exceptions
		catch(InputMismatchException e) {
			System.out.println("Incorrect field type found in item list file");
			e.printStackTrace();
		}
		return(itemList);
	}
	
	/**
	 * Reads the TypeCombatTable file
	 * @param TypeCombatFile the file to read the type Combat Table from
	 * @throws NullPointerException if the typecombatTable file can't be found this exception is thrown
	 * @return the typeCombatTable file as a HashMap that maps Strings representing a target monsters type
	 * to another ConcurrentHashmap that maps Strings that represent a moves type to floats that represent the combat modifier
	 * applied when that move type is used on the target type that is mapping to the ConcurrentHashMap it is a part of.
	 */
	public static  ConcurrentHashMap<String,ConcurrentHashMap<String,Float>> readTypeCombatTable (InputStream TypeCombatFile) throws NullPointerException{
		//creates a new hash map that is used to map target types to damage modifier floats
		ConcurrentHashMap<String,Float> targetTypeToModifier;
		ConcurrentHashMap<String,ConcurrentHashMap<String,Float>> typeCombatInfo = new ConcurrentHashMap<String,ConcurrentHashMap<String,Float>>();
		String TargetType;
		float dmgModifier;
		ArrayList<String> targetTypes = new ArrayList<String>();//list of possible target monster types
		
		try {
		    Scanner myReader = new Scanner(TypeCombatFile);	  
		    myReader.useDelimiter(",|\\s*\n");
		    myReader.nextLine(); // skips field names
		    int numOfTypes = 8; //declares the number of types in the type combat table
		    for(int i = 0; i <numOfTypes; i++) {
		    	targetTypes.add(myReader.next());	    	
		    }
		    while(myReader.hasNext()) {
		    	TargetType = myReader.next();
	    		targetTypeToModifier = new ConcurrentHashMap<String,Float>(); 
		    	for (int i =0; i<numOfTypes; i++) {
		    		dmgModifier = myReader.nextFloat();
		    		//puts a targetType modifier pair into the targetTypeToModifier hashMap
		    		targetTypeToModifier.put(targetTypes.get(i), dmgModifier); 
		    	}
		    	typeCombatInfo.put(TargetType, targetTypeToModifier);
		    }
		    myReader.close();
		}
		//catch exceptions
		catch(InputMismatchException e) {
			System.out.println("Incorrect field type found in typeCombatTable file");
			e.printStackTrace();
		}
		return(typeCombatInfo);

		
		
		
		
	}
	
	
}

