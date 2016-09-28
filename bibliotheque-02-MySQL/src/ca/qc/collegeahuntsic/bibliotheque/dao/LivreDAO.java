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
    
      private static final String ADD_REQUEST = "INSERT INTO livre(idLivre, titre, auteur, dateAcquisition, idMembre, datePret)"
       + "VALUES (?, ?, ?, ?, NULL, NULL)";
    
      private static final String READ_REQUEST = "SELECT idLivre, titre, auteur, dateAcquisition, idMembre, pretDate"
       + "FROM livre"
       + "WHERE idLivre = ?";
    
      private static final String UPDATE_REQUEST = "UPDATE livre"
       + "SET titre = ?, auteur = ?, dateAcquisition = ?, idMembre = ?, datePret = ?"
       + "WHERE idLivre = ?";
    
      private static final String DELETE_REQUEST = "DELETE from livre"
       + "WHERE idLivre = ?";
/*
      private static final String GET_ALL_REQUEST = "SELECT idLivre, titre, auteur, dateAcquisition, idMembre, pretDate"
      + "FROM livre ";
*/
    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser.
     */
    public LivreDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     * Verifie si un livre existe.
     *
     * @param idLivre identifiant du livre
     * @return boolean si le livre existe ou pas
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public boolean existe(int idLivre) throws DAOException {
        try (PreparedStatement statementExiste = this.getConnexion().getConnection().prepareStatement(LivreDAO.READ_REQUEST)) {
            statementExiste.setInt(1,
                idLivre);
            final ResultSet resultset = statementExiste.executeQuery();
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
     * @return LivreDTO retourne un DTO de livre
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public LivreDTO getLivre(int idLivre) throws DAOException {
        try (PreparedStatement statementExiste = this.getConnexion().getConnection().prepareStatement(LivreDAO.READ_REQUEST)) {
            statementExiste.setInt(1,
                idLivre);
            final ResultSet resultset = statementExiste.executeQuery();
            if(resultset.next()) {
                final LivreDTO livreDTO = new LivreDTO();
                livreDTO.setIdLivre(idLivre);
                livreDTO.setTitre(resultset.getString(2));
                livreDTO.setAuteur(resultset.getString(3));
                livreDTO.setDateAcquisition(resultset.getDate(4));
                livreDTO.setIdMembre(resultset.getInt(5));
                livreDTO.setDatePret(resultset.getDate(6));
                resultset.close();
                return livreDTO;
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
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public void acquerir(int idLivre,
        String titre,
        String auteur,
        String dateAcquisition) throws DAOException {
        /* Ajout du livre. */
        try (PreparedStatement statementInsert = this.getConnexion().getConnection().prepareStatement(LivreDAO.ADD_REQUEST)) {
            statementInsert.setInt(1,
                idLivre);
            statementInsert.setString(2,
                titre);
            statementInsert.setString(3,
                auteur);
            statementInsert.setDate(4,
                Date.valueOf(dateAcquisition));
            statementInsert.executeUpdate();
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
     * @return int resultat de la commande de pret
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int preter(int idLivre,
        int idMembre,
        String datePret) throws DAOException {
        try (PreparedStatement statementUpdate = this.getConnexion().getConnection().prepareStatement(LivreDAO.UPDATE_REQUEST)) {
            /* Enregistrement du pret. */
            statementUpdate.setInt(1,
                idMembre);
            statementUpdate.setDate(2,
                Date.valueOf(datePret));
            statementUpdate.setInt(3,
                idLivre);
            return statementUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Rendre le livre disponible (non-prete).
     *
     * @param idLivre identifiant du livre
     * @return int resultat de la commande de pret
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int retourner(int idLivre) throws DAOException {
        try (PreparedStatement statementUpdate = this.getConnexion().getConnection().prepareStatement(LivreDAO.UPDATE_REQUEST)) {
            /* Enregistrement du pret. */
            statementUpdate.setNull(1,
                Types.INTEGER);
            statementUpdate.setNull(2,
                Types.DATE);
            statementUpdate.setInt(3,
                idLivre);
            return statementUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Suppression d'un livre.
     *
     * @param idLivre identifiant du livre
     * @return int resultat de la commande de pret
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int vendre(int idLivre) throws DAOException {
        try (PreparedStatement statementDelete = this.getConnexion().getConnection().prepareStatement(LivreDAO.DELETE_REQUEST)) {
            /* Suppression du livre. */
            statementDelete.setInt(1,
                idLivre);
            return statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
}
