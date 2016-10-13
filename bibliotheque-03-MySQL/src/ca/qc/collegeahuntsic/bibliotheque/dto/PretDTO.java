// Fichier PretDTO.java
// Auteur : Dominic Leroux
// Date de création : 2016-10-13

package ca.qc.collegeahuntsic.bibliotheque.dto;

import java.sql.Timestamp;

/**
 * DTO de la table pret.
 *
 * @author Dominic Leroux
 */
public class PretDTO extends DTO {

    private int idPret;

    private int idLivre;

    private int idMembre;

    private Timestamp datePret;

    private Timestamp dateRetour;

    /**
     * Constructeur de PretDTO par défaut.
     */
    public PretDTO() {

    }

    /**
     * Getter de la variable d'instance <code>this.idPret</code>.
     *
     * @return La variable d'instance <code>this.idPret</code>
     */
    public int getIdPret() {
        return this.idPret;
    }

    /**
     * Setter de la variable d'instance <code>this.idPret</code>.
     *
     * @param idPret La valeur à utiliser pour la variable d'instance <code>this.idPret</code>
     */
    public void setIdPret(int idPret) {
        this.idPret = idPret;
    }

    /**
     * Getter de la variable d'instance <code>this.idLivre</code>.
     *
     * @return La variable d'instance <code>this.idLivre</code>
     */
    public int getIdLivre() {
        return this.idLivre;
    }

    /**
     * Setter de la variable d'instance <code>this.idLivre</code>.
     *
     * @param idLivre La valeur à utiliser pour la variable d'instance <code>this.idLivre</code>
     */
    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    /**
     * Getter de la variable d'instance <code>this.idMembre</code>.
     *
     * @return La variable d'instance <code>this.idMembre</code>
     */
    public int getIdMembre() {
        return this.idMembre;
    }

    /**
     * Setter de la variable d'instance <code>this.idMembre</code>.
     *
     * @param idMembre La valeur à utiliser pour la variable d'instance <code>this.idMembre</code>
     */
    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    /**
     * Getter de la variable d'instance <code>this.datePret</code>.
     *
     * @return La variable d'instance <code>this.datePret</code>
     */
    public Timestamp getDatePret() {
        return this.datePret;
    }

    /**
     * Setter de la variable d'instance <code>this.datePret</code>.
     *
     * @param datePret La valeur à utiliser pour la variable d'instance <code>this.datePret</code>
     */
    public void setDatePret(Timestamp datePret) {
        this.datePret = datePret;
    }

    /**
     * Getter de la variable d'instance <code>this.dateRetour</code>.
     *
     * @return La variable d'instance <code>this.dateRetour</code>
     */
    public Timestamp getDateRetour() {
        return this.dateRetour;
    }

    /**
     * Setter de la variable d'instance <code>this.dateRetour</code>.
     *
     * @param dateRetour La valeur à utiliser pour la variable d'instance <code>this.dateRetour</code>
     */
    public void setDateRetour(Timestamp dateRetour) {
        this.dateRetour = dateRetour;
    }

}
