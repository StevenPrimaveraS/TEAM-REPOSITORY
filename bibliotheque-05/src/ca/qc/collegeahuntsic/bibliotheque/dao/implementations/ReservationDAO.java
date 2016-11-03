// Fichier ReservationDAO.java
// Auteur : Mathieu Lafond
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.dao.implementations;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.ReservationDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;

/**
 * DAO pour effectuer des CRUDs avec la table reservation.
 *
 * @author Mathieu Lafond
 */

public class ReservationDAO extends DAO implements IReservationDAO {
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
     * Crée le DAO de la table <code>reservation</code>.
     *
     * @param reservationDTOClass La classe de reservation DTO à utiliser
     * @throws InvalidDTOClassException Si la classe de DTO est <code>null</code>
     */
    public ReservationDAO(Class<ReservationDTO> reservationDTOClass) throws InvalidDTOClassException {
        super(reservationDTOClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }
        if(!dto.getClass().equals(getDtoClass())) {
            throw new InvalidDTOClassException("Le DTO doit être un "
                + getDtoClass().getName());
        }
        final ReservationDTO reservationDTO = (ReservationDTO) dto;
        //TODO : vérifier si la base de donnée utilise des String ou encore des int.
        try(
            PreparedStatement createPreparedStatement = connexion.getConnection().prepareStatement(ReservationDAO.ADD_REQUEST)) {
            createPreparedStatement.setString(1,
                reservationDTO.getLivreDTO().getIdLivre());
            createPreparedStatement.setString(2,
                reservationDTO.getMembreDTO().getIdMembre());
            createPreparedStatement.executeUpdate();
            createPreparedStatement.setTimestamp(3,
                reservationDTO.getDateReservation());
            createPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DTO get(Connexion connexion,
        Serializable primaryKey) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(primaryKey == null) {
            throw new InvalidPrimaryKeyException("La clef primaire ne peut être null");
        }
        final String idReservation = (String) primaryKey;
        ReservationDTO reservationDTO = null;
        try(
            PreparedStatement readPreparedStatement = connexion.getConnection().prepareStatement(ReservationDAO.READ_REQUEST)) {
            readPreparedStatement.setString(1,
                idReservation);
            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    reservationDTO = new ReservationDTO();
                    reservationDTO.setIdReservation(resultSet.getString(1));
                    final LivreDTO livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getString(2));
                    reservationDTO.setLivreDTO(livreDTO);
                    final MembreDTO membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getString(3));
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
     * {@inheritDoc}
     */
    @Override
    public void update(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }
        if(!dto.getClass().equals(getDtoClass())) {
            throw new InvalidDTOClassException("Le DTO doit être un "
                + getDtoClass().getName());
        }
        final ReservationDTO reservationDTO = (ReservationDTO) dto;
        try(
            PreparedStatement updatePreparedStatement = connexion.getConnection().prepareStatement(ReservationDAO.UPDATE_REQUEST)) {
            updatePreparedStatement.setString(1,
                reservationDTO.getLivreDTO().getIdLivre());
            updatePreparedStatement.setString(2,
                reservationDTO.getMembreDTO().getIdMembre());
            updatePreparedStatement.setTimestamp(3,
                reservationDTO.getDateReservation());
            updatePreparedStatement.setString(4,
                reservationDTO.getIdReservation());
            updatePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Connexion connexion,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }
        if(!dto.getClass().equals(getDtoClass())) {
            throw new InvalidDTOClassException("Le DTO doit être un "
                + getDtoClass().getName());
        }
        final ReservationDTO reservationDTO = (ReservationDTO) dto;
        try(
            PreparedStatement deletePreparedStatement = connexion.getConnection().prepareStatement(ReservationDAO.DELETE_REQUEST)) {
            deletePreparedStatement.setString(1,
                reservationDTO.getIdReservation());
            deletePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends DTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        List<ReservationDTO> reservations = Collections.emptyList();
        try(
            PreparedStatement getAllPreparedStatement = connexion.getConnection().prepareStatement(ReservationDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = getAllPreparedStatement.executeQuery()) {
                ReservationDTO reservationDTO = null;
                if(resultSet.next()) {
                    reservations = new ArrayList<>();
                    do {
                        reservationDTO = new ReservationDTO();
                        reservationDTO.setIdReservation(resultSet.getString(1));
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getString(2));
                        reservationDTO.setLivreDTO(livreDTO);
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getString(3));
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
     * {@inheritDoc}
     */
    @Override
    public List<ReservationDTO> findByLivre(Connexion connexion,
        String idLivre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(idLivre == null) {
            throw new InvalidCriterionException("L'ID de livre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        List<ReservationDTO> reservations = Collections.emptyList();
        try(
            PreparedStatement findByLivrePreparedStatement = connexion.getConnection().prepareStatement(ReservationDAO.FIND_BY_LIVRE_REQUEST)) {
            findByLivrePreparedStatement.setString(1,
                idLivre);
            try(
                ResultSet resultSet = findByLivrePreparedStatement.executeQuery()) {
                ReservationDTO reservationDTO = null;
                if(resultSet.next()) {
                    reservations = new ArrayList<>();
                    do {
                        reservationDTO = new ReservationDTO();
                        reservationDTO.setIdReservation(resultSet.getString(1));
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getString(2));
                        reservationDTO.setLivreDTO(livreDTO);
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getString(3));
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
     * {@inheritDoc}
     */
    @Override
    public List<ReservationDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(idMembre == null) {
            throw new InvalidCriterionException("L'ID de membre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        List<ReservationDTO> reservations = Collections.emptyList();
        try(
            PreparedStatement findByLivrePreparedStatement = connexion.getConnection().prepareStatement(ReservationDAO.FIND_BY_MEMBRE_REQUEST)) {
            findByLivrePreparedStatement.setString(1,
                idMembre);
            try(
                ResultSet resultSet = findByLivrePreparedStatement.executeQuery()) {
                ReservationDTO reservationDTO = null;
                if(resultSet.next()) {
                    reservations = new ArrayList<>();
                    do {
                        reservationDTO = new ReservationDTO();
                        reservationDTO.setIdReservation(resultSet.getString(1));
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getString(2));
                        reservationDTO.setLivreDTO(livreDTO);
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getString(3));
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
