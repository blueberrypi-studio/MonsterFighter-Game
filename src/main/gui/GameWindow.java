package main.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.functionality.Game;
import main.functionality.Player;

import java.awt.Color;

public class GameWindow {

	private JFrame frame;
	private Game game;
	Player player;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameWindow window = new GameWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameWindow() {
		initialize();
	}
	
	public GameWindow(Game newGame) {
		game =  newGame;
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
		game.closeGameWindow(this);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1920, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel gameWindowPanel = new JPanel();
		gameWindowPanel.setBackground(Color.WHITE);
		gameWindowPanel.setBounds(0, 0, 1904, 1041);
		frame.getContentPane().add(gameWindowPanel);
		
		GamePanel gamePanel = new GamePanel(game);
		gameWindowPanel.add(gamePanel);
		
	}

	
}
