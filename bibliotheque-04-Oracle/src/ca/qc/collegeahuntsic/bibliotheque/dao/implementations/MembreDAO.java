// Fichier MembreDAO.java
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
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;

/**
 * DAO pour effectuer des CRUDs avec la table membre.
 *
 * @author Mathieu Lafond
 */

public class MembreDAO extends DAO implements IMembreDAO {
    private static final String ADD_REQUEST = "INSERT INTO membre (idMembre, "
        + "                                                        nom, "
        + "                                                        telephone, "
        + "                                                        limitePret) "
        + "                                    VALUES             (seqIdMembre.nextVal "
        + "                                                        ?, "
        + "                                                        ?, "
        + "                                                        ?  )";

    private static final String READ_REQUEST = "SELECT idMembre, "
        + "                                            nom, "
        + "                                            telephone, "
        + "                                            limitePret "
        + "                                     FROM   membre "
        + "                                     WHERE  idMembre = ?";

    private static final String UPDATE_REQUEST = "UPDATE membre "
        + "                                       SET    nom = ?, "
        + "                                              telephone = ?, "
        + "                                              limitePret = ? "
        + "                                       WHERE  idMembre = ?";

    private static final String DELETE_REQUEST = "DELETE FROM membre "
        + "                                       WHERE       idMembre = ?";

    private static final String GET_ALL_REQUEST = "SELECT idMembre, "
        + "                                               nom, "
        + "                                               telephone, "
        + "                                               limitePret "
        + "                                        FROM   membre";

    private static final String FIND_BY_NOM = "SELECT idMembre, "
        + "                                             nom, "
        + "                                             telephone, "
        + "                                             limitePret "
        + "                                      FROM   membre "
        + "                                      WHERE  LOWER(membre) LIKE LOWER(?)";

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param membreDTOclass classe du membre dto
     * @throws InvalidDTOClassException S'il y a une erreur avec l'objet DTO
     */
    public MembreDAO(Class<MembreDTO> membreDTOclass) throws InvalidDTOClassException {
        super(membreDTOclass);
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
        final MembreDTO membreDTO = (MembreDTO) dto;
        try(
            PreparedStatement createPreparedStatement = connexion.getConnection().prepareStatement(MembreDAO.ADD_REQUEST)) {
            createPreparedStatement.setString(1,
                membreDTO.getNom());
            createPreparedStatement.setString(2,
                membreDTO.getTelephone());
            createPreparedStatement.setString(3,
                membreDTO.getLimitePret());
            createPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MembreDTO get(Connexion connexion,
        Serializable primaryKey) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(primaryKey == null) {
            throw new InvalidPrimaryKeyException("La clef primaire ne peut être null");
        }
        final String idMembre = (String) primaryKey;
        MembreDTO membreDTO = null;
        try(
            PreparedStatement readPreparedStatement = connexion.getConnection().prepareStatement(MembreDAO.READ_REQUEST)) {
            readPreparedStatement.setString(1,
                idMembre);
            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getString(1));
                    membreDTO.setNom(resultSet.getString(2));
                    membreDTO.setTelephone(resultSet.getString(3));
                    membreDTO.setLimitePret(resultSet.getString(4));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return membreDTO;
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
        final MembreDTO membreDTO = (MembreDTO) dto;
        try(
            PreparedStatement updatePreparedStatement = connexion.getConnection().prepareStatement(MembreDAO.UPDATE_REQUEST)) {
            updatePreparedStatement.setString(1,
                membreDTO.getNom());
            updatePreparedStatement.setString(2,
                membreDTO.getTelephone());
            updatePreparedStatement.setString(3,
                membreDTO.getLimitePret());
            updatePreparedStatement.setString(4,
                membreDTO.getIdMembre());
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
        final MembreDTO membreDTO = (MembreDTO) dto;
        try(
            PreparedStatement deletePreparedStatement = connexion.getConnection().prepareStatement(MembreDAO.DELETE_REQUEST)) {
            deletePreparedStatement.setString(1,
                membreDTO.getIdMembre());
            deletePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MembreDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        List<MembreDTO> membre = Collections.emptyList();
        try(
            PreparedStatement getAllPreparedStatement = connexion.getConnection().prepareStatement(MembreDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = getAllPreparedStatement.executeQuery()) {
                MembreDTO membreDTO = null;
                if(resultSet.next()) {
                    membre = new ArrayList<>();
                    do {
                        membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getString(1));
                        membreDTO.setNom(resultSet.getString(2));
                        membreDTO.setTelephone(resultSet.getString(3));
                        membreDTO.setLimitePret(resultSet.getString(4));
                        membre.add(membreDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return membre;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MembreDTO> findByNom(Connexion connexion,
        String nom,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(nom == null) {
            throw new InvalidCriterionException("Le titre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        List<MembreDTO> membres = Collections.emptyList();
        try(
            PreparedStatement findByNomPreparedStatement = connexion.getConnection().prepareStatement(MembreDAO.FIND_BY_NOM)) {
            findByNomPreparedStatement.setString(1,
                "%"
                    + nom
                    + "%");
            try(
                ResultSet resultSet = findByNomPreparedStatement.executeQuery()) {
                MembreDTO membreDTO = null;
                if(resultSet.next()) {
                    membres = new ArrayList<>();
                    do {
                        membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getString(1));
                        membreDTO.setNom(resultSet.getString(2));
                        membreDTO.setTelephone(resultSet.getString(3));
                        membreDTO.setLimitePret(resultSet.getString(4));
                        membres.add(membreDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return membres;
    }
}
