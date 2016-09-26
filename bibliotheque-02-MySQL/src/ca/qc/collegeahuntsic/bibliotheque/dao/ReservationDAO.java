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

    /*
    private static final String ADD_REQUEST = "INSERT INTO reservation(idReservation, idlivre, idMembre, dateReservation)"
     + "values (?,?,?,to_date(?,'YYYY-MM-DD'))";

    private static final String READ_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation"
     + "FROM reservation"
     + "WHERE idReservation = ?";

    private static final String UPDATE_REQUEST = "UPDATE reservation"
     + "SET idLivre = ?, idMembre = ?, dateReservation = ?"
     + "WHERE idReservation = ?";

    private static final String DELETE_REQUEST = "DELETE from reservation"
     + "WHERE idReservation = ?";

    private static final String GET_ALL_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation"
    + "FROM reservation";
    */

    private PreparedStatement statementExiste;

    private PreparedStatement statementExisteLivre;

    private PreparedStatement statementExisteMembre;

    private PreparedStatement statementInsert;

    private PreparedStatement statementDelete;

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion - La connexion à utiliser
     * @throws DAOException si une erreur survient
     */
    public ReservationDAO(Connexion connexion) throws DAOException {
        super(connexion);
        try {
            this.statementExiste = connexion.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
                + "from reservation where idReservation = ?");
            this.statementExisteLivre = connexion.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
                + "from reservation where idLivre = ? "
                + "order by dateReservation");
            this.statementExisteMembre = connexion.getConnection().prepareStatement("select idReservation, idLivre, idMembre, dateReservation "
                + "from reservation where idMembre = ? ");
            this.statementInsert = connexion.getConnection().prepareStatement("insert into reservation (idReservation, idlivre, idMembre, dateReservation) "
                + "values (?,?,?,to_date(?,'YYYY-MM-DD'))");
            this.statementDelete = connexion.getConnection().prepareStatement("delete from reservation where idReservation = ?");
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Verifie si une reservation existe.
     *
     * @param idReservation identifiant de la reservation
     * @return boolean si le livre existe ou pas
     * @throws DAOException si une erreur survient
     */
    public boolean existe(int idReservation) throws DAOException {
        try {
            this.statementExiste.setInt(1,
                idReservation);
            final ResultSet resultset = this.statementExiste.executeQuery();
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
        try {
            this.statementExiste.setInt(1,
                idReservation);
            final ResultSet resultset = this.statementExiste.executeQuery();
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
     * @param idLivre identifiant du livre.
     * @throws DAOException si une erreur survient
     * @return ReservationDTO retourne un DTO de reservation
     */
    public ReservationDTO getReservationLivre(int idLivre) throws DAOException {
        try {
            this.statementExisteLivre.setInt(1,
                idLivre);
            final ResultSet resultset = this.statementExisteLivre.executeQuery();
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
        try {
            this.statementExisteMembre.setInt(1,
                idMembre);
            final ResultSet resultset = this.statementExisteMembre.executeQuery();
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
        try {
            this.statementInsert.setInt(1,
                idReservation);
            this.statementInsert.setInt(2,
                idLivre);
            this.statementInsert.setInt(3,
                idMembre);
            this.statementInsert.setString(4,
                dateReservation);
            this.statementInsert.executeUpdate();
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
        try {
            this.statementDelete.setInt(1,
                idReservation);
            return this.statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
}
