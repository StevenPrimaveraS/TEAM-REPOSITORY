// Fichier PretDAO.java
// Auteur : Primavera Sequeira Steven
// Date de création : 2016-10-17

package ca.qc.collegeahuntsic.bibliotheque.dao;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

/**
 * DAO pour effectuer des CRUDs avec la table <code>pret</code>.
 *
 * @author Primavera Sequeira Steven
 */
public class PretDAO extends DAO {
    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     */
    public PretDAO(Connexion connexion) {
        super(connexion);
    }
}
