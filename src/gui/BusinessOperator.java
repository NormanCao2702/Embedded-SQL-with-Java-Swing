package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTable;
import javax.swing.JTextField;

import abstractClass.BusinessTableModel;
import dbMangager.DatabaseManager;
import generateRandom.StringUtil;
import resultSet.BusinessRS;
import user.UserManager;

public class BusinessOperator extends JFrame {
	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTable table;
    private String business_id = new String();


	public BusinessOperator(ArrayList<BusinessRS> businessData) {
		super("Business Operator");
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // First card: The JTable with results
        BusinessTableModel tableModel = new BusinessTableModel(businessData);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        cardPanel.add(tablePanel, "ResultsTable");

        setUpOperationCard();
        // Add mouse listener for double-click on table row
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Switch to operation card when a row is double-clicked
                    int row = table.getSelectedRow();
                    if (row != -1) {
                    	business_id = table.getModel().getValueAt(row, 0).toString();
                        cardLayout.show(cardPanel, "Operation");
                    }
                }
            }
        });

        this.add(cardPanel);
        this.setSize(600, 400); // Set desired size
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
	
	private void setUpOperationCard() {
		// Second card: The operation panel for further operations
		JPanel panel = new JPanel(new BorderLayout());

	    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Ensure the button is aligned left

	    // Create the back button and set its icon
	    ImageIcon backButtonIcon = new ImageIcon(this.getClass().getResource("/resources/img2.png"));
	    JButton backButton = new JButton(backButtonIcon);

	    backButton.addActionListener(e -> cardLayout.show(cardPanel, "ResultsTable"));
	    headerPanel.add(backButton); // No need for BorderLayout.WEST

	    headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
	    panel.add(headerPanel, BorderLayout.NORTH); // Ensure headerPanel is at the top

	    // Content Panel with a specified layout, for example GridLayout
	    JPanel contentPanel = new JPanel(new GridBagLayout()); 
	    
	    String[] labels = {"Stars:", "Useful:", "Funny:", "Cool:"};
        JTextField[] textFields = new JTextField[labels.length];
        
        // Add labels, text fields, and the search button to the content panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        for (int i = 0; i < labels.length; i++) {
        	JLabel label = new JLabel(labels[i]);
            gbc.gridx = 0;
            gbc.gridy = i;
            contentPanel.add(label, gbc);
            JTextField textField = new JTextField(10); // Set preferred width to 10 columns
            gbc.gridx = 1;
            contentPanel.add(textField, gbc);
            textFields[i] = textField;
        }
        
        // Add the search button at the bottom
        JButton submitButton = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(submitButton, gbc);
        
        submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String sSQL = "INSERT INTO dbo.review VALUES (?,?,?,?,?,?,?,?)";
				String new_reviewId = StringUtil.generateRandomAlphanumeric(22);
				UserManager u = UserManager.getInstance();
				String uid = u.getLoginUser();
				LocalDateTime u_time = u.getLoginTime();
				int stars = 0;
				int useful = 0;
				int funny = 0;
				int cool = 0;
				try {
					stars = Integer.parseInt(textFields[0].getText());
					if(stars<1 || stars >5) {
						JOptionPane.showMessageDialog(null, "Please enter valid integers for stars", "Input Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					useful = !textFields[1].getText().trim().isEmpty() ? Integer.parseInt(textFields[1].getText().trim()) : 0;
				    funny = !textFields[2].getText().trim().isEmpty() ? Integer.parseInt(textFields[2].getText().trim()) : 0;
				    cool = !textFields[3].getText().trim().isEmpty() ? Integer.parseInt(textFields[3].getText().trim()) : 0;
				} catch (Exception e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Please enter valid integers for stars, useful, funny, or cool.", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Connection con;
				PreparedStatement pstmt = null;
				con = DatabaseManager.getInstance().getConnection();
				try {		
					pstmt = con.prepareStatement(sSQL.toString());
					pstmt.setString(1, new_reviewId);
					pstmt.setString(2, uid);
					pstmt.setString(3, business_id);
					pstmt.setInt(4, stars);
					pstmt.setInt(5, useful);
					pstmt.setInt(6, funny);
					pstmt.setInt(7, cool);
					pstmt.setObject(8, u_time);
					int affectedRows = pstmt.executeUpdate();
					if (affectedRows > 0) {
						JOptionPane.showMessageDialog(null, "Insert successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Insert failed. No rows affected.", "Insert Failed", JOptionPane.ERROR_MESSAGE);
					}
					System.out.println("done updating review! \n");
				} catch (SQLException se) {
					// TODO: handle exception
					se.printStackTrace();
					System.out.println("\nSQL Exception occurred, the state :" + se.getSQLState()+"\nMessage:\n" + se.getMessage()+"\n");
					JOptionPane.showMessageDialog(null, "SQL Exception occurred, the state :" + se.getSQLState(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

	    panel.add(contentPanel, BorderLayout.CENTER);

	    cardPanel.add(panel, "Operation"); 
	}

	
}
