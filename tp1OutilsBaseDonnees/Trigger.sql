USE tp01;

DROP TRIGGER IF EXISTS trig_before_insert_per;
DELIMITER $$
CREATE TRIGGER trig_before_insert_per BEFORE INSERT ON tp01_personnel_per
    FOR EACH ROW
	BEGIN
	IF UPPER(NEW.per_departement) NOT IN ('ADMINISTRATIVE', 'MEDICAL', 'TECHNIQUE') THEN
		SIGNAL SQLSTATE '12345'
			 SET MESSAGE_TEXT = 'check constraint on tp01_personnel_per.per_departement failed';
	END IF;
	
	IF NOT ((UPPER(NEW.per_emploi) IN ('DOCTEUR', 'INFIRMIER') AND UPPER(NEW.per_departement) = 'MEDICAL') OR
		(UPPER(NEW.per_emploi) = 'SECRETARIAT' AND UPPER(NEW.per_departement) = 'ADMINISTRATIVE') OR
		(UPPER(NEW.per_emploi) IN ('TECHNICIEN', 'LOGISTIQUE') AND UPPER(NEW.per_departement) = 'TECHNIQUE')) THEN
		SIGNAL SQLSTATE '12345'
			SET MESSAGE_TEXT = 'check constraint on tp01_personnel_per.per_emploi failed';
	END IF;
    
	IF NOT (UPPER(NEW.per_scolarite) IN ('SECONDAIRE', 'TECHNIQUE', 'BACCALAUREAT', 'MAITRISE', 'DOCTORAT')) THEN
		SIGNAL SQLSTATE '12345'
			SET MESSAGE_TEXT = 'check constraint on tp01_personnel_per.per_scolarite failed';
	END IF;
	
    IF (NEW.per_date_naissance >= NEW.per_date_embauche) THEN
		SIGNAL SQLSTATE '12345'
			SET MESSAGE_TEXT = 'check constraint on tp01_personnel_per.date_embauche failed';
	END IF;
	
	IF ((NEW.per_date_fin_emploi < NEW.per_date_embauche) AND NEW.per_date_fin_emploi IS NOT NULL) THEN
		SIGNAL SQLSTATE '12345'
			SET MESSAGE_TEXT = 'check constraint on tp01_personnel_per.date_fin_emploi failed';
	END IF;

	IF ((NEW.per_specialite IS NOT NULL) AND UPPER(NEW.per_emploi) <> 'DOCTEUR' ) THEN
		SIGNAL SQLSTATE '12345'
			SET MESSAGE_TEXT = 'check constraint on tp01_personnel_per.specialite failed';
	END IF;	
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS trig_before_insert_mxp;
DELIMITER $$
CREATE TRIGGER trig_before_insert_mxp BEFORE INSERT ON tp01_maladieXpatient_mxp
    FOR EACH ROW
	BEGIN
	IF UPPER(NEW.mxp_gravite) NOT IN ('TRIVIAL', 'FAIBLE', 'MOYEN', 'SEVERE', 'MORTEL') THEN
		SIGNAL SQLSTATE '12345'
			 SET MESSAGE_TEXT = 'check constraint on tp01_maladieXpatient_mxp.mxp_gravite failed';
	END IF;
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS trig_before_insert_not;
DELIMITER $$
CREATE TRIGGER trig_before_insert_not BEFORE INSERT ON tp01_note_not
    FOR EACH ROW
	BEGIN
	IF UPPER(NEW.not_discriminant) NOT IN ('A','N','H') THEN
		SIGNAL SQLSTATE '12345'
			 SET MESSAGE_TEXT = 'check constraint on tp01_note_not.not_discriminant failed';
	END IF;
	
	IF (UPPER(NEW.not_discriminant) IN ('A', 'N') AND NEW.not_date_sortie_hospitalisation IS NOT NULL) THEN
		SIGNAL SQLSTATE '12345'
			 SET MESSAGE_TEXT = 'check constraint on tp01_note_not.not_date_sortie_hospitalisation failed';
	END IF;
END$$
DELIMITER ;


DROP TRIGGER IF EXISTS trig_before_insert_mal;
DELIMITER $$
CREATE TRIGGER trig_before_insert_mal BEFORE INSERT ON tp01_maladie_mal
    FOR EACH ROW
	BEGIN
	IF UPPER(NEW.mal_discriminant) NOT IN ('A', 'M') THEN
		SIGNAL SQLSTATE '12345'
			 SET MESSAGE_TEXT = 'check constraint on tp01_maladie_mal.mal_discriminant failed';
	END IF;
END$$
DELIMITER ;