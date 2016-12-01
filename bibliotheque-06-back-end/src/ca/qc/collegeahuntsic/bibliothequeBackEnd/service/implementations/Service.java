// Fichier Service.java
// Auteur : Gilles Bénichou
// Date de création : 2016-05-18

package ca.qc.collegeahuntsic.bibliothequeBackEnd.service.implementations;

import java.io.Serializable;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dao.interfaces.IDAO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.DTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.service.interfaces.IService;
import org.hibernate.Session;

/**
 * Classe de base pour tous les services.
 *
 * @author Gilles Bénichou
 */
public class Service implements IService {
    private IDAO dao;

    /**
     * Crée un service.
     *
     * @param dao Le DAO à utiliser
     * @throws InvalidDAOException Si le DAO est <code>null</code>
     */
    protected Service(IDAO dao) throws InvalidDAOException {
        super();
        if(dao == null) {
            throw new InvalidDAOException("Le DAO ne peut être null");
        }
        setDao(dao);
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.dao</code>.
     *
     * @return La variable d'instance <code>this.dao</code>
     */
    protected IDAO getDao() {
        return this.dao;
    }

    /**
     * Setter de la variable d'instance <code>this.dao</code>.
     *
     * @param dao La valeur à utiliser pour la variable d'instance <code>this.dao</code>
     */
    private void setDao(IDAO dao) {
        this.dao = dao;
    }
    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }
        try {
            getDao().add(session,
                dto);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DTO get(Session session,
        Serializable primaryKey) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(primaryKey == null) {
            throw new InvalidPrimaryKeyException("La clef primaire ne peut être null");
        }
        try {
            final DTO dto = getDao().get(session,
                primaryKey);
            return dto;
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }
        try {
            getDao().update(session,
                dto);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }
        try {
            getDao().save(session,
                dto);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Session session,
        DTO dto) throws InvalidHibernateSessionException,
        InvalidDTOException,
        ServiceException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(dto == null) {
            throw new InvalidDTOException("Le DTO ne peut être null");
        }
        try {
            getDao().delete(session,
                dto);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends DTO> getAll(Session session,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {
        if(session == null) {
            throw new InvalidHibernateSessionException("La session Hibernate ne peut être null");
        }
        if(sortByPropertyName == null) {
            throw new InvalidSortByPropertyException("La propriété utilisée pour classer ne peut être null");
        }
        try {
            final List<? extends DTO> results = getDao().getAll(session,
                sortByPropertyName);
            return results;
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }
}
