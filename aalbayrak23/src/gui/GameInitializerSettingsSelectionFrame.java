package gui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logic.Era;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class GameInitializerSettingsSelectionFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNumberOfRounds;

	/**
	 * constructor method of the GameInitializerSettingsSelectionFrame Class that is used for creating a
	 * game setting selection screen
	 */
	public GameInitializerSettingsSelectionFrame() {
		//WindowBuilder
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(30, 30, 35));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JComboBox eraComboBox = new JComboBox();
		eraComboBox.setModel(new DefaultComboBoxModel(new String[] {"PAST", "PRESENT", "FUTURE"}));
		eraComboBox.setToolTipText("");
		eraComboBox.setBounds(340, 100, 90, 21);
		contentPane.add(eraComboBox);
		
		JComboBox gridSizeComboBox = new JComboBox();
		gridSizeComboBox.setModel(new DefaultComboBoxModel(new String[] {"10 X 10", "15 X 15", "20 X 20"}));
		gridSizeComboBox.setToolTipText("10x10 15x15 20x20");
		gridSizeComboBox.setBounds(340, 140, 90, 21);
		contentPane.add(gridSizeComboBox);
		
		textFieldNumberOfRounds = new JTextField();
		textFieldNumberOfRounds.setText("10");
		textFieldNumberOfRounds.setBounds(340, 180, 90, 19);
		contentPane.add(textFieldNumberOfRounds);
		textFieldNumberOfRounds.setColumns(10);
		
		JButton bttnStart = new JButton("NEW GAME");
		bttnStart.addActionListener(new ActionListener() {
			/**
			 * @param e: used to add action to the start button
			 */
			public void actionPerformed(ActionEvent e) {
				try {
		            String eraStr = (String) eraComboBox.getSelectedItem();
		            Era selectedEra;
		            if (eraStr.equalsIgnoreCase("Past")) {
		            	selectedEra = Era.PAST;
		            }
		            else if (eraStr.equalsIgnoreCase("Present")) {
		            	selectedEra = Era.PRESENT;
		            }
		            else {
		            	selectedEra = Era.FUTURE;
		            }

		            String gridSizeStr = (String) gridSizeComboBox.getSelectedItem();
		            String cleanSize = gridSizeStr.split(" ")[0].trim(); 
		            int gridSize = Integer.parseInt(cleanSize);

		            int rounds = Integer.parseInt(textFieldNumberOfRounds.getText().trim());
		            if (rounds < 10) {
		                JOptionPane.showMessageDialog(null, "Error: You have to enter at least 10 rounds!");
		                return;
		            }
		            dispose();
		            new GameFrame(selectedEra, gridSize, rounds).setVisible(true);

		        }
				catch (Exception ex) {
		            ex.printStackTrace(); 
		            JOptionPane.showMessageDialog(null, "Please enter valid numbers!");
		        }
		    }
		});
		//WindowBuilder
		bttnStart.setFont(new Font("Consolas", Font.BOLD, 16));
		bttnStart.setBounds(315, 265, 100, 45);
		bttnStart.setContentAreaFilled(false);
		bttnStart.setOpaque(true);
		bttnStart.setForeground(new Color(255, 255, 255));
		bttnStart.setBackground(new Color(150, 30, 30));
		bttnStart.setBorder(new javax.swing.border.LineBorder(new Color(255, 50, 50), 2));
		contentPane.add(bttnStart);
		
		JLabel labelSettingsTitle = new JLabel("Game Settings");
		labelSettingsTitle.setForeground(new Color(255, 255, 255));
		labelSettingsTitle.setFont(new Font("Consolas", Font.BOLD, 28));
		labelSettingsTitle.setBounds(0, 25, 586, 29);
		labelSettingsTitle.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelSettingsTitle);
		
		JLabel lblEraSelection = new JLabel("Game Era Selection:");
		lblEraSelection.setForeground(new Color(255, 255, 255));
		lblEraSelection.setFont(new Font("Consolas", Font.BOLD, 16));
		lblEraSelection.setBounds(120, 100, 210, 21);
		contentPane.add(lblEraSelection);
		
		JLabel lblGridSizeSelection = new JLabel("Grid Size Selection:");
		lblGridSizeSelection.setForeground(new Color(255, 255, 255));
		lblGridSizeSelection.setFont(new Font("Consolas", Font.BOLD, 16));
		lblGridSizeSelection.setBounds(120, 140, 196, 21);
		contentPane.add(lblGridSizeSelection);
		
		JLabel lblRoundCount = new JLabel("Round Count Selection:");
		lblRoundCount.setForeground(new Color(255, 255, 255));
		lblRoundCount.setFont(new Font("Consolas", Font.BOLD, 16));
		lblRoundCount.setBounds(120, 180, 221, 21);
		contentPane.add(lblRoundCount);
		
		JButton btnLoadSavedGame = new JButton("LOAD GAME");
		btnLoadSavedGame.addActionListener(new ActionListener() {
			/**
			 * @param e : the event to be processed
			 * this method invoked when an action occurs(when player clicking)
			 */
			public void actionPerformed(ActionEvent e) {
				String username = LoginPageFrame.loggedInUser;
				File saveFile = new File(username + "_save.txt");

				if (!saveFile.exists()) {
				    JOptionPane.showMessageDialog(null, 
				        "No save file found for " + username, 
				        "Error", 
				        JOptionPane.WARNING_MESSAGE);
				    return;
				}

				try {
				    GameFrame game = new GameFrame(Era.PRESENT, 10, 10);
				    // create a new GameFrame object
				    // parameters are not important, loadGame method will change them
				    game.getGameEngine().loadGame(username);
				    game.updateInfos();
				    game.setVisible(true);
				    dispose(); 
				    
				}
				catch (Exception ex) {
				    ex.printStackTrace();
				}
			}
		});
		btnLoadSavedGame.setForeground(new Color(255, 255, 255));
		btnLoadSavedGame.setBackground(new Color(0, 120, 150));
		btnLoadSavedGame.setFont(new Font("Consolas", Font.BOLD, 16));
		
		btnLoadSavedGame.setBorder(new javax.swing.border.LineBorder(new Color(0, 255, 255), 2));
        btnLoadSavedGame.setBounds(130, 265, 100, 45);
        btnLoadSavedGame.setContentAreaFilled(false);
		btnLoadSavedGame.setOpaque(true);
		contentPane.add(btnLoadSavedGame);
	}
}
