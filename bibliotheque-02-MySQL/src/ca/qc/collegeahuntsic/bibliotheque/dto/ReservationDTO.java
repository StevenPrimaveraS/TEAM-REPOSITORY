// Fichier ReservationDTO.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Date;

/**
 * Permet de representer un tuple de la table membre.
 *
 */

public class ReservationDTO {

    public int idReservation;

    public int idLivre;

    public int idMembre;

    public Date dateReservation;
}
