package gui;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import logic.Era;
import logic.GameEngine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private GameEngine gameEngine;
	private JLabel labelRoundInfo;
    private JLabel labelApexStats;
    private JLabel labelPlayerStats;
    private JLabel labelPreyStats;
    private GamePanel panel;
    String currentPlayerName = LoginPageFrame.loggedInUser;
    private JPanel savePanel;
    private JButton btnSave;

	

    /**
     * the constructor of the  GameFrame class that is called when we create the GameFrame object.
     * @param era: the era of the game
     * @param gridSize: an edge length of the square board which the game is played on
     * @param rounds: total number of rounds
     */
	public GameFrame(Era era, int gridSize, int rounds) {
		System.out.println("GameFrame is being initialized...");
		
		this.gameEngine = new GameEngine(gridSize,rounds,era);
		this.panel = new GamePanel(this.gameEngine);
	    this.gameEngine.setGamePanel(panel);
	    
		
		
	    
	    //WindowBuilder
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		
		panel.setBackground(new Color(255, 255, 255));
		contentPane.add(this.panel, BorderLayout.CENTER);
		
		JPanel informationPanel = new JPanel();
		informationPanel.setFont(new Font("Consolas", Font.BOLD, 14));
		informationPanel.setForeground(new Color(255, 255, 255));
		informationPanel.setPreferredSize(new Dimension(360, 10));
		informationPanel.setBackground(new Color(30, 30, 35));
		contentPane.add(informationPanel, BorderLayout.EAST);
		informationPanel.setLayout(new GridLayout(5, 1, 0, 25));
		
	    labelRoundInfo = new JLabel("Round: 0/10 | Era: Present");
	    labelRoundInfo.setFont(new Font("Consolas", Font.BOLD, 14));
	    labelRoundInfo.setForeground(new Color(255, 255, 255));
		labelRoundInfo.setBackground(new Color(255, 255, 255));
		informationPanel.add(labelRoundInfo);
		labelRoundInfo.setHorizontalAlignment(SwingConstants.CENTER); 
        labelRoundInfo.setBorder(new LineBorder(Color.BLACK, 1));    
		
	    labelApexStats = new JLabel("Apex | Score: 0");
	    labelApexStats.setForeground(new Color(255, 0, 0));
	    labelApexStats.setFont(new Font("Consolas", Font.BOLD, 14));
		labelApexStats.setBackground(new Color(193, 38, 57));
		informationPanel.add(labelApexStats);
		labelApexStats.setHorizontalAlignment(SwingConstants.CENTER);
        labelApexStats.setBorder(new LineBorder(Color.BLACK, 1));
		
	    labelPlayerStats = new JLabel("Player | Score: 0");
	    labelPlayerStats.setForeground(new Color(255, 128, 0));
	    labelPlayerStats.setFont(new Font("Consolas", Font.BOLD, 14));
		labelPlayerStats.setBackground(new Color(240, 168, 79));
		informationPanel.add(labelPlayerStats);
		labelPlayerStats.setHorizontalAlignment(SwingConstants.CENTER);
        labelPlayerStats.setBorder(new LineBorder(Color.BLACK, 1));
		
	    labelPreyStats = new JLabel("Prey | Score: 0");
	    labelPreyStats.setFont(new Font("Consolas", Font.BOLD, 14));
	    labelPreyStats.setForeground(new Color(0, 255, 255));
		labelPreyStats.setBackground(new Color(128, 255, 255));
		informationPanel.add(labelPreyStats);
		labelPreyStats.setHorizontalAlignment(SwingConstants.CENTER);
        labelPreyStats.setBorder(new LineBorder(Color.BLACK, 1));
        
        savePanel = new JPanel();
        savePanel.setBackground(new Color(30, 30, 35));
        informationPanel.add(savePanel);
        savePanel.setLayout(null);
        
        btnSave = new JButton("SAVE");
        btnSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// adds action to save button, it uses the saveGame method of GameEngine class.
        		gameEngine.saveGame(LoginPageFrame.loggedInUser);
        	}
        });
        btnSave.setBounds(130, 40, 100, 50);
		btnSave.setContentAreaFilled(false);
		btnSave.setOpaque(true);
		btnSave.setForeground(new Color(255, 255, 255));
		btnSave.setBackground(new Color(150, 30, 30));
		btnSave.setBorder(new javax.swing.border.LineBorder(new Color(255, 50, 50), 2));
		btnSave.setFont(new Font("Consolas", Font.BOLD, 18));
        savePanel.add(btnSave);
        this.gameEngine.startGame();
        updateInfos();
        this.setVisible(true);
	}
	

	/**
	 * 
	 * @return gameEngine:
	 */
	public GameEngine getGameEngine() {
		return gameEngine;
	}


	/**
	 * update the scores, round and era infos icons of the organisms that can be seen in the 
	 * information panel at the right of the main game window.
	 */
	public void updateInfos() {
	    String apexName = gameEngine.getCurrentApexName(); 
	    String preyName = gameEngine.getCurrentPreyName(); 
	    String predatorName = gameEngine.getPlayer().getName(); 
	    int playerCD = gameEngine.getPlayer().getCurrentCoolDown();
	    int apexCD = gameEngine.getApexCooldown();
	    int preyCD = gameEngine.getPreyCooldown();

	   
	    labelRoundInfo.setText("Round: " + gameEngine.getCurrentRound() + "/" + gameEngine.getTotalRound() + " | Era: " + gameEngine.getEra());
	    labelPlayerStats.setText(predatorName + " (" + currentPlayerName + ") | Score: " + gameEngine.getPredatorScore()+ " | CD: " + playerCD) ;
	    labelApexStats.setText(apexName + " | Score: " + gameEngine.getApexPScore() + " | CD: " + apexCD);
	    labelPreyStats.setText(preyName + " | Score: " + gameEngine.getPreyScore() + " | CD: " + preyCD);

	  
	    try {
	        labelPlayerStats.setIcon(new ImageIcon(new ImageIcon("resources/" + predatorName.toLowerCase().trim() + ".png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
	        labelApexStats.setIcon(new ImageIcon(new ImageIcon("resources/" + apexName.toLowerCase().trim() + ".png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
	        labelPreyStats.setIcon(new ImageIcon(new ImageIcon("resources/" + preyName.toLowerCase().trim() + ".png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
	        
	        labelPlayerStats.setIconTextGap(10);
	        labelApexStats.setIconTextGap(10);
	        labelPreyStats.setIconTextGap(10);
	    }
	    catch (Exception e) {
	        System.out.println("Icons not found in resources folder.");
	    }
	}

}
