use tp01;

DROP TABLE IF EXISTS tp01_note_not CASCADE;
DROP TABLE IF EXISTS tp01_personnel_per CASCADE;
DROP TABLE IF EXISTS tp01_hopital_hop CASCADE;
DROP TABLE IF EXISTS tp01_prescription_pre CASCADE;
DROP TABLE IF EXISTS tp01_maladieXpatient_mxp CASCADE;
DROP TABLE IF EXISTS tp01_patient_pat CASCADE;
DROP TABLE IF EXISTS tp01_maladie_mal CASCADE;
DROP TABLE IF EXISTS tp01_medicament_med CASCADE;

CREATE TABLE tp01_maladie_mal(
	mal_id VARCHAR(36),
	mal_nom VARCHAR(30) NOT NULL,
	mal_discriminant VARCHAR(1) NOT NULL,
	PRIMARY KEY(mal_id)
);

CREATE TABLE tp01_medicament_med(
	med_id VARCHAR(36),
	med_nom VARCHAR(30) NOT NULL,
	PRIMARY KEY (med_id)
);

CREATE TABLE tp01_patient_pat(
	pat_id VARCHAR(36),
	pat_nom VARCHAR(30) NOT NULL,
	pat_prenom VARCHAR(30) NOT NULL,
	pat_sexe VARCHAR(2) NOT NULL,
	pat_adresse VARCHAR(50) NOT NULL,
	pat_date_naissance DATE NOT NULL,
	pat_email VARCHAR(255) NULL,
	PRIMARY KEY(pat_id)
);

CREATE TABLE tp01_maladieXpatient_mxp(
	mxp_id VARCHAR(36),
	mxp_id_maladie VARCHAR(36) NOT NULL,
	mxp_id_patient VARCHAR(36) NOT NULL,
	mxp_date_decelage DATE NOT NULL,
	mxp_date_guerison DATE NULL,
	mxp_gravite VARCHAR(10) NULL,
	
	PRIMARY KEY(mxp_id),
	
	FOREIGN KEY(mxp_id_patient)
		REFERENCES tp01_patient_pat(pat_id)
		ON UPDATE CASCADE ON DELETE CASCADE,
		
	FOREIGN KEY(mxp_id_maladie)
		REFERENCES tp01_maladie_mal(mal_id)
		ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE tp01_prescription_pre(
	pre_id VARCHAR(36),
	pre_id_patient VARCHAR(36) NOT NULL,
	pre_id_medicament VARCHAR(36) NOT NULL,
	pre_quantite VARCHAR(20) NOT NULL,
	pre_date_prescription DATE NOT NULL,
	
	PRIMARY KEY(pre_id),
	
	FOREIGN KEY(pre_id_medicament)
		REFERENCES tp01_medicament_med(med_id)
		ON UPDATE CASCADE ON DELETE CASCADE,
	
	FOREIGN KEY(pre_id_patient)
		REFERENCES tp01_patient_pat(pat_id)
		ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE tp01_hopital_hop(
	hop_id VARCHAR(36),
	hop_nom VARCHAR(30) NOT NULL,
	hop_adresse VARCHAR(50) NOT NULL,
	hop_telephone VARCHAR(20),
	
	PRIMARY KEY(hop_id)
);

CREATE TABLE tp01_personnel_per(
	per_id VARCHAR(36),
	per_hopital_id VARCHAR(36),
	per_departement VARCHAR(20) NULL,
	per_nom VARCHAR(30) NOT NULL,
	per_prenom VARCHAR(30) NOT NULL,
	per_sexe VARCHAR(2) NOT NULL,
	per_email VARCHAR(255) NOT NULL,
	per_adresse VARCHAR(50) NOT NULL,
	per_date_naissance DATE NOT NULL,
	per_date_embauche DATE NOT NULL,
	per_emploi VARCHAR(20) NOT NULL,
	per_scolarite VARCHAR(30) NULL,
	per_date_fin_emploi DATE NULL ,
	per_salaire FLOAT(12,2) NULL,
	per_specialite VARCHAR(20) NULL,
	
	PRIMARY KEY(per_id),
	
	FOREIGN KEY(per_hopital_id)
		REFERENCES tp01_hopital_hop(hop_id)
		ON UPDATE CASCADE ON DELETE SET NULL
);


CREATE TABLE tp01_note_not(
	not_id VARCHAR(36),
	not_id_patient VARCHAR(36) NOT NULL,
	not_id_auteur VARCHAR(36) NOT NULL,
	not_texte TEXT NOT NULL,
	not_date_archive DATE NOT NULL,
	not_discriminant VARCHAR(1) NOT NULL,
	not_date_sortie_hospitalisation DATE NULL,
	
	PRIMARY KEY(not_id),
	
	FOREIGN KEY(not_id_patient)
		REFERENCES tp01_patient_pat(pat_id)
		ON UPDATE CASCADE ON DELETE CASCADE,
	
	FOREIGN KEY(not_id_auteur)
		REFERENCES tp01_personnel_per(per_id)
		ON UPDATE CASCADE ON DELETE CASCADE
);