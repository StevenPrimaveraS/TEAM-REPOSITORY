// Fichier MembreService.java
// Auteur : Dominic Leroux
// Date de création : 2016-09-14

package ca.qc.collegeahuntsic.bibliotheque.service.implementations;

import java.util.List;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.ILivreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IMembreDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IPretDAO;
import ca.qc.collegeahuntsic.bibliotheque.dao.interfaces.IReservationDAO;
import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;
import ca.qc.collegeahuntsic.bibliotheque.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.DAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidCriterionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOClassException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.dto.MissingDTOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingLoanException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ExistingReservationException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.InvalidDAOException;
import ca.qc.collegeahuntsic.bibliotheque.exception.service.ServiceException;
import ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IMembreService;

/**
 * Service de la table membre.
 *
 * @author Primavera Sequeira Steven
 */

public class MembreService extends Service implements IMembreService {

    private IMembreDAO membreDAO;

    private IReservationDAO reservationDAO;

    private IPretDAO pretDAO;

    private ILivreDAO livreDAO;

    /**
     * Crée le service de la table <code>membre</code>.
     *
     * @param membreDAO Le DAO de la table <code>membre</code>
     * @param livreDAO Le DAO de la table <code>livre</code>
     * @param reservationDAO Le DAO de la table <code>reservation</code>
     * @param pretDAO Le DAO de la table <code>pret</code>
     * @throws InvalidDAOException Si le DAO de livre est <code>null</code>, si le DAO de membre est <code>null</code>, si le DAO de prêt est
     *         <code>null</code> ou si le DAO de réservation est <code>null</code>
     */
    public MembreService(IMembreDAO membreDAO,
        ILivreDAO livreDAO,
        IReservationDAO reservationDAO,
        IPretDAO pretDAO) throws InvalidDAOException {
        super();
        if(livreDAO == null) {
            throw new InvalidDAOException("Le DAO de livre ne peut être null");
        }
        if(membreDAO == null) {
            throw new InvalidDAOException("Le DAO de membre ne peut être null");
        }
        if(pretDAO == null) {
            throw new InvalidDAOException("Le DAO de prêt ne peut être null");
        }
        if(reservationDAO == null) {
            throw new InvalidDAOException("Le DAO de réservation ne peut être null");
        }
        setMembreDAO(membreDAO);
        setLivreDAO(livreDAO);
        setReservationDAO(reservationDAO);
        setPretDAO(pretDAO);
    }

    /**
     * Getter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @return La variable d'instance <code>this.pretDAO</code>
     */
    public IPretDAO getPretDAO() {
        return this.pretDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.pretDAO</code>.
     *
     * @param pretDAO La valeur à utiliser pour la variable d'instance <code>this.pretDAO</code>
     */
    public void setPretDAO(IPretDAO pretDAO) {
        this.pretDAO = pretDAO;
    }

    // Region Getters and Setters
    /**
     * Getter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @return La variable d'instance <code>this.membreDAO</code>
     */
    private IMembreDAO getMembreDAO() {
        return this.membreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.membreDAO</code>.
     *
     * @param membreDAO
     *            La valeur à utiliser pour la variable d'instance
     *            <code>this.membreDAO</code>
     */
    private void setMembreDAO(IMembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @return La variable d'instance <code>this.livreDAO</code>
     */
    public ILivreDAO getLivreDAO() {
        return this.livreDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.livreDAO</code>.
     *
     * @param livreDAO
     *            La valeur à utiliser pour la variable d'instance
     *            <code>this.livreDAO</code>
     */
    private void setLivreDAO(ILivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Getter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @return La variable d'instance <code>this.reservationDAO</code>
     */
    private IReservationDAO getReservationDAO() {
        return this.reservationDAO;
    }

    /**
     * Setter de la variable d'instance <code>this.reservationDAO</code>.
     *
     * @param reservationDAO
     *            La valeur à utiliser pour la variable d'instance
     *            <code>this.reservationDAO</code>
     */
    private void setReservationDAO(IReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    // EndRegion Getters and Setters

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Connexion connexion,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getMembreDAO().add(connexion,
                membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MembreDTO get(Connexion connexion,
        int idMembre) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {
        try {
            return (MembreDTO) getMembreDAO().get(connexion,
                idMembre);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Connexion connexion,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getMembreDAO().update(connexion,
                membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Connexion connexion,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        ServiceException {
        try {
            getMembreDAO().delete(connexion,
                membreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override

    public List<MembreDTO> getAll(Connexion connexion,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return (List<MembreDTO>) getMembreDAO().getAll(connexion,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inscrire(Connexion connexion,
        MembreDTO membreDTO) throws ServiceException,
        InvalidHibernateSessionException {
        if(get(connexion,
            membreDTO.getIdMembre()) != null) {
            throw new ServiceException("Le membre "
                + membreDTO.getIdMembre()
                + " existe déjà");
        }
        add(connexion,
            membreDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void desinscrire(Connexion connexion,
        MembreDTO membreDTO,
        String sortByPropertyName) throws ServiceException {
        try {
            final MembreDTO unMembreDTO = get(connexion,
                membreDTO.getIdMembre());
            if(unMembreDTO == null) {
                throw new ServiceException("Le membre "
                    + membreDTO.getIdMembre()
                    + " n'existe pas");
            }
            // TODO : Le gerer avec la table pret. Faire un count avec find_by_membre.
            if(!getPretDAO().findByMembre(connexion,
                membreDTO.getIdMembre(),
                sortByPropertyName).isEmpty()) {
                throw new ServiceException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a encore des prêts");
            }
            if(!getReservationDAO().findByMembre(connexion,
                unMembreDTO.getIdMembre(),
                sortByPropertyName).isEmpty()) {
                throw new ServiceException("Le membre "
                    + unMembreDTO.getNom()
                    + " (ID de membre : "
                    + unMembreDTO.getIdMembre()
                    + ") a des réservations");
            }
            delete(connexion,
                unMembreDTO);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IMembreService#get(ca.qc.collegeahuntsic.bibliotheque.db.Connexion, java.lang.String)
     */
    @Override
    public LivreDTO get(Connexion connexion,
        String idMembre) throws InvalidHibernateSessionException,
        InvalidPrimaryKeyException,
        ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IMembreService#findByNom(ca.qc.collegeahuntsic.bibliotheque.db.Connexion, java.lang.String, java.lang.String)
     */
    @Override
    public List<LivreDTO> findByNom(Connexion connexion,
        String nom,
        String sortByPropertyName) throws InvalidHibernateSessionException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ServiceException {
        try {
            return getLivreDAO().findByTitre(connexion,
                nom,
                sortByPropertyName);
        } catch(DAOException daoException) {
            throw new ServiceException(daoException);
        }
    }

    /* (non-Javadoc)
     * @see ca.qc.collegeahuntsic.bibliotheque.service.interfaces.IMembreService#desinscrire(ca.qc.collegeahuntsic.bibliotheque.db.Connexion, ca.qc.collegeahuntsic.bibliotheque.dto.MembreDTO)
     */
    @Override
    public void desinscrire(Connexion connexion,
        MembreDTO membreDTO) throws InvalidHibernateSessionException,
        InvalidDTOException,
        InvalidDTOClassException,
        InvalidPrimaryKeyException,
        MissingDTOException,
        InvalidCriterionException,
        InvalidSortByPropertyException,
        ExistingLoanException,
        ExistingReservationException,
        ServiceException {
        // TODO Auto-generated method stub

    }

} // CLASS
