package com.csumb.cst363;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/*
 * Controller class for patient interactions.
 *   register as a new patient.
 *   update patient profile.
 */
@Controller
public class ControllerPatient {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
     * Request blank patient registration form.
     */
    @GetMapping("/patient/new")
    public String newPatient(Model model) {
        // return blank form for new patient registration
        model.addAttribute("patient", new Patient());
        return "patient_register";
    }

    /*
     * Process new patient registration	 */
    @PostMapping("/patient/new")
    public String newPatient(Patient p, Model model) throws SQLException {

        // Validate primaryName field
        if (!isPrimaryCareSpecialist(p.getPrimaryName(), p.getBirthdate())) {
            model.addAttribute("message", "Error: Invalid primary care specialist.");
            model.addAttribute("patient", p);
            return "patient_register";
        }

        // Validate last name
        if (!isValidName(p.getLast_name())) {
            model.addAttribute("message", "Error: Invalid last name.");
            model.addAttribute("patient", p);
            return "patient_register";
        }

        // Validate first name
        if (!isValidName(p.getFirst_name())) {
            model.addAttribute("message", "Error: Invalid first name.");
            model.addAttribute("patient", p);
            return "patient_register";
        }

        // Validate birthdate
        if (!isValidDate(p.getBirthdate())) {
            model.addAttribute("message", "Error: Invalid birthdate.");
            model.addAttribute("patient", p);
            return "patient_register";
        }

        // Validate city
        if (!isValidName(p.getCity())) {
            model.addAttribute("message", "Error: Invalid city.");
            model.addAttribute("patient", p);
            return "patient_register";
        }

        // Validate state
        if (!isValidName(p.getState())) {
            model.addAttribute("message", "Error: Invalid state.");
            model.addAttribute("patient", p);
            return "patient_register";
        }

        // Validate zipcode
        if (!isValidZipcode(p.getZipcode())) {
            model.addAttribute("message", "Error: Invalid zipcode.");
            model.addAttribute("patient", p);
            return "patient_register";
        }

        if (!isSSN(p.getSsn())) {
            model.addAttribute("message", "Error: Invalid ssn.");
            model.addAttribute("patient", p);
            return "patient_register";
        }

        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO patient (last_name, first_name, birthdate, ssn, street, city, state, zipcode, primaryName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getLast_name());
            ps.setString(2, p.getFirst_name());
            ps.setString(3, p.getBirthdate());
            ps.setString(4, p.getSsn());
            ps.setString(5, p.getStreet());
            ps.setString(6, p.getCity());
            ps.setString(7, p.getState());
            ps.setString(8, p.getZipcode());
            ps.setString(9, p.getPrimaryName());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                p.setPatientId(String.valueOf(rs.getLong(1)));
            }

            // Display message and patient information
            model.addAttribute("message", "Registration Successful.");
            model.addAttribute("patient", p);
            return "patient_show";
        } catch (SQLException e) {
            model.addAttribute("message", "SQL Error: " + e.getMessage());
            model.addAttribute("patient", p);
            return "patient_register";
        }
    }


    @GetMapping("/patient/edit")
    public String getPatientForm(Model model) {
        return "patient_get";
    }

    /*
     * Perform search for patient by patient id and name.
     */
    @PostMapping("/patient/show")
    public String getPatientForm(@RequestParam("patientId") String patientId, @RequestParam("last_name") String last_name, Model model) {

        try (Connection con = getConnection()) {
            System.out.println("start getPatient  " + last_name);
            PreparedStatement ps = con.prepareStatement("SELECT last_name, first_name, birthdate, ssn, street, city, state, zipcode, primaryName FROM patient WHERE patientID = ? AND last_name = ?");
            ps.setString(1, patientId);
            ps.setString(2, last_name);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                patient.setLast_name(rs.getString("last_name"));
                patient.setFirst_name(rs.getString("first_name"));
                patient.setBirthdate(rs.getString("birthdate"));
                patient.setSsn(rs.getString("ssn"));
                patient.setStreet(rs.getString("street"));
                patient.setCity(rs.getString("city"));
                patient.setState(rs.getString("state"));
                patient.setZipcode(rs.getString("zipcode"));
                patient.setPrimaryName(rs.getString("primaryName"));

                // display patient information
                model.addAttribute("patient", patient);
                return "patient_show";
            } else {
                model.addAttribute("message", "Patient not found.");
                return "patient_get";
            }

        } catch (SQLException e) {
            System.out.println("SQL error in getPatientForm " + e.getMessage());
            model.addAttribute("message", "SQL Error: " + e.getMessage());
            return "patient_get";
        }
    }

    @GetMapping("/patient/edit/{patientId}")
    public String updatePatient(@PathVariable String patientId, Model model) {

        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT last_name, first_name, birthdate, ssn, street, city, state, zipcode, primaryName FROM patient WHERE patientID = ?");
            ps.setString(1, patientId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Patient patient = new Patient();
                patient.setPatientId(patientId);
                patient.setLast_name(rs.getString("last_name"));
                patient.setFirst_name(rs.getString("first_name"));
                patient.setBirthdate(rs.getString("birthdate"));
                patient.setSsn(rs.getString("ssn"));
                patient.setStreet(rs.getString("street"));
                patient.setCity(rs.getString("city"));
                patient.setState(rs.getString("state"));
                patient.setZipcode(rs.getString("zipcode"));
                patient.setPrimaryName(rs.getString("primaryName"));

                // Display patient information for editing
                model.addAttribute("patient", patient);
                return "patient_edit";
            } else {
                model.addAttribute("message", "Patient not found.");
                return "patient_get";
            }
        } catch (SQLException e) {
            model.addAttribute("message", "SQL Error: " + e.getMessage());
            return "patient_get";
        }
    }


    /*
     * Process changes to patient profile.
     */
    @PostMapping("/patient/edit")
    public String updatePatient(Patient p, Model model) throws SQLException {
        // Validate primaryName field
        if (!isPrimaryCareSpecialist(p.getPrimaryName(), p.getBirthdate())) {
            model.addAttribute("message", "Error: Invalid primary care specialist.");
            model.addAttribute("patient", p);
            return "patient_edit";
        }

        // Validate city
        if (!isValidName(p.getCity())) {
            model.addAttribute("message", "Error: Invalid city.");
            model.addAttribute("patient", p);
            return "patient_edit";
        }

        // Validate state
        if (!isValidName(p.getState())) {
            model.addAttribute("message", "Error: Invalid state.");
            model.addAttribute("patient", p);
            return "patient_edit";
        }

        // Validate zipcode
        if (!isValidZipcode(p.getZipcode())) {
            model.addAttribute("message", "Error: Invalid zipcode.");
            model.addAttribute("patient", p);
            return "patient_edit";
        }

        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE patient SET street=?, city=?, state=?, zipcode=?, primaryName=? WHERE patientID=?");
            ps.setString(1, p.getStreet());
            ps.setString(2, p.getCity());
            ps.setString(3, p.getState());
            ps.setString(4, p.getZipcode());
            ps.setString(5, p.getPrimaryName());
            ps.setString(6, p.getPatientId());

            int rc = ps.executeUpdate();
            if (rc == 1) {
                model.addAttribute("message", "Update successful");
                model.addAttribute("patient", p);
                return "patient_show";
            } else {
                model.addAttribute("message", "Error: Update was not successful");
                model.addAttribute("patient", p);
                return "patient_edit";
            }
        } catch (SQLException e) {
            model.addAttribute("message", "SQL Error: " + e.getMessage());
            model.addAttribute("patient", p);
            return "patient_edit";
        }
    }


    private boolean isPrimaryCareSpecialist(String lastName, String birthDateStr) throws SQLException {
        if (!isValidBirthDate(birthDateStr)) {
            return hasPediatricsSpecialty(lastName);
        }

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS count FROM Doctor WHERE last_name = ? AND specialty IN ('Family Medicine', 'Internal Medicine')")) {
            ps.setString(1, lastName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        }

        return false;
    }

    private boolean isValidBirthDate(String birthDateStr) {
        try {
            LocalDate birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears() >= 18;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasPediatricsSpecialty(String lastName) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS count FROM Doctor WHERE last_name = ? AND specialty = 'Pediatrics'")) {
            ps.setString(1, lastName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        }

        return false;
    }

    private boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z ]+$");
    }

    private boolean isValidDate(String date) {
        return date != null && date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$");
    }

    private boolean isValidZipcode(String zipcode) {
        return zipcode != null && zipcode.matches("^\\d{5}(-\\d{4})?$");
    }

    private static boolean isSSN(String ssn) {
        return ssn != null && ssn.matches("^[1-8]\\d{4}[0-8]\\d{3}$");
    }

    /*
     * return JDBC Connection using jdbcTemplate in Spring Server
     */

    private Connection getConnection() throws SQLException {
        Connection conn = jdbcTemplate.getDataSource().getConnection();
        return conn;
    }

}

