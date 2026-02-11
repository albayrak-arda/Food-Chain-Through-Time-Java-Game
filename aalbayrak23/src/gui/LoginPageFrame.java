package gui;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.Color;

public class LoginPageFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldUsername;
	private JPasswordField passwordField;
	
	public static String loggedInUser = "Player";

	/**
	 * the constructor method of the LoginPageFrame Class that is used for creating a Login Page of game.
	 */
	public LoginPageFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(30, 30, 35));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelTitle = new JLabel("");
		labelTitle.setFont(new Font("Consolas", Font.BOLD, 28));
		labelTitle.setBounds(83, 0, 425, 229);
		contentPane.add(labelTitle);
		labelTitle.setText("");

		String gameIconPath = "resources/loginicon.png"; //

		try {
		    File file = new File(gameIconPath);
		    if (file.exists()) {
		        ImageIcon icon = new ImageIcon(gameIconPath);
		        
		        int targetWidth = 425; // same width with the JLabel labelTitle
		        Image scaledImg = icon.getImage().getScaledInstance(targetWidth, -1, Image.SCALE_SMOOTH);
		        
		        labelTitle.setIcon(new javax.swing.ImageIcon(scaledImg));
		        labelTitle.setHorizontalAlignment(SwingConstants.CENTER); 
		    }
		} 
		catch (Exception e) {
		    e.printStackTrace();
		}
		//WindowBuilder
		JLabel labelUsername = new JLabel("Username:");
		labelUsername.setForeground(new Color(255, 255, 255));
		labelUsername.setFont(new Font("Consolas", Font.BOLD, 14));
		labelUsername.setBounds(145, 145, 113, 40);
		contentPane.add(labelUsername);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldUsername.setBounds(245, 155, 150, 20);
		contentPane.add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		JLabel labelPassword = new JLabel("Password:");
		labelPassword.setForeground(new Color(255, 255, 255));
		labelPassword.setFont(new Font("Consolas", Font.BOLD, 14));
		labelPassword.setBounds(145, 185, 89, 29);
		contentPane.add(labelPassword);
		
		JButton bttnRegister = new JButton("REGISTER");
		bttnRegister.setContentAreaFilled(false);
		bttnRegister.setOpaque(true);
		bttnRegister.setForeground(new Color(255, 255, 255));
		bttnRegister.setBackground(new Color(0, 120, 150));
		bttnRegister.setBorder(new javax.swing.border.LineBorder(new Color(0, 255, 255), 2));
		bttnRegister.setFont(new Font("Consolas", Font.BOLD, 18));
		bttnRegister.setBounds(120, 270, 100, 45);
		contentPane.add(bttnRegister);
		bttnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// add action to register button
				String username = textFieldUsername.getText();
				String password = new String(passwordField.getPassword());
				boolean found = false;
				
				if (username.isEmpty() || password.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Please fill in all the fields!");
		            return;
		        }
				try {
		            Formatter output = new Formatter(new FileOutputStream("users.txt", true));
		            output.format("%s,%s%n", username, password);
		            output.close();
		            
		            JOptionPane.showMessageDialog(null, "Registered successfully!");
		        }
				catch (IOException ex) {
		            JOptionPane.showMessageDialog(null, "File Writing Error!");
		        }
				if(found) {
					loggedInUser = username;
				}
				
			}
			
		});
		
		
		JButton bttnLogin = new JButton("LOGIN");
		bttnLogin.setContentAreaFilled(false);
		bttnLogin.setOpaque(true);
		bttnLogin.setForeground(new Color(255, 255, 255));
		bttnLogin.setBackground(new Color(150, 30, 30));
		bttnLogin.setBorder(new javax.swing.border.LineBorder(new Color(255, 50, 50), 2));
		bttnLogin.setFont(new Font("Consolas", Font.BOLD, 18));
		bttnLogin.setBounds(360, 270, 100, 45);
		contentPane.add(bttnLogin);
		bttnLogin.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {// add action to login button
		        String enteredUser = textFieldUsername.getText();
		        String enteredPass = new String(passwordField.getPassword());
		        boolean found = false;

		        try {
		            File file = new File("users.txt");
		            if (file.exists()) {
		                Scanner reader = new Scanner(file);
		                while (reader.hasNextLine()) {
		                    String line = reader.nextLine();
		                    String[] parts = line.split(",");
		                    if (parts.length == 2 && parts[0].equals(enteredUser) && parts[1].equals(enteredPass)) {
		                        found = true;
		                        break;
		                    }
		                }
		                reader.close();
		            } 
		            else {
		                JOptionPane.showMessageDialog(null,  "No users have registered yet!");
		                return;
		            }
		        } 
		        catch (IOException ex) {
		            ex.printStackTrace(); 
		        }

		        if (found) {
		        	loggedInUser = enteredUser;
		            dispose();// validation of the personal informations of the user
		            new GameInitializerSettingsSelectionFrame().setVisible(true); 
		        } 
		        else {
		            JOptionPane.showMessageDialog(null,  "incorrect username or password!");
		        }
		    }
		});
			
		
		
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordField.setBounds(245, 188, 150, 20);
		contentPane.add(passwordField);
	}
}
