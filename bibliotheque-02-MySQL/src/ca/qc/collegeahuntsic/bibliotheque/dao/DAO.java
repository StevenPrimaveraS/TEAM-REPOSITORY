// Fichier DAO.java
// Auteur : Primavera Sequeira Steven
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import java.sql.Connection;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 * Classe de base pour tous les DAOs.
 *
 * @author Mathieu Lafond
 */
public class DAO implements Serializable {
    private static final long serialVersionUID = 1L;

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

    /**
     * Retourne la connection de this.connexion.
     *
     * @return Connection retourne la variable d'instance connexion
     */
    public Connection getConnection() {

        return getConnexion().getConnection();
    }

}
