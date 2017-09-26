/* requete 01 */

DROP PROCEDURE IF EXISTS get_departmentWomenNotLogistics;

DELIMITER $$
CREATE PROCEDURE get_departmentWomenNotLogistics()
BEGIN
SELECT per_departement as departement
FROM tp01_personnel_per
WHERE per_sexe = "F"
AND per_emploi != "LOGISTIQUE"
GROUP BY per_departement;
    END$$
DELIMITER ;


/* requete 02 */

DROP PROCEDURE IF EXISTS get_totalSalaryDoctors;

DELIMITER $$
CREATE PROCEDURE get_totalSalaryDoctors()
BEGIN
SELECT SUM(per_salaire) as totalSalaire, per_adresse
FROM tp01_personnel_per
INNER JOIN tp01_note_not ON not_id_auteur = per_id
WHERE per_emploi = "DOCTEUR"
AND not_discriminant = "A"
AND 0 < 	(SELECT COUNT(pat_id)
			FROM tp01_patient_pat
            WHERE not_id_patient = pat_id
            AND (DATE_FORMAT(NOW(), '%Y') -
            DATE_FORMAT(pat_date_naissance, '%Y')
            BETWEEN 35 AND 55))
GROUP BY per_adresse;
    END$$
DELIMITER ;


/* requete 03 */

DROP PROCEDURE IF EXISTS get_emailsAsc;

DELIMITER $$
CREATE PROCEDURE get_emailsAsc()
BEGIN
SELECT per_email as email
FROM tp01_personnel_per	
WHERE UPPER(LEFT(per_nom, 1)) BETWEEN 'D' AND 'M'
AND per_scolarite LIKE '%TECHNIQUE%'
ORDER BY per_email ASC;
    END$$
DELIMITER ;

/* requete 05 */

/* requete 01 */

DROP PROCEDURE IF EXISTS get_departmentWomenNotLogistics;

DELIMITER $$
CREATE PROCEDURE get_departmentWomenNotLogistics()
BEGIN
SELECT per_departement as departement
FROM tp01_personnel_per
WHERE per_sexe = "F"
AND per_emploi != "LOGISTIQUE"
GROUP BY per_departement;
    END$$
DELIMITER ;


/* requete 02 */

DROP PROCEDURE IF EXISTS get_totalSalaryDoctors;

DELIMITER $$
CREATE PROCEDURE get_totalSalaryDoctors()
BEGIN
SELECT SUM(per_salaire) as totalSalaire, per_adresse
FROM tp01_personnel_per
INNER JOIN tp01_note_not ON not_id_auteur = per_id
WHERE per_emploi = "DOCTEUR"
AND not_discriminant = "A"
AND 0 < 	(SELECT COUNT(pat_id)
			FROM tp01_patient_pat
            WHERE not_id_patient = pat_id
            AND (DATE_FORMAT(NOW(), '%Y') -
            DATE_FORMAT(pat_date_naissance, '%Y')
            BETWEEN 35 AND 55))
GROUP BY per_adresse;
    END$$
DELIMITER ;


/* requete 03 */

DROP PROCEDURE IF EXISTS get_emailsAsc;

DELIMITER $$
CREATE PROCEDURE get_emailsAsc()
BEGIN
SELECT per_email as email
FROM tp01_personnel_per	
WHERE UPPER(LEFT(per_nom, 1)) BETWEEN 'D' AND 'M'
AND per_scolarite LIKE '%TECHNIQUE%'
ORDER BY per_email ASC;
    END$$
DELIMITER ;

/* requete 05 */

DROP PROCEDURE IF EXISTS add_note_to_patient_with_disease;

DELIMITER //
CREATE PROCEDURE add_note_to_patient_with_disease(id_employe VARCHAR(36), id_maladie VARCHAR(36))
BEGIN
	DECLARE id_patient_malade VARCHAR(36);
	DECLARE nom_maladie VARCHAR(30);
	DECLARE exit_loop BOOLEAN;  
    
	DECLARE cur1 CURSOR FOR 
		SELECT pat_id 
		FROM tp01_patient_pat
        INNER JOIN tp01_maladieXpatient_mxp
        ON mxp_id_patient = pat_id
        WHERE mxp_id_maladie = id_maladie;
		
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET exit_loop = TRUE;
	
	OPEN cur1;
	
	read_loop: LOOP
		FETCH cur1 into id_patient_malade;
		SELECT id_patient_malade;
		INSERT INTO tp01_note_not(SELECT UUID(), id_patient_malade, id_employe, CONCAT("A la maladie: ", COALESCE(nom_maladie, 'MALADIE INCONNUE'), "."), NOW(), 'N', NULL);
		IF exit_loop THEN
				LEAVE read_loop;
			END IF;
		END LOOP;
	SELECT id_maladie;
	CLOSE cur1;
END //







