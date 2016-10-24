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

    private static final String ADD_REQUEST = "INSERT INTO reservation (idlivre, "
        + "                                                             idMembre, "
        + "                                                             dateReservation) "
        + "                                    VALUES                  (?, "
        + "                                                             ?, "
        + "                                                             ?)";

    private static final String READ_REQUEST = "SELECT idReservation, "
        + "                                            idLivre, "
        + "                                            idMembre, "
        + "                                            dateReservation "
        + "                                     FROM   reservation "
        + "                                     WHERE  idReservation = ?";

    private static final String UPDATE_REQUEST = "UPDATE reservation "
        + "                                       SET    idLivre = ?, "
        + "                                              idMembre = ?, "
        + "                                              dateReservation = ? "
        + "                                       WHERE  idReservation = ?";

    private static final String DELETE_REQUEST = "DELETE FROM reservation "
        + "                                       WHERE       idReservation = ?";

    private static final String GET_ALL_REQUEST = "SELECT idReservation, "
        + "                                               idLivre, "
        + "                                               idMembre, "
        + "                                               dateReservation "
        + "                                        FROM   reservation";

    private static final String FIND_BY_LIVRE_REQUEST = "SELECT   idReservation, "
        + "                                                       idLivre, "
        + "                                                       idMembre, "
        + "                                                       dateReservation "
        + "                                              FROM     reservation "
        + "                                              WHERE    idLivre = ? "
        + "                                              ORDER BY dateReservation ASC";

    private static final String FIND_BY_MEMBRE_REQUEST = "SELECT idReservation, "
        + "                                                      idLivre, "
        + "                                                      idMembre, "
        + "                                                      dateReservation "
        + "                                               FROM   reservation "
        + "                                               WHERE  idMembre = ?";

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param connexion La connexion à utiliser
     */
    public ReservationDAO(Connexion connexion) {
        super(connexion);
    }

    /**
     * Ajoute une nouvelle réservation.
     *
     * @param reservationDTO La réservation à ajouter
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void add(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement addPreparedStatement = getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {
            addPreparedStatement.setInt(1,
                reservationDTO.getLivreDTO().getIdLivre());
            addPreparedStatement.setInt(2,
                reservationDTO.getMembreDTO().getIdMembre());
            addPreparedStatement.executeUpdate();
            addPreparedStatement.setTimestamp(3,
                reservationDTO.getDateReservation());
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Lit une réservation. Si aucune réservation n'est trouvée, <code>null</code> est retourné.
     *
     * @param idReservation La réservation à lire
     * @return La réservation lue ; <code>null</code> sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public ReservationDTO read(int idReservation) throws DAOException {
        ReservationDTO reservationDTO = null;
        try(
            PreparedStatement readPreparedStatement = getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            readPreparedStatement.setInt(1,
                idReservation);
            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getInt(1));
                    final LivreDTO livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(2));
                    reservationDTO.setLivreDTO(livreDTO);
                    final MembreDTO membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(3));
                    reservationDTO.setMembreDTO(membreDTO);
                    reservationDTO.setDateReservation(resultSet.getTimestamp(4));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return reservationDTO;
    }

    /**
     * Met à jour une réservation.
     *
     * @param reservationDTO La réservation à mettre à jour
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void update(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(ReservationDAO.UPDATE_REQUEST)) {
            updatePreparedStatement.setInt(1,
                reservationDTO.getLivreDTO().getIdLivre());
            updatePreparedStatement.setInt(2,
                reservationDTO.getMembreDTO().getIdMembre());
            updatePreparedStatement.setTimestamp(3,
                reservationDTO.getDateReservation());
            updatePreparedStatement.setInt(4,
                reservationDTO.getIdReservation());
            updatePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Supprime une réservation.
     *
     * @param reservationDTO La réservation à supprimer
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public void delete(ReservationDTO reservationDTO) throws DAOException {
        try(
            PreparedStatement deletePreparedStatement = getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {
            deletePreparedStatement.setInt(1,
                reservationDTO.getIdReservation());
            deletePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * Trouve toutes les réservations.
     *
     * @return La liste des réservations ; une liste vide sinon
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
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(2));
                        reservationDTO.setLivreDTO(livreDTO);
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(3));
                        reservationDTO.setMembreDTO(membreDTO);
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
     * Trouve les réservations à partir d'un livre.
     *
     * @param idLivre L'ID du livre à utiliser
     * @return La liste des réservations correspondantes, triée par date de réservation croissante ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> findByLivre(int idLivre) throws DAOException {
        List<ReservationDTO> reservations = Collections.EMPTY_LIST;
        try(
            PreparedStatement findByLivrePreparedStatement = getConnection().prepareStatement(ReservationDAO.FIND_BY_LIVRE_REQUEST)) {
            findByLivrePreparedStatement.setInt(1,
                idLivre);
            try(
                ResultSet resultSet = findByLivrePreparedStatement.executeQuery()) {
                ReservationDTO reservationDTO = null;
                if(resultSet.next()) {
                    reservations = new ArrayList<>();
                    do {
                        reservationDTO = new ReservationDTO();
                        reservationDTO.setIdReservation(resultSet.getInt(1));
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(2));
                        reservationDTO.setLivreDTO(livreDTO);
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(3));
                        reservationDTO.setMembreDTO(membreDTO);
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
     * Trouve les réservations à partir d'un membre.
     *
     * @param idMembre L'ID du membre à utiliser
     * @return La liste des réservations correspondantes ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    public List<ReservationDTO> findByMembre(int idMembre) throws DAOException {
        List<ReservationDTO> reservations = Collections.EMPTY_LIST;
        try(
            PreparedStatement findByMembrePreparedStatement = getConnection().prepareStatement(ReservationDAO.FIND_BY_MEMBRE_REQUEST)) {
            findByMembrePreparedStatement.setInt(1,
                idMembre);
            try(
                ResultSet resultSet = findByMembrePreparedStatement.executeQuery()) {
                ReservationDTO reservationDTO = null;
                if(resultSet.next()) {
                    reservations = new ArrayList<>();
                    do {
                        reservationDTO = new ReservationDTO();
                        reservationDTO.setIdReservation(resultSet.getInt(1));
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(2));
                        reservationDTO.setLivreDTO(livreDTO);
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(3));
                        reservationDTO.setMembreDTO(membreDTO);
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
