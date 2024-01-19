package main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import dbMangager.DatabaseManager;
import gui.LoginPage;

public class Main {
	private static ArrayList<String> userID = new ArrayList<>();
	public ArrayList<String> getUserID(){
		return userID;
	}
	private static void addUserID(String ID) {
		userID.add(ID);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("hello world");
		Connection con;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String sSQL ="select user_id\r\n"
				+ "from dbo.user_yelp";
		String temp ="";
		DatabaseManager dbm = DatabaseManager.getInstance();
		con = dbm.getConnection();
		
		try {
			pstmt = con.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			System.out.println("The user_ID contains:");
			while(rs.next()) {
				temp = rs.getString("user_id");
				addUserID(temp);
				System.out.println(temp);
			}
			rs.close();
			System.out.println("sucessfully connected to CSIL SQL Server! \n\n");
		} catch (SQLException se) {
			// TODO: handle exception
			System.out.println("\nSQL Exception occurred, the state :" + se.getSQLState()+"\nMessage:\n" + se.getMessage()+"\n");
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginPage();
            }
        });
	}

}
