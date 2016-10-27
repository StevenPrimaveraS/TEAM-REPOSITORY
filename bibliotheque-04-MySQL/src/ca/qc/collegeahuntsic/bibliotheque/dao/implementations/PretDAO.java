// Fichier PretDAO.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliotheque.dao.implementations;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.DTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.PretDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;

/**
 * DAO pour effectuer des CRUDs avec la table <code>pret</code>.
 *
 * @author Gilles Bénichou
 */
public class PretDAO extends DAO implements IPretDAO {
    private static final String ADD_REQUEST = "INSERT INTO pret (idMembre, "
        + "                                                      idLivre, "
        + "                                                      datePret, "
        + "                                                      dateRetour) "
        + "                                    VALUES           (?, "
        + "                                                      ?, "
        + "                                                      ?, "
        + "                                                      NULL)";

    private static final String READ_REQUEST = "SELECT idPret, "
        + "                                            idMembre, "
        + "                                            idLivre, "
        + "                                            datePret, "
        + "                                            dateRetour "
        + "                                     FROM   pret "
        + "                                     WHERE  idPret = ?";

    private static final String UPDATE_REQUEST = "UPDATE pret "
        + "                                       SET    idMembre = ?, "
        + "                                              idLivre = ?, "
        + "                                              datePret = ?, "
        + "                                              dateRetour = ? "
        + "                                       WHERE  idPret = ?";

    private static final String DELETE_REQUEST = "DELETE FROM pret "
        + "                                       WHERE       idPret = ?";

    private static final String GET_ALL_REQUEST = "SELECT idPret, "
        + "                                               idMembre, "
        + "                                               idLivre, "
        + "                                               datePret, "
        + "                                               dateRetour "
        + "                                        FROM   pret";

    private static final String FIND_BY_MEMBRE = "SELECT   idPret, "
        + "                                                idMembre, "
        + "                                                idLivre, "
        + "                                                datePret, "
        + "                                                dateRetour "
        + "                                       FROM     pret "
        + "                                       WHERE    idMembre = ? "
        + "                                       AND      dateRetour IS NULL "
        + "                                       ORDER BY datePret ASC";

    /**
     * Crée un DAO à partir d'une connexion à la base de données.
     *
     * @param pretDTOClass La classe de livre DTO à utiliser
     * @throws InvalidDTOClassException Si la classe de DTO est <code>null</code>
     */
    public PretDAO(Class<PretDTO> pretDTOClass) throws InvalidDTOClassException {
        super(pretDTOClass);
    }

    /* ANCIENNE VERSION
    /**
     * Ajoute un nouveau prêt.
     *
     * @param pretDTO Le prêt à ajouter
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    /*  public void add(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement addPreparedStatement = getConnection().prepareStatement(PretDAO.ADD_REQUEST)) {
            addPreparedStatement.setInt(1,
                pretDTO.getMembreDTO().getIdMembre());
            addPreparedStatement.setInt(2,
                pretDTO.getLivreDTO().getIdLivre());
            addPreparedStatement.setTimestamp(3,
                pretDTO.getDatePret());
            addPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
    */
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
        final PretDTO pretDTO = (PretDTO) dto;
        try(
            PreparedStatement createPreparedStatement = connexion.getConnection().prepareStatement(PretDAO.ADD_REQUEST)) {
            createPreparedStatement.setString(1,
                pretDTO.getMembreDTO().getIdMembre());
            createPreparedStatement.setString(2,
                pretDTO.getLivreDTO().getIdLivre());
            createPreparedStatement.setTimestamp(3,
                pretDTO.getDatePret());
            createPreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
    /*/**
     * Lit un prêt. Si aucun prêt n'est trouvé, <code>null</code> est retourné.
     *
     * @param idPret L'ID du prêt à lire
     * @return Le prêt lu ; <code>null</code> sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */

    /*   public PretDTO read(int idPret) throws DAOException { ANCIENNE VERSION
        PretDTO pretDTO = null;
        try(
            PreparedStatement readPreparedStatement = getConnection().prepareStatement(PretDAO.READ_REQUEST)) {
            readPreparedStatement.setInt(1,
                idPret);
            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    pretDTO = new PretDTO();
                    pretDTO.setIdPret(resultSet.getInt(1));
                    final MembreDTO membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(2));
                    pretDTO.setMembreDTO(membreDTO);
                    final LivreDTO livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(3));
                    pretDTO.setLivreDTO(livreDTO);
                    pretDTO.setDatePret(resultSet.getTimestamp(4));
                    pretDTO.setDateRetour(resultSet.getTimestamp(5));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return pretDTO;
    }*/

    /**
     * {@inheritDoc}
     */
    @Override
    public PretDTO get(Connexion connexion,
        Serializable primaryKey) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(primaryKey == null) {
            throw new InvalidPrimaryKeyException("La clef primaire ne peut être null");
        }
        final String idPret = (String) primaryKey;
        PretDTO pretDTO = null;
        try(
            PreparedStatement readPreparedStatement = connexion.getConnection().prepareStatement(PretDAO.READ_REQUEST)) {
            readPreparedStatement.setString(1,
                idPret);
            try(
                ResultSet resultSet = readPreparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    pretDTO = new PretDTO();
                    pretDTO.setIdPret(resultSet.getString(1));
                    pretDTO.getMembreDTO().setIdMembre(resultSet.getString(2));
                    pretDTO.getLivreDTO().setIdLivre(resultSet.getString(3));
                    pretDTO.setDatePret(resultSet.getTimestamp(4));
                    pretDTO.setDateRetour(resultSet.getTimestamp(5));
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return pretDTO;
    }

    /*   /** ANCIENNE VERSION
     * Met à jour un prêt.
     *
     * @param pretDTO Le prêt à mettre à jour
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    /* public void update(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement updatePreparedStatement = getConnection().prepareStatement(PretDAO.UPDATE_REQUEST)) {
            updatePreparedStatement.setInt(1,
                pretDTO.getMembreDTO().getIdMembre());
            updatePreparedStatement.setInt(2,
                pretDTO.getLivreDTO().getIdLivre());
            updatePreparedStatement.setTimestamp(3,
                pretDTO.getDatePret());
            updatePreparedStatement.setTimestamp(4,
                pretDTO.getDateRetour());
            updatePreparedStatement.setInt(5,
                pretDTO.getIdPret());
            updatePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
    */
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
        final PretDTO pretDTO = (PretDTO) dto;
        try(
            PreparedStatement updatePreparedStatement = connexion.getConnection().prepareStatement(PretDAO.UPDATE_REQUEST)) {
            updatePreparedStatement.setString(1,
                pretDTO.getMembreDTO().getIdMembre());
            updatePreparedStatement.setString(2,
                pretDTO.getLivreDTO().getIdLivre());
            updatePreparedStatement.setTimestamp(3,
                pretDTO.getDatePret());
            updatePreparedStatement.setTimestamp(4,
                pretDTO.getDateRetour());
            updatePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }

    /* /**
     * Supprime un prêt.
     *
     * @param pretDTO Le prêt à supprimer
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    /*                  ANCIENNE VERSION
     * public void delete(PretDTO pretDTO) throws DAOException {
        try(
            PreparedStatement deletePreparedStatement = getConnection().prepareStatement(PretDAO.DELETE_REQUEST)) {
            deletePreparedStatement.setInt(1,
                pretDTO.getIdPret());
            deletePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }*/

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
        final PretDTO pretDTO = (PretDTO) dto;
        try(
            PreparedStatement deletePreparedStatement = connexion.getConnection().prepareStatement(PretDAO.DELETE_REQUEST)) {
            deletePreparedStatement.setString(1,
                pretDTO.getIdPret());
            deletePreparedStatement.executeUpdate();
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
    }
    /*
    /**
     * Trouve tous les prêts.
     *
     * @return La liste des prêts ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    /*          ANCIENNE VERSION
     * public List<PretDTO> getAll() throws DAOException {
        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement getAllPreparedStatement = getConnection().prepareStatement(PretDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = getAllPreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setLivreDTO(livreDTO);
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));
                        prets.add(pretDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return prets;
    }*/

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement getAllPreparedStatement = connexion.getConnection().prepareStatement(PretDAO.GET_ALL_REQUEST)) {
            try(
                ResultSet resultSet = getAllPreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getString(1));
                        pretDTO.getMembreDTO().setIdMembre(resultSet.getString(2));
                        pretDTO.getLivreDTO().setIdLivre(resultSet.getString(3));
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));
                        prets.add(pretDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return prets;
    }

    /* /**
     * Trouve les prêts non terminés d'un membre.
     *
     * @param idMembre L'ID du membre à trouver
     * @return La liste des prêts correspondants ; une liste vide sinon
     * @throws DAOException S'il y a une erreur avec la base de données
     */
    /*           ANCIENNE VERSION
    public List<PretDTO> findByMembre(int idMembre) throws DAOException {
        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement findByMembrePreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_MEMBRE)) {
            findByMembrePreparedStatement.setInt(1,
                idMembre);
            try(
                ResultSet resultSet = findByMembrePreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setLivreDTO(livreDTO);
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));
                        prets.add(pretDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return prets;
    }*/
    /**
     * {@inheritDoc}
     */
    @Override
    public List<PretDTO> findByMembre(Connexion connexion,
        String idMembre,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        DAOException {
        if(connexion == null) {
            throw new InvalidHibernateSessionException("La connexion ne peut être null");
        }
        if(idMembre == null) {
            throw new InvalidCriterionException("Le titre ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement findByTitrePreparedStatement = connexion.getConnection().prepareStatement(PretDAO.FIND_BY_MEMBRE)) {
            findByTitrePreparedStatement.setString(1,
                "%"
                    + idMembre
                    + "%");
            try(
                ResultSet resultSet = findByTitrePreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getString(1));
                        pretDTO.getMembreDTO().setIdMembre(resultSet.getString(2));
                        pretDTO.getLivreDTO().setIdLivre(resultSet.getString(3));
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));
                        prets.add(pretDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return prets;
    }

}

/*   /**
    * Trouve les livres en cours d'emprunt.
    *
    * @param idLivre L'ID du livre à trouver
    * @return La liste des prêts correspondants ; une liste vide sinon
    * @throws DAOException S'il y a une erreur avec la base de données
    */
/*
    public List<PretDTO> findByLivre(int idLivre) throws DAOException {
        List<PretDTO> prets = Collections.emptyList();
        try(
            PreparedStatement findByLivrePreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_LIVRE)) {
            findByLivrePreparedStatement.setInt(1,
                idLivre);
            try(
                ResultSet resultSet = findByLivrePreparedStatement.executeQuery()) {
                PretDTO pretDTO = null;
                if(resultSet.next()) {
                    prets = new ArrayList<>();
                    do {
                        pretDTO = new PretDTO();
                        pretDTO.setIdPret(resultSet.getInt(1));
                        final MembreDTO membreDTO = new MembreDTO();
                        membreDTO.setIdMembre(resultSet.getInt(2));
                        pretDTO.setMembreDTO(membreDTO);
                        final LivreDTO livreDTO = new LivreDTO();
                        livreDTO.setIdLivre(resultSet.getInt(3));
                        pretDTO.setLivreDTO(livreDTO);
                        pretDTO.setDatePret(resultSet.getTimestamp(4));
                        pretDTO.setDateRetour(resultSet.getTimestamp(5));
                        prets.add(pretDTO);
                    } while(resultSet.next());
                }
            }
        } catch(SQLException sqlException) {
            throw new DAOException(sqlException);
        }
        return prets;
    }*/

/* /**
 * Trouve les prêts à partir d'une date de prêt.
 *
 * @param datePret La date de prêt à trouver
 * @return La liste des prêts correspondants ; une liste vide sinon
 * @throws DAOException S'il y a une erreur avec la base de données
 */
/* public List<PretDTO> findByDatePret(Timestamp datePret) throws DAOException {
    List<PretDTO> prets = Collections.emptyList();
    try(
        PreparedStatement findByDatePretPreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_DATE_PRET)) {
        findByDatePretPreparedStatement.setTimestamp(1,
            datePret);
        try(
            ResultSet resultSet = findByDatePretPreparedStatement.executeQuery()) {
            PretDTO pretDTO = null;
            if(resultSet.next()) {
                prets = new ArrayList<>();
                do {
                    pretDTO = new PretDTO();
                    pretDTO.setIdPret(resultSet.getInt(1));
                    final MembreDTO membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(2));
                    pretDTO.setMembreDTO(membreDTO);
                    final LivreDTO livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(3));
                    pretDTO.setLivreDTO(livreDTO);
                    pretDTO.setDatePret(resultSet.getTimestamp(4));
                    pretDTO.setDateRetour(resultSet.getTimestamp(5));
                    prets.add(pretDTO);
                } while(resultSet.next());
            }
        }
    } catch(SQLException sqlException) {
        throw new DAOException(sqlException);
    }
    return prets;
}*/

/*/**
 * Trouve les prêts à partir d'une date de retour.
 *
 * @param dateRetour La date de retour à trouver
 * @return La liste des prêts correspondants ; une liste vide sinon
 * @throws DAOException S'il y a une erreur avec la base de données
 */
/* public List<PretDTO> findByDateRetour(Timestamp dateRetour) throws DAOException {
    List<PretDTO> prets = Collections.emptyList();
    try(
        PreparedStatement findByDateRetourPreparedStatement = getConnection().prepareStatement(PretDAO.FIND_BY_DATE_RETOUR)) {
        findByDateRetourPreparedStatement.setTimestamp(1,
            dateRetour);
        try(
            ResultSet resultSet = findByDateRetourPreparedStatement.executeQuery()) {
            PretDTO pretDTO = null;
            if(resultSet.next()) {
                prets = new ArrayList<>();
                do {
                    pretDTO = new PretDTO();
                    pretDTO.setIdPret(resultSet.getInt(1));
                    final MembreDTO membreDTO = new MembreDTO();
                    membreDTO.setIdMembre(resultSet.getInt(2));
                    pretDTO.setMembreDTO(membreDTO);
                    final LivreDTO livreDTO = new LivreDTO();
                    livreDTO.setIdLivre(resultSet.getInt(3));
                    pretDTO.setLivreDTO(livreDTO);
                    pretDTO.setDatePret(resultSet.getTimestamp(4));
                    pretDTO.setDateRetour(resultSet.getTimestamp(5));
                    prets.add(pretDTO);
                } while(resultSet.next());
            }
        }
    } catch(SQLException sqlException) {
        throw new DAOException(sqlException);
    }
    return prets;
}*/
