package com.csumb.cst363;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ArrayList;


public class DataGenerate {

	static final String DBURL = "jdbc:mysql://localhost:3306/HospitalDB";
	static final String USERID = "root";
	static final String PASSWORD = "battlemage";

	static ArrayList<String> patientSSNs = new ArrayList<>();
	static ArrayList<String> doctorSSNs = new ArrayList<>();
	static ArrayList<Integer> doctorIDs = new ArrayList<>();
	static final String[] specialties = {"Internal Medicine", "Family Medicine", "Pediatrics", "Orthopedics", "Dermatology", "Cardiology", "Gynecology", "Gastroenterology", "Psychiatry", "Oncology"};
	static final String[] firstNames = {
			// Boy Names
			"John", "David", "Michael", "William", "James", "Andrew", "Joseph", "Charles", "Daniel", "Christopher",
			"Matthew", "Jacob", "Nicholas", "Ethan", "Joshua", "Alexander", "Benjamin", "Ryan", "Samuel", "Jonathan",
			"Nathan", "Tyler", "Brandon", "Christian", "Dylan", "Zachary", "Noah", "Logan", "Caleb", "Justin",
			"Aaron", "Kevin", "Austin", "Elijah", "Gabriel", "Benjamin", "Adam", "Henry", "Jason", "Luke",

			// Girl Names
			"Emma", "Olivia", "Ava", "Isabella", "Sophia", "Mia", "Charlotte", "Amelia", "Harper", "Evelyn",
			"Abigail", "Emily", "Elizabeth", "Mila", "Ella", "Scarlett", "Grace", "Victoria", "Chloe", "Lily",
			"Avery", "Sofia", "Aria", "Penelope", "Layla", "Riley", "Zoe", "Nora", "Lillian", "Hannah",
			"Addison", "Aubrey", "Ellie", "Stella", "Natalie", "Zoey", "Leah", "Hazel", "Violet", "Aurora"
	};
	static final String[] lastNames = {
			"Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Garcia", "Rodriguez", "Wilson",
			"Anderson", "Taylor", "Thomas", "Martinez", "Hernandez", "Moore", "Martin", "Jackson", "Thompson", "White",
			"Lopez", "Lee", "Gonzalez", "Harris", "Clark", "Lewis", "Young", "Hall", "Walker", "Allen",
			"King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Adams", "Nelson", "Baker", "Carter",
			"Green", "Hall", "Mitchell", "Turner", "Roberts", "Phillips", "Campbell", "Parker", "Evans", "Edwards"
	};

	static final String[] streets = {
			"1 Main Street", "2 Park Avenue", "3 Oak Street", "4 Cedar Lane", "5 Elm Street", "6 Maple Avenue", "7 Pine Street",
			"8 Washington Boulevard", "9 Lakeview Drive", "10 Hillside Road", "11 River Street", "12 Sunset Boulevard",
			"13 Highland Avenue", "14 Broadway", "15 Valley Road", "16 Church Street", "17 College Avenue", "18 Spring Street",
			"19 Smith Street", "20 Forest Drive", "21 Meadow Lane", "22 Grove Street", "23 Franklin Avenue", "24 Chestnut Street",
			"25 First Street", "26 Second Avenue", "27 Third Street", "28 Fourth Avenue", "29 Fifth Street", "30 Sixth Avenue",
			"31 Seventh Street", "32 Eighth Avenue", "33 Ninth Street", "34 Tenth Avenue", "35 Park Street", "36 Market Street",
			"37 Court Square", "38 Liberty Street", "39 Willow Lane", "40 Beech Street"
	};

	static final String[] cities = {
			"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose",
			"Austin", "Jacksonville", "San Francisco", "Indianapolis", "Columbus", "Fort Worth", "Charlotte", "Seattle", "Denver", "El Paso",
			"Detroit", "Washington", "Boston", "Memphis", "Nashville", "Portland", "Oklahoma City", "Las Vegas", "Baltimore", "Louisville",
			"Milwaukee", "Albuquerque", "Tucson", "Fresno", "Sacramento", "Kansas City", "Long Beach", "Mesa", "Atlanta", "Colorado Springs",
			"Virginia Beach", "Raleigh", "Omaha", "Miami", "Oakland", "Minneapolis", "Tulsa", "Wichita", "New Orleans", "Arlington"
	};

	static final String[] states = {
			"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
			"Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
			"Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey",
			"New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
			"South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"
	};

	public static void main(String[] args) {

		Random gen = new Random();
		try (Connection conn = DriverManager.getConnection(DBURL, USERID, PASSWORD)) {

			PreparedStatement ps;
			ResultSet rs;
			int id;
			int row_count;

			// Delete all doctor rows
			ps = conn.prepareStatement("DELETE FROM Doctor");
			row_count = ps.executeUpdate();
			System.out.println("Rows deleted from doctor: " + row_count);

			// Delete all patient rows
			ps = conn.prepareStatement("DELETE FROM Patient");
			row_count = ps.executeUpdate();
			System.out.println("Rows deleted from patient: " + row_count);

			// Delete all prescription rows
			ps = conn.prepareStatement("DELETE FROM Prescription");
			row_count = ps.executeUpdate();
			System.out.println("Rows deleted from prescription: " + row_count);

			// Prepare statements for insertion
			String sqlInsertDoctor = "INSERT INTO Doctor(last_name, first_name, practice_since, specialty, ssn) VALUES (?, ?, ?, ?, ?)";
			String sqlInsertPatient = "INSERT INTO Patient(last_name, first_name, birthdate, ssn, street, city, state, zipcode, primaryName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			String sqlInsertPrescription = "INSERT INTO Prescription(drugName, quantity, Patient_SSN, PatientFirstName, PatientLastName, Doctor_SSN, DoctorFirstName, DoctorLastName) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			String[] keycols = {"ID"};
			String ssn;

			ps = conn.prepareStatement(sqlInsertDoctor, keycols);
			for (int k = 1; k <= 10; k++) {
				String lastName = generateRandomLastName(gen);
				String firstName = generateRandomFirstName(gen);
				ssn = generateUniqueSSN(gen, doctorSSNs);
				ps.setString(5, ssn);
				doctorSSNs.add(ssn);
				String practice_since = Integer.toString(2000 + gen.nextInt(20));
				ps.setString(1, lastName);
				ps.setString(2, firstName);
				ps.setString(3, practice_since);
				ps.setString(4, specialties[k % specialties.length]);
				row_count = ps.executeUpdate();
				System.out.println("Row inserted for doctor: " + row_count);
				if (row_count > 0) {
					rs = ps.getGeneratedKeys();
					if (rs.next()) {
						doctorIDs.add(rs.getInt(1));
					}
				}
			}

			// Insert patients
			ps = conn.prepareStatement(sqlInsertPatient, keycols);
			for (int k = 1; k <= 100; k++) {
				ssn = generateUniqueSSN(gen, patientSSNs);
				ps.setString(4, ssn);
				patientSSNs.add(ssn);
				String birthdate = (1950 + gen.nextInt(73)) + "-" + (1 + gen.nextInt(12)) + "-" + (1 + gen.nextInt(28));
				ps.setString(1, generateRandomLastName(gen));
				ps.setString(2, generateRandomFirstName(gen));
				ps.setString(3, birthdate);
				ps.setString(5, streets[gen.nextInt(streets.length)]);
				ps.setString(6, cities[gen.nextInt(cities.length)]);
				ps.setString(7, states[gen.nextInt(states.length)]);
				ps.setString(8, generateRandomZipCode(gen));
				String doctorLastName = getDoctorLastName(doctorIDs.get(gen.nextInt(doctorIDs.size())), conn);
				ps.setString(9, doctorLastName);
				row_count = ps.executeUpdate();
				System.out.println("Row inserted for patient: " + row_count);
			}

			// Insert prescriptions
			ps = conn.prepareStatement(sqlInsertPrescription, keycols);
			for (int k = 1; k <= 100; k++) {
				String patient_ssn = getRandomPatientSSN(gen, patientSSNs);
				int doctor_id = doctorIDs.get(gen.nextInt(doctorIDs.size()));

				String sqlSelectDrug = "SELECT trade_name FROM drug ORDER BY RAND() LIMIT 1";
				PreparedStatement selectDrugStatement = conn.prepareStatement(sqlSelectDrug);
				ResultSet drugResultSet = selectDrugStatement.executeQuery();

				if (drugResultSet.next()) {
					String drugName = drugResultSet.getString("trade_name");
					int quantity = 1 + gen.nextInt(100);
					String patientFirstName = getPatientFirstName(patient_ssn, conn);
					String patientLastName = getPatientLastName(patient_ssn, conn);

					String doctor_ssn = getDoctorSSN(doctor_id, conn);
					String doctorFirstName = getDoctorFirstName(doctor_id, conn);
					String doctorLastName = getDoctorLastName(doctor_id, conn);

					ps.setString(1, drugName);
					ps.setInt(2, quantity);
					ps.setString(3, patient_ssn);
					ps.setString(4, patientFirstName);
					ps.setString(5, patientLastName);
					ps.setString(6, doctor_ssn);
					ps.setString(7, doctorFirstName);
					ps.setString(8, doctorLastName);
					row_count = ps.executeUpdate();
					System.out.println("Row inserted for prescription: " + row_count);
				}
			}

			// Display all rows for doctors
			System.out.println("All doctors");
			String sqlSELECT = "SELECT ID, last_name, first_name, practice_since, specialty, ssn FROM Doctor";

			ps = conn.prepareStatement(sqlSELECT);
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt("ID");
				String last_name = rs.getString("last_name");
				String first_name = rs.getString("first_name");
				String practice_since = rs.getString("practice_since");
				String specialty = rs.getString("specialty");
				ssn = rs.getString("ssn");
				System.out.printf("%10d %-30s %-20s %4s %11s\n", id, last_name + ", " + first_name, specialty, practice_since, ssn);
			}

			// Display all rows for patients
			System.out.println("All patients");
			sqlSELECT = "SELECT patientId, last_name, first_name, birthdate, ssn, street, city, state, zipcode, primaryName FROM Patient";
			ps = conn.prepareStatement(sqlSELECT);
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt("patientId");
				String last_name = rs.getString("last_name");
				String first_name = rs.getString("first_name");
				String birthdate = rs.getString("birthdate");
				ssn = rs.getString("ssn");
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zipcode = rs.getString("zipcode");
				String primaryName = rs.getString("primaryName");

				System.out.printf("%10d %-30s %-20s %10s %11s %-15s %-10s %-10s %s\n", id, last_name + ", " + first_name, birthdate, ssn, street, city, state, zipcode, primaryName);
			}

			// Display all rows for prescriptions
			System.out.println("All prescriptions");
			sqlSELECT = "SELECT RXNumber, drugName, quantity, Patient_SSN, PatientFirstName, PatientLastName, Doctor_SSN, DoctorFirstName, DoctorLastName FROM Prescription";
			ps = conn.prepareStatement(sqlSELECT);
			rs = ps.executeQuery();
			while (rs.next()) {
				String rxid = rs.getString("RXNumber");
				String drugName = rs.getString("drugName");
				int quantity = rs.getInt("quantity");
				String patient_ssn = rs.getString("Patient_SSN");
				String patientFirstName = rs.getString("PatientFirstName");
				String patientLastName = rs.getString("PatientLastName");
				String doctor_ssn = rs.getString("Doctor_SSN");
				String doctorFirstName = rs.getString("DoctorFirstName");
				String doctorLastName = rs.getString("DoctorLastName");

				System.out.printf("%10s %-30s %-20d %13s %20s %20s %13s %20s %20s\n", rxid, drugName, quantity, patient_ssn, patientFirstName, patientLastName, doctor_ssn, doctorFirstName, doctorLastName);
			}
		} catch (SQLException e) {
			System.out.println("Error: SQLException " + e.getMessage());
		}
	}

	private static String generateRandomLastName(Random gen) {
		return lastNames[gen.nextInt(lastNames.length)];
	}

	private static String generateRandomFirstName(Random gen) {
		return firstNames[gen.nextInt(firstNames.length)];
	}

	private static String generateUniqueSSN(Random gen, ArrayList<String> generatedSSNs) {
		String ssn;
		do {
			int areaNumber = 1 + gen.nextInt(899); // Random number between 001 and 899
			int groupNumber = 1 + gen.nextInt(99); // Random number between 01 and 99
			int serialNumber = 1 + gen.nextInt(9999); // Random number between 0001 and 9999

			// Format the digits to have leading zeros if necessary
			String formattedAreaNumber = String.format("%03d", areaNumber);
			String formattedGroupNumber = String.format("%02d", groupNumber);
			String formattedSerialNumber = String.format("%04d", serialNumber);

			ssn = formattedAreaNumber + formattedGroupNumber  + formattedSerialNumber;
		} while (generatedSSNs.contains(ssn));

		generatedSSNs.add(ssn);
		return ssn;
	}

	private static String getRandomPatientSSN(Random gen, ArrayList<String> patientSSNs) {
		return patientSSNs.get(gen.nextInt(patientSSNs.size()));
	}

	private static String getDoctorSSN(int doctorId, Connection conn) throws SQLException {
		String sqlSELECT = "SELECT ssn FROM Doctor WHERE ID = ?";
		PreparedStatement ps = conn.prepareStatement(sqlSELECT);
		ps.setInt(1, doctorId);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getString("ssn");
		}
		return "";
	}

	private static String getDoctorFirstName(int doctorId, Connection conn) throws SQLException {
		String sqlSELECT = "SELECT first_name FROM Doctor WHERE ID = ?";
		PreparedStatement ps = conn.prepareStatement(sqlSELECT);
		ps.setInt(1, doctorId);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getString("first_name");
		}
		return "";
	}

	private static String getDoctorLastName(int doctorId, Connection conn) throws SQLException {
		String sqlSELECT = "SELECT last_name FROM Doctor WHERE ID = ?";
		PreparedStatement ps = conn.prepareStatement(sqlSELECT);
		ps.setInt(1, doctorId);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getString("last_name");
		}
		return "";
	}

	private static String getPatientFirstName(String patientSSN, Connection conn) throws SQLException {
		String sqlSELECT = "SELECT first_name FROM Patient WHERE ssn = ?";
		PreparedStatement ps = conn.prepareStatement(sqlSELECT);
		ps.setString(1, patientSSN);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getString("first_name");
		}
		return "";
	}

	private static String getPatientLastName(String patientSSN, Connection conn) throws SQLException {
		String sqlSELECT = "SELECT last_name FROM Patient WHERE ssn = ?";
		PreparedStatement ps = conn.prepareStatement(sqlSELECT);
		ps.setString(1, patientSSN);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getString("last_name");
		}
		return "";
	}

	private static String generateRandomZipCode(Random gen) {
		return String.format("%05d", gen.nextInt(100000));
	}
}