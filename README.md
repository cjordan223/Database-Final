# Hospital Management System

## Overview
The Hospital Management System is a web-based application built with **Spring Boot** for managing hospital-related data such as doctors, patients, prescriptions, and pharmacies. The system allows users to perform CRUD operations and offers basic prescription handling workflows.

## Features
- **Doctor Management**: Add, edit, and view doctor information, including specialties and practice years.
- **Patient Management**: Create and manage patient records, including personal and contact information.
- **Prescription Handling**: Doctors can prescribe drugs to patients, and pharmacies can fill these prescriptions.
- **Pharmacy and Drug Management**: Maintain a list of pharmacies and drugs available for prescriptions.

## Architecture
- **Backend**: The backend is powered by **Spring Boot**, providing RESTful services for the hospital's entities.
- **Database**: **MySQL** is used to store data for doctors, patients, prescriptions, pharmacies, and drugs.
- **Frontend**: The frontend is built using **Thymeleaf** for server-side rendering of dynamic content.

## Entities
- **Doctor**: Stores doctor details like name, specialty, practice start year, and social security number.
- **Patient**: Contains patient details such as name, birthdate, address, and social security number.
- **Prescription**: Links doctors, patients, and drugs, tracking prescribed medications.
- **Drug**: Represents drugs that can be prescribed.
- **Pharmacy**: Manages pharmacy information and links to filled prescriptions.

## Data Flow
1. Doctors can create and manage patients, and prescribe medications.
2. Patients can view their prescriptions and get them filled at a pharmacy.
3. Pharmacies fill prescriptions based on the doctor's orders and the patient's information.

## Technologies Used
- **Spring Boot**: Backend framework for REST API and web application development.
- **Thymeleaf**: Template engine for rendering HTML views.
- **MySQL**: Relational database management system for persistent data storage.
- **Maven**: Dependency and build management tool.
