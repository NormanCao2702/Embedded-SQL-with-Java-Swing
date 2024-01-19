package gui;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dbMangager.DatabaseManager;
import resultSet.UserRS;

public class UserPage extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public UserPage() {
		setTitle("User Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,300);
        setLocationRelativeTo(null);
        
        // Create a panel to hold the header
        JPanel headerPanel = new JPanel(new BorderLayout());

        // Create the back button and set its icon
        ImageIcon backButtonIcon = new ImageIcon(this.getClass().getResource("/resources/img2.png"));
        JButton backButton = new JButton(backButtonIcon);
        
        backButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		new MainPage();
        	}
        });
        headerPanel.add(backButton, BorderLayout.WEST);

        // Create the content panel for the form (as shown in the previous example)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        // Add labels, text fields, and the search button to the content panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);


        // Labels and TextFields
        String[] labels = {"Name:", "Min Review Count:", "Min Avg Stars:"};
        JTextField[] textFields = new JTextField[labels.length];

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
        JButton searchButton = new JButton("Search");
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(searchButton, gbc);
        
        searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
                String name = textFields[0].getText();
                System.out.println(name);
                String review_count = textFields[1].getText();
                String avg_stars = textFields[2].getText();
                Integer reviewValue = null;
                Double stars = null;
                StringBuilder errorM = new StringBuilder();
                
                if (!review_count.isEmpty()) {
                    try {
                        reviewValue = Integer.parseInt(review_count);
                    } catch (NumberFormatException e1) {
                        textFields[1].requestFocus();
                        errorM.append("Invalid input for review count.\n");
                        return; 
                    }
                }
                
                if(!avg_stars.isEmpty()) {
                	try {
                        stars = Double.parseDouble(avg_stars);
                    } catch (NumberFormatException e1) {
                        textFields[2].requestFocus();
                        errorM.append("Invalid input for stars.\n");
                        return; 
                    }
                }
                
                if(errorM.length() > 0) {
                	JOptionPane.showMessageDialog(null, errorM.toString(),"Input Error", JOptionPane.ERROR_MESSAGE);
                }
                
                ArrayList<UserRS> result = SQLExe(name, reviewValue, stars);
                if(result.isEmpty()) {
                	JOptionPane.showMessageDialog(UserPage.this, "No result found!","Input Error ", JOptionPane.ERROR_MESSAGE);
                	return;
                }
                
                UserOperator userOperators = new UserOperator(result);
                userOperators.setVisible(true);
			}
		});


        // Add the header panel and content panel to the main content pane
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
	}
	
	private ArrayList<UserRS> SQLExe(String name, Integer review_count,Double avg_stars) {
		StringBuilder sSQL = new StringBuilder("SELECT user_id, name, review_count, useful, funny, cool, average_stars,yelping_since FROM dbo.user_yelp WHERE ");
		boolean firstConditionAdded = false;
		ArrayList<Object> parameters = new ArrayList<>();
		
		if(name.isEmpty() && review_count ==null && avg_stars ==null) {
		 int whereIndex = sSQL.lastIndexOf(" WHERE ");
		    if (whereIndex != -1) {
		        sSQL.delete(whereIndex, sSQL.length());
		    }
		}
		
		if (name != null && !name.isEmpty()) {
		    sSQL.append("name LIKE ?");
		    parameters.add("%" + name + "%");
		    firstConditionAdded = true;
		}
		
		if (review_count != null) {
		    if (firstConditionAdded) {
		        sSQL.append(" AND ");
		    }
		    sSQL.append("review_count >= ?");
		    parameters.add(review_count);
		    firstConditionAdded = true;
		}
		
		if (avg_stars != null) { 
		    if (firstConditionAdded) {
		        sSQL.append(" AND ");
		    }
		    sSQL.append("average_stars >= ?");
		    parameters.add(avg_stars);
		}
		
		
		sSQL.append(" ORDER BY ").append("name");
		
		
		ArrayList<UserRS> result = new ArrayList<UserRS>();
		Connection con;
		PreparedStatement pstmt = null;
		ResultSet rs;
		
		
		con = DatabaseManager.getInstance().getConnection();
		try {		
			pstmt = con.prepareStatement(sSQL.toString());

			for (int i = 0; i < parameters.size(); i++) {
			    Object param = parameters.get(i);
			    if (param instanceof String) {
			        pstmt.setString(i + 1, (String) param);
			    } else if (param instanceof Double) { 
			        pstmt.setDouble(i + 1, (Double) param);
			    } else if(param instanceof Integer) {
			    	pstmt.setInt(i+1, (Integer) param);
			    }
			}
			
			rs = pstmt.executeQuery();
			result = convertResultSetToList(rs);
			rs.close();
			System.out.println("done getting all users! \n");
		} catch (SQLException se) {
			// TODO: handle exception
			System.out.println("\nSQL Exception occurred, the state :" + se.getSQLState()+"\nMessage:\n" + se.getMessage()+"\n");
		}
		return result;
	}
    
    private ArrayList<UserRS> convertResultSetToList(ResultSet rs) throws SQLException {
        ArrayList<UserRS> dataList = new ArrayList<>();

        while (rs.next()) {
            String id = rs.getString("user_id");
            String name = rs.getString("name");
            int review_count = rs.getInt("review_count");
            int useful = rs.getInt("useful");
            int funny = rs.getInt("funny");
            int cool = rs.getInt("cool");
            double avg_stars = rs.getDouble("average_stars");
            String date = rs.getString("yelping_since");

            UserRS business = new UserRS(id, name, review_count,useful, funny, cool, avg_stars,date);
            dataList.add(business);
        }

        return dataList;
    }

}
