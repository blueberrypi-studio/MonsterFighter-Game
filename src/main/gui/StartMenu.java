package main.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.border.SoftBevelBorder;

import main.functionality.Game;
import main.functionality.monster.Monster;
import main.functionality.Player;

import javax.swing.border.BevelBorder;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public class StartMenu {

	private JFrame frame;
	private JTextField nameTextField;
	private Game game;
	private Monster starter;
	private ArrayList<Monster> starters;
	private String customMonsterName;

	/**
	 * Create the application.
	 */
	
	
	/**
	 * @wbp.parser.constructor
	 */
	public StartMenu() {
		initialize();
	}
	
	public StartMenu(Game newGame) {
		game =  newGame;
		starters = game.generateStarters();
		initialize();
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void closeWindow() {
		frame.dispose();
	}
	
	public void finishedWindow() {
		game.closeStartMenu(this);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.ORANGE);
		
		
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JPanel menuContainer = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, menuContainer, 14, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, menuContainer, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, menuContainer, -489, SpringLayout.EAST, frame.getContentPane());
		menuContainer.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		springLayout.putConstraint(SpringLayout.WEST, menuContainer, 500, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(menuContainer);
		menuContainer.setLayout(null);
		
		JLabel titleLabel = new JLabel("MonsterFighter Game*");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(10, 11, 884, 86);
		titleLabel.setFont(new Font("Script MT Bold", Font.PLAIN, 71));
		menuContainer.add(titleLabel);
		
		JLabel finePrintLabel = new JLabel("*Any Similarities to Pok\u00E9mon are unintentional and completely coincedental.");
		finePrintLabel.setBounds(18, 964, 884, 38);
		menuContainer.add(finePrintLabel);
		
		JLabel subtitleLabel = new JLabel("Tactics, Adventure, fun");
		subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		subtitleLabel.setFont(new Font("Script MT Bold", Font.PLAIN, 57));
		subtitleLabel.setBounds(10, 93, 884, 70);
		menuContainer.add(subtitleLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 174, 884, 2);
		menuContainer.add(separator);
		
		JLabel nameQuestionLabel = new JLabel("What is your name?");
		nameQuestionLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		nameQuestionLabel.setBounds(64, 214, 279, 38);
		menuContainer.add(nameQuestionLabel);
		
		nameTextField = new JTextField();
		nameTextField.setBounds(399, 214, 416, 37);
		menuContainer.add(nameTextField);
		nameTextField.setColumns(10);
		
		JSlider timeSlider = new JSlider();
		timeSlider.setSnapToTicks(true);
		timeSlider.setPaintLabels(true);
		timeSlider.setPaintTicks(true);
		timeSlider.setMinorTickSpacing(3);
		timeSlider.setMajorTickSpacing(3);
		timeSlider.setMaximum(15);
		timeSlider.setMinimum(3);
		timeSlider.setBounds(10, 314, 884, 38);
		menuContainer.add(timeSlider);
		
		JLabel daysLabel = new JLabel("Game Lenth (Days)");
		daysLabel.setHorizontalAlignment(SwingConstants.CENTER);
		daysLabel.setBounds(10, 278, 884, 44);
		menuContainer.add(daysLabel);
		
		JPanel difficultyButtonPanel = new JPanel();
		difficultyButtonPanel.setBounds(10, 403, 884, 93);
		menuContainer.add(difficultyButtonPanel);
		difficultyButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton easyButton = new JButton("Easy");
		easyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.setDifficulty(1);
			}
		});
		difficultyButtonPanel.add(easyButton);
		
		JButton mediumButton = new JButton("Medium");
		mediumButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.setDifficulty(2);
			}
		});
		difficultyButtonPanel.add(mediumButton);
		
		JButton hardButton = new JButton("Hard");
		hardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.setDifficulty(3);
			}
		});
		difficultyButtonPanel.add(hardButton);
		
		JLabel difficultyLabel = new JLabel("Choose Difficulty");
		difficultyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		difficultyLabel.setBounds(10, 356, 870, 61);
		menuContainer.add(difficultyLabel);
		
		
		//button that starts the main game screen with values that have been set by the user
		JButton startButton = new JButton("Start your Adventure!");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = (nameTextField.getText()); // Set Player Name
				if(name ==null || name.length() ==0 || name.length() >15){
					JOptionPane.showMessageDialog(null, "Please enter a name for yourself that is 15 characters or less");
					return;
				}
				game.setNumberOfDays(timeSlider.getValue()); // Set Number of Days
				
				if( starter ==null) {
					JOptionPane.showMessageDialog(null, "Please select a starter monster");
					return;
				}
				//if the player gave their monster a custom starter name set their starters name 
				if (customMonsterName != null && customMonsterName.length() !=0) {
					starter.setName(customMonsterName);
				}
				Player player = new Player(name,starter);
				
				game.setPlayer(player);
				game.refreshShop();
				player.setBattlesLeft(3);
				if (game.getDifficulty() ==0){
					JOptionPane.showMessageDialog(null, "Please select a difficulty option");
					return;
				}
				player.setGold(60/game.getDifficulty());
				game.getShopToday().setPlayer(player);
				finishedWindow();
				
			}
		});
		startButton.setBackground(Color.CYAN);
		startButton.setBounds(21, 910, 884, 55);
		menuContainer.add(startButton);
		
		JPanel starterChoicePanel = new JPanel();
		starterChoicePanel.setBounds(22, 570, 868, 258);
		menuContainer.add(starterChoicePanel);
		
		JButton starter1Choice = new JButton("");
		starter1Choice.setBounds(0, 0, 294, 344);
		starter1Choice.setIcon(new ImageIcon(StartMenu.class.getResource("/main/resources/sprites/Arctic-Beast.png")));
		starter1Choice.setSelectedIcon(new ImageIcon(StartMenu.class.getResource("/main/resources/sprites/Arctic-Beast.png")));
		starter1Choice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Max this is starter 1 button press
				starter = starters.get(0);
				
			}
		});
		starterChoicePanel.setLayout(null);
		starterChoicePanel.add(starter1Choice);
		
		JButton starterChoice2 = new JButton("");
		starterChoice2.setBounds(284, 0, 294, 324);
		starterChoice2.setIcon(new ImageIcon(StartMenu.class.getResource("/main/resources/sprites/Gilled-Beast.png")));
		starterChoice2.setSelectedIcon(new ImageIcon(StartMenu.class.getResource("/main/resources/sprites/Gilled-Beast.png")));
		starterChoice2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Max this is starter 2 button press
				starter = starters.get(1);
			}
		});
		starterChoicePanel.add(starterChoice2);
		
		JButton starterChoice3 = new JButton("");
		starterChoice3.setBounds(575, 0, 294, 324);
		starterChoice3.setIcon(new ImageIcon(StartMenu.class.getResource("/main/resources/sprites/Flaming-Beast.png")));
		starterChoice3.setSelectedIcon(new ImageIcon(StartMenu.class.getResource("/main/resources/sprites/Flaming-Beast.png")));
		starterChoice3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Max this is starter 3 button press
				starter = starters.get(2);
			}
		});
		starterChoicePanel.add(starterChoice3);
		
		JLabel chooseStarterLabel = new JLabel("Choose your starting Monster (Click to choose)");
		chooseStarterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		chooseStarterLabel.setBounds(10, 507, 884, 27);
		menuContainer.add(chooseStarterLabel);
		
		JLabel starter1Label = new JLabel("Arctic Beast");
		starter1Label.setFont(new Font("Elephant", Font.PLAIN, 15));
		starter1Label.setBounds(121, 539, 102, 24);
		menuContainer.add(starter1Label);
		
		JLabel starter2Label = new JLabel("Gilled Beast");
		starter2Label.setFont(new Font("Elephant", Font.PLAIN, 15));
		starter2Label.setBounds(408, 538, 102, 24);
		menuContainer.add(starter2Label);
		
		JLabel starter3Label = new JLabel("Flaming Beast");
		starter3Label.setFont(new Font("Elephant", Font.PLAIN, 15));
		starter3Label.setBounds(682, 538, 122, 24);
		menuContainer.add(starter3Label);
		
		//button that bring up a box to input a custom monster name
		JButton customStarterName = new JButton("Give your starter a custom name");
		customStarterName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				customMonsterName = JOptionPane.showInputDialog("Please enter a new name for your chosen monster, that is 15 or less characters");
				if(customMonsterName ==null){return;}	
				while (customMonsterName.length() >15) {
					customMonsterName = JOptionPane.showInputDialog("Please enter a new name for your chosen monster, that is 15 or less characters"); // open rename option pane
				}		
			}
		});
		customStarterName.setBounds(286, 845, 317, 42);
		menuContainer.add(customStarterName);
		
		
		
		frame.setBounds(100, 100, 1920, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}
