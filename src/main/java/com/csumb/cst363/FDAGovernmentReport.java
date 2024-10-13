package com.csumb.cst363;

import java.sql.*;
import java.util.Scanner;

public class FDAGovernmentReport {
    static final String DBURL = "jdbc:mysql://localhost:3306/HospitalDB";
    static final String USERID = "root";
    static final String PASSWORD = "battlemage";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Drug Name (partial name):");
        String drugName = scanner.nextLine();

        System.out.println("Start Date (yyyy-mm-dd):");
        String startDate = scanner.nextLine();

        System.out.println("End Date (yyyy-mm-dd):");
        String endDate = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DBURL, USERID, PASSWORD)) {
            String query = "SELECT CONCAT(d.first_name, ' ', d.last_name) AS doctor_name, " +
                    "       SUM(p.Quantity) AS total_quantity " +
                    "FROM Prescription p " +
                    "JOIN Doctor d ON p.Doctor_SSN = d.ssn " +
                    "JOIN Drug dr ON p.DrugName = dr.trade_name " +
                    "WHERE dr.trade_name LIKE ? " +
                    "  AND p.DateFilled BETWEEN ? AND ? " +
                    "GROUP BY d.first_name, d.last_name";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, "%" + drugName + "%");
            statement.setString(2, startDate);
            statement.setString(3, endDate);

            ResultSet resultSet = statement.executeQuery();

            System.out.println("FDA Government Report:");
            System.out.println("Doctor Name\t\tQuantity");

            while (resultSet.next()) {
                String doctorName = resultSet.getString("doctor_name");
                int quantity = resultSet.getInt("total_quantity");

                System.out.println(doctorName + "\t\t" + quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

