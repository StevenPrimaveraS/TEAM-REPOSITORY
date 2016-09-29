// Fichier ReservationDAO.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.DAOException;

/**
 * DAO pour effectuer des CRUDs avec la table reservation.
 *
 * @author Mathieu Lafond
 */

public class ReservationDAO extends DAO {
    private static final long serialVersionUID = 1L;

    
    private static final String ADD_REQUEST = "INSERT INTO reservation(idReservation, idlivre, idMembre, dateReservation)"
     + "values (?,?,?,to_date(?,'YYYY-MM-DD'))";

    private static final String READ_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation"
     + "FROM reservation"
     + "WHERE idReservation = ?";
    
    private static final String READ_REQUEST_LIVRE = "SELECT idReservation, idLivre, idMembre, dateReservation "
     + "FROM reservation where idLivre = ? "
     + "ORDER BY dateReservation";
    
    private static final String READ_REQUEST_MEMBRE = "SELECT idReservation, idLivre, idMembre, dateReservation "
     + "FROM reservation "
     + "WHERE idMembre = ? ";
/*
    private static final String UPDATE_REQUEST = "UPDATE reservation"
     + "SET idLivre = ?, idMembre = ?, dateReservation = ?"
     + "WHERE idReservation = ?";
*/
    private static final String DELETE_REQUEST = "DELETE from reservation"
     + "WHERE idReservation = ?";
/*
    private static final String GET_ALL_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation"
    + "FROM reservation";
*/
    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser
     */
    public ReservationDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     * Verifie si une reservation existe.
     *
     * @param idReservation identifiant de la reservation
     * @return boolean si le livre existe ou pas
     * @throws DAOException si une erreur survient
     */
    public boolean existe(int idReservation) throws DAOException {
        try (PreparedStatement statementExiste = this.getConnexion().getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            statementExiste.setInt(1,
                idReservation);
            final ResultSet resultset = statementExiste.executeQuery();
            final boolean reservationExiste = resultset.next();
            resultset.close();
            return reservationExiste;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lecture d'une reservation.
     *
     * @param idReservation identifiant de la reservation.
     * @throws DAOException si une erreur survient
     * @return ReservationDTO retourne un DTO de reservation
     */
    public ReservationDTO getReservation(int idReservation) throws DAOException {
        try (PreparedStatement statementExiste = this.getConnexion().getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            statementExiste.setInt(1,
                idReservation);
            final ResultSet resultset = statementExiste.executeQuery();
            if(resultset.next()) {
                final ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO.setIdReservation(resultset.getInt(1));
                reservationDTO.setIdLivre(resultset.getInt(2));
                reservationDTO.setIdMembre(resultset.getInt(3));
                reservationDTO.setDateReservation(resultset.getDate(4));
                resultset.close();
                return reservationDTO;
            }
            resultset.close();
            return null;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lecture de la premiere reservation d'un livre.
     *
     * @param idLivre identifiant du livre.
     * @throws DAOException si une erreur survient
     * @return ReservationDTO retourne un DTO de reservation
     */
    public ReservationDTO getReservationLivre(int idLivre) throws DAOException {
        try (PreparedStatement statementExisteLivre = this.getConnexion().getConnection().prepareStatement(ReservationDAO.READ_REQUEST_LIVRE)) {
            statementExisteLivre.setInt(1,
                idLivre);
            final ResultSet resultset = statementExisteLivre.executeQuery();
            if(resultset.next()) {
                final ReservationDTO tupleReservation = new ReservationDTO();
                tupleReservation.setIdReservation(resultset.getInt(1));
                tupleReservation.setIdLivre(resultset.getInt(2));
                tupleReservation.setIdMembre(resultset.getInt(3));
                tupleReservation.setDateReservation(resultset.getDate(4));
                resultset.close();
                return tupleReservation;
            }
            resultset.close();
            return null;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lecture de la premiere reservation d'un livre.
     *
     * @param idMembre identifiant de la reservation.
     * @throws DAOException si une erreur survient
     * @return ReservationDTO retourne un DTO de reservation
     */
    public ReservationDTO getReservationMembre(int idMembre) throws DAOException {
        try (PreparedStatement statementExisteMembre = this.getConnexion().getConnection().prepareStatement(ReservationDAO.READ_REQUEST_MEMBRE)) {
            statementExisteMembre.setInt(1,
                idMembre);
            final ResultSet resultset = statementExisteMembre.executeQuery();
            if(resultset.next()) {
                final ReservationDTO tupleReservation = new ReservationDTO();
                tupleReservation.setIdReservation(resultset.getInt(1));
                tupleReservation.setIdLivre(resultset.getInt(2));
                tupleReservation.setIdMembre(resultset.getInt(3));
                tupleReservation.setDateReservation(resultset.getDate(4));
                resultset.close();
                return tupleReservation;
            }
            resultset.close();
            return null;
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Reservation d'un livre.
     *
     * @param idReservation identifiant de la reservation.
     * @param idLivre identifiant du livre
     * @param idMembre identifiant du membre
     * @param dateReservation date de la reservation
     * @throws DAOException si une erreur survient
     */
    public void reserver(int idReservation,
        int idLivre,
        int idMembre,
        String dateReservation) throws DAOException {
        try (PreparedStatement statementInsert = this.getConnexion().getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {
            statementInsert.setInt(1,
                idReservation);
            statementInsert.setInt(2,
                idLivre);
            statementInsert.setInt(3,
                idMembre);
            statementInsert.setString(4,
                dateReservation);
            statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Suppression d'une reservation.
     *
     * @param idReservation identifiant de la reservation.
     * @throws DAOException si une erreur survient
     * @return ReservationDTO retourne un DTO de reservation
     */
    public int annulerRes(int idReservation) throws DAOException {
        try (PreparedStatement statementDelete = this.getConnexion().getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {
            statementDelete.setInt(1,
                idReservation);
            return statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
// Source > Toggle Comment | À implementer plus tard en cours
//  /**
//   * Ajoute une nouvelle reservation.
//   * 
//   * @param reservationDTO - La reservation à ajouter
//   * @throws DAOException - S'il y a une erreur avec la base de données
//   */
//  public void add(ReservationDTO reservationDTO) throws DAOException {
//  	
//  }
//  
//  /**
//   * Lit une reservation.
//   * 
//   * @param idReservation - L'ID de la reservation à lire
//   * @return La reservation
//   * @throws DAOException - S'il y a une erreur avec la base de données
//   */
//  public ReservationDTO read(int idReservation) throws DAOException {
//  	return null;
//  }
//  
//  /**
//   * Met à jour une reservation.
//   * 
//   * @param reservationDTO - La reservation à mettre à jour
//   * @throws DAOException - S'il y a une erreur avec la base de données
//   */
//  public void update(ReservationDTO reservationDTO) throws DAOException {
//  	
//  }
//  
//  /**
//   * Supprime un reservation.
//   * 
//   * @param reservationDTO - La reservation à supprimer
//   * @throws DAOException - S'il y a une erreur avec la base de données
//   */
//  public void delete(ReservationDTO reservationDTO) throws DAOException {
//  	
//  }
}
