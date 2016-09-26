// Fichier LivreDAO.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * DAO pour effectuer des CRUDs avec la table livre.
 *
 * @author Mathieu Lafond
 */

public class LivreDAO extends DAO {
    private static final long serialVersionUID = 1L;

    private PreparedStatement statementExiste;

    private PreparedStatement statementInsert;

    private PreparedStatement statementUpdate;

    private PreparedStatement statementDelete;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser.
     * @throws SQLException si une erreur survient.
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public LivreDAO(Connexion connexion) throws DAOException {
        super(connexion);
        try {
            this.statementExiste = connexion.getConnection()
                .prepareStatement("select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?");
            this.statementInsert = connexion.getConnection().prepareStatement("insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
                + "values (?,?,?,?,null,null)");
            this.statementUpdate = connexion.getConnection().prepareStatement("update livre set idMembre = ?, datePret = ? "
                + "where idLivre = ?");
            this.statementDelete = connexion.getConnection().prepareStatement("delete from livre where idlivre = ?");
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Verifie si un livre existe.
     *
     * @param idLivre identifiant du livre
     * @throws SQLException si une erreur survient
     * @return boolean si le livre existe ou pas
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public boolean existe(int idLivre) throws DAOException {
        try {
            this.statementExiste.setInt(1,
                idLivre);
            final ResultSet resultset = this.statementExiste.executeQuery();
            final boolean livreExiste = resultset.next();
            resultset.close();
            return livreExiste;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lecture d'un livre.
     *
     * @param idLivre identifiant du livre
     * @throws SQLException si une erreur survient
     * @return LivreDTO retourne un DTO de livre
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public LivreDTO getLivre(int idLivre) throws DAOException {
        try {
            this.statementExiste.setInt(1,
                idLivre);
            final ResultSet resultset = this.statementExiste.executeQuery();
            if(resultset.next()) {
                final LivreDTO tupleLivre = new LivreDTO();
                tupleLivre.setIdLivre(idLivre);
                tupleLivre.setTitre(resultset.getString(2));
                tupleLivre.setAuteur(resultset.getString(3));
                tupleLivre.setDateAcquisition(resultset.getDate(4));
                tupleLivre.setIdMembre(resultset.getInt(5));
                tupleLivre.setDatePret(resultset.getDate(6));
                resultset.close();
                return tupleLivre;
            }
            resultset.close();
            return null;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Ajout d'un nouveau livre dans la base de donnees.
     *
     * @param idLivre identifiant du livre
     * @param titre titre du livre
     * @param auteur auteur du livre
     * @param dateAcquisition date d'acquisition
     * @throws SQLException si une erreur survient
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws DAOException {
        /* Ajout du livre. */
        try {
            this.statementInsert.setInt(1,
                idLivre);
            this.statementInsert.setString(2,
                titre);
            this.statementInsert.setString(3,
                auteur);
            this.statementInsert.setDate(4,
                Date.valueOf(dateAcquisition));
            this.statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Enregistrement de l'emprunteur d'un livre.
     *
     * @param idLivre identifiant du livre
     * @param idMembre identifiant du membre
     * @param datePret date du Pret
     * @throws SQLException si une erreur survient
     * @return int resultat de la commande de pret
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int preter(int idLivre,
        int idMembre,
        String datePret) throws DAOException {
        try {
            /* Enregistrement du pret. */
            this.statementUpdate.setInt(1,
                idMembre);
            this.statementUpdate.setDate(2,
                Date.valueOf(datePret));
            this.statementUpdate.setInt(3,
                idLivre);
            return this.statementUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Rendre le livre disponible (non-prete).
     *
     * @param idLivre identifiant du livre
     * @throws SQLException si une erreur survient
     * @return int resultat de la commande de pret
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int retourner(int idLivre) throws DAOException {
        try {
            /* Enregistrement du pret. */
            this.statementUpdate.setNull(1,
                Types.INTEGER);
            this.statementUpdate.setNull(2,
                Types.DATE);
            this.statementUpdate.setInt(3,
                idLivre);
            return this.statementUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Suppression d'un livre.
     *
     * @param idLivre identifiant du livre
     * @throws SQLException si une erreur survient
     * @return int resultat de la commande de pret
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int vendre(int idLivre) throws DAOException {
        try {
            /* Suppression du livre. */
            this.statementDelete.setInt(1,
                idLivre);
            return this.statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
}
