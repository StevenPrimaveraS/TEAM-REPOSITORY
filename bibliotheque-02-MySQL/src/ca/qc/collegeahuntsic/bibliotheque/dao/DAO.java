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
    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     * 
     * @param connexion - La connexion à utiliser
     */
    protected Connexion cx;

    public DAO(Connexion cx) {
        super();
        this.cx = cx;
    }

    /**
     * Getter de la variable d'instance this.connexion.
     * @param La variable d'instance this.connexion
     */
    public Connexion getConnexion() {

        return this.cx;
    }

}
