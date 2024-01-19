package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JTextField;

import dbMangager.DatabaseManager;
import resultSet.BusinessRS;

public class BusinessPage extends JFrame {
	private static final long serialVersionUID = 1L;

	public BusinessPage(){
		setTitle("Business Application");
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
        String[] orderByOptions = {"None","By Name", "By City", "By Number of Stars"};
        JComboBox<String> orderByComboBox = new JComboBox<>(orderByOptions);
        orderByComboBox.setPreferredSize(new Dimension(10 * orderByComboBox.getFontMetrics(orderByComboBox.getFont()).charWidth('W'), orderByComboBox.getPreferredSize().height));

        // Labels and TextFields
        String[] labels = {"Name:", "City:", "Min Stars:", "Ordered By:"};
        JTextField[] textFields = new JTextField[labels.length-1];

        for (int i = 0; i < labels.length; i++) {
        	JLabel label = new JLabel(labels[i]);
            gbc.gridx = 0;
            gbc.gridy = i;
            contentPanel.add(label, gbc);

            if (i == 3) {
                // Create the JComboBox for "Ordered By" with preferred width of 10 columns
                
                gbc.gridx = 1;
                contentPanel.add(orderByComboBox, gbc);
            } else {
                JTextField textField = new JTextField(10); // Set preferred width to 10 columns
                gbc.gridx = 1;
                contentPanel.add(textField, gbc);
                textFields[i] = textField;
            }
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
                String selectedOption = (String) orderByComboBox.getSelectedItem();
                String orderBy = getOrderBy(selectedOption);

                String name = textFields[0].getText();
                System.out.println(name);
                String city = textFields[1].getText();
                String minStars = textFields[2].getText();
                double starsValue = 0.0;
                if (!minStars.isEmpty()) {
                    try {
                        starsValue = Double.parseDouble(minStars);
                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(null, "Invalid input for stars. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        textFields[2].requestFocus();
                        return; // Exit the method or handle it as needed
                    }
                }
                ArrayList<BusinessRS> result = SQLExe(name, city, starsValue, orderBy);
                if(result.isEmpty()) {
                	JOptionPane.showMessageDialog(BusinessPage.this, "No result found!","Input Error ", JOptionPane.ERROR_MESSAGE);
                	return;
                }
                
                BusinessOperator businessOperator = new BusinessOperator(result);
                businessOperator.setVisible(true);
			}
		});


        // Add the header panel and content panel to the main content pane
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
	}
	
	private ArrayList<BusinessRS> SQLExe(String name, String city, double stars, String order) {
		StringBuilder sSQL = new StringBuilder("SELECT business_id, name, address, city, stars FROM dbo.business WHERE ");
		boolean firstConditionAdded = false;
		ArrayList<Object> parameters = new ArrayList<>();
		
		if(name.isEmpty() && city.isEmpty() && stars == 0.0 && order.isEmpty()) {
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
		
		if (city != null && !city.isEmpty()) {
		    if (firstConditionAdded) {
		        sSQL.append(" AND ");
		    }
		    sSQL.append("city LIKE ?");
		    parameters.add("%" + city + "%");
		    firstConditionAdded = true;
		}
		
		if (stars!= 0.0) { 
		    if (firstConditionAdded) {
		        sSQL.append(" AND ");
		    }
		    sSQL.append("stars >= ?");
		    parameters.add(stars);
		}
		
		if(order != null && !order.isEmpty()) {
			sSQL.append(" ORDER BY ").append(order);
		}
		
		ArrayList<BusinessRS> result = new ArrayList<BusinessRS>();
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
			    }
			}
			
			rs = pstmt.executeQuery();
			result = convertResultSetToList(rs);
			rs.close();
			System.out.println("done getting all business! \n");
		} catch (SQLException se) {
			// TODO: handle exception
			System.out.println("\nSQL Exception occurred, the state :" + se.getSQLState()+"\nMessage:\n" + se.getMessage()+"\n");
		}
		return result;
	}
	
    private String getOrderBy(String selectedOption) {
        switch (selectedOption) {
            case "By Name":
                return "name";
            case "By City":
                return "city";
            case "By Number of Stars":
                return "stars";
            case "None":
            	return "";
            default:
                return "";
        }
    }
    
    private ArrayList<BusinessRS> convertResultSetToList(ResultSet rs) throws SQLException {
        ArrayList<BusinessRS> dataList = new ArrayList<>();

        while (rs.next()) {
            String id = rs.getString("business_id"); // Assuming the column name is "id"
            String name = rs.getString("name");
            String address = rs.getString("address");
            String city = rs.getString("city");
            double stars = rs.getDouble("stars");

            BusinessRS business = new BusinessRS(id, name, address, city, stars);
            dataList.add(business);
        }

        return dataList;
    }

}

