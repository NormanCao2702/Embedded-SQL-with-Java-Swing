package gui;

import java.awt.BorderLayout;
import main.Main;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import user.UserManager;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPage extends JFrame {
	private static final long serialVersionUID = 1L;
	public LoginPage() {
		setTitle("GUI SQL SERVER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,300);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Login Page");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 50, 0)); // add a top and bottom margin
        Font titleFont = new Font("Arial", Font.BOLD, 20);
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel userIdLabel = new JLabel("Enter your UserID:");
        JTextField userIdField = new JTextField(10);

        inputPanel.add(userIdLabel);
        inputPanel.add(userIdField);

        
//        panel.add(inputPanel, BorderLayout.CENTER);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String userId = userIdField.getText().trim();
            // Check if the entered userId is valid
            if (isValidUserId(userId)) {
            	UserManager u = UserManager.getInstance();
            	u.saveLoginUser(userId);
            	System.out.println(u.getLoginUser());
                JOptionPane.showMessageDialog(this, "Login successful!");
                System.out.println(u.getLoginTime());
                new MainPage();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid userID. Please try again.");
            }
        });
        
        inputPanel.add(loginButton); // Add the button to the inputPanel
        panel.add(inputPanel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
	}
	
	

	private boolean isValidUserId(String userId) {
		Main maininstance = new Main();
		ArrayList<String> userIDs = maininstance.getUserID();
        return userIDs.contains(userId);
	}


}
