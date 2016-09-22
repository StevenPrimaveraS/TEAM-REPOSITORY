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

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtUpdate;

    private PreparedStatement stmtDelete;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     * @param cx - La connexion à utiliser.
     * @throws SQLException si une erreur survient.
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public LivreDAO(Connexion cx) throws DAOException {
        super(cx);
        try {
            this.stmtExiste = cx.getConnection()
                .prepareStatement("select idlivre, titre, auteur, dateAcquisition, idMembre, datePret from livre where idlivre = ?");
            this.stmtInsert = cx.getConnection().prepareStatement("insert into livre (idLivre, titre, auteur, dateAcquisition, idMembre, datePret) "
                + "values (?,?,?,?,null,null)");
            this.stmtUpdate = cx.getConnection().prepareStatement("update livre set idMembre = ?, datePret = ? "
                + "where idLivre = ?");
            this.stmtDelete = cx.getConnection().prepareStatement("delete from livre where idlivre = ?");
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Verifie si un livre existe.
      * @param idLivre identifiant du livre
      * @throws SQLException si une erreur survient
      * @return boolean si le livre existe ou pas
      * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
      */
    public boolean existe(int idLivre) throws DAOException {
        try {
            this.stmtExiste.setInt(1,
                idLivre);
            final ResultSet rset = this.stmtExiste.executeQuery();
            final boolean livreExiste = rset.next();
            rset.close();
            return livreExiste;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Lecture d'un livre.
      * @param idLivre identifiant du livre
      * @throws SQLException si une erreur survient
      * @return LivreDTO retourne un DTO de livre
      * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
      */
    public LivreDTO getLivre(int idLivre) throws DAOException {
        try {
            this.stmtExiste.setInt(1,
                idLivre);
            final ResultSet rset = this.stmtExiste.executeQuery();
            if(rset.next()) {
                final LivreDTO tupleLivre = new LivreDTO();
                tupleLivre.setIdLivre(idLivre);
                tupleLivre.setTitre(rset.getString(2));
                tupleLivre.setAuteur(rset.getString(3));
                tupleLivre.setDateAcquisition(rset.getDate(4));
                tupleLivre.setIdMembre(rset.getInt(5));
                tupleLivre.setDatePret(rset.getDate(6));
                rset.close();
                return tupleLivre;
            }
            rset.close();
            return null;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Ajout d'un nouveau livre dans la base de donnees.
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
            this.stmtInsert.setInt(1,
                idLivre);
            this.stmtInsert.setString(2,
                titre);
            this.stmtInsert.setString(3,
                auteur);
            this.stmtInsert.setDate(4,
                Date.valueOf(dateAcquisition));
            this.stmtInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Enregistrement de l'emprunteur d'un livre.
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
            this.stmtUpdate.setInt(1,
                idMembre);
            this.stmtUpdate.setDate(2,
                Date.valueOf(datePret));
            this.stmtUpdate.setInt(3,
                idLivre);
            return this.stmtUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Rendre le livre disponible (non-prete).
      * @param idLivre identifiant du livre
      * @throws SQLException si une erreur survient
      * @return int resultat de la commande de pret
      * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
      */
    public int retourner(int idLivre) throws DAOException {
        try {
            /* Enregistrement du pret. */
            this.stmtUpdate.setNull(1,
                Types.INTEGER);
            this.stmtUpdate.setNull(2,
                Types.DATE);
            this.stmtUpdate.setInt(3,
                idLivre);
            return this.stmtUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
      * Suppression d'un livre.
      * @param idLivre identifiant du livre
      * @throws SQLException si une erreur survient
      * @return int resultat de la commande de pret
      * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
      */
    public int vendre(int idLivre) throws DAOException {
        try {
            /* Suppression du livre. */
            this.stmtDelete.setInt(1,
                idLivre);
            return this.stmtDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
}
