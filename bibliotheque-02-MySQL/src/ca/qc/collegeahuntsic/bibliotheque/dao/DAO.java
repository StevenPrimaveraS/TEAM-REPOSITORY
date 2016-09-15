// Fichier DAO.java
// Auteur : Primavera Sequeira Steven
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 * TODO Auto-generated class javadoc
 *
 * @author Primavera Sequeira Steven
 */
public class DAO implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Connexion cx;

    public DAO(Connexion cx) {
        super();
        this.cx = cx;
    }

    /**
     * Retourner la connexion associee.
     */
    public Connexion getConnexion() {

        return this.cx;
    }

}
