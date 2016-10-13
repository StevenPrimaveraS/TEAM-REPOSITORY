// Fichier ReservationDAO.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
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
    private static final String ADD_REQUEST_2 = "INSERT INTO reservation(idReservation,idLivre,idMembre,dateReservation)"
        + "values (?,?,?,to_date(?,'YYYY-MM-DD'))";
    */
    // Pour ne pas générer d'erreur avec l'ancienne version, l'ancien à
    // remplacer par celui là
    private static final String ADD_REQUEST = "INSERT INTO reservation(idReservation,idLivre,idMembre,dateReservation) "
        + "values (?,?,?,?)";

    private static final String READ_REQUEST = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idReservation = ?";

    private static final String READ_REQUEST_LIVRE = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idLivre = ? "
        + "ORDER BY dateReservation";

    private static final String READ_REQUEST_MEMBRE = "SELECT idReservation, idLivre, idMembre, dateReservation "
        + "FROM reservation "
        + "WHERE idMembre = ? ";

    private static final String UPDATE_REQUEST = "UPDATE reservation "
        + "SET idLivre = ?, idMembre = ?, dateReservation = ? "
        + "WHERE idReservation = ?";

    private static final String DELETE_REQUEST = "DELETE from reservation "
        + "WHERE idReservation = ?";

    private static final String GET_ALL_REQUEST = "SELECT idReservation, "
        + "                                               idLivre, "
        + "                                               idMembre, "
        + "                                               dateReservation, "
        + "                                        FROM   reservation";

    /*
     * private static final String GET_ALL_REQUEST =
     * "SELECT idReservation, idLivre, idMembre, dateReservation" +
     * "FROM reservation";
     */
    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion
     *            - La connexion à utiliser
     */
    public ReservationDAO(Connexion connexion) {
        super(connexion);
    }
    // Source > Toggle Comment | Au cas où nécessaire plus tard
    //    /**
    //     * Verifie si une reservation existe.
    //     *
    //     * @param idReservation
    //     *            identifiant de la reservation
    //     * @return boolean si le livre existe ou pas
    //     * @throws DAOException
    //     *             si une erreur survient
    //     */
    //    public boolean existe(int idReservation) throws DAOException {
    //        try(
    //            PreparedStatement statementExiste = getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
    //            statementExiste.setInt(1,
    //                idReservation);
    //            final ResultSet resultset = statementExiste.executeQuery();
    //            final boolean reservationExiste = resultset.next();
    //            resultset.close();
    //            return reservationExiste;
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }
    //
    //    /**
    //     * Lecture d'une reservation.
    //     *
    //     * @param idReservation
    //     *            identifiant de la reservation.
    //     * @throws DAOException
    //     *             si une erreur survient
    //     * @return ReservationDTO retourne un DTO de reservation
    //     */
    //    public ReservationDTO getReservation(int idReservation) throws DAOException {
    //        try(
    //            PreparedStatement statementExiste = getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
    //            statementExiste.setInt(1,
    //                idReservation);
    //            final ResultSet resultset = statementExiste.executeQuery();
    //            if(resultset.next()) {
    //                final ReservationDTO reservationDTO = new ReservationDTO();
    //                reservationDTO.setIdReservation(resultset.getInt(1));
    //                reservationDTO.setIdLivre(resultset.getInt(2));
    //                reservationDTO.setIdMembre(resultset.getInt(3));
    //                reservationDTO.setDateReservation(resultset.getTimestamp(4));
    //                resultset.close();
    //                return reservationDTO;
    //            }
    //            resultset.close();
    //            return null;
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }
    //
    //    /**
    //     * Lecture de la premiere reservation d'un livre.
    //     *
    //     * @param idLivre
    //     *            identifiant du livre.
    //     * @throws DAOException
    //     *             si une erreur survient
    //     * @return ReservationDTO retourne un DTO de reservation
    //     */
    //    public ReservationDTO getReservationLivre(int idLivre) throws DAOException {
    //        try(
    //            PreparedStatement statementExisteLivre = getConnection().prepareStatement(ReservationDAO.READ_REQUEST_LIVRE)) {
    //            statementExisteLivre.setInt(1,
    //                idLivre);
    //            final ResultSet resultset = statementExisteLivre.executeQuery();
    //            if(resultset.next()) {
    //                final ReservationDTO tupleReservation = new ReservationDTO();
    //                tupleReservation.setIdReservation(resultset.getInt(1));
    //                tupleReservation.setIdLivre(resultset.getInt(2));
    //                tupleReservation.setIdMembre(resultset.getInt(3));
    //                tupleReservation.setDateReservation(resultset.getTimestamp(4));
    //                resultset.close();
    //                return tupleReservation;
    //            }
    //            resultset.close();
    //            return null;
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }
    //
    //    /**
    //     * Lecture de la premiere reservation d'un livre.
    //     *
    //     * @param idMembre
    //     *            identifiant de la reservation.
    //     * @throws DAOException
    //     *             si une erreur survient
    //     * @return ReservationDTO retourne un DTO de reservation
    //     */
    //    public ReservationDTO getReservationMembre(int idMembre) throws DAOException {
    //        try(
    //            PreparedStatement statementExisteMembre = getConnection().prepareStatement(ReservationDAO.READ_REQUEST_MEMBRE)) {
    //            statementExisteMembre.setInt(1,
    //                idMembre);
    //            final ResultSet resultset = statementExisteMembre.executeQuery();
    //            if(resultset.next()) {
    //                final ReservationDTO reservationDTO = new ReservationDTO();
    //                reservationDTO.setIdReservation(resultset.getInt(1));
    //                reservationDTO.setIdLivre(resultset.getInt(2));
    //                reservationDTO.setIdMembre(resultset.getInt(3));
    //                reservationDTO.setDateReservation(resultset.getTimestamp(4));
    //                resultset.close();
    //                return reservationDTO;
    //            }
    //            resultset.close();
    //            return null;
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }
    //
    //    /**
    //     * Reservation d'un livre.
    //     *
    //     * @param idReservation
    //     *            identifiant de la reservation.
    //     * @param idLivre
    //     *            identifiant du livre
    //     * @param idMembre
    //     *            identifiant du membre
    //     * @param dateReservation
    //     *            date de la reservation
    //     * @throws DAOException
    //     *             si une erreur survient
    //     */
    //    public void reserver(int idReservation,
    //        int idLivre,
    //        int idMembre,
    //        String dateReservation) throws DAOException {
    //        try(
    //            PreparedStatement statementInsert = getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {
    //            statementInsert.setInt(1,
    //                idReservation);
    //            statementInsert.setInt(2,
    //                idLivre);
    //            statementInsert.setInt(3,
    //                idMembre);
    //            statementInsert.setString(4,
    //                dateReservation);
    //            statementInsert.executeUpdate();
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }
    //
    //    /**
    //     * Suppression d'une reservation.
    //     *
    //     * @param idReservation
    //     *            identifiant de la reservation.
    //     * @throws DAOException
    //     *             si une erreur survient
    //     * @return ReservationDTO retourne un DTO de reservation
    //     */
    //    public int annulerRes(int idReservation) throws DAOException {
    //        try(
    //            PreparedStatement statementDelete = getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {
    //            statementDelete.setInt(1,
    //                idReservation);
    //            return statementDelete.executeUpdate();
    //        } catch(SQLException sqlException) {
    //            throw new DAOException(sqlException);
    //        }
    //    }

    /**
     * Ajoute une nouvelle reservation.
     *
     * @param reservationDTO
     *            - La reservation à ajouter
     * @throws DAOException
     *             - S'il y a une erreur avec la base de données
     */
    public void add(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement statementInsert = getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {
            /* Ajout du reservation. */
            statementInsert.setInt(1,
                reservationDTO.getIdReservation());
            statementInsert.setInt(2,
                reservationDTO.getIdLivre());
            statementInsert.setInt(3,
                reservationDTO.getIdMembre());
            statementInsert.setTimestamp(4,
                reservationDTO.getDateReservation());
            statementInsert.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lit une reservation.
     *
     * @param idReservation
     *            - L'ID de la reservation à lire
     * @return La reservation
     * @throws DAOException
     *             - S'il y a une erreur avec la base de données
     */
    public ReservationDTO read(int idReservation) throws DAOException {
        ReservationDTO reservationDTO = null;
        try(
            PreparedStatement readPreparedStatement = getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            readPreparedStatement.setInt(1,
                idReservation);
            try(
                ResultSet resultset = readPreparedStatement.executeQuery()) {
                if(resultset.next()) {
                    reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultset.getInt(1));
                    reservationDTO.setIdLivre(resultset.getInt(2));
                    reservationDTO.setIdMembre(resultset.getInt(3));
                    reservationDTO.setDateReservation(resultset.getTimestamp(4));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return reservationDTO;
    }

    /**
     * Met à jour une reservation.
     *
     * @param reservationDTO
     *            - La reservation à mettre à jour
     * @throws DAOException
     *             - S'il y a une erreur avec la base de données
     */
    public void update(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement statementUpdate = getConnection().prepareStatement(ReservationDAO.UPDATE_REQUEST)) {
            /* Ajout du reservation. */
            statementUpdate.setInt(1,
                reservationDTO.getIdLivre());
            statementUpdate.setInt(2,
                reservationDTO.getIdMembre());
            statementUpdate.setTimestamp(3,
                reservationDTO.getDateReservation());
            statementUpdate.setInt(4,
                reservationDTO.getIdReservation());
            statementUpdate.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Supprime une reservation.
     *
     * @param reservationDTO
     *            - La reservation à supprimer
     * @throws DAOException
     *             - S'il y a une erreur avec la base de données
     */
    public void delete(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement statementDelete = getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {
            statementDelete.setInt(1,
                reservationDTO.getIdReservation());
            statementDelete.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Trouve toutes les reservations.
     *
     * @return La liste des reservations ; une liste vide sinon.
     * @throws DAOException S'il y a une erreur avec la base de données
     */

    public List<ReservationDTO> getAll() throws DAOException {
        List<ReservationDTO> reservations = Collections.EMPTY_LIST;
        try(
            PreparedStatement getAllPreparedStatement = getConnection().prepareStatement(ReservationDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = getAllPreparedStatement.executeQuery()) {
                ReservationDTO reservationDTO = null;
                if(resultSet.next()) {
                    reservations = new ArrayList<>();
                    do {
                        reservationDTO = new ReservationDTO();
                        reservationDTO.setIdReservation(resultSet.getInt(1));
                        reservationDTO.setIdLivre(resultSet.getInt(2));
                        reservationDTO.setIdMembre(resultSet.getInt(3));
                        reservationDTO.setDateReservation(resultSet.getTimestamp(4));
                        reservations.add(reservationDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return reservations;
    }

    /**
     * Lecture de la premiere reservation d'un livre.
     *
     * @param livreDTO - Le livre à utiliser
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return ReservationDTO retourne un DTO de reservation
     */
    public List<ReservationDTO> findByLivre(LivreDTO livreDTO) throws DAOException {
        List<ReservationDTO> reservations = Collections.EMPTY_LIST;
        try(
            PreparedStatement findByLivrePreparedStatement = getConnection().prepareStatement(ReservationDAO.READ_REQUEST_LIVRE)) {
            findByLivrePreparedStatement.setInt(1,
                livreDTO.getIdLivre());
            try(
                ResultSet resultSet = findByLivrePreparedStatement.executeQuery()) {
                ReservationDTO reservationDTO = null;
                if(resultSet.next()) {
                    reservations = new ArrayList<>();
                    do {
                        reservationDTO = new ReservationDTO();
                        reservationDTO.setIdReservation(resultSet.getInt(1));
                        reservationDTO.setIdLivre(resultSet.getInt(2));
                        reservationDTO.setIdMembre(resultSet.getInt(3));
                        reservationDTO.setDateReservation(resultSet.getTimestamp(4));
                        reservations.add(reservationDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return reservations;
    }

    /**
     * Lecture de la premiere reservation d'un livre.
     *
     * @param membreDTO - Le membre à utiliser
     * @throws DAOException - S'il y a une erreur avec la base de données
     * @return ReservationDTO retourne un DTO de reservation
     */
    public List<ReservationDTO> findByMembre(MembreDTO membreDTO) throws DAOException {
        List<ReservationDTO> reservations = Collections.EMPTY_LIST;
        try(
            PreparedStatement findByMembrePreparedStatement = getConnection().prepareStatement(ReservationDAO.READ_REQUEST_MEMBRE)) {
            findByMembrePreparedStatement.setInt(1,
                membreDTO.getIdMembre());
            try(
                ResultSet resultSet = findByMembrePreparedStatement.executeQuery()) {
                ReservationDTO reservationDTO = null;
                if(resultSet.next()) {
                    reservations = new ArrayList<>();
                    do {
                        reservationDTO = new ReservationDTO();
                        reservationDTO.setIdReservation(resultSet.getInt(1));
                        reservationDTO.setIdLivre(resultSet.getInt(2));
                        reservationDTO.setIdMembre(resultSet.getInt(3));
                        reservationDTO.setDateReservation(resultSet.getTimestamp(4));
                        reservations.add(reservationDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return reservations;
    }

}
