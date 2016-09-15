// Fichier LivreDTO.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * Permet de representer un tuple de la table livre.
 *
*/

public class LivreDTO {

    public int idLivre;

    public String titre;

    public String auteur;

    public Date dateAcquisition;

    public int idMembre;

    public Date datePret;
}
