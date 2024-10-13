-- This script creates a HospitalDB database and defines several tables: Doctor, Patient, Drug, Pharmacy, and Prescription.

-- Drop the HospitalDB database if it already exists to start fresh.
DROP DATABASE IF EXISTS HospitalDB;

-- Create the HospitalDB database.
CREATE DATABASE HospitalDB;

-- Switch to using the HospitalDB database.
USE HospitalDB;

-- Create the Doctor table.
-- This table stores information about doctors.
CREATE TABLE Doctor
(
    ID             INT AUTO_INCREMENT PRIMARY KEY,                                    -- Unique identifier for each doctor (auto-incremented).
    last_name      VARCHAR(50) NOT NULL,                                              -- Last name of the doctor.
    first_name     VARCHAR(50) NOT NULL,                                              -- First name of the doctor.
    practice_since INT(4)      NOT NULL CHECK (practice_since BETWEEN 1900 AND 2022), -- Year the doctor started practicing.
    specialty      VARCHAR(50) NOT NULL,                                              -- Medical specialty of the doctor.
    ssn            VARCHAR(20) NOT NULL UNIQUE,                                       -- Social Security Number of the doctor.
    INDEX (ssn)                                                                       -- Indexing the ssn column for efficient queries.
);

-- Create the Patient table.
-- This table stores information about patients.
CREATE TABLE Patient
(
    patientId   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each patient (auto-incremented).
    last_name   VARCHAR(255) NOT NULL,                            -- Last name of the patient.
    first_name  VARCHAR(255) NOT NULL,                            -- First name of the patient.
    birthdate   DATE         NOT NULL,                            -- Birthdate of the patient.
    ssn         VARCHAR(20)  NOT NULL UNIQUE,                     -- Social Security Number of the patient.
    street      VARCHAR(255) NOT NULL,                            -- Street address of the patient.
    city        VARCHAR(255) NOT NULL,                            -- City of the patient.
    state       VARCHAR(255) NOT NULL,                            -- State of the patient.
    zipcode     VARCHAR(10)  NOT NULL,                            -- Zip code of the patient.
    primaryName VARCHAR(255) NOT NULL,                            -- Primary name of the patient.
    INDEX (ssn)                                                   -- Indexing the ssn column for efficient queries.
);

-- Create the Drug table.
-- This table stores information about drugs.
CREATE TABLE drug
(
    drug_id    int(11)      NOT NULL, -- Unique identifier for each drug.
    trade_name varchar(100) NOT NULL, -- Trade name of the drug.
    formula    varchar(200) NOT NULL, -- Chemical formula of the drug.
    PRIMARY KEY (drug_id)             -- Primary key constraint on the drug_id column.
);

-- Insert data into the Drug table.
-- This inserts sample drug records.
INSERT INTO drug
VALUES (1, 'Tylenol with Codeine', 'acetaminophen and codeine'),
       (2, 'Proair Proventil', 'albuterol aerosol'),
       (3, 'Accuneb', 'albuterol HFA'),
       (4, 'Fosamax', 'alendronate'),
       (5, 'Zyloprim', 'allopurinol'),
       (6, 'Xanax', 'alprazolam'),
       (7, 'Elavil', 'amitriptyline'),
       (8, 'Augmentin', 'amoxicillin and clavulanate K+'),
       (9, 'Amoxil', 'amoxicillin'),
       (10, 'Adderall XR', 'amphetamine and dextroamphetamine XR'),
       (11, 'Tenormin', 'atenolol'),
       (12, 'Lipitor', 'atorvastatin'),
       (13, 'Zithromax', 'azithromycin'),
       (14, 'Lotrel', 'benazepril and amlodipine'),
       (15, 'Soma', 'carisoprodol'),
       (16, 'Coreg', 'carvedilol'),
       (17, 'Omnicef', 'cefdinir'),
       (18, 'Celebrex', 'celecoxib'),
       (19, 'Keflex', 'cephalexin'),
       (20, 'Cipro', 'ciprofloxacin'),
       (21, 'Celexa', 'citalopram'),
       (22, 'Klonopin', 'clonazepam'),
       (23, 'Catapres', 'clonidine HCl'),
       (24, 'Plavix', 'clopidogrel'),
       (25, 'Premarin', 'conjugated estrogens'),
       (26, 'Flexeril', 'cyclobenzaprine'),
       (27, 'Valium', 'diazepam'),
       (28, 'Voltaren', 'diclofenac sodium'),
       (29, 'Yaz', 'drospirenone and ethinyl estradiol'),
       (30, 'Cymbalta', 'Duloxetine'),
       (31, 'Vibramycin', 'doxycycline hyclate'),
       (32, 'Vasotec', 'enalapril'),
       (33, 'Lexapro', 'escitalopram'),
       (34, 'Nexium', 'esomeprazole'),
       (35, 'Zetia', 'ezetimibe'),
       (36, 'Tricor', 'fenofibrate'),
       (37, 'Allegra', 'fexofenadine'),
       (38, 'Diflucan', 'fluconozole'),
       (39, 'Prozac', 'fluoxetine HCl'),
       (40, 'Advair', 'fluticasone and salmeterol inhaler'),
       (41, 'Flonase', 'fluticasone nasal spray'),
       (42, 'Folic Acid', 'folic acid'),
       (43, 'Lasix', 'furosemide'),
       (44, 'Neurontin', 'gabapentin'),
       (45, 'Amaryl', 'glimepiride'),
       (46, 'Diabeta', 'glyburide'),
       (47, 'Glucotrol', 'glipizide'),
       (48, 'Microzide', 'hydrochlorothiazide'),
       (49, 'Lortab', 'hydrocodone and acetaminophen'),
       (50, 'Motrin', 'ibuprophen'),
       (51, 'Lantus', 'insulin glargine'),
       (52, 'Imdur', 'isosorbide mononitrate'),
       (53, 'Prevacid', 'lansoprazole'),
       (54, 'Levaquin', 'levofloxacin'),
       (55, 'Levoxl', 'levothyroxine sodium'),
       (56, 'Zestoretic', 'lisinopril and hydrochlorothiazide'),
       (57, 'Prinivil', 'lisinopril'),
       (58, 'Ativan', 'lorazepam'),
       (59, 'Cozaar', 'losartan'),
       (60, 'Mevacor', 'lovastatin'),
       (61, 'Mobic', 'meloxicam'),
       (62, 'Glucophage', 'metformin HCl'),
       (63, 'Medrol', 'methylprednisone'),
       (64, 'Toprol', 'metoprolol succinate XL'),
       (65, 'Lopressor', 'metoprolol tartrate'),
       (66, 'Nasonex', 'mometasone'),
       (67, 'Singulair', 'montelukast'),
       (68, 'Naprosyn', 'naproxen'),
       (69, 'Prilosec', 'omeprazole'),
       (70, 'Percocet', 'oxycodone and acetaminophen'),
       (71, 'Protonix', 'pantoprazole'),
       (72, 'Paxil', 'paroxetine'),
       (73, 'Actos', 'pioglitazone'),
       (74, 'Klor-Con', 'potassium Chloride'),
       (75, 'Pravachol', 'pravastatin'),
       (76, 'Deltasone', 'prednisone'),
       (77, 'Lyrica', 'pregabalin'),
       (78, 'Phenergan', 'promethazine'),
       (79, 'Seroquel', 'quetiapine'),
       (80, 'Zantac', 'ranitidine'),
       (81, 'Crestor', 'rosuvastatin'),
       (82, 'Zoloft', 'sertraline HCl'),
       (83, 'Viagra', 'sildenafil HCl'),
       (84, 'Vytorin', 'simvastatin and ezetimibe'),
       (85, 'Zocor', 'simvastatin'),
       (86, 'Aldactone', 'spironolactone'),
       (87, 'Bactrim DS', 'sulfamethoxazole and trimethoprim DS'),
       (88, 'Flomax', 'tamsulosin'),
       (89, 'Restoril', 'temezepam'),
       (90, 'Topamax', 'topiramate'),
       (91, 'Ultram', 'tramadol'),
       (92, 'Aristocort', 'triamcinolone Ace topical'),
       (93, 'Desyrel', 'trazodone HCl'),
       (94, 'Dyazide', 'triamterene and hydrochlorothiazide'),
       (95, 'Valtrex', 'valaciclovir'),
       (96, 'Diovan', 'valsartan'),
       (97, 'Effexor XR', 'venlafaxine XR'),
       (98, 'Calan SR', 'verapamil SR'),
       (99, 'Ambien', 'zolpidem');

-- Create the Pharmacy table.
-- This table stores information about pharmacies.
CREATE TABLE Pharmacy
(
    PharmacyId  INT(8) PRIMARY KEY AUTO_INCREMENT, -- Unique identifier for each pharmacy (auto-incremented).
    Name        VARCHAR(50) NOT NULL,              -- Name of the pharmacy.
    Address     VARCHAR(50) NOT NULL,              -- Address of the pharmacy.
    PhoneNumber VARCHAR(20) NOT NULL,              -- Phone number of the pharmacy.
    INDEX (Name)                                   -- Indexing the Name column for efficient queries.
);

-- Insert data into the Pharmacy table.
-- This inserts sample pharmacy records.
INSERT INTO Pharmacy (Name, Address, PhoneNumber)
VALUES ('ABC Pharmacy', '123 Main Street', '555-1234'),
       ('XYZ Pharmacy', '456 Elm Avenue', '555-5678'),
       ('123 Pharmacy', '789 Oak Lane', '555-9101'),
       ('MediCare Pharmacy', '321 Pine Road', '555-1213'),
       ('QuickMeds', '987 Cedar Street', '555-1415');

-- Create the Prescription table.
-- This table stores information about prescriptions.
CREATE TABLE Prescription
(
    RXNumber            INT PRIMARY KEY AUTO_INCREMENT,             -- Unique identifier for each prescription (auto-incremented).
    DrugName            VARCHAR(255) NOT NULL,                      -- Name of the prescribed drug.
    Quantity            INT          NOT NULL CHECK (Quantity > 0), -- Quantity of the drug prescribed.
    Patient_SSN         VARCHAR(20)  NOT NULL,                      -- Social Security Number of the patient.
    PatientFirstName    VARCHAR(255) NOT NULL,                      -- First name of the patient.
    PatientLastName     VARCHAR(255) NOT NULL,                      -- Last name of the patient.
    Doctor_SSN          VARCHAR(20)  NOT NULL,                      -- Social Security Number of the doctor.
    DoctorFirstName     VARCHAR(255) NOT NULL,                      -- First name of the doctor.
    DoctorLastName      VARCHAR(255) NOT NULL,                      -- Last name of the doctor.
    PharmacyId          INT(8)      DEFAULT NULL,                   -- Pharmacy identifier for the prescription.
    PharmacyName        VARCHAR(50) DEFAULT NULL,                   -- Name of the pharmacy where the prescription was filled.
    PharmacyAddress     VARCHAR(50) DEFAULT NULL,                   -- Address of the pharmacy where the prescription was filled.
    PharmacyPhoneNumber VARCHAR(20) DEFAULT NULL,                   -- Phone number of the pharmacy where the prescription was filled.
    DateFilled          DATE        DEFAULT NULL,                   -- Date when the prescription was filled.
    FOREIGN KEY (Patient_SSN) REFERENCES Patient (ssn),             -- Foreign key constraint referencing the Patient table.
    FOREIGN KEY (Doctor_SSN) REFERENCES Doctor (ssn),               -- Foreign key constraint referencing the Doctor table.
    FOREIGN KEY (PharmacyId) REFERENCES Pharmacy (pharmacyid)       -- Foreign key constraint referencing the Pharmacy table.
);

-- Select all rows from the Doctor table.
SELECT *
FROM Doctor;

-- Select all rows from the Patient table.
SELECT *
FROM Patient;

-- Select all rows from the Prescription table.
SELECT *
FROM Prescription;

-- Select all rows from the Pharmacy table.
SELECT *
FROM Pharmacy;
