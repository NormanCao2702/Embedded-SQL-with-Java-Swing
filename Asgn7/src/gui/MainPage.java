package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainPage extends JFrame {
	private static final long serialVersionUID = 1L;
	public MainPage() {
		setTitle("Main Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null); // Center the JFrame on the screen

        // Set the layout of the JFrame to BorderLayout
        setLayout(new BorderLayout());

        // Create a JPanel to hold the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 rows, 2 columns, 10px horizontal and vertical gap

        // Create the buttons
        JButton businessSearch = new JButton("Business Search");
        JButton userSearch = new JButton("User Search");
        
        businessSearch.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		new BusinessPage();
        		dispose();
        	}
        });
        
        userSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new UserPage();
				dispose();
			}
        	
        });
        

        // Add the buttons to the panel
        buttonPanel.add(businessSearch);
        buttonPanel.add(userSearch);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));

        // Create a JLabel for the title
        JLabel titleLabel = new JLabel("Main Page", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Customize font and size
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0)); // add a top and bottom margin

        // Add the title label to the NORTH region of the BorderLayout
        add(titleLabel, BorderLayout.NORTH);

        // Add the panel to the CENTER region of the BorderLayout
        add(buttonPanel, BorderLayout.CENTER);

        // Display the JFrame
        setVisible(true);
	}
	

}
