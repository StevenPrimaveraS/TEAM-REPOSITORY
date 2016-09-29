// Fichier LivreDAO.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

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

    private static final String ADD_REQUEST = "INSERT INTO livre"
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
        try(
            PreparedStatement statementExiste = this.getConnexion().getConnection().prepareStatement(LivreDAO.READ_REQUEST)) {
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
    public LivreDTO read(int idLivre) throws DAOException {
        try(
            PreparedStatement statementExiste = this.getConnexion().getConnection().prepareStatement(LivreDAO.READ_REQUEST)) {
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
     * @param livreDTO - Le livre à supprimer.
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public void add(LivreDTO livreDTO) throws DAOException {
        /* Ajout du livre. */
        try(
            PreparedStatement statementInsert = this.getConnexion().getConnection().prepareStatement(LivreDAO.ADD_REQUEST)) {
            statementInsert.setInt(1,
                livreDTO.getIdLivre());
            statementInsert.setString(2,
                livreDTO.getTitre());
            statementInsert.setString(3,
                livreDTO.getAuteur());
            statementInsert.setDate(4,
                livreDTO.getDateAcquisition());
            statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Enregistrement de l'emprunteur d'un livre.
     *Date.valueOf(dateAcquisition)
     * @param livreDTO - Le livre à supprimer
     * @return int resultat de la commande de pret
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public int update(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement statementUpdate = this.getConnexion().getConnection().prepareStatement(LivreDAO.UPDATE_REQUEST)) {
            /* Enregistrement du pret. */
            statementUpdate.setInt(1,
                livreDTO.getIdMembre());
            statementUpdate.setDate(2,
                livreDTO.getDatePret());
            statementUpdate.setInt(3,
                livreDTO.getIdLivre());
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
        try(
            PreparedStatement statementUpdate = this.getConnexion().getConnection().prepareStatement(LivreDAO.UPDATE_REQUEST)) {
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
     * @param livreDTO - Le livre à supprimer
     * @throws DAOException Si une erreur survient, elle l'encapsule avec DAOException.
     */
    public void delete(LivreDTO livreDTO) throws DAOException {
        try(
            PreparedStatement statementDelete = this.getConnexion().getConnection().prepareStatement(LivreDAO.DELETE_REQUEST)) {
            statementDelete.setInt(1,
                livreDTO.getIdLivre());
            statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
    // Source > Toggle Comment | À implementer plus tard en cours
    //    /**
    //     * Ajoute un nouveau livre.
    //     *
    //     * @param livreDTO - Le livre à ajouter
    //     * @throws DAOException - S'il y a une erreur avec la base de données
    //     */
    //    public void add(LivreDTO livreDTO) throws DAOException {
    //
    //    }
    //
    //    /**
    //     * Lit un livre.
    //     *
    //     * @param idLivre - L'ID du livre à lire
    //     * @return Le livre
    //     * @throws DAOException - S'il y a une erreur avec la base de données
    //     */
    //    public LivreDTO read(int idLivre) throws DAOException {
    //      return null;
    //    }
    //
    //    /**
    //     * Met à jour un livre.
    //     *
    //     * @param livreDTO - Le livre à mettre à jour
    //     * @throws DAOException - S'il y a une erreur avec la base de données
    //     */
    //    public void update(LivreDTO livreDTO) throws DAOException {
    //
    //    }
    //
    //    /**
    //     * Supprime un livre.
    //     *
    //     * @param livreDTO - Le livre à supprimer
    //     * @throws DAOException - S'il y a une erreur avec la base de données
    //     */
    //    public void delete(LivreDTO livreDTO) throws DAOException {
    //
    //    }
}
