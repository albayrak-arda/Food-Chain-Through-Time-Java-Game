package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logic.Era;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;

public class ResultFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * constructor of the ResultFrame that is used for creating result(final) screen of the game.
	 * @param predatorScore: predator's score that will be reflected in the result screen
	 * @param preyScore: prey's score that will be reflected in the result screen
	 * @param apexScore: apex's score that will be reflected in the result screen
	 * @param era: era information of the finished game that will be showed in the result screen
	 * @param round: total number of round information of the finished game that 
	 * @param predName: predator's name that ı use it to access images under the resources folder
	 * @param preyName: prey's name that ı use it to access images under the resources folder
	 * @param apexName: apexpredator's name that ı use it to access images under the resources folder
	 * 
	 */
	public ResultFrame(int predatorScore, int preyScore, int apexScore, Era era, int round,
			String predName, String preyName, String apexName) {
		//WindowBuilder
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 450);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(30, 30, 35));
		contentPane.setForeground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblWinner = new JLabel("WINNER: ???");
		lblWinner.setForeground(new Color(255, 255, 255));
		lblWinner.setFont(new Font("Consolas", Font.BOLD, 36));
		lblWinner.setBounds(0, 52, 586, 33);
		lblWinner.setHorizontalAlignment(SwingConstants.CENTER); // not wb
		contentPane.add(lblWinner);
		
		JLabel lblApexScore = new JLabel("ApexPredator's Score:" + apexScore);
		lblApexScore.setForeground(new Color(255, 0, 0));
		lblApexScore.setFont(new Font("Consolas", Font.BOLD, 18));
		lblApexScore.setBounds(90, 170, 566, 21);
		contentPane.add(lblApexScore);
		
		JLabel lblPredatorScore = new JLabel("Predator's Score:" + predatorScore);
		lblPredatorScore.setForeground(new Color(255, 128, 0));
		lblPredatorScore.setFont(new Font("Consolas", Font.BOLD, 18));
		lblPredatorScore.setBounds(90, 210, 224, 24);
		contentPane.add(lblPredatorScore);
		
		JLabel lblPreyScore = new JLabel("Prey's Score:" + preyScore);
		lblPreyScore.setForeground(new Color(0, 255, 255));
		lblPreyScore.setFont(new Font("Consolas", Font.BOLD, 18));
		lblPreyScore.setBounds(90, 250, 224, 33);
		contentPane.add(lblPreyScore);
		
		JLabel lblRoundInfo = new JLabel("Round:" + round);
		lblRoundInfo.setForeground(new Color(0, 0, 255));
		lblRoundInfo.setFont(new Font("Consolas", Font.BOLD, 18));
		lblRoundInfo.setBounds(360, 100, 166, 29);
		contentPane.add(lblRoundInfo);
		
		JLabel lblEraInfo = new JLabel("Era:" + era.toString());
		lblEraInfo.setForeground(new Color(0, 255, 0));
		lblEraInfo.setFont(new Font("Consolas", Font.BOLD, 18));
		lblEraInfo.setBounds(90, 100, 176, 24);
		contentPane.add(lblEraInfo);
		
		JButton btnRestart = new JButton("RESTART");
		btnRestart.setContentAreaFilled(false);
		btnRestart.setOpaque(true);
		btnRestart.setBackground(new Color(0, 120, 150));
		btnRestart.setForeground(new Color(255, 255, 255));
		btnRestart.setFont(new Font("Consolas", Font.BOLD, 14));
		btnRestart.setBounds(140, 340, 100, 45);
		btnRestart.setBorder(new javax.swing.border.LineBorder(new Color(0, 255, 255), 2));
		contentPane.add(btnRestart);
		btnRestart.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        dispose(); // close the current ResultFrame object
		        new GameInitializerSettingsSelectionFrame().setVisible(true); 
		        // open the Game Settings Selection screen again
		    }
		});
		
		JButton btnExit = new JButton("EXIT");
		btnExit.setContentAreaFilled(false);
		btnExit.setOpaque(true);
		btnExit.setForeground(new Color(255, 255, 255));
		btnExit.setBackground(new Color(150, 30, 30));
		btnExit.setFont(new Font("Consolas", Font.BOLD, 14));
		btnExit.setBounds(330, 340, 100, 45);
		btnExit.setBorder(new javax.swing.border.LineBorder(new Color(255, 50, 50), 2));
		contentPane.add(btnExit);
		btnExit.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        System.exit(0); // exit button terminated the game 
		    }
		});
		
		JLabel lblTitle = new JLabel("GAME RESULTS");
		lblTitle.setForeground(new Color(255, 255, 255));
		lblTitle.setFont(new Font("Consolas", Font.BOLD, 24));
		lblTitle.setBounds(0, 10, 586, 39);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTitle);
		
		lblPredatorScore.setText("Predator Score: " + predatorScore);
	    lblPreyScore.setText("Prey Score: " + preyScore);
	    lblApexScore.setText("Apex Score: " + apexScore);
		
		String winner;
		if (predatorScore > preyScore && predatorScore > apexScore) {
	        winner = "PREDATOR";
	    }
		else if (preyScore > predatorScore && preyScore > apexScore) {
	        winner = "PREY";
	    }
		else if (apexScore > predatorScore && apexScore > preyScore) {
	        winner = "APEX PREDATOR";
	    }
		else {
	        winner = "TIE";
	    }

	    
	    lblWinner.setText("WINNER: " + winner);
	    
	    JLabel lblWinnerImage = new JLabel("");
	    lblWinnerImage.setBounds(324, 131, 200, 180);
	    contentPane.add(lblWinnerImage);
	     
	    String imgName ="";

	    if (winner.equals("PREDATOR")) {
	        imgName = predName;
	    }
	    else if (winner.equals("PREY")) {
	        imgName = preyName;
	    }
	    else if (winner.equals("APEX PREDATOR")) {
	        imgName = apexName;
	    }
	    else if (winner.equals("TIE")) {
	        imgName = "tie_icon"; 
	    }

	    String imgPath = "";
	    if (!imgName.isEmpty()) {
	    	imgPath = "resources/" + imgName.toLowerCase().trim() + ".png";
	    	
	    }
	    if (!imgPath.isEmpty()) {
	        try {
	            File file = new File(imgPath);
	            if (file.exists()) {
	                ImageIcon icon = new ImageIcon(imgPath);
	                Image scaledImg = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
	                lblWinnerImage.setIcon(new ImageIcon(scaledImg));
	            }
	            else {
	                System.err.println("Image could not found: " + imgPath);
	                lblWinnerImage.setIcon(null); //if file could not found
	            }
	        } 
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
}
