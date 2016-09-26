// Fichier DAO.java
// Auteur : Primavera Sequeira Steven
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 * Classe de base pour tous les DAOs.
 *
 * @author Mathieu Lafond
 */
public class DAO implements Serializable {
    private static final long serialVersionUID = 1L;

    /* private static final String ADD_REQUEST = "INSERT INTO livre(idLivre, titre, auteur, dateAcquisition, idMembre, datePret)"
     *  + "VALUES (?, ?, ?, ?, NULL, NULL)";
     *
     *  private static final String READ_REQUEST = "SELECT idLivre, titre, auteur, dateAcquisition, idMembre, pretDate"
     *  + "FROM livre"
     *  + "WHERE idLivre = "?"";
     *
     * private static final String UPDATE_REQUEST = "UPDATE livre"
     *  + "SET titre = ?, auteur = ?, dateAcquisition = ?, idMembre = ?, datePret = ?"
     *  + "WHERE idLivre = ?";
     *
     * private static final String DELETE_REQUEST = "DELETE from livre"
     * + "WHERE idLivre = ?";
     *
     * private static final String GET_ALL_REQUEST = "SELECT idLivre, titre, auteur, dateAcquisition, idMembre, pretDate";
     */
    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser
     */
    private Connexion connexion;

    /**
     * Constructeur de DAO de base.
     *
     * @param connexion connexion à utiliser
     */
    public DAO(Connexion connexion) {
        super();
        this.connexion = connexion;
    }

    /**
     * Getter de la variable d'instance this.connexion.
     *
     * @return Connexion retourne la variable d'instance connexion
     */
    public Connexion getConnexion() {

        return this.connexion;
    }

}
