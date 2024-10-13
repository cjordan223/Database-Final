// This is a package declaration. All Java classes reside in a package. This helps to avoid naming collisions.
package com.csumb.cst363;

// Here we import necessary packages and classes from the Java standard library and Spring library.
import java.sql.*;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControllerPrescriptionFill {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/prescription/fill")
	public String getFillForm(Model model) {
		// Here we add a new Prescription object to the model. This object can be used to populate form fields in the view.
		model.addAttribute("prescription", new Prescription());
		return "prescription_fill";
	}

	@PostMapping("/prescription/fill")
	public String processFillForm(Prescription p, Model model) {
		try (Connection conn = getConnection()) {
			String sql = "SELECT * FROM Prescription WHERE RXNumber = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, p.getRxid());
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				p.setRxid(resultSet.getString("RXNumber"));
				p.setDoctorFirstName(resultSet.getString("DoctorFirstName"));
				p.setDoctorLastName(resultSet.getString("DoctorLastName"));
				p.setPatientFirstName(resultSet.getString("PatientFirstName"));
				p.setPatientLastName(resultSet.getString("PatientLastName"));
				p.setDrugName(resultSet.getString("DrugName"));
				p.setQuantity(resultSet.getInt("Quantity"));
				p.setDoctor_ssn(resultSet.getString("Doctor_SSN"));
				p.setPatient_ssn(resultSet.getString("Patient_SSN"));
				p.setCost("10");

				Date dateFilled = resultSet.getDate("DateFilled");
				if (dateFilled != null) {
					model.addAttribute("message", "Prescription has already been filled.");
					model.addAttribute("prescription", p);
					return "prescription_fill";
				}

				sql = "SELECT * FROM PHARMACY WHERE Name = ? AND Address = ?";
				statement = conn.prepareStatement(sql);
				statement.setString(1, p.getPharmacyName());
				statement.setString(2, p.getPharmacyAddress());
				resultSet = statement.executeQuery();

				if (resultSet.next()) {
					int pharmacyId = resultSet.getInt("PharmacyId");
					String pharmacyAddress = resultSet.getString("Address");
					String pharmacyPhoneNumber = resultSet.getString("PhoneNumber");
					String pharmacyName = resultSet.getString("Name");
					p.setPharmacyPhone(resultSet.getString("PhoneNumber"));
					p.setPharmacyID(String.valueOf(pharmacyId));
					dateFilled = Date.valueOf(LocalDate.now());
					p.setDateFilled(dateFilled.toString());

					sql = "UPDATE Prescription SET DateFilled = ?, PharmacyId = ?, PharmacyAddress = ?, PharmacyPhoneNumber = ?, PharmacyName = ?  WHERE RXNumber = ?";
					statement = conn.prepareStatement(sql);
					statement.setDate(1, dateFilled);
					statement.setInt(2, pharmacyId);
					statement.setString(3, pharmacyAddress);
					statement.setString(4, pharmacyPhoneNumber);
					statement.setString(5, pharmacyName);
					statement.setString(6, p.getRxid());
					statement.executeUpdate();

					model.addAttribute("message", "Prescription has been filled.");
					model.addAttribute("prescription", p);
					return "prescription_show";
				} else {
					model.addAttribute("message", "Invalid Pharmacy Details.");
					model.addAttribute("prescription", p);
					return "prescription_fill";
				}
			} else {
				model.addAttribute("message", "Invalid Prescription ID.");
				model.addAttribute("prescription", p);
				return "prescription_fill";
			}
		} catch (SQLException e) {
			model.addAttribute("message", "Error occurred: " + e.getMessage());
			model.addAttribute("prescription", p);
			return "prescription_fill";
		}
	}


	private Connection getConnection() throws SQLException {
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		return conn;
	}
}

