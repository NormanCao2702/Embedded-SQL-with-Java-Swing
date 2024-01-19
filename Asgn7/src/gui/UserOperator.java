package gui;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import user.UserManager;
import resultSet.UserRS;
import abstractClass.UserTableModel;
import dbMangager.DatabaseManager;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserOperator extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTable table;
    private String selected_id = new String();
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    public UserOperator(ArrayList<UserRS> users) {
    	super("User Operator");
    	cardLayout = new CardLayout();
    	cardPanel = new JPanel(cardLayout);
    	// First card: The JTable with results
        UserTableModel tableModel = new UserTableModel(users);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        cardPanel.add(tablePanel, "ResultsTable");

        // Add mouse listener for double-click on table row
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Switch to operation card when a row is double-clicked
                    int row = table.getSelectedRow();
                    if (row != -1) {
                    	selected_id = table.getModel().getValueAt(row, 0).toString();
                    	String sSQL = "INSERT INTO dbo.friendship VALUES (?,?)";
                    	String sSQL2 = "SELECT friend FROM dbo.friendship WHERE user_id = ?";
                    	String uid = new String();
                    	UserManager u = UserManager.getInstance();
                    	uid = u.getLoginUser();
  
    					
    					Connection con;
    					PreparedStatement pstmt = null;
    					PreparedStatement pstmt2 = null;
    					con = DatabaseManager.getInstance().getConnection();
    					try {		
    						pstmt2 = con.prepareStatement(sSQL2.toString());
    						pstmt2.setString(1, uid);
    						ResultSet rs; 
    						ArrayList<String> friend_list = new ArrayList<String>();
    						rs = pstmt2.executeQuery();
    						System.out.println("user id is:"+uid);
    						System.out.println("selected id is:"+selected_id);
    						
    						
    						while(rs.next()) {
    							friend_list.add(rs.getString("friend"));
    						}
    						if(friend_list.contains(selected_id)) {
    							JOptionPane.showMessageDialog(null, "You already made friend with this guy!", "Error", JOptionPane.ERROR_MESSAGE);
    						}
    						else if(selected_id.compareTo(uid) ==0) {
    							JOptionPane.showMessageDialog(null, "You cannot make friend with yourself!", "Error", JOptionPane.ERROR_MESSAGE);
    						}
    						else {
    							pstmt = con.prepareStatement(sSQL.toString());
	    						pstmt.setString(1, uid);
	    						pstmt.setString(2, selected_id);
	    						int affectedRows = pstmt.executeUpdate();
	    						if (affectedRows > 0) {
	    							JOptionPane.showMessageDialog(null, "Make friend successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
	    						} else {
	    							JOptionPane.showMessageDialog(null, "Make friend failed. No rows affected.", "Insert Failed", JOptionPane.ERROR_MESSAGE);
	    						}
    						}
    					} catch (SQLException se) {
    						// TODO: handle exception
    						se.printStackTrace();
    						System.out.println("\nSQL Exception occurred, the state :" + se.getSQLState()+"\nMessage:\n" + se.getMessage()+"\n");
    						JOptionPane.showMessageDialog(null, "SQL Exception occurred, the state :" + se.getSQLState(), "Error", JOptionPane.ERROR_MESSAGE);
    					}
    					System.out.println("done making friends! \n");
                    }
                }
            }
        });

        this.add(cardPanel);
        this.setSize(600, 400); // Set desired size
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

	
}
