package com.csumb.cst363;

import java.sql.*;
import java.util.Scanner;

public class PharmacyReport {
	static final String DBURL = "jdbc:mysql://localhost:3306/HospitalDB";  // database URL
	static final String USERID = "root";
	static final String PASSWORD = "battlemage";

    public static void main(String[] args) {
    	
    	// Create a Scanner object to read input from the console
        Scanner scanner = new Scanner(System.in);

        System.out.println("Pharmacy ID: XXXXXXXX");
        int pharmacyId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("Start Date: yyyy-mm-dd");
        String startDate = scanner.nextLine();
        
        System.out.println("End Date: yyyy-mm-dd");
        String endDate = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DBURL, USERID, PASSWORD)) {
            String query = "SELECT d.trade_name, SUM(p.Quantity) AS total_quantity " +
                           "FROM Prescription p " +
                           "JOIN drug d ON p.DrugName = d.trade_name " +
                           "WHERE p.PharmacyId = ? AND p.DateFilled BETWEEN ? AND ? " +
                           "GROUP BY d.trade_name";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, pharmacyId);
            statement.setString(2, startDate);
            statement.setString(3, endDate);

            ResultSet resultSet = statement.executeQuery();

            System.out.println("Pharmacy Report:");
            System.out.println("Drug Name\t\tQuantity");

            while (resultSet.next()) {
                String drugName = resultSet.getString("trade_name");
                int quantity = resultSet.getInt("total_quantity");

                System.out.println(drugName + "\t\t" + quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
